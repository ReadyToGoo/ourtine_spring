//package ourtine.aws.s3;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import ourtine.domain.Habit;
//import ourtine.domain.User;
//import ourtine.service.HabitService;
//import ourtine.service.HabitSessionService;
//import ourtine.service.UserService;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@RestController
//@RequiredArgsConstructor
//public class UploadTestController {
//
//    private final UploadService uploadService;
//    private final UserService userService;
//    private final HabitService habitService;
//    private final HabitSessionService habitSessionService;
//
//    @ResponseBody   // Long 타입을 리턴하고 싶은 경우 붙여야 함 (Long - 객체)
//    @PatchMapping(value="/user/{id}/profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public void uploadUserProfile(@PathVariable Long id, HttpServletRequest request, @RequestParam(value="image") MultipartFile image) throws IOException {
//        User user = userService.findById(id);
//        user.updateImage(uploadService.uploadUserProfile(image));
//        userService.saveOrUpdateUser(user);
//    }
//
//    @ResponseBody   // Long 타입을 리턴하고 싶은 경우 붙여야 함 (Long - 객체)
//    @PatchMapping(value="/habit/{id}/profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public void uploadHabitProfile(@PathVariable Long id, HttpServletRequest request, @RequestParam(value="image") MultipartFile image) throws IOException {
//        Habit habit = habitService.findById(id);
//        habit.updateImage(uploadService.uploadUserProfile(image));
//        habitService.saveOrUpdateHabit(habit);
//    }
//
//    @ResponseBody   // Long 타입을 리턴하고 싶은 경우 붙여야 함 (Long - 객체)
//    @PatchMapping(value="/habit-session/{id}/profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public void uploadUserImage(@PathVariable Long id, HttpServletRequest request, @RequestParam(value="image") MultipartFile image) throws IOException {
//
//    }
//}
