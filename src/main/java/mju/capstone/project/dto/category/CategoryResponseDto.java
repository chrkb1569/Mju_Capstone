package mju.capstone.project.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.project.domain.category.Category;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CategoryResponseDto {
    private String category;

    public CategoryResponseDto toDto(Category category) {
        return CategoryResponseDto.builder()
                .category(category.getCategoryName())
                .build();
    }
}
