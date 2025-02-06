package it.unina.dietiestates25.agency.port.in;

import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.ManagerDto;
import it.unina.dietiestates25.agency.port.out.AdminRepository;
import it.unina.dietiestates25.agency.port.out.AgencyRepository;
import it.unina.dietiestates25.agency.port.out.ManagerRepository;
import it.unina.dietiestates25.exception.UserAlreadyExistsException;
import it.unina.dietiestates25.exception.AgencyAlreadyExistsException;
import it.unina.dietiestates25.model.Admin;
import it.unina.dietiestates25.model.Agency;
import it.unina.dietiestates25.model.Manager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {
    @Qualifier("managerRepository")
    private final ManagerRepository managerRepository;
    @Qualifier("adminRepository")
    private final AdminRepository adminRepository;
    private final AgencyRepository agencyRepository;

    public AgencyService(@Qualifier("managerRepository") ManagerRepository managerRepository,
                         @Qualifier("adminRepository") AdminRepository adminRepository,
                         AgencyRepository agencyRepository) {
        this.managerRepository = managerRepository;
        this.adminRepository = adminRepository;
        this.agencyRepository = agencyRepository;
    }

    @Transactional
    public Admin createAdminAndAgency(Admin admin)
            throws AgencyAlreadyExistsException, UserAlreadyExistsException {
        if (agencyRepository.existsByPartitaIvaOrRagioneSociale(admin.getAgency().getPartitaIva(),
                admin.getAgency().getRagioneSociale())) {
            throw new AgencyAlreadyExistsException(String.format("Agency already exists: %s, %s",
                    admin.getAgency().getPartitaIva(), admin.getAgency().getRagioneSociale()));
        }
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new UserAlreadyExistsException(String.format("Admin already exists, email: %s", admin.getEmail()));
        }
        agencyRepository.save(admin.getAgency());
        return adminRepository.save(admin);
    }

    public Manager createManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Transactional
    public void updateManager(ManagerDto managerDto, String id)
            throws UserAlreadyExistsException {
        Manager existingManager = managerRepository.findById(id).orElseThrow(() ->
                new UserAlreadyExistsException(String.format("Manager does not exist, email: %s", managerDto.email())));
        existingManager.setEmail(managerDto.email());
        existingManager.setFirstName(managerDto.firstName());
        existingManager.setLastName(managerDto.lastName());
        existingManager.setDob(managerDto.dob());
        existingManager.setPasswordHash(managerDto.password());
    }

    public List<Manager> getManagers(String agencyId, String adminEmail) {
        return managerRepository.findAllByAgency(agencyId, adminEmail);
    }

    public Manager getManager(Agency agency, String id) throws UserAlreadyExistsException {
        return managerRepository.findManagerByIdAndAgency(agency, id).orElseThrow(() ->
                new UserAlreadyExistsException(String.format("Manager %s of agency %s does not exist",
                        id, agency.getRagioneSociale())));
    }

    public Agency getAgencyByManagerEmail(String managerEmail) {
        return managerRepository.findAgencyByManagerEmail(managerEmail);
    }

    @Transactional
    public void deleteManager(Manager manager) {
        managerRepository.delete(manager);
    }
}
