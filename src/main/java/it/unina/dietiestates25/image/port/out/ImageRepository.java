package it.unina.dietiestates25.image.port.out;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageRepository {
    String save(MultipartFile file, String path);
    List<String> saveAll(List<MultipartFile> images, String path);
    void delete(String objectName);
    void deleteAll(List<String> paths);
}
