package ourtine.server.web.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitCreateRequestDto {
        @NotEmpty(message = "습관명을 입력해주세요")
        @Size(min= 1, max = 25, message = "길이는 ")
        private String title; // 제목

        @Size(min= 1, max = 25, message = "길이는")
        private String detail;


        @NotEmpty
        private LocalTime startTime;

        @NotEmpty
        private LocalTime endTime;

        @NotEmpty
        private LocalDate startDate;

        @NotEmpty
        private LocalDate endDate;

        @NotEmpty
        private Long followerLimit;

        @NotEmpty
        private Long categoryId;

        @NotEmpty
        private List<String> hashtags;

        @NotEmpty
        private String habitStatus;


}
