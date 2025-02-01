package it.unina.dietiestates25.manager.port.in;

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

    public ManagerService(ManagerRepository repository) {
        this.repository = repository;
    }

    public void createManager(Manager manager) {
        repository.save(manager);
    }

    @Transactional
    public void updateManager(Manager manager) {
        Manager existingManager = repository.findById(manager.getId()).orElseThrow(() ->
                new IllegalStateException(String.format("Manager does not exist, email: %s", manager.getEmail())));
        existingManager.update(manager);
    }

    public List<Manager> getManagers(Agency agency) {
        return repository.findAllByAgency(agency.getId());
    }

    @Transactional
    public void deleteManager(Manager manager) {
        repository.delete(manager);
    }
}
