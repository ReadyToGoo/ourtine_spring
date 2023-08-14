package ourtine.web.dto.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

        @NotBlank(message = "습관명을 입력해주세요.")
        @Size(min = 2, max = 20, message = "길이는 2자 이상, 25자 이하여야 합니다.")
        @Pattern(regexp = "/^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$/",message = "한글과 영어 대소문자만 입력가능 합니다.")
        private String title;

        @NotBlank(message = "소개글을 입력해 주세요.")
        @Size(min = 10, max = 100, message = "길이는 10자 이상이어야 합니다.")
        private String detail;

        @NotNull
        @DateTimeFormat(pattern = "HH:mm:ss")
        private LocalTime startTime;

        @NotNull
        @DateTimeFormat(pattern = "HH:mm:ss")
        private LocalTime endTime;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        @Valid
        @NotNull(message = "최소 하나의 요일을 선택해야 합니다.")
        @Size(min = 1, max = 7,message = "최소 하나의 요일을 선택해야 합니다.")
        private List<Day> days;

        @NotNull(message = "인원 수 입력은 필수입니다.")
        @Positive(message = "양수만 입력가능합니다.")
        @Max(value = 6,message = "1~6명의 인원만 가능합니다.")@Min(value = 1,message = "1~6명의 인원만 가능합니다.")
        private Long followerLimit;

        @NotNull(message = "카테고리 입력은 필수입니다.")
        @Enumerated(value = EnumType.STRING)
        private CategoryList category;

        @NotEmpty(message = "해시태그는 1~8개로 작성해야 합니다.")
        @Size(min = 1, max = 8, message = "해시태그는 1~10개까지 작성 가능합니다.")
        private List<@Size(min = 2,max = 10,message = "해시태그는 2~10자 이내로 입력해야합니다.")
                @Pattern(regexp = "/^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/",message = "공백과 특수문자를 불포함한 해시태그만 입력할 수 있습니다.")
                String> hashtags;

        @NotNull(message = "습관 타입 입력은 필수입니다.")
        private HabitStatus habitStatus;



}
