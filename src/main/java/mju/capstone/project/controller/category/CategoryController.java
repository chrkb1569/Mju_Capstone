package mju.capstone.project.controller.category;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import mju.capstone.project.dto.category.CategoryCreateDto;
import mju.capstone.project.response.Response;
import mju.capstone.project.service.category.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    // 전체 카테고리 조회
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "전체 카테고리 조회", notes = "현재 데이터베이스에 저장된 모든 카테고리를 조회하는 로직")
    public Response getCategories() {
        return Response.success(categoryService.findAll());
    }

    @GetMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "서브 카테고리 조회", notes = "특정 카테고리의 서브 카테고리 조회")
    @ApiImplicitParam(name = "categoryId", value = "조회하기위한 특정 카테고리의 PK값", example = "1")
    public Response getSubCategories(@PathVariable long categoryId) {
        return Response.success(categoryService.findSubCategories(categoryId));
    }

    @GetMapping("/categories/{categoryId}/{subCategoryId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "리프 카테고리 조회", notes = "리프 카테고리들을 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "조회할 최상위 카테고리의 PK값", example = "1"),
            @ApiImplicitParam(name = "subCategoryId", value = "조회할 세부 카테고리의 PK값", example = "1")
    })
    public Response getLeafCategories(@PathVariable long categoryId, @PathVariable long subCategoryId) {
        return Response.success(categoryService.findLeafCategories(categoryId, subCategoryId));
    }

    // 카테고리 생성
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "카테고리 추가", notes = "품목을 분류하기 위한 카테고리를 추가하는 로직")
    @ApiImplicitParam(name = "requestDto", value = "카테고리를 추가하기 력위한 값을 입력")
    public void makeCategory(@RequestBody @Valid CategoryCreateDto requestDto) {
        categoryService.makeCategory(requestDto);
    }

    // 특정 카테고리 삭제
    @DeleteMapping("/category/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "카테고리 삭제", notes = "id에 해당하는 카테고리를 삭제하는 로직")
    @ApiImplicitParam(name = "id", value = "삭제할 카테고리의 id", example = "1")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
