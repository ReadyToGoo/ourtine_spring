package ourtine.converter;

import org.springframework.core.convert.converter.Converter;
import ourtine.domain.enums.Emotion;

public class EmotionConverter implements Converter<String, Emotion> {
    @Override
    public Emotion convert(String source) {
        return Emotion.valueOf(source);
    }
}
