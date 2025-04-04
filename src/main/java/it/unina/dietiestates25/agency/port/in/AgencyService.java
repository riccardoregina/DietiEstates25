package it.unina.dietiestates25.agency.port.in;

import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.AgentDto;
import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.UpdateAgentDto;
import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.UserDto;
import it.unina.dietiestates25.agency.port.out.AdminRepository;
import it.unina.dietiestates25.agency.port.out.AgencyRepository;
import it.unina.dietiestates25.agency.port.out.AgentRepository;
import it.unina.dietiestates25.agency.port.out.ManagerRepository;
import it.unina.dietiestates25.exception.EntityAlreadyExistsException;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.agency.model.Admin;
import it.unina.dietiestates25.agency.model.Agency;
import it.unina.dietiestates25.agency.model.Agent;
import it.unina.dietiestates25.agency.model.Manager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {
    @Qualifier("managerRepository")
    private final ManagerRepository managerRepository;
    @Qualifier("adminRepository")
    private final AdminRepository adminRepository;
    @Qualifier("agentRepository")
    private final AgentRepository agentRepository;
    private final AgencyRepository agencyRepository;
    private final PasswordEncoder passwordEncoder;

    public AgencyService(@Qualifier("managerRepository") ManagerRepository managerRepository,
                         @Qualifier("adminRepository") AdminRepository adminRepository,
                         @Qualifier("agentRepository") AgentRepository agentRepository,
                         AgencyRepository agencyRepository,
                         PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
        this.agencyRepository = agencyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Admin createAdminAndAgency(Admin admin)
            throws EntityAlreadyExistsException {
        if (agencyRepository.existsByPartitaIvaOrRagioneSociale(admin.getAgency().getPartitaIva(),
                admin.getAgency().getRagioneSociale())) {
            throw new EntityAlreadyExistsException(String.format("Agency already exists: %s, %s",
                    admin.getAgency().getPartitaIva(), admin.getAgency().getRagioneSociale()));
        }
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new EntityAlreadyExistsException(String.format("Admin already exists, email: %s", admin.getEmail()));
        }
        agencyRepository.save(admin.getAgency());
        return adminRepository.save(admin);
    }

    public Admin getAdminByEmail(String email)
            throws EntityNotExistsException {
        return adminRepository.findAdminByEmail(email).orElseThrow(() ->
                new EntityNotExistsException(String.format("Admin does not exist, email: %s", email)));
    }

    public Manager createManager(Manager manager)
            throws EntityAlreadyExistsException {
        if (managerRepository.existsByEmail(manager.getEmail())) {
            throw new EntityAlreadyExistsException(String.format("Manager already exists, email: %s", manager.getEmail()));
        }
        return managerRepository.save(manager);
    }

    @Transactional
    public void updateManager(UserDto userDto, String id)
            throws EntityNotExistsException {
        Manager existingManager = managerRepository.findById(id).orElseThrow(() ->
                new EntityNotExistsException(String.format("Manager does not exist, email: %s", userDto.email())));
        existingManager.setEmail(userDto.email());
        existingManager.setFirstName(userDto.firstName());
        existingManager.setLastName(userDto.lastName());
        existingManager.setDob(userDto.dob());
        existingManager.setPasswordHash(passwordEncoder.encode(userDto.password()));
        if (userDto.profilePicUrl() != null) existingManager.setProfilePicUrl(userDto.profilePicUrl());
    }

    public List<Manager> getManagers(String agencyId, String adminEmail) {
        return managerRepository.findAllByAgency(agencyId, adminEmail);
    }

    public Manager getManagerById(String id)
            throws EntityNotExistsException {
        return managerRepository.findById(id).orElseThrow(() ->
                new EntityNotExistsException(String.format("Manager does not exist, id: %s", id)));
    }

    public Manager getManagerByEmail(String email)
            throws EntityNotExistsException {
        return managerRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotExistsException(String.format("Manager does not exist, email: %s", email)));
    }

    public Agency getAgencyByManagerEmail(String email)
            throws EntityNotExistsException {
        return managerRepository.findAgencyByManagerEmail(email).orElseThrow(() ->
                new EntityNotExistsException(String.format("Agency does not exist, manager's email: %s", email)));
    }

    @Transactional
    public void deleteManager(Manager manager) {
        Agency agency = manager.getAgency();
        Admin admin = adminRepository.findByAgency(agency);
        List<Agent> agents = agentRepository.findAllByManager(manager.getId());
        agents.forEach(agent -> agent.setManager(admin));
        managerRepository.delete(manager);
    }

    public Agent createAgent(Agent agent)
            throws EntityAlreadyExistsException {
        if(agentRepository.existsByEmail(agent.getEmail())) {
            throw new EntityAlreadyExistsException(String.format("Agent already exists, email: %s", agent.getEmail()));
        }
        return agentRepository.save(agent);
    }

    @Transactional
    public void updateAgent(UpdateAgentDto updateAgentDto, String id)
            throws EntityNotExistsException {
        Agent existingAgent = agentRepository.findById(id).orElseThrow(() ->
                new EntityNotExistsException(String.format("Agent does not exist, id: %s", id)));
        if (updateAgentDto.email() != null) {existingAgent.setEmail(updateAgentDto.email());}
        if (updateAgentDto.firstName() != null) {existingAgent.setFirstName(updateAgentDto.firstName());}
        if (updateAgentDto.lastName() != null) {existingAgent.setLastName(updateAgentDto.lastName());}
        if (updateAgentDto.dob() != null) {existingAgent.setDob(updateAgentDto.dob());}
        if (updateAgentDto.password() != null) {existingAgent.setPasswordHash(passwordEncoder.encode(updateAgentDto.password()));}
        if (updateAgentDto.profilePicUrl() != null) existingAgent.setProfilePicUrl(updateAgentDto.profilePicUrl());
        if (updateAgentDto.bio() != null) existingAgent.setBio(updateAgentDto.bio());
    }

    public List<Agent> getAgents(Agency agency) {
        return agentRepository.findAllByAgency(agency);
    }

    public Agent getAgentById(String id)
            throws EntityNotExistsException {
        return agentRepository.findById(id).orElseThrow(() ->
                new EntityNotExistsException(String.format("Agent does not exist, id: %s", id)));
    }

    public Agent getAgentByEmail(String email)
            throws EntityNotExistsException {
        return agentRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotExistsException(String.format("Agent does not exist, email: %s", email)));
    }

    public Agency getAgencyByAgentEmail(String email)
            throws EntityNotExistsException {
        return agentRepository.findAgencyByAgentEmail(email).orElseThrow(() ->
                new EntityNotExistsException(String.format("Agency does not exist, agent's email: %s", email)));
    }

    @Transactional
    public void deleteAgent(Agent agent) {
        agentRepository.delete(agent);
    }
}
