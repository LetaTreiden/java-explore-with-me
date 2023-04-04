package ru.practicum.explorewithme.category;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.service.CategoryService;

@RestController
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping("/admin/categories")
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
    return categoryService.createCategory(categoryDto);
  }

  @DeleteMapping("/admin/categories/{categoryId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCategory(@PathVariable long categoryId) {
    categoryService.deleteCategory(categoryId);
  }

  @PatchMapping("/admin/categories/{categoryId}")
  public CategoryDto updateCategory(@PathVariable long categoryId, @Valid @RequestBody CategoryDto categoryDto) {
    return categoryService.updateCategory(categoryId, categoryDto);
  }

  @GetMapping("/categories")
  public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
      @RequestParam(defaultValue = "10") int size) {
    return categoryService.getCategories(from, size);
  }

  @GetMapping("/categories/{categoryId}")
  public CategoryDto getCategoryById(@PathVariable long categoryId) {
    return categoryService.getCategoryById(categoryId);
  }
}
