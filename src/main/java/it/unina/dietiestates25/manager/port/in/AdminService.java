package it.unina.dietiestates25.manager.port.in;

import it.unina.dietiestates25.manager.port.out.AdminRepository;
import it.unina.dietiestates25.manager.port.out.AgencyRepository;
import it.unina.dietiestates25.model.Admin;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Qualifier("adminRepository")
    private final AdminRepository adminRepository;
    private final AgencyRepository agencyRepository;

    public AdminService(@Qualifier("adminRepository") AdminRepository adminRepository,
                        AgencyRepository agencyRepository) {
        this.adminRepository = adminRepository;
        this.agencyRepository = agencyRepository;
    }

    @Transactional
    public void createAdminAndAgency(Admin admin) {
        if (agencyRepository.existsByPartitaIvaOrRagioneSociale(admin.getAgency().getPartitaIva(),
                admin.getAgency().getRagioneSociale())) {
            throw new IllegalStateException(String.format("Agency already exists: %s, %s",
                    admin.getAgency().getPartitaIva(), admin.getAgency().getRagioneSociale()));
        }
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new IllegalStateException(String.format("Admin already exists, email: %s", admin.getEmail()));
        }
        agencyRepository.save(admin.getAgency());
        adminRepository.save(admin);
    }
}
