package org.example.productservice.mapper;

import org.example.productservice.dto.product.ProductDto;
import org.example.productservice.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(ProductEntity products);
}
