package com.coutomariel.wishlist.domain.service;

import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;

import java.util.List;
import java.util.Optional;


public interface WishlistService {
    Wishlist add(String customerId, Product product);
    void remove(String customerId, String productId);
    List<Product> getCustomerWishlist(String customerId);
    Optional<Product> getProductByIdInCustomerWishList(String customerId, String productId);
}
