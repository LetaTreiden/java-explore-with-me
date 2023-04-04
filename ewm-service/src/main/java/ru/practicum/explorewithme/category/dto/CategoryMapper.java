package ru.practicum.explorewithme.category.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.model.CategoryEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

  public static CategoryEntity toEntity(CategoryDto categoryDto) {
    return new CategoryEntity(categoryDto.getId(), categoryDto.getName());
  }

  public static CategoryDto toDto(CategoryEntity categoryEntity) {
    return new CategoryDto(categoryEntity.getId(), categoryEntity.getName());
  }
}
