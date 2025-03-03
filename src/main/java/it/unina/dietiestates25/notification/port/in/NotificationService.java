package it.unina.dietiestates25.notification.port.in;

import it.unina.dietiestates25.model.*;
import it.unina.dietiestates25.notification.port.out.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    public static final String DESTINATION_PATH = "/topic/notifications/";

    public NotificationService(SimpMessagingTemplate messagingTemplate,
                               NotificationRepository notificationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotifications(User user) {
        return notificationRepository.findAllByUserOrderByTimestampDesc(user);
    }

    @Transactional
    public void notifyUsersOfNewListing(List<User> users, Listing listing) {
        String message = "You could like this listing in " + listing.getLocation().getCity() + ": " +
                listing.getTitle();
        users.forEach(user -> {
            Notification notification = new ListingNotification(user, message, listing);
            messagingTemplate.convertAndSendToUser(user.getEmail(),
                    DESTINATION_PATH,
                    notification);
            notificationRepository.save(notification);
        });
    }

    @Transactional
    public void notifyUsersOfListingUpdate(Listing listing) {
        String message = "One of your favorite listings has been updated: " +
                listing.getTitle();
        listing.getFollowingUsers()
                .forEach(user -> {
                    Notification notification = new ListingNotification(user, message, listing);
                    messagingTemplate.convertAndSendToUser(user.getEmail(),
                            DESTINATION_PATH,
                            notification);
                    notificationRepository.save(notification);
                });
    }

    @Transactional
    public void notifyAgentOfVisitRequest(Agent agent, VisitRequest visitRequest) {
        String message = "A new visit request has been received for " +
                visitRequest.getListing().getTitle();
        Notification notification = new VisitRequestNotification(agent, message, visitRequest);
        messagingTemplate.convertAndSendToUser(agent.getEmail(),
                DESTINATION_PATH,
                notification);
        notificationRepository.save(notification);
    }

    @Transactional
    public void notifyCustomerOfVisitAccepted(Customer customer, Visit visit) {
        String message = "Your visit request for " + visit.getListing().getTitle() + " has been accepted";
        Notification notification = new VisitResponseNotification(customer, message, visit);
        messagingTemplate.convertAndSendToUser(customer.getEmail(),
                DESTINATION_PATH,
                notification);
        notificationRepository.save(notification);
    }

    @Transactional
    public void notifyCustomerOfVisitRejected(Customer customer, String listingTitle, String agentMsg) {
        String message = "Your visit request for " + listingTitle + " has been rejected: " + agentMsg;
        Notification notification = new Notification(customer, message);
        messagingTemplate.convertAndSendToUser(customer.getEmail(),
                DESTINATION_PATH,
                notification);
        notificationRepository.save(notification);
    }

}
