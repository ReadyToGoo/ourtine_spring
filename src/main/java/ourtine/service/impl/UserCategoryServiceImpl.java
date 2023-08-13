package ourtine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ourtine.domain.Category;
import ourtine.domain.User;
import ourtine.domain.mapping.UserCategory;
import ourtine.repository.UserCategoryRepository;
import ourtine.service.UserCategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCategoryServiceImpl implements UserCategoryService {
    private final UserCategoryRepository userCategoryRepository;
    @Override
    public void deleteUsersAllCategory(Long userId) {
        for (UserCategory userCategory : userCategoryRepository.findByUserId(userId)) {
            userCategoryRepository.delete(userCategory);
        }
    }

    @Override
    public List<String> findUsersAllCategory(Long userId) {
        List<String> categories = new ArrayList<>();
        List<UserCategory> byUserId = userCategoryRepository.findByUserId(userId);
        for (UserCategory userCategory : byUserId) {
            categories.add(userCategory.getCategory().getName().getDescription());
        }
        return categories;
    }

    @Override
    public void saveCategories(User user, List<Category> categoryList) {
        for (Category category : categoryList) {
            UserCategory userCategory = new UserCategory(user, category);
            userCategoryRepository.save(userCategory);
        }

    }
}
