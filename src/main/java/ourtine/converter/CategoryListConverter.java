package ourtine.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ourtine.domain.enums.CategoryList;

import java.util.List;

@Component
public class CategoryListConverter implements Converter<String, CategoryList> {
    @Override
    public CategoryList convert(String category) {
        return CategoryList.valueOf(category.toUpperCase());
    }
}
