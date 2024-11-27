package com.firstspringtutorial.dreamshops.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
//    private String brand;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> products;


public Category(String name) {
    this.name = name;
    this.products = new ArrayList<>(); // Initialize products list
}
}

