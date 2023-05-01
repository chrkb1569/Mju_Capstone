package mju.capstone.project.repository.category;

import mju.capstone.project.domain.category.LeafCategory;
import mju.capstone.project.domain.category.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeafCategoryRepository extends JpaRepository<LeafCategory, Long> {

    List<LeafCategory> findLeafCategoriesBySubCategory(SubCategory subCategory);
}
