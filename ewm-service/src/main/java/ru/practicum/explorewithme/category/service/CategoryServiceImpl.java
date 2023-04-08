package ru.practicum.explorewithme.category.service;

import java.util.List;
import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.CategoryMapper;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  @Transactional
  public CategoryDto create(CategoryDto categoryDto) {
    Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
    return CategoryMapper.toDto(category);
  }

  @Override
  @Transactional
  public void delete(long categoryId) {
    categoryRepository.deleteById(categoryId);
  }

  @Override
  @Transactional
  public CategoryDto update(long categoryId, CategoryDto categoryDto) {
    Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchElementException());
    category.setName(categoryDto.getName());
    categoryRepository.save(category);
    return CategoryMapper.toDto(category);
  }

  @Override
  public List<CategoryDto> getAll(int from, int size) {
    Sort sort = Sort.by("id").ascending();
    PageRequest pageable = PageRequest.of(from / size, size, sort);

    return categoryRepository.findAll(pageable).map(CategoryMapper::toDto).toList();
  }

  @Override
  public CategoryDto getById(long categoryId) {
    Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchElementException());
    return CategoryMapper.toDto(category);
  }
}
