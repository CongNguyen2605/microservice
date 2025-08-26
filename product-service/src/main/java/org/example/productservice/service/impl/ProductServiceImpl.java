package org.example.productservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.ApiResponse;
import org.example.productservice.dto.product.ProductDto;
import org.example.productservice.entity.ProductEntity;
import org.example.productservice.mapper.ProductMapper;
import org.example.productservice.repository.ProductRepository;
import org.example.productservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ApiResponse<Page<ProductDto>> list(Pageable pageable) {
        Page<ProductEntity> products = productRepository.findAll(pageable);
        return new ApiResponse<>(products.map(productMapper::toDto));
    }
}
