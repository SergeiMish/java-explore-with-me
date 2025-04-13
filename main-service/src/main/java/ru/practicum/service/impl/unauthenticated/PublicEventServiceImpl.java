package ru.practicum.service.impl.unauthenticated;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.dto.category.CategoryDto;

import java.util.List;
import java.util.stream.Collectors;

public class PublicEventServiceImpl {

//    @Transactional(readOnly = true)
//    public List<CategoryDto> getCategories(int from, int size) {
//        PageRequest page = PageRequest.of(from / size, size);
//        return categoryRepository.findAll(page)
//                .stream()
//                .map(categoryMapper::toDto)
//                .collect(Collectors.toList());
//    }
}
