package com.coutomariel.wishlist.domain.service;

import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;
import com.coutomariel.wishlist.domain.exception.CustomerWishlistNotFoundException;
import com.coutomariel.wishlist.domain.exception.ProductAlreadyExistsInCustomerWishlistException;
import com.coutomariel.wishlist.domain.exception.ProductNotFoundInWishlistCustomerException;
import com.coutomariel.wishlist.domain.repository.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WishlistServiceImpl implements WishlistService {

    private final Integer wishlistLimitProducts;
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

        if (wishlistIsFull(products)) {
            log.info("wish list is full.");
            removeOldestProduct(products);
        }

        products.add(product);
        return repository.save(wishlist);
    }

    private boolean wishlistIsFull(List<Product> products) {
        return products.size() >= wishlistLimitProducts;
    }

    private void removeOldestProduct(List<Product> products) {
        log.warn("Removing oldest product.");
        products.remove(0);
    }

    @Override
    @Transactional
    public void remove(String customerId, String productId) {
        Wishlist wishlist = repository.findByIdAndProduct(customerId, productId)
                .orElseThrow(() -> new ProductNotFoundInWishlistCustomerException(String.format(
                        "O produto ID:%s não está associado com uma lista do cliente ID:%s.", productId, customerId)));

        List<Product> products = wishlist.getProducts();
        if (products.size()==1){
            repository.delete(wishlist);
            return;
        }

        products.removeIf(p -> p.getProductId().equals(productId));
        repository.save(wishlist);
    }

    @Override
    public Wishlist getCustomerWishlist(String customerId) {
        return repository.findById(customerId).orElseThrow(()-> new CustomerWishlistNotFoundException(String.format(
                "Não foi encontrada wishlist associada ao customerID $s", customerId)
        ));
    }

    @Override
    public Optional<Product> getProductByIdInCustomerWishList(String customerId, String productId) {
        Wishlist wishlist = repository.findById(customerId)
                .orElseThrow(() -> new CustomerWishlistNotFoundException(String.format(
                        "O produto ID:%s não está associado com uma lista do cliente ID:%s.", productId, customerId)));

        return wishlist.getProducts().stream()
                .filter(product -> product.getProductId().equals(productId))
                .findFirst();
    }
}
