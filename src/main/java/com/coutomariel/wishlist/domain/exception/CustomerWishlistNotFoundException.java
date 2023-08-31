package com.coutomariel.wishlist.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomerWishlistNotFoundException extends RuntimeException{
    public final HttpStatusCode statusCode = HttpStatus.NOT_FOUND;
    public CustomerWishlistNotFoundException(String message) {
        super(message);
    }
}
