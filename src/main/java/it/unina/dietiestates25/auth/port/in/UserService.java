package it.unina.dietiestates25.auth.port.in;

import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.image.port.out.ImageRepository;
import it.unina.dietiestates25.notification.infrastructure.adapter.in.dto.NotificationSettingsDto;
import it.unina.dietiestates25.notification.model.NotificationSettings;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import it.unina.dietiestates25.auth.port.out.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService implements UserDetailsService {

  @Qualifier("userRepository")
  private final UserRepository<User> repository;
  private final ImageRepository imageRepository;

  public UserService(UserRepository<User> repository,
                     ImageRepository imageRepository) {
    this.repository = repository;
      this.imageRepository = imageRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email)
          throws UsernameNotFoundException {

    User user = repository.findUserByEmail(email).orElseThrow(() ->
        new UsernameNotFoundException("User not found"));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPasswordHash())
        .roles(user.getClass().getSimpleName().toUpperCase())
        .build();
  }

  public User getUser(String email) throws EntityNotExistsException {
      return repository.findUserByEmail(email)
              .orElseThrow(() -> new EntityNotExistsException(String.format("User does not exist, email: %s", email)));
  }

  public static boolean hasRole(UserDetails userDetails, String role) {
      return userDetails.getAuthorities().stream()
              .anyMatch(auth -> auth.getAuthority().equals(role));
  }

  @Transactional
  public String setProfilePic(MultipartFile file, String userId, String userEmail)
          throws ForbiddenException, EntityNotExistsException {
    User user = getUser(userEmail);
    if (!user.getId().equals(userId)) {
      throw new ForbiddenException("Users can only modify their own file picture");
    }

    if (user.getProfilePicUrl() != null) {
      imageRepository.delete(user.getProfilePicUrl().replaceFirst("^.*?dietiestates25/", ""));
    }
    String path = "users/" + user.getId() + "/";
    String imageUrl = imageRepository.save(file, path);
    user.setProfilePicUrl(imageUrl);
    return imageUrl;
  }

  @Transactional
  public void removeProfilePic(String userId, String userEmail)
          throws EntityNotExistsException, ForbiddenException {
    User user = getUser(userEmail);
    if (!user.getId().equals(userId)) {
      throw new ForbiddenException("Users can only modify their own file picture");
    }

    imageRepository.delete(user.getProfilePicUrl().replaceFirst("^.*?dietiestates25/", ""));
    user.setProfilePicUrl(null);
  }

  public NotificationSettings getNotificationSettings(User user) {
    return user.getNotificationSettings();
  }

  @Transactional
  public void updateNotificationSettings(User user,
                                         NotificationSettingsDto notificationSettingsDto) {
    if (notificationSettingsDto.recommendedListings() != null) {
      user.getNotificationSettings().setRecommendedListings(notificationSettingsDto.recommendedListings());
    }
    if (notificationSettingsDto.starredListings() != null) {
      user.getNotificationSettings().setStarredListings(notificationSettingsDto.starredListings());
    }
    if (notificationSettingsDto.visit() != null) {
      user.getNotificationSettings().setVisit(notificationSettingsDto.visit());
    }
  }
}
