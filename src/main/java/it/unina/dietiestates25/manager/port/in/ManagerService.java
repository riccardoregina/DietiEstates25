package it.unina.dietiestates25.manager.port.in;

import it.unina.dietiestates25.manager.infrastructure.adapter.in.dto.ManagerDto;
import it.unina.dietiestates25.manager.port.out.ManagerRepository;
import it.unina.dietiestates25.model.Agency;
import it.unina.dietiestates25.model.Manager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {
    @Qualifier("managerRepository")
    private final ManagerRepository repository;

    public ManagerService(@Qualifier("managerRepository") ManagerRepository repository) {
        this.repository = repository;
    }

    public Manager createManager(Manager manager) {
        return repository.save(manager);
    }

    @Transactional
    public void updateManager(ManagerDto managerDto, String id) {
        Manager existingManager = repository.findById(id).orElseThrow(() ->
                new IllegalStateException(String.format("Manager does not exist, email: %s", managerDto.email())));
        if (managerDto.email() != null && !managerDto.email().isEmpty()) {
            existingManager.setEmail(managerDto.email());
        }
        if (managerDto.firstName() != null && !managerDto.firstName().isEmpty()) {
            existingManager.setFirstName(managerDto.firstName());
        }
        if (managerDto.lastName() != null && !managerDto.lastName().isEmpty()) {
            existingManager.setLastName(managerDto.lastName());
        }
        if (managerDto.dob() != null) {
            existingManager.setDob(managerDto.dob());
        }
        if (managerDto.password() != null && !managerDto.password().isEmpty()) {
            existingManager.setPasswordHash(managerDto.password());
        }
    }

    public List<Manager> getManagers(String agencyId, String adminEmail) {
        return repository.findAllByAgency(agencyId, adminEmail);
    }

    public Manager getManager(Agency agency, String id) {
        return repository.findManagerByIdAndAgency(agency, id).orElseThrow(() ->
                new IllegalStateException(String.format("Manager %s of agency %s does not exist",
                        agency.getId(), agency.getRagioneSociale())));
    }

    public Agency getAgencyByManagerEmail(String managerEmail) {
        return repository.findAgencyByManagerEmail(managerEmail);
    }

    @Transactional
    public void deleteManager(Manager manager) {
        repository.delete(manager);
    }
}
