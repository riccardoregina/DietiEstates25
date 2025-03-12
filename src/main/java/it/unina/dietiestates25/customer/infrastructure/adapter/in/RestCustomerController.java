package it.unina.dietiestates25.customer.infrastructure.adapter.in;

import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.UserDto;
import it.unina.dietiestates25.customer.port.in.CustomerService;
import it.unina.dietiestates25.exception.EntityAlreadyExistsException;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.customer.model.Customer;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/customers")
public class RestCustomerController {
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    public static final String FORBIDDEN_EXCEPTION_MSG_CUSTOMER = "A customer can only modify itself";

    public RestCustomerController(CustomerService customerService,
                                  PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<Customer> signUp(@Valid @RequestBody UserDto userDto)
            throws EntityAlreadyExistsException {
        Customer customer = customerService.signUp(new Customer(
                userDto.firstName(),
                userDto.lastName(),
                userDto.email(),
                userDto.dob(),
                passwordEncoder.encode(userDto.password()),
                userDto.profilePicUrl()));
        return ResponseEntity.created(URI.create("/api/customers/" + customer.getId())).body(customer);
    }

    @PutMapping("/{customer-id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> updateCustomer(@PathVariable("customer-id") String customerId,
                                              @Valid @RequestBody UserDto userDto,
                                              @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        Customer customer = customerService.getCustomer(userDetails.getUsername());
        validateCustomer(customerId, customer, FORBIDDEN_EXCEPTION_MSG_CUSTOMER);
        customerService.updateCustomer(userDto, customer);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{customer-id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Customer> getCustomer(@PathVariable("customer-id") String customerId,
                                                @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        Customer customer = customerService.getCustomer(userDetails.getUsername());
        validateCustomer(customerId, customer, FORBIDDEN_EXCEPTION_MSG_CUSTOMER);
        return ResponseEntity.ok(customer);
    }

    private void validateCustomer(String customerId, Customer customer, String errorMsg)
            throws ForbiddenException {
        if (!customerId.equals(customer.getId())) {
            throw new ForbiddenException(errorMsg);
        }
    }
}
