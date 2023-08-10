package ourtine.web.dto.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.CategoryList;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.HabitStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class HabitCreatePostRequestDto {
        @NotBlank(message = "습관명을 입력해주세요")
        private String title; // 제목

        @NotBlank
        private String detail;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
        private LocalTime startTime;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
        private LocalTime endTime;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate startDate;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate endDate;

        @NotEmpty
        private List<Day> days;

        @NotNull
        private Long followerLimit;

        @NotNull
        @Enumerated(value = EnumType.STRING)
        private CategoryList category;

        @NotEmpty
        private List<String> hashtags;

        @NotNull
        private HabitStatus habitStatus;



}
