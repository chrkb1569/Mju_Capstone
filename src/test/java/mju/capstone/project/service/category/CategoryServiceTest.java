package mju.capstone.project.service.category;

import mju.capstone.project.domain.category.Category;
import mju.capstone.project.domain.category.LeafCategory;
import mju.capstone.project.domain.category.SubCategory;
import mju.capstone.project.dto.category.CategoryCreateDto;
import mju.capstone.project.dto.category.CategoryResponseDto;
import mju.capstone.project.repository.category.CategoryRepository;
import mju.capstone.project.repository.category.LeafCategoryRepository;
import mju.capstone.project.repository.category.SubCategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private LeafCategoryRepository leafCategoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName(value = "findAll() - 최상위 카테고리 전체 조회")
    public void findAllTest() {
        //given
        List<Category> categories = new ArrayList<>();
        Category category = new Category(1L, "category1");

        categories.add(category);

        //stub
        given(categoryRepository.findAll()).willReturn(categories);

        //when
        List<CategoryResponseDto> findList = categoryService.findAll();

        //then
        Assertions.assertThat(findList).isNotEmpty();
        verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName(value = "findSubCategories() - 중간 카테고리 전체 조회")
    public void findSubCategoriesTest() {
        //given
        Category category = new Category(1L, "category1");

        List<SubCategory> categories = new ArrayList<>();
        SubCategory subCategory1 = new SubCategory(1L, "subCategory1", category);
        SubCategory subCategory2 = new SubCategory(2L, "subCategory2", category);

        categories.add(subCategory1);
        categories.add(subCategory2);

        //stub
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));
        given(subCategoryRepository.findSubCategoriesByCategory(any())).willReturn(categories);

        //when
        List<CategoryResponseDto> categoryList = categoryService.findSubCategories(category.getId());

        //then
        Assertions.assertThat(categoryList.size()).isEqualTo(2);
        verify(categoryRepository).findById(any());
        verify(subCategoryRepository).findSubCategoriesByCategory(any());
    }

    @Test
    @DisplayName(value = "findLeafCategories() - 최하위 카테고리 전체 조회")
    public void findLeafCategoriesTest() {
        //given
        Category category = new Category(1L, "category");
        SubCategory subCategory = new SubCategory(1L, "subCategory", category);

        List<LeafCategory> categories = new ArrayList<>();
        LeafCategory leafCategory1 = new LeafCategory(1L, "leafCategory1", subCategory);
        LeafCategory leafCategory2 = new LeafCategory(2L, "leafCategory2", subCategory);

        categories.add(leafCategory1);
        categories.add(leafCategory2);

        //stub
        given(subCategoryRepository.findById(any())).willReturn(Optional.of(subCategory));
        given(leafCategoryRepository.findLeafCategoriesBySubCategory(any())).willReturn(categories);

        //when
        List<CategoryResponseDto> categoryList = categoryService.findLeafCategories(category.getId(), subCategory.getId());

        //then
        Assertions.assertThat(categoryList.size()).isEqualTo(2);
        verify(subCategoryRepository).findById(any());
        verify(leafCategoryRepository).findLeafCategoriesBySubCategory(any());
    }

    @Test
    @DisplayName(value = "makeCategory() - 카테고리 생성 테스트")
    public void makeCategoryTest() {
        //given
        CategoryCreateDto createDto = new CategoryCreateDto("Test Category");

        //stub
        given(categoryRepository.save(any())).willReturn(any());

        //when
        categoryService.makeCategory(createDto);

        //then
        verify(categoryRepository).save(any());
    }

    @Test
    @DisplayName(value = "deleteCategory() - 카테고리 삭제 테스트")
    public void deleteCategoryTest() {
        //given
        Category category = new Category(1L, "category");

        //stub
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));
        willDoNothing().given(categoryRepository).deleteById(any());

        //when
        categoryService.deleteCategory(any());

        //then
        verify(categoryRepository).deleteById(any());
    }
}
