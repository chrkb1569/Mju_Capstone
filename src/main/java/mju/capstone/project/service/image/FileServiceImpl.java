package mju.capstone.project.service.image;

import mju.capstone.project.exception.image.ImageUploadFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.dir}")
    private String location;

    @PostConstruct
    public void initFileService() {
        File file = new File(location);

        if(!file.exists()) file.mkdir();
    }

    @Override
    public void saveImage(MultipartFile multipartFile, String filename) {
        try {
            multipartFile.transferTo(new File(location + filename));
        } catch(IOException e) {
            throw new ImageUploadFailureException(e.getCause());
        }
    }

    @Override
    public void deleteImage(String filename) {
        new File(location + filename).delete();
    }
}
