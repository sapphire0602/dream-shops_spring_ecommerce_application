package com.firstspringtutorial.dreamshops.controller;

import com.firstspringtutorial.dreamshops.DTO.ProductDto;
import com.firstspringtutorial.dreamshops.exceptions.AlreadyExistsException;
import com.firstspringtutorial.dreamshops.exceptions.ResourceNotFoundException;
import com.firstspringtutorial.dreamshops.model.Category;
import com.firstspringtutorial.dreamshops.model.Product;
import com.firstspringtutorial.dreamshops.request.AddProductRequest;
import com.firstspringtutorial.dreamshops.request.ProductUpdateRequest;
import com.firstspringtutorial.dreamshops.response.ApiResponse;
import com.firstspringtutorial.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error : PRODUCT NOT FOUND", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product theProduct = productService.addProduct(product);
            ProductDto productDto =productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("PRODUCT ADDED SUCCESSFULLY", product));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId) {
        try {
            Product theProduct = productService.updateProduct(request, productId);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", theProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", productId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@PathVariable String brandName, @PathVariable String productName) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("NO PRODUCT FOUND WITH THAT BRAND AND NAME", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage() , null));
        }
    }

    @GetMapping("/product/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("NO PRODUCT FOUND WITH THAT CATEGORY AND BRAND", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage() , null));
        }
    }

    @GetMapping("/product/by-name")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("NO PRODUCT FOUND WITH THAT NAME ", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage() , null));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("NO PRODUCT FOUND WITH THAT BRAND ", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage() , null));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("NO PRODUCT FOUND WITH THAT CATEGORY ", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("SUCCESS", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage() , null));
        }
    }

    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand , @RequestParam String name){
        try {
            var productCount = productService.countProductsByBrandAndName(brand , name);
            return ResponseEntity.ok(new ApiResponse("PRODUCT COUNTED!" , productCount));
        } catch (Exception e) {
           return ResponseEntity.ok(new ApiResponse(e.getMessage() , null));
        }
    }
}
