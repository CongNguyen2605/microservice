package org.example.productservice.service;

import org.example.productservice.dto.cart.CartDto;
import org.example.productservice.dto.idreponse.IdResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    IdResponse create(CartDto cartDto);


    List<CartDto> getCart();
}
