package com.coutomariel.wishlist.domain.service;

import com.coutomariel.wishlist.domain.domain.Product;

import java.util.List;
import java.util.Optional;


public interface WishlistService {
    Product add(Product product);
    void remove(String customerId, String productId);

    List<Product> getCustomerWishlist(String customerId);
    Optional<Product> getProductByIdInCustomerWishList(String customerId, String productId);
}
