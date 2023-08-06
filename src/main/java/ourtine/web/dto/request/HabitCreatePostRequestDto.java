package ourtine.web.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.HabitStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitCreatePostRequestDto {
        @NotBlank(message = "습관명을 입력해주세요")
        @Size(min= 1, max = 25, message = "길이는 ")
        private String title; // 제목

        @NotBlank
        @Size(min= 1, max = 25, message = "길이는")
        private String detail;

        @NotBlank
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
        private LocalTime startTime;

        @NotBlank
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
        private LocalTime endTime;

        @NotBlank
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate startDate;

        @NotBlank
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate endDate;

        @NotBlank
        private List<Day> days;

        @NotBlank
        private Long followerLimit;

        @NotBlank
        private String category;

        @NotBlank
        private List<String> hashtags;

        @NotBlank
        private HabitStatus habitStatus;



}
