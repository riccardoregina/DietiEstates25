package it.unina.dietiestates25.customer.infrastructure.adapter.in;

import it.unina.dietiestates25.customer.infrastructure.adapter.in.dto.CustomerDto;
import it.unina.dietiestates25.customer.port.in.CustomerService;
import it.unina.dietiestates25.exception.EntityAlreadyExistsException;
import it.unina.dietiestates25.model.Customer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class RestCustomerController {
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    public RestCustomerController(CustomerService customerService,
                                  PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<Void> signUp(@Valid @RequestBody CustomerDto customerDto)
            throws EntityAlreadyExistsException {
        customerService.signUp(new Customer(
                customerDto.firstName(),
                customerDto.lastName(),
                customerDto.email(),
                customerDto.dob(),
                passwordEncoder.encode(customerDto.password())));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
