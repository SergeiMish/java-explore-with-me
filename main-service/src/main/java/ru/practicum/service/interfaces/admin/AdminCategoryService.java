package ru.practicum.service.interfaces.admin;

import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.category.NewCategoryDto;

import java.util.List;

public interface AdminCategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);
    void deleteCategory(Long categoryId);
    CategoryDto updateCategory(Long categoryId, NewCategoryDto newCategoryDto);

}
