package com.firstspringtutorial.dreamshops.service.product;

import com.firstspringtutorial.dreamshops.model.Product;
import com.firstspringtutorial.dreamshops.request.AddProductRequest;
import com.firstspringtutorial.dreamshops.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    Product updateProduct(ProductUpdateRequest  product , Long productId);
    void deleteProductById(Long id);

    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category , String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand , String name);
    Long countProductsByBrandAndName(String brand , String name);
}
