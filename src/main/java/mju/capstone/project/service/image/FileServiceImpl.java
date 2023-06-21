package mju.capstone.project.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final String bucketName = "chrkb1569";

    private final AmazonS3Client amazonS3Client;

    @Override
    public String saveImage(MultipartFile multipartFile, String filename) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            metadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(bucketName, filename, multipartFile.getInputStream(), metadata);

            return amazonS3Client.getUrl(bucketName, filename).toString();
        } catch (IOException e) {

        }
        return "";
    }

    @Override
    public S3Object getFile(String filename) {
        return amazonS3Client.getObject(bucketName, filename);
    }


    @Override
    public void deleteImage(String filename) {
        amazonS3Client.deleteObject(bucketName, filename);
    }
}
