package com.coutomariel.wishlist.utils;

import com.coutomariel.wishlist.api.request.ProductRequest;
import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;

import java.time.LocalDateTime;
import java.util.*;

public class MockUtils {
    public static Product mockProduct() {
        return Product.builder()
                .productId(randomId())
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
                .productId(randomId())
                .name("product name")
                .description("description")
                .build();
    }


    public static List<Product> mockProductList(Integer qtd) {
        List<Product> products = new LinkedList<>();
        for (int i = 0; i < qtd; i++) {
            products.add(MockUtils.mockProduct());
        }
        return products;
    }

    public static String randomId() {
        return UUID.randomUUID().toString();
    }
}
