package it.unina.dietiestates25.image.infrastructure.adapter.out;

import com.google.cloud.storage.*;
import it.unina.dietiestates25.exception.ByteConversionException;
import it.unina.dietiestates25.image.port.out.ImageRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GoogleCloudStorageImageRepository implements ImageRepository {

    private final String projectId = System.getenv("GC_PROJECT_ID");
    private final String bucketName = System.getenv("GC_BUCKET_NAME");

    private String getExtension(MultipartFile file) {
        String contentType = file.getContentType();

        return (contentType != null) ? "." + contentType.split("/")[1] : "";
    }

    @Override
    public String save(MultipartFile file, String path) {
        String objectName = path + System.currentTimeMillis() + getExtension(file);

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        byte[] content;
        try {
            content = file.getBytes();
        } catch (IOException e) {
            throw new ByteConversionException("Could not get bytes from multipart file");
        }

        Storage.BlobTargetOption precondition;
        if (storage.get(bucketName, objectName) == null) {
            precondition = Storage.BlobTargetOption.doesNotExist();
        } else {
            precondition =
                    Storage.BlobTargetOption.generationMatch(
                            storage.get(bucketName, objectName).getGeneration());
        }
        storage.create(blobInfo, content, precondition);

        return "https://storage.googleapis.com/" + bucketName + "/" + objectName;
    }

    @Override
    public List<String> saveAll(List<MultipartFile> images, String path) {
        List<String> pathList = new ArrayList<>();
        images.forEach(image -> pathList.add(save(image, path)));
        return pathList;
    }

    @Override
    public void delete(String objectName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Blob blob = storage.get(bucketName, objectName);
        if (blob == null) {
            return;
        }
        BlobId idWithGeneration = blob.getBlobId();
        storage.delete(idWithGeneration);
    }

    @Override
    public void deleteAll(List<String> paths) {
        paths.forEach(this::delete);
    }
}
