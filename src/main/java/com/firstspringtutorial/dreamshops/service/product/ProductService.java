package com.firstspringtutorial.dreamshops.service.product;
import com.firstspringtutorial.dreamshops.request.ProductUpdateRequest;
import jakarta.persistence.*;
import com.firstspringtutorial.dreamshops.exceptions.productNotFoundException;
import com.firstspringtutorial.dreamshops.model.Category;
import com.firstspringtutorial.dreamshops.model.Product;
import com.firstspringtutorial.dreamshops.repository.CategoryRepository;
import com.firstspringtutorial.dreamshops.repository.ProductRepository;
import com.firstspringtutorial.dreamshops.request.AddProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    @Autowired
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                }
        );
        request.setCategory(category);
        return productRepository.save(createProduct(request ,category));
    }

    private Product createProduct(AddProductRequest request , Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }
    @Override
    public Product getProductById(Long id) {
        return  productRepository.findById(id).orElseThrow(() -> new productNotFoundException("Product not found!"));
    }

    public Product updateProduct(ProductUpdateRequest request , Long productId) {
        return productRepository.findById(productId)
                .map((existingProduct) -> updateExistingProduct(request, existingProduct))
                .map(productRepository :: save)
                .orElseThrow(() -> new productNotFoundException("Product not found!"));
    }
        private Product updateExistingProduct(ProductUpdateRequest request , Product existingProduct){

            existingProduct.setName(request.getName());
            existingProduct.setBrand(request.getBrand());
            existingProduct.setPrice(request.getPrice());
            existingProduct.setInventory(request.getInventory());
            existingProduct.setDescription(request.getDescription());
            Category category = categoryRepository.findByName(request.getCategory().getName());
            existingProduct.setCategory(category);

            return existingProduct;
    }


    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete , () -> {throw new productNotFoundException("Product not found!"); } ); //product -> productRepository.delete(product); instead of ::
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByCategoryBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override 
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
}
