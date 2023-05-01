package mju.capstone.project.repository.category;

import mju.capstone.project.domain.category.Category;
import mju.capstone.project.domain.category.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findSubCategoriesByCategory(Category category);
}
