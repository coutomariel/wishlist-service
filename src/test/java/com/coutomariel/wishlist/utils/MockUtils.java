package com.coutomariel.wishlist.utils;

import com.coutomariel.wishlist.api.request.ProductRequest;
import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class MockUtils {
    public static Product mockProduct() {
        return Product.builder()
                .productId(UUID.randomUUID().toString())
                .name("product name")
                .description("description")
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static Wishlist mockWishlist(String customerId, ArrayList<Product> list) {
        return Wishlist.builder()
                .id(customerId)
                .products(list)
                .build();
    }

    public static ProductRequest mockProductRequest() {
        return ProductRequest.builder()
                .productId(UUID.randomUUID().toString())
                .name("product name")
                .description("description")
                .build();
    }


}
