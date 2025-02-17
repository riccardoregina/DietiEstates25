package it.unina.dietiestates25.notification.infrastructure.adapter.in;

import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.model.Notification;
import it.unina.dietiestates25.model.User;
import it.unina.dietiestates25.notification.port.in.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class RestNotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    public RestNotificationController(NotificationService notificationService,
                                      UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/{user_id}")
    public List<Notification> getNotifications(@PathVariable("user_id") String userId,
                                               @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        User user = userService.getUser(userDetails.getUsername());
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException("Users can only read their own notifications");
        }
        return notificationService.getNotifications(user);
    }

}
