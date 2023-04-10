package ru.practicum.explorewithme.category.service;

import java.util.List;
import ru.practicum.explorewithme.category.dto.CategoryDto;

public interface CategoryService {

  CategoryDto create(CategoryDto categoryDto);

  void delete(long categoryId);

  CategoryDto update(long categoryId, CategoryDto categoryDto);

  List<CategoryDto> getAll(int from, int size);

  CategoryDto getById(long categoryId);
}
