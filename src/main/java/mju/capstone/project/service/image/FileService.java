package mju.capstone.project.service.image;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveImage(MultipartFile multipartFile, String filename);
    S3Object getFile(String filename);
    void deleteImage(String filename);
}
