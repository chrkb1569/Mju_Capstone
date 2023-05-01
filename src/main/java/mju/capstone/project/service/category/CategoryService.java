package mju.capstone.project.service.category;

import lombok.RequiredArgsConstructor;
import mju.capstone.project.domain.category.Category;
import mju.capstone.project.domain.category.LeafCategory;
import mju.capstone.project.domain.category.SubCategory;
import mju.capstone.project.dto.category.CategoryCreateDto;
import mju.capstone.project.dto.category.CategoryResponseDto;
import mju.capstone.project.exception.category.CategoryListEmptyException;
import mju.capstone.project.exception.category.CategoryNotFoundException;
import mju.capstone.project.repository.category.CategoryRepository;
import mju.capstone.project.repository.category.LeafCategoryRepository;
import mju.capstone.project.repository.category.SubCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final LeafCategoryRepository leafCategoryRepository;

    // 카테고리 전체 조회
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {
        List<Category> findCategories = categoryRepository.findAll();

        if(findCategories.isEmpty()) throw new CategoryListEmptyException();

        return findCategories.stream()
                .map(category -> new CategoryResponseDto().toDto(category)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findSubCategories(long categoryId) {
        Category findItem = categoryRepository.findById(categoryId).orElseThrow();

        List<SubCategory> findList = subCategoryRepository.findSubCategoriesByCategory(findItem);

        if(findList.isEmpty()) throw new CategoryListEmptyException();

        return findList.stream()
                .map(value -> new CategoryResponseDto().toDto(value)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findLeafCategories(long categoryId, long subCategoryId) {
        SubCategory findItem = subCategoryRepository.findById(subCategoryId).orElseThrow();

        List<LeafCategory> findList = leafCategoryRepository.findLeafCategoriesBySubCategory(findItem);

        if(findList.isEmpty()) throw new CategoryListEmptyException();

        return findList.stream()
                .map(value -> new CategoryResponseDto().toDto(value)).collect(Collectors.toList());
    }

    // 카테고리 생성
    @Transactional
    public void makeCategory(CategoryCreateDto requestDto) {
        Category category = new Category(requestDto.getCategory());

        categoryRepository.save(category);
    }

    // 카테고리 삭제
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        categoryRepository.deleteById(id);
    }
}
