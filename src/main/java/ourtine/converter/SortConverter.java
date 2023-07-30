package ourtine.converter;

import org.springframework.core.convert.converter.Converter;
import ourtine.domain.enums.Sort;

public class SortConverter implements Converter<String, Sort> {

    @Override
    public Sort convert(String source) {
        return Sort.valueOf(source.toUpperCase());
    }
}
