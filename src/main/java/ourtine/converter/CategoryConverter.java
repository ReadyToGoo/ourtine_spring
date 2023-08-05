package ourtine.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ourtine.domain.enums.Category;

@Component
public class CategoryConverter implements Converter<String, Category> {
    @Override
    public Category convert(String category) {
        return Category.valueOf(category.toUpperCase());
    }
}
