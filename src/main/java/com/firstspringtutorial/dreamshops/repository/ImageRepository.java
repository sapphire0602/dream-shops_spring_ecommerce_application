package com.firstspringtutorial.dreamshops.repository;

import com.firstspringtutorial.dreamshops.model.Image;
import com.firstspringtutorial.dreamshops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
}
