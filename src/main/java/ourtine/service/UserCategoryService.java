package ourtine.service;

import ourtine.domain.Category;
import ourtine.domain.User;

import java.util.List;

public interface UserCategoryService {

    public void deleteUsersAllCategory(Long userId);

    public void saveCategories(User user, List<Category> categoryList);

}
