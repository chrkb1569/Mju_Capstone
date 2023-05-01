package mju.capstone.project.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.category.Category;
import mju.capstone.project.domain.category.LeafCategory;
import mju.capstone.project.domain.category.SubCategory;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CategoryResponseDto {
    private Long id;
    private String category;

    public CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .category(category.getCategoryName())
                .build();
    }

    public CategoryResponseDto toDto(SubCategory category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .category(category.getCategoryName())
                .build();
    }

    public CategoryResponseDto toDto(LeafCategory category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .category(category.getCategoryName())
                .build();
    }
}
