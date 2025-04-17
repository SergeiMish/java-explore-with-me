package ru.practicum.service.impl.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.category.NewCategoryDto;
import ru.practicum.service.interfaces.admin.AdminCategoryService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {

        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            log.warn("Attempt to create category with existing name: {}", newCategoryDto.getName());
            throw new ConflictException("Category with this name already exists");
        }
        log.info("Create new category: {}", newCategoryDto.getName());
        Category category = categoryMapper.fromNewDto(newCategoryDto);
        log.info("Create new category: {}", category.getName());
        Category savedCategory = categoryRepository.save(category);
        log.info("Created new category with id: {}", savedCategory.getId());
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Attempt to delete non-existent category with id: {}", categoryId);
                    return new NotFoundException("Category with id " + categoryId + " not found");
                });

        if (eventRepository.existsByCategoryId(categoryId)) {
            log.warn("Attempt to delete category {} with linked events", categoryId);
            throw new ConflictException("The category is not empty");
        }

        categoryRepository.delete(category);
        log.info("Deleted category with id: {}", categoryId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long categoryId, NewCategoryDto newCategoryDto) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Attempt to update non-existent category with id: {}", categoryId);
                    return new NotFoundException("Category with id " + categoryId + " not found");
                });

        if (category.getName().equals(newCategoryDto.getName())) {
            return categoryMapper.toDto(category);
        }

        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            log.warn("Attempt to update category to existing name: {}", newCategoryDto.getName());
            throw new ConflictException("Category with this name already exists");
        }

        category.setName(newCategoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        log.info("Updated category with id: {}", categoryId);
        return categoryMapper.toDto(updatedCategory);
    }

}