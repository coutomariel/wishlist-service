package com.coutomariel.wishlist.domain.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class ProductAlreadyExistsInCustomerWishlistException extends RuntimeException {
    private final HttpStatusCode statusCode = HttpStatus.UNPROCESSABLE_ENTITY;
    public ProductAlreadyExistsInCustomerWishlistException(String productId) {
        super(String.format("Produto ID: %s já existe na lista de desejos do cliente.", productId));
    }
}
