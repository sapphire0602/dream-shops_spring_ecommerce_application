package com.firstspringtutorial.dreamshops.request;

import com.firstspringtutorial.dreamshops.model.Category;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductUpdateRequest {
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
