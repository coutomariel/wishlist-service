package com.coutomariel.wishlist.domain.service;

import com.coutomariel.wishlist.domain.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService{
    @Override
    public Product add(Product product) {
        return null;
    }

    @Override
    public void remove(String customerId, String productId) {

    }

    @Override
    public List<Product> getCustomerWishlist(String customerId) {
        return null;
    }

    @Override
    public Optional<Product> getProductByIdInCustomerWishList(String customerId, String productId) {
        return Optional.empty();
    }
}
