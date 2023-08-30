package com.coutomariel.wishlist.domain.service;

import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;
import com.coutomariel.wishlist.domain.exception.ProductAlreadyExistsInCustomerWishlistException;
import com.coutomariel.wishlist.domain.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

    private Integer wishlistLimitProducts;
    private final WishlistRepository repository;

    public WishlistServiceImpl(@Value("${wishlist.limit-products}") Integer wishlistLimitProducts, WishlistRepository repository) {
        this.wishlistLimitProducts = wishlistLimitProducts;
        this.repository = repository;
    }

    @Override
    public Wishlist add(String customerId, Product product) {
        Wishlist wishlist  = repository.findById(customerId)
                .orElse(Wishlist.builder()
                        .id(customerId)
                        .products(new LinkedList<>()).build());

        List<Product> products = wishlist.getProducts();
        if (products.contains(product)) throw new ProductAlreadyExistsInCustomerWishlistException(product.getProductId());

        if (wishlistIsFull(products)) removeOldestProduct(products);

        products.add(product);
        return repository.save(wishlist);
    }

    private boolean wishlistIsFull(List<Product> products) {
        return products.size() >= wishlistLimitProducts;
    }

    private void removeOldestProduct(List<Product> products) {
        products.remove(0);
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
