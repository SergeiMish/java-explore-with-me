package ru.practicum.service.interfaces.unauthenticated;

import ru.practicum.service.dto.category.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);
}
