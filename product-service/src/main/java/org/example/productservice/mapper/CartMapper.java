package org.example.productservice.mapper;

import org.example.productservice.dto.cart.CartDto;
import org.example.productservice.entity.CartEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CartMapper {
    CartEntity toEntity(CartDto cartDto);

}
