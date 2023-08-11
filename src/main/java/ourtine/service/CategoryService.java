package ourtine.service;

import org.springframework.stereotype.Service;
import ourtine.domain.Category;
import ourtine.domain.enums.CategoryList;

import java.util.List;

public interface CategoryService {
    public List<Category> findCategories(List<CategoryList> categoryList);

}
