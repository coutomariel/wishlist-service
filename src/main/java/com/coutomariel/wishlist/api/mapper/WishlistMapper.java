package com.coutomariel.wishlist.api.mapper;

import com.coutomariel.wishlist.api.request.ProductRequest;
import com.coutomariel.wishlist.api.response.ProductResponse;
import com.coutomariel.wishlist.api.response.WishlistResponse;
import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WishlistMapper {

    public Product mapToProductEntity(ProductRequest request) {
        return Product
                .builder()
                .productId(request.getProductId())
                .name(request.getName())
                .description(request.getDescription())
                .createdOn(LocalDateTime.now())
                .build();
    }

    public ProductResponse mapToProductResponse(Product entity) {
        return ProductResponse
                .builder()
                .id(entity.getProductId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public List<ProductResponse> mapToProductResponseList(List<Product> productList) {
        return productList.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    public WishlistResponse mapToWishlistResponse(Wishlist wishlist) {
        List<ProductResponse> productResponses = mapToProductResponseList(wishlist.getProducts());
        return WishlistResponse
                .builder()
                .customerId(wishlist.getId())
                .products(productResponses)
                .build();
    }
}
