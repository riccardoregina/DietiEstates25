package it.unina.dietiestates25.visit.port.in;

import it.unina.dietiestates25.agency.port.in.AgencyService;
import it.unina.dietiestates25.customer.port.in.CustomerService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.listing.port.in.ListingService;
import it.unina.dietiestates25.model.*;
import it.unina.dietiestates25.notification.port.in.NotificationService;
import it.unina.dietiestates25.visit.infrastructure.adapter.in.dto.VisitRequestDto;
import it.unina.dietiestates25.visit.port.out.VisitRepository;
import it.unina.dietiestates25.visit.port.out.VisitRequestRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitService {
    private final CustomerService customerService;
    private final AgencyService agencyService;
    private final ListingService listingService;
    private final NotificationService notificationService;
    private final VisitRequestRepository visitRequestRepository;
    private final VisitRepository visitRepository;

    public VisitService(CustomerService customerService,
                        AgencyService agencyService,
                        ListingService listingService,
                        NotificationService notificationService,
                        VisitRequestRepository visitRequestRepository,
                        VisitRepository visitRepository) {
        this.customerService = customerService;
        this.agencyService = agencyService;
        this.listingService = listingService;
        this.notificationService = notificationService;
        this.visitRequestRepository = visitRequestRepository;
        this.visitRepository = visitRepository;
    }


    public VisitRequest createVisitRequest(@Valid VisitRequestDto visitRequestDto,
                                           UserDetails userDetails)
            throws EntityNotExistsException {
        Customer customer = customerService.getCustomer(userDetails.getUsername());
        Listing listing = listingService.getListing(visitRequestDto.listingId());
        VisitRequest visitRequest = visitRequestRepository
                .save(new VisitRequest(customer, listing, visitRequestDto.timeSlots()));
        notificationService.notifyAgentOfVisitRequest(listing.getAgent(), visitRequest);
        return visitRequest;
    }

    public Visit acceptVisitRequest(String visitRequestId,
                                    UserDetails userDetails,
                                    LocalDateTime dateTime)
            throws EntityNotExistsException, ForbiddenException {
        Agent agent = agencyService.getAgentByEmail(userDetails.getUsername());
        VisitRequest visitRequest = visitRequestRepository.findById(visitRequestId).orElseThrow(() ->
                new EntityNotExistsException(String.format("VisitRequest with id '%s' not found", visitRequestId)));
        if (!agent.getId().equals(visitRequest.getListing().getAgent().getId())) {
            throw new ForbiddenException("Agent can accept only visit requests on their listings");
        }
        Visit visit = new Visit(visitRequest.getCustomer(), visitRequest.getListing(), dateTime);
        visitRepository.save(visit);
        visitRequestRepository.delete(visitRequest);
        notificationService.notifyCustomerOfVisitAccepted(visitRequest.getCustomer(), visit);
        return visit;
    }

    public void rejectVisitRequest(String visitRequestId,
                                   String agentMsg,
                                   UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        Agent agent = agencyService.getAgentByEmail(userDetails.getUsername());
        VisitRequest visitRequest = visitRequestRepository.findById(visitRequestId).orElseThrow(() ->
                new EntityNotExistsException(String.format("VisitRequest with id '%s' not found", visitRequestId)));
        if (!agent.getId().equals(visitRequest.getListing().getAgent().getId())) {
            throw new ForbiddenException("Agent can reject only visit requests on their listings");
        }
        visitRequestRepository.delete(visitRequest);
        notificationService.notifyCustomerOfVisitRejected(visitRequest.getCustomer(),
                visitRequest.getListing().getTitle(),
                agentMsg);
    }

    public List<Visit> getVisits(String agentId, int year, int month, UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        Agent agent = agencyService.getAgentByEmail(userDetails.getUsername());
        if (!agent.getId().equals(agentId)) {
            throw new ForbiddenException("Agent can only access his own visits");
        }
        return visitRepository.findAllByAgentIdAndMonth(agentId, year, month);
    }
}
