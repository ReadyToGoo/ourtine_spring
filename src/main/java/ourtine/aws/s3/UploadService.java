package ourtine.aws.s3;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UploadTestService {


    private final S3Uploader s3Uploader;
    @Transactional
    public String uploadTest(MultipartFile image) throws IOException {
        if(!image.isEmpty()) {
            String storedFileName = s3Uploader.upload(image,"images");
            return "File name : "+storedFileName;
        }
        return "No image!!";
    }
}
