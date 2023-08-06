package ourtine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.Category;
import ourtine.domain.enums.CategoryList;
import ourtine.repository.CategoryRepository;
import ourtine.service.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findCategories(List<CategoryList> categoryList) {
        List<Category> categories = new ArrayList<>();
        for (CategoryList category : categoryList) {
            Category category1 = categoryRepository.findByName(category).get();
            categories.add(category1);
        }
        return categories;
    }

}
