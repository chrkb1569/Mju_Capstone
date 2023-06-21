package mju.capstone.project.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import mju.capstone.project.dto.category.CategoryCreateDto;
import mju.capstone.project.dto.category.CategoryResponseDto;
import mju.capstone.project.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .build();
    }

    @Test
    @DisplayName(value = "상위 카테고리 조회")
    public void getCategoriesTest() throws Exception {
        //given
        List<CategoryResponseDto> responseList = new ArrayList<>();

        //stub
        given(categoryService.findAll()).willReturn(responseList);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(categoryService).findAll();
    }

    @Test
    @DisplayName(value = "상위 카테고리에 포함되는 하위 카테고리 조회")
    public void getSubCategoriesTest() throws Exception {
        //given
        Long categoryId = 1L;
        List<CategoryResponseDto> responseList = new ArrayList<>();

        //stub
        given(categoryService.findSubCategories(categoryId)).willReturn(responseList);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/categories/" + categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(categoryService).findSubCategories(categoryId);
    }

    @Test
    @DisplayName(value = "최하위 카테고리 조회")
    public void getLeafCategoriesTest() throws Exception{
        //given
        Long categoryId = 1L;
        Long subCategoryId = 1L;
        List<CategoryResponseDto> responseList = new ArrayList<>();

        //stub
        given(categoryService.findLeafCategories(categoryId, subCategoryId)).willReturn(responseList);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/categories/" + categoryId + "/" + subCategoryId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(categoryService).findLeafCategories(categoryId, subCategoryId);
    }

    @Test
    @DisplayName(value = "카테고리 생성")
    public void makeCategoryTest() throws Exception {
        //given
        CategoryCreateDto createDto = new CategoryCreateDto("category");

        //stub
        willDoNothing().given(categoryService).makeCategory(any());

        //when
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //then
        verify(categoryService).makeCategory(any());
    }

    @Test
    @DisplayName(value = "카테고리 삭제")
    public void deleteCategoryTest() throws Exception {
        //given
        Long id = 1L;

        //stub
        willDoNothing().given(categoryService).deleteCategory(id);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/category/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //then
        verify(categoryService).deleteCategory(id);
    }
}
