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
    public static final String DESTINATION_PATH = "/queue/notifications";

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
            Notification notification = new Notification(user, message);
            messagingTemplate.convertAndSendToUser(user.getEmail(),
                    DESTINATION_PATH,
                    notification);
            notificationRepository.save(notification);
        });
    }

    @Transactional
    public void notifyUsersOfListingUpdate(Listing listing) {
        String message = "A listing you saved as a favorite has been updated: " +
                listing.getTitle();
        listing.getFollowingUsers()
                .forEach(user -> {
                    Notification notification = new Notification(user, message);
                    messagingTemplate.convertAndSendToUser(user.getEmail(),
                            DESTINATION_PATH,
                            notification);
                    notificationRepository.save(notification);
                });
    }

    @Transactional
    public void notifyAgentOfVisitRequest(Agent agent, VisitRequest visitRequest) {
        String message = "A new visit request has been received for the " +
                visitRequest.getListing().getTitle();
        Notification notification = new Notification(agent, message);
        messagingTemplate.convertAndSendToUser(agent.getEmail(),
                DESTINATION_PATH,
                notification);
        notificationRepository.save(notification);
    }

    @Transactional
    public void notifyCustomerOfVisitResponse(Customer customer, String listingTitle, boolean requestAccepted) {
        String message = "Your visit request to the " + listingTitle + " has been "
                + ((requestAccepted) ? "accepted" : "declined");
        Notification notification = new Notification(customer, message);
        messagingTemplate.convertAndSendToUser(customer.getEmail(),
                DESTINATION_PATH,
                notification);
        notificationRepository.save(notification);
    }

}
