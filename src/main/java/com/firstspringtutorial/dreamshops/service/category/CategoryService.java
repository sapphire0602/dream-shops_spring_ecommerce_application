package com.firstspringtutorial.dreamshops.service.category;

import com.firstspringtutorial.dreamshops.exceptions.AlreadyExistsException;
import com.firstspringtutorial.dreamshops.exceptions.ResourceNotFoundException;
import com.firstspringtutorial.dreamshops.model.Category;
import com.firstspringtutorial.dreamshops.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    @Autowired
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save).orElseThrow(() -> new AlreadyExistsException("Category Not Found"));
    }

    @Override
    public Category updateCategory(Category category , Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());

            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository :: delete , () -> {
            throw new ResourceNotFoundException("Category Not Found");
        });
    }
}
