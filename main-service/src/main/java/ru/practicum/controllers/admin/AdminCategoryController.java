package ru.practicum.controllers.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.category.NewCategoryDto;
import ru.practicum.service.interfaces.admin.AdminCategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        CategoryDto createNewCategoryDto = adminCategoryService.createCategory(newCategoryDto);
        return ResponseEntity.status(201).body(createNewCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        adminCategoryService.deleteCategory(catId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long catId,
            @Valid @RequestBody NewCategoryDto updateDto) {
        CategoryDto updatedCategory = adminCategoryService.updateCategory(catId, updateDto);
        return ResponseEntity.ok(updatedCategory);
    }
}
