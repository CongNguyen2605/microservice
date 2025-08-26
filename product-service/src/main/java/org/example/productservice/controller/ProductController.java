package org.example.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.productservice.dto.ApiResponse;
import org.example.productservice.dto.product.ProductDto;
import org.example.productservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/list")
    public ApiResponse<Page<ProductDto>> list(Pageable pageable) {
        return productService.list(pageable);
    }
}
