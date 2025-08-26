package org.example.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.cart.CartDto;
import org.example.productservice.dto.idreponse.IdResponse;
import org.example.productservice.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping()
    public IdResponse create(@RequestBody CartDto cartDto) {
        return cartService.create(cartDto);
    }

    @GetMapping()
    public List<CartDto> getCart() {
        return cartService.getCart();
    }
}
