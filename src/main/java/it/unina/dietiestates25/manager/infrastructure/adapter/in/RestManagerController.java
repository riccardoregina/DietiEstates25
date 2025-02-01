package it.unina.dietiestates25.manager.infrastructure.adapter.in;

import it.unina.dietiestates25.manager.infrastructure.adapter.in.dto.CreateManagerRequest;
import it.unina.dietiestates25.manager.infrastructure.adapter.in.dto.CreateManagerResponse;
import it.unina.dietiestates25.manager.port.in.ManagerService;
import it.unina.dietiestates25.manager.port.out.ManagerRepository;
import it.unina.dietiestates25.model.Manager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/manager")
public class RestManagerController {

    private final ManagerService service;

    public RestManagerController(ManagerService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateManagerResponse> createManager(@RequestBody CreateManagerRequest requestDto) {
        service.createManager(new Manager(
                requestDto.firstName(),
                requestDto.lastName(),
                requestDto.email(),
                requestDto.dob(),
                requestDto.tempPassword(),
                requestDto.agency()
        ));
        return ResponseEntity.ok(new CreateManagerResponse("Manager created successfully"));
    }
}
