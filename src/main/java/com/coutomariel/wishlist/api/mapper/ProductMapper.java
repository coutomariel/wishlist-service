package com.coutomariel.wishlist.api.mapper;

import com.coutomariel.wishlist.api.request.ProductRequest;
import com.coutomariel.wishlist.api.response.ProductResponse;
import com.coutomariel.wishlist.domain.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public Product mapToProductEntity(String customerId, ProductRequest request) {
        return Product
                .builder()
                .customerId(customerId)
                .id(request.getId())
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public ProductResponse mapToProductResponse(Product entity) {
        return ProductResponse
                .builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .description(entity.getDescription())
                .build();
    }

    public List<ProductResponse> mapToProductResponseList(List<Product> productList) {
        return productList.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }


}
