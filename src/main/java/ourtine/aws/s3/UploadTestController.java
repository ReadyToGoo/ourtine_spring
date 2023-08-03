package ourtine.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.domain.User;
import ourtine.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UploadTestController {

    private final UploadService uploadService;

    private final UserService userService;

    @ResponseBody   // Long 타입을 리턴하고 싶은 경우 붙여야 함 (Long - 객체)
    @PostMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void upload(HttpServletRequest request, @RequestParam(value="image") MultipartFile image) throws IOException {
        System.out.println(uploadService.uploadTest(image));
    }

    @ResponseBody   // Long 타입을 리턴하고 싶은 경우 붙여야 함 (Long - 객체)
    @PatchMapping(value="/user/{id}/Profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadUserImage(@PathVariable Long id, HttpServletRequest request, @RequestParam(value="image") MultipartFile image) throws IOException {
        User user = userService.findById(id);
        user.updateImage(uploadService.uploadUserProfile(image));
        userService.saveOrUpdateUser(user);

    }
}
