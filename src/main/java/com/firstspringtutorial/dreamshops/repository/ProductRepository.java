package com.firstspringtutorial.dreamshops.repository;
import com.firstspringtutorial.dreamshops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String category);
    List<Product> findByCategoryBrand(String brand);
    List<Product> findByCategoryNameAndBrand(String category, String brand);
    List<Product> findByName(String name);
    List<Product> findByBrandAndName(String brand, String name);

    Long countByBrandAndName(String brand, String name);
    // Define custom query methods if needed
}
