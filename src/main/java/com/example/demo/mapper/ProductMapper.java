package com.example.demo.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
@Component
public class ProductMapper implements Function<Product, ProductDTO> {

    @Override
    public ProductDTO apply(Product product) {

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getName(),
                product.getRating(),
                product.getImage());
    }
}