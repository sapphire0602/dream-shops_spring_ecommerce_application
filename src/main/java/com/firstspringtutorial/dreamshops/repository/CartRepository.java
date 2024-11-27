package com.firstspringtutorial.dreamshops.repository;

import com.firstspringtutorial.dreamshops.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
