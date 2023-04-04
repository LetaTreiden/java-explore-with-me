package ru.practicum.explorewithme.category.service;

import java.util.List;
import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.CategoryMapper;
import ru.practicum.explorewithme.category.repository.CategoryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public CategoryDto createCategory(CategoryDto categoryDto) {
    var category = categoryRepository.save(CategoryMapper.toEntity(categoryDto));
    return CategoryMapper.toDto(category);
  }

  @Override
  public void deleteCategory(long categoryId) {
    categoryRepository.deleteById(categoryId);
  }

  @Override
  public CategoryDto updateCategory(long categoryId, CategoryDto categoryDto) {
    var category = categoryRepository.findById(categoryId).orElseThrow(NoSuchElementException::new);
    category.setName(categoryDto.getName());
    categoryRepository.save(category);
    return CategoryMapper.toDto(category);
  }

  @Override
  public List<CategoryDto> getCategories(int from, int size) {
    var sort = Sort.by("id").ascending();
    var pageable = PageRequest.of(from / size, size, sort);

    return categoryRepository.findAll(pageable).map(CategoryMapper::toDto).toList();
  }

  @Override
  public CategoryDto getCategoryById(long categoryId) {
    var category = categoryRepository.findById(categoryId).orElseThrow(NoSuchElementException::new);
    return CategoryMapper.toDto(category);
  }
}
