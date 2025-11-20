package org.example.productservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.productservice.dto.cart.CartDto;
import org.example.productservice.dto.idreponse.IdResponse;
import org.example.productservice.entity.CartEntity;
import org.example.productservice.entity.ProductEntity;
import org.example.productservice.exception.AppException;
import org.example.productservice.exception.ErrorCode;
import org.example.productservice.mapper.CartMapper;
import org.example.productservice.repository.CartRepository;
import org.example.productservice.repository.ProductRepository;
import org.example.productservice.service.CartService;
import org.example.productservice.util.AuthUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final AuthUtils authUtils;


    @Override
    public IdResponse create(CartDto cartDto) {
        Long userId = authUtils.getCurrentUserId();
        ProductEntity productEntity = productRepository.findById(cartDto.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        CartEntity cart = cartMapper.toEntity(cartDto);
        cart.setUserId(userId);
        cart.setProduct(productEntity);
        cartRepository.save(cart);

        return IdResponse.builder()
                .id(cart.getId())
                .build();
    }

    @Override
    public List<CartDto> getCart() {
        Long userId = authUtils.getCurrentUserId();
        return cartRepository.findByUserId(Long.valueOf(userId));
    }


}
