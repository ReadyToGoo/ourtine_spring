package ourtine.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ourtine.domain.enums.CategoryList;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryListsConverter implements Converter<List<String>, List<CategoryList>> {
    @Override
    public List<CategoryList> convert(List<String> category) {
        List<CategoryList> categoryLists = new ArrayList<>();
        for (String categories : category) {
            categoryLists.add(CategoryList.valueOf(categories.toUpperCase()));
        }
        return categoryLists;
    }
}