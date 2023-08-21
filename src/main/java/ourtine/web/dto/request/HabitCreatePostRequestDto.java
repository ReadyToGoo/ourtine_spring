package ourtine.web.dto.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
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
public class HabitCreatePostRequestDto {

        @NotBlank(message = "습관명은 2자 이상, 20자 이하여야 합니다.")
        @Size(min = 2, max = 20, message = "습관명은 2자 이상, 20자 이하여야 합니다.")
        @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎ가-힣 ]*$",message = "습관명은 한글, 영어 대/소문자, 숫자, 띄어쓰기 입력가능 합니다.")
        @Schema(title = "습관명", description = "공백이 아닌 2자 이상 20자 이하의 한글, 영어 대/소문자, 띄어쓰기만 입력")
        private String title;

        @NotBlank(message = "소개글은 10자 이상, 120자 이하여야 합니다.")
        @Size(min = 10, max = 200, message = "소개글은 10자 이상, 200자 이하여야 합니다.")
        @Schema(title = "소개글", description = "공백이 아닌 10자 이상 200자 이하의 문자열 입력")
        private String detail;

        @NotNull(message = "시작 시간 입력은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(title = "시작 시간",  description = "24시간제")
        private LocalTime startTime;

        @NotNull(message = "종료 시간 입력은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(title = "종료 시간",  description = "24시간제")
        private LocalTime endTime;

        @NotNull(message = "시작 날짜 입력은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @FutureOrPresent(message = "시작 날짜는 현재를 포함한 미래의 날짜만 가능합니다.")
        @Schema(title = "시작 날짜", description = "현재를 포함한 미래의 날짜만 가능")
        private LocalDate startDate;

        @NotNull(message = "종료 날짜 입력은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @FutureOrPresent(message = "종료 날짜는 현재를 포함한 미래의 날짜만 가능합니다.")
        @Schema(title = "종료 날짜", description = "현재를 포함한 미래의 날짜만 가능")
        private LocalDate endDate;

        @Valid
        @NotNull(message = "1개 이상 7개 이하의 요일을 선택해야 합니다.")
        @Size(min = 1, max = 7,message = "1개 이상 7개 이하의 요일을 선택해야 합니다.")
        @Schema(title = "요일", description = "1개 이상 7개 이하의 요일 선택")
        private List<Day> days;

        @NotNull(message = "인원 수는 1명 이상 6명 이하의 인원만 가능합니다.")
        @Max(value = 6,message = "인원 수는 1명 이상 6명 이하의 인원만 가능합니다.")@Min(value = 1,message = "인원 수는 1명 이상 6명 이하의 인원만 가능합니다.")
        @Schema(title = "인원 수", description = "1명 이상 6명 이하의 인원만 가능")
        private Long followerLimit;

        @NotNull(message = "카테고리 입력은 필수입니다.")
        @Enumerated(value = EnumType.STRING)
        private CategoryList category;

        @NotEmpty(message = "해시태그는 1개 이상 8개 이하로 작성해야 합니다.")
        @Size(min = 1, max = 8, message = "해시태그는 1개 이상 8개 이하로 작성해야 합니다.")
        @Schema(title = "해시태그", description = "1개 이상 8개까지 입력 가능, 2자 이상 10자 이내의 한글, 영어, _(언더바)만 입력 가능")
        private List<
                @NotBlank(message = "해시태그를 입력해주세요.")
                @Size(min = 2,max = 10,message = "해시태그는 2자 이상 10자 이하여야 합니다.")
                @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎ가-힣]*$",message = "해시태그는 공백과 특수문자를 불포함한 해시태그만 입력할 수 있습니다.")
                String> hashtags;

        //private MultipartFile profileImage;

        @NotNull(message = "습관 타입 입력은 필수입니다.")
        @Schema(title = "습관 타입", description = "PRIVATE 혹은 PUBLIC만 입력 가능")
        private HabitStatus habitStatus;



}
