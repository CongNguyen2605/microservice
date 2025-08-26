package org.example.productservice.service;

import org.example.productservice.dto.ApiResponse;
import org.example.productservice.dto.product.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ApiResponse<Page<ProductDto>> list(Pageable pageable);
}
