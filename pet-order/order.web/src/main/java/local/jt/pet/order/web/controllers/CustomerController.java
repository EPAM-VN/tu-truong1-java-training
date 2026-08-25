package local.jt.pet.order.web.controllers;

import local.jt.pet.order.web.dto.CreateCustomerCommand;
import local.jt.pet.order.web.dto.CustomerDto;
import local.jt.pet.order.web.dto.UpdateCustomerCommand;
import local.jt.pet.order.web.enums.Membership;
import local.jt.pet.order.web.mappers.CustomerMapper;
import local.jt.pet.order.web.services.CustomerService;
import local.jt.pet.order.web.models.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v{version}/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping(version = "1.0")
    public ResponseEntity<List<CustomerDto>> getAll(@PageableDefault(sort = "id") Pageable pageable) {

        Page<Customer> customers = customerService.getAll(pageable);

        List<CustomerDto> viewModel = customers.map(customerMapper::toDto).getContent();

        return new ResponseEntity<>(viewModel, HttpStatus.OK);
    }

    @GetMapping(path = "{customerId}", version = "1.0")
    public ResponseEntity<Optional<CustomerDto>> getById(@PathVariable UUID customerId) {
        Optional<Customer> customer = customerService.findById(customerId);

        if  (customer.isPresent()) {
            Optional<CustomerDto> viewModel = Optional.of(customer.map(customerMapper::toDto).orElseGet(null));
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "filter", version = "1.0")
    public ResponseEntity<Optional<CustomerDto>> getByEmail(@RequestParam String email) {
        Optional<Customer> customer = customerService.findByEmail(email);

        if  (customer.isPresent()) {
            Optional<CustomerDto> viewModel = Optional.of(customer.map(customerMapper::toDto).orElseGet(null));
            new ResponseEntity<>(viewModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "search", version = "1.0")
    public ResponseEntity<Page<CustomerDto>> getPlatinumMember(@RequestParam String email, @PageableDefault(sort = "email") Pageable pageable) {
        Page<Customer> customers = customerService.search(Membership.PLATINUM, email, pageable);
        Page<CustomerDto> viewModel = customers.map(customerMapper::toDto);
        return new ResponseEntity<>(viewModel, HttpStatus.OK);
    }

    @GetMapping(path = "members", version = "1.0")
    public ResponseEntity<Page<CustomerDto>> findMemberByIdentifier(
            @RequestParam(required = false) Membership membershipType,
            @RequestParam(required = false) String identifier,
            @PageableDefault(sort = "id") Pageable pageable
    ) {
        Page<Customer> customers = customerService.findMemberByIdentifier(membershipType, identifier, pageable);
        Page<CustomerDto> viewModel = customers.map(customerMapper::toDto);
        return new ResponseEntity<>(viewModel, HttpStatus.OK);
    }

    @PostMapping(version = "1.0")
    public ResponseEntity<CustomerDto> create(@RequestBody CreateCustomerCommand cmd) {
        Customer result = customerService.create(cmd);
        CustomerDto viewModel = customerMapper.toDto(result);
        return new ResponseEntity<>(viewModel, HttpStatus.CREATED);
    }

    @PutMapping(version = "1.0")
    public ResponseEntity<Optional<CustomerDto>> update(@RequestBody UpdateCustomerCommand cmd) {
        Optional<Customer> result = customerService.update(cmd);

        if  (result.isPresent()) {
            Optional<CustomerDto> viewModel = Optional.of(result.map(customerMapper::toDto).orElseGet(null));
            new ResponseEntity<>(viewModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
