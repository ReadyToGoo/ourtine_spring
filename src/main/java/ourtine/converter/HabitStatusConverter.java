package ourtine.converter;

import org.springframework.core.convert.converter.Converter;
import ourtine.domain.enums.HabitStatus;

public class HabitStatusConverter implements Converter<String, HabitStatus> {
    @Override
    public HabitStatus convert(String source) {
        return HabitStatus.valueOf(source.toUpperCase());
    }
}
