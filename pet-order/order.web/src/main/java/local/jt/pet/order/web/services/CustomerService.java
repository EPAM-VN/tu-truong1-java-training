package local.jt.pet.order.web.services;

import local.jt.pet.order.web.dto.CreateCustomerCommand;
import local.jt.pet.order.web.dto.UpdateCustomerCommand;
import local.jt.pet.order.web.enums.Membership;
import local.jt.pet.order.web.mappers.CustomerMapper;
import local.jt.pet.order.web.models.Customer;
import local.jt.pet.order.web.repositories.CustomerRepository;
import local.jt.pet.order.web.repositories.CustomerSpecs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public Optional<Customer> findById(UUID customerId) {
        return customerRepository.findById(customerId);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Page<Customer> search(Membership membership, String email, Pageable pageable) {
        return customerRepository.search(membership.name(), email, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Customer> getAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Customer create(CreateCustomerCommand cmd) {
        log.info("Logging from {} - action {} - cmd = {}", CustomerService.class.getName(), "create()", cmd);
        Customer customer = customerMapper.toEntity(cmd);
        return customerRepository.save(customer);
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

    public Page<Customer> findMemberByIdentifier(Membership membership, String identifier, Pageable pageable) {
        Specification<Customer> specs = Specification.allOf(
                CustomerSpecs.isMember(membership),
                CustomerSpecs.hasIdentifier(identifier)
        );

        return customerRepository.findAll(specs, pageable);
    }

    public Optional<Customer> getIncludeAddresses(UUID id) {
        return customerRepository.getIncludeAdresses(id);
    }
}
