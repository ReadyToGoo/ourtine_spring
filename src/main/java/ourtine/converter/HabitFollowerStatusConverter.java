package ourtine.converter;

import org.springframework.core.convert.converter.Converter;
import ourtine.domain.enums.HabitFollowerStatus;

public class HabitFollowerStatusConverter implements Converter<String, HabitFollowerStatus> {
    @Override
    public HabitFollowerStatus convert(String source) {
        return HabitFollowerStatus.valueOf(source);
    }
}
