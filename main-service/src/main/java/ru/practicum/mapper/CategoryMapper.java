package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.model.Category;
import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.category.NewCategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category fromNewDto(NewCategoryDto newCategoryDto);
}