package ourtine.converter;

import org.springframework.stereotype.Component;
import ourtine.domain.enums.Day;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Component
public class DayConverter{

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
    public LocalDateTime getCurMonday(){

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        return c.getTime().toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

    }


}
