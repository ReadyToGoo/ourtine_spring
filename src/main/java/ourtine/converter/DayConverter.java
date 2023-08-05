package ourtine.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ourtine.domain.enums.Day;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

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

    public Day dayOfWeek(int dayNum){
        switch (dayNum) {
            case 1 : return Day.MON;
            case 2 : return Day.TUE;
            case 3 : return Day.WED;
            case 4 : return Day.THU;
            case 5 : return Day.FRI;
            case 6 : return Day.SAT;
            case 7 : return Day.SUN;
            default: return  null;
        }
    }

    public Day curDayOfWeek() {
        int dayNum = LocalDate.now(ZoneId.of("Asia/Seoul")).getDayOfWeek().getValue();
        return dayOfWeek(dayNum);
    }
    public Day dayOfWeek(LocalDateTime date) {
        int dayNum = date.atZone(ZoneId.of("Asia/Seoul")).getDayOfWeek().getValue();
        return dayOfWeek(dayNum);
    }
    // 이번주 월요일 날짜 구하기
    public String getCurMonday(){

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM.-d");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        return formatter.format(c.getTime());

    }


}
