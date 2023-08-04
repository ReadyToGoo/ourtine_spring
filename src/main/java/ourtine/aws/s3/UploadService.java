package ourtine.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final S3Uploader s3Uploader;

    @Transactional
    public String uploadUserProfile(MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            return s3Uploader.upload(image, "images/users");
        }
        return "No image";
    }

    @Transactional
    public String uploadHabitProfile(MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            return s3Uploader.upload(image, "images/habits");
        }
        return "No image";
    }
}
