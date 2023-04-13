package mju.capstone.project.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveImage(MultipartFile multipartFile, String filename);
    void deleteImage(String filename);
}
