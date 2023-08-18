package ourtine.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ourtine.exception.BusinessException;
import ourtine.exception.enums.ResponseMessage;

import javax.validation.constraints.Null;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final S3Uploader s3Uploader;

    @Transactional
    public String uploadUserProfile(MultipartFile image) throws IOException {
        if(image==null) throw new BusinessException(ResponseMessage.EMPTY_FILE);
        else if (!image.isEmpty()) {
            return s3Uploader.upload(image, "images/users");
        }
        else throw new BusinessException(ResponseMessage.EMPTY_FILE);
    }

    @Transactional
    public String uploadHabitProfile(MultipartFile image) throws IOException {
        if(image==null) throw new BusinessException(ResponseMessage.EMPTY_FILE);
        else if (!image.isEmpty()) {
            return s3Uploader.upload(image, "images/habits");
        }
        else throw new BusinessException(ResponseMessage.EMPTY_FILE);
    }

    @Transactional
    public String uploadSessionVideo(MultipartFile video) throws IOException{
        if(video==null) throw new BusinessException(ResponseMessage.EMPTY_FILE);
        else if (!video.isEmpty()) {
            return s3Uploader.upload(video, "images/habit-sessions");
        }
        else throw new BusinessException(ResponseMessage.EMPTY_FILE);
    }
}
