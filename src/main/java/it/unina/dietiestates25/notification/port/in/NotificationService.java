package it.unina.dietiestates25.notification.port.in;

import it.unina.dietiestates25.model.Listing;
import it.unina.dietiestates25.model.Notification;
import it.unina.dietiestates25.model.User;
import it.unina.dietiestates25.notification.port.out.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

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
                    "/queue/notifications",
                    message);
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
                            "/queue/notifications",
                            message);
                    notificationRepository.save(notification);
                });
    }

}
