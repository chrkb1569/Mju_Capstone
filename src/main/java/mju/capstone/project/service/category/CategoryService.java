package mju.capstone.project.service.category;

import lombok.RequiredArgsConstructor;
import mju.capstone.project.domain.category.Category;
import mju.capstone.project.dto.category.CategoryCreateDto;
import mju.capstone.project.dto.category.CategoryResponseDto;
import mju.capstone.project.exception.category.CategoryListEmptyException;
import mju.capstone.project.exception.category.CategoryNotFoundException;
import mju.capstone.project.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 전체 조회
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {

        List<Category> findCategories = categoryRepository.findAll();

        if(findCategories.isEmpty()) throw new CategoryListEmptyException();

        return findCategories.stream()
                .map(category -> new CategoryResponseDto().toDto(category)).collect(Collectors.toList());
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
