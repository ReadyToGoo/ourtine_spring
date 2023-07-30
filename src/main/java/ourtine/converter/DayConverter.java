package ourtine.converter;

import org.springframework.core.convert.converter.Converter;
import ourtine.domain.enums.Day;

public class DayConverter implements Converter<String, Day> {

    @Override
    public Day convert(String source) {
        return Day.valueOf(source);
    }

}
