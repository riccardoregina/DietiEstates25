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
        agencyRepository.save(admin.getAgency());
        adminRepository.save(admin);
    }
}
