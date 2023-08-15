package ourtine.web.dto.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
        @Size(min = 2, max = 20, message = "길이는 2자 이상, 20자 이하여야 합니다.")
        @Pattern(regexp = "/^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/",message = "한글, 영어 대/소문자, 공백만 입력가능 합니다.")
        @ApiModelProperty(value = "습관명", notes = "공백이 아닌 2자 이상 20자 이하의 한글, 영어 대/소문자, 공백만 입력")
        private String title;

        @NotBlank(message = "소개글을 입력해 주세요.")
        @Size(min = 10, max = 100, message = "길이는 10자 이상이어야 합니다.")
        @ApiModelProperty(value = "소개글", notes = "공백이 아닌 10자 이상 20자 이하의 문자열 입력")
        private String detail;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        @ApiModelProperty(value = "시작 시간",  notes = "24시간제")
        private LocalTime startTime;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        @ApiModelProperty(value = "종료 시간",  notes = "24시간제")
        private LocalTime endTime;

        @NotNull
        @DateTimeFormat(pattern = "yy-MM-dd")
        @FutureOrPresent
        @ApiModelProperty(value = "시작 날짜", notes = "현재를 포함한 미래의 날짜만 가능")
        private LocalDate startDate;

        @NotNull
        @DateTimeFormat(pattern = "yy-MM-dd")
        @FutureOrPresent
        @ApiModelProperty(value = "종료 날짜", notes = "현재를 포함한 미래의 날짜만 가능")
        private LocalDate endDate;

        @Valid
        @NotNull(message = "최소 하나의 요일을 선택해야 합니다.")
        @Size(min = 1, max = 7,message = "최소 하나의 요일을 선택해야 합니다.")
        @ApiModelProperty(value = "요일", notes = "1개 이상 7개 이하의 요일 선택")
        private List<Day> days;

        @NotNull(message = "인원 수 입력은 필수입니다.")
        @Positive(message = "양수만 입력가능합니다.")
        @Max(value = 6,message = "1명 이상 6명 이하의 인원만 가능합니다.")@Min(value = 1,message = "1~6명의 인원만 가능합니다.")
        @ApiModelProperty(value = "인원 수", notes = "1명 이상 6명 이하의 인원만 가능")
        private Long followerLimit;

        @NotNull(message = "카테고리 입력은 필수입니다.")
        @Enumerated(value = EnumType.STRING)
        private CategoryList category;

        @NotEmpty(message = "해시태그는 1~8개로 작성해야 합니다.")
        @Size(min = 1, max = 8, message = "해시태그는 1~8개까지 작성 가능합니다.")
        @ApiModelProperty(value = "해시태그", notes = "1개 이상 8개까지 입력 가능, 2자 이상 10자 이내의 한글, 영어, _(언더바)만 입력 가능")
        private List<@Size(min = 2,max = 10,message = "해시태그는 2~10자 이내로 입력해야합니다.")
                @Pattern(regexp = "/^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/",message = "공백과 특수문자를 불포함한 해시태그만 입력할 수 있습니다.")
                String> hashtags;

        @NotNull(message = "습관 타입 입력은 필수입니다.")
        @ApiModelProperty(value = "습관 타입", notes = "private 혹은 public만 입력 가능")
        private HabitStatus habitStatus;



}
