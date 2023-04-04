package ru.practicum.explorewithme.category.service;

import java.util.List;
import ru.practicum.explorewithme.category.dto.CategoryDto;

public interface CategoryService {

  CategoryDto createCategory(CategoryDto categoryDto);

  void deleteCategory(long categoryId);

  CategoryDto updateCategory(long categoryId, CategoryDto categoryDto);

  List<CategoryDto> getCategories(int from, int size);

  CategoryDto getCategoryById(long categoryId);
}
