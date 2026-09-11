package local.jt.pet.order.web.services;

import local.jt.pet.order.web.dto.CreateCustomerCommand;
import local.jt.pet.order.web.dto.CustomerDto;
import local.jt.pet.order.web.dto.UpdateCustomerCommand;
import local.jt.pet.order.web.enums.Membership;
import local.jt.pet.order.web.mappers.CustomerCreatedEventMapper;
import local.jt.pet.order.web.mappers.CustomerMapper;
import local.jt.pet.order.web.messaging.customers.events.CustomerCreatedEvent;
import local.jt.pet.order.web.models.Customer;
import local.jt.pet.order.web.repositories.CustomerRepository;
import local.jt.pet.order.web.repositories.CustomerSpecs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RedisLockRegistry lockRegistry;
    private final CustomerEventPublisher eventPublisher;
    private final String SYNC_LOCK_KEY = "customer-sync";

    @Cacheable(value = "customer", key = "#customerId")
    public Optional<CustomerDto> findById(UUID customerId) {
        return customerRepository.findById(customerId).map(customerMapper::toDto);
    }

    @Cacheable(value = "default", key = "#email")
    public Optional<CustomerDto> findByEmail(String email) {
        return customerRepository.findByEmail(email).map(customerMapper::toDto);
    }

    public Page<Customer> search(Membership membership, String email, Pageable pageable) {
        return customerRepository.search(membership.name(), email, pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "customers")
    public Page<Customer> getAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @CachePut(value = "customer", key = "#result.id")
    public CustomerDto create(CreateCustomerCommand cmd) {
        log.info("Logging from {} - action {} - cmd = {}", CustomerService.class.getName(), "create()", cmd);
        Customer customer = customerMapper.toEntity(cmd);
        customer = customerRepository.save(customer);
        eventPublisher.publishIntransaction(customer);
        return customerMapper.toDto(customer);
    }

    @Transactional
    public Optional<Customer> update(UpdateCustomerCommand cmd) {
        Optional<Customer> customer = customerRepository.findById(cmd.id());

        if (customer.isPresent()) {
            Customer updatingCustomer = customerMapper.toEntity(customer.get());
            Customer updatedCustomer = customerRepository.save(updatingCustomer);

            return Optional.of(updatedCustomer);
        }

        return customer;
    }

    @CacheEvict(value = "customer", key = "#id")
    public void delete(UUID id) {
        Optional<Customer> entity = customerRepository.findById(id);

        entity.ifPresent(customerRepository::delete);
    }

    public Page<Customer> findMemberByIdentifier(Membership membership, String identifier, Pageable pageable) {
        Specification<Customer> specs = Specification.allOf(
                CustomerSpecs.isMember(membership),
                CustomerSpecs.hasIdentifier(identifier)
        );

        return customerRepository.findAll(specs, pageable);
    }

    @Cacheable(value = "default", key = "#id")
    public Optional<CustomerDto> getIncludeAddresses(UUID id) {
        Optional<CustomerDto> rs = customerRepository.getIncludeAdresses(id).map(customerMapper::toDto);

        return rs;
    }

    @Async("asyncSimulatorExecutor")
    public CompletableFuture<String> syncCustomers() throws Throwable {
        Lock lock = lockRegistry.obtain(SYNC_LOCK_KEY);
        boolean acquired = false;

        try {
            acquired = lock.tryLock(5, TimeUnit.SECONDS);

            if (!acquired) {
                log.info("Sync already running");
                return CompletableFuture.completedFuture("The distributed lock has already been acquired");
            }

            return doSync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }
    }

    private CompletableFuture<String> doSync() throws InterruptedException {
        try {
            // Simulate a heavy 5-second background operation
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }

        log.info("Acquired the distributed lock");
        log.info("Task finished by thread: " + Thread.currentThread().getName());
        return CompletableFuture.completedFuture("Acquired the distributed lock");
    }
}
