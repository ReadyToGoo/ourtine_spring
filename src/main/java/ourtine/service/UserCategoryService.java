package ourtine.service;

import org.springframework.stereotype.Service;
import ourtine.domain.Category;
import ourtine.domain.User;

import java.util.List;

@Service
public interface UserCategoryService {

    public void deleteUsersAllCategory(Long userId);

    public List<String> findUsersAllCategory(Long userId);

    public void saveCategories(User user, List<Category> categoryList);

}
