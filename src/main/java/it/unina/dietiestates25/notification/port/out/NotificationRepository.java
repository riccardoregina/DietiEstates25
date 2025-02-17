package it.unina.dietiestates25.notification.port.out;

import it.unina.dietiestates25.model.Notification;
import it.unina.dietiestates25.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findAllByUserOrderByTimestampDesc(User user);

}
