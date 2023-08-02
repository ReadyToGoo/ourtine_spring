package ourtine.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ourtine.domain.enums.Day;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class DayConverter implements Converter<String, Day> {

    @Override
    public Day convert(String source) {
        Day day = null;
        switch (source) {
            case "월" : day = Day.MON;break;
            case "화" : day = Day.TUE;break;
            case "수" : day = Day.WED;break;
            case "목" : day = Day.THU;break;
            case "금" : day = Day.FRI;break;
            case "토" : day = Day.SAT;break;
            case "일" : day = Day.SUN;break;
        }
        return day;
    }

    public Day dayOfWeek() {
        int dayNum = LocalDate.now(ZoneId.of("Asia/Seoul")).getDayOfWeek().getValue();
        Day day = null;
        switch (dayNum) {
            case 1 : day = Day.MON;break;
            case 2 : day = Day.TUE;break;
            case 3 : day = Day.WED;break;
            case 4 : day = Day.THU;break;
            case 5 : day = Day.FRI;break;
            case 6 : day = Day.SAT;break;
            case 7 : day = Day.SUN;break;
        }
        return day;
    }

}
