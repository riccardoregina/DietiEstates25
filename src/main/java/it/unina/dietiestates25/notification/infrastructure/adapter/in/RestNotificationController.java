package it.unina.dietiestates25.notification.infrastructure.adapter.in;

import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.notification.infrastructure.adapter.in.dto.NotificationSettingsDto;
import it.unina.dietiestates25.notification.model.Notification;
import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.notification.port.in.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping(value = "/settings")
    public ResponseEntity<Void> updateNotificationSettings(@RequestParam String userId,
                                               @RequestBody NotificationSettingsDto notificationSettingsDto,
                                               @AuthenticationPrincipal UserDetails userDetails)
            throws ForbiddenException, EntityNotExistsException {
        User user = userService.getUser(userDetails.getUsername());
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException("Users can only modify their own settings");
        }
        userService.updateNotificationSettings(user, notificationSettingsDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Notification> getNotifications(@RequestParam String userId,
                                               @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        User user = userService.getUser(userDetails.getUsername());
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException("Users can only read their own notifications");
        }
        return notificationService.getNotifications(user);
    }

}
