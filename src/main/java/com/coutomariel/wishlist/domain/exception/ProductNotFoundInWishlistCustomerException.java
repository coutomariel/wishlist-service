package com.coutomariel.wishlist.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class ProductNotFoundInWishlistCustomerException extends RuntimeException{
    public final HttpStatusCode statusCode = HttpStatus.NOT_FOUND;
    public ProductNotFoundInWishlistCustomerException(String mensagem) {
        super(mensagem);
    }
}
