package it.unina.dietiestates25.image.port.in;

import it.unina.dietiestates25.agency.model.Agent;
import it.unina.dietiestates25.agency.port.in.AgencyService;
import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.image.port.out.ImageRepository;
import it.unina.dietiestates25.listing.model.listing.Listing;
import it.unina.dietiestates25.listing.port.in.ListingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ListingService listingService;
    private final AgencyService agencyService;
    private final UserService userService;

    public ImageService(ImageRepository imageRepository,
                        ListingService listingService,
                        AgencyService agencyService,
                        UserService userService) {
        this.imageRepository = imageRepository;
        this.listingService = listingService;
        this.agencyService = agencyService;
        this.userService = userService;
    }

    private String getRelativePath(String url) {
        return url.replaceFirst("^.*?dietiestates25/", "");
    }

    @Transactional
    public String setProfilePic(MultipartFile file, String userId, String userEmail)
            throws ForbiddenException, EntityNotExistsException {
        User user = userService.getUser(userEmail);
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException("Users can only modify their own file picture");
        }

        if (user.getProfilePicUrl() != null) {
            imageRepository.delete(getRelativePath(user.getProfilePicUrl()));
        }
        String path = "users/" + user.getId() + "/";
        String imageUrl = imageRepository.save(file, path);
        user.setProfilePicUrl(imageUrl);
        return imageUrl;
    }

    @Transactional
    public void removeProfilePic(String userId, String userEmail)
            throws EntityNotExistsException, ForbiddenException {
        User user = userService.getUser(userEmail);
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException("Users can only modify their own file picture");
        }

        imageRepository.delete(getRelativePath(user.getProfilePicUrl()));
        user.setProfilePicUrl(null);
    }

    @Transactional
    public List<String> addListingImages(List<MultipartFile> images, String listingId, String agentEmail)
            throws EntityNotExistsException, ForbiddenException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        Listing listing = listingService.getListing(listingId);
        if (!listing.getAgent().equals(agent)) {
            throw new ForbiddenException("Agents can only modify their listings");
        }

        String path = "listings/" + listingId + "/";
        List<String> imagesUrls = imageRepository.saveAll(images, path);
        listing.getPhotos().addAll(imagesUrls);
        return imagesUrls;
    }

    @Transactional
    public void removeListingImages(List<String> paths, String listingId, String agentEmail)
            throws EntityNotExistsException, ForbiddenException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        Listing listing = listingService.getListing(listingId);
        if (!listing.getAgent().equals(agent)) {
            throw new ForbiddenException("Agents can only modify their listings");
        }

        paths = paths.stream().map(this::getRelativePath).toList();
        imageRepository.deleteAll(paths);
        listing.getPhotos().removeAll(paths);
    }
}
