package org.example.productservice.repository;

import org.example.productservice.dto.cart.CartDto;
import org.example.productservice.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    List<CartDto> findByUserId(Long userId);
}
