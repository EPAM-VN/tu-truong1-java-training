package local.jt.pet.order.web.services;

import local.jt.pet.order.web.dto.CreateCustomerCommand;
import local.jt.pet.order.web.dto.UpdateCustomerCommand;
import local.jt.pet.order.web.enums.Membership;
import local.jt.pet.order.web.mappers.CustomerMapper;
import local.jt.pet.order.web.models.Customer;
import local.jt.pet.order.web.repositories.CustomerRepository;
import local.jt.pet.order.web.repositories.CustomerSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    public Page<Customer> getAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Customer create(CreateCustomerCommand cmd) {
        Customer customer = customerMapper.toEntity(cmd);
        return customerRepository.save(customer);
    }

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
}
