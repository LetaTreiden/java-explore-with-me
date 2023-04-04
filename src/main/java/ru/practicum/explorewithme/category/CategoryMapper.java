package ru.practicum.explorewithme.category;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

  public static Category toCategory(CategoryDto categoryDto) {
    return new Category(categoryDto.getId(), categoryDto.getName());
  }

  public static CategoryDto toDto(Category category) {
    return new CategoryDto(category.getId(), category.getName());
  }
}
