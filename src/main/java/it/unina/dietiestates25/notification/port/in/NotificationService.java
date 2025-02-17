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
    public void notifyUsers(List<User> users, Listing listing) {
//        System.out.println("How many users: " + users.size()); // debug
        users.forEach(user -> {
//            System.out.println(listing); // debug
            String message = "You could like this listing in " + listing.getLocation().getCity() + ": " +
                    listing.getTitle();
            Notification notification = new Notification(user, message);
            messagingTemplate.convertAndSendToUser(user.getEmail(),
                    "/queue/notifications",
                    message);
//            System.out.println("Notification sent to user: " + user.getEmail()); // debug
            notificationRepository.save(notification);
        });
    }

}
