package com.coutomariel.wishlist.api.exception;

import com.coutomariel.wishlist.domain.exception.CustomerWishlistNotFoundException;
import com.coutomariel.wishlist.domain.exception.ProductAlreadyExistsInCustomerWishlistException;
import com.coutomariel.wishlist.domain.exception.ProductNotFoundInWishlistCustomerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> map = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .forEach(m -> map.put(m.getField(), m.getDefaultMessage()));

        ApiErrorResponse apiErrorMessage = ApiErrorResponse
                .builder()
                .status(status)
                .errors(map)
                .build();

        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        ApiErrorResponse apiErrorMessage = ApiErrorResponse
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.unprocessableEntity().body(apiErrorMessage);
    }

    @ExceptionHandler(ProductAlreadyExistsInCustomerWishlistException.class)
    public ResponseEntity<Object> handleProductAlreadyExistsNotFoundException(ProductAlreadyExistsInCustomerWishlistException ex) {
        ApiErrorResponse apiErrorMessage = ApiErrorResponse
                .builder()
                .status(ex.getStatusCode())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.unprocessableEntity().body(apiErrorMessage);
    }

    @ExceptionHandler(ProductNotFoundInWishlistCustomerException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundInWishlistCustomerException ex) {
        ApiErrorResponse apiErrorMessage = ApiErrorResponse
                .builder()
                .status(ex.getStatusCode())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorMessage);
    }

    @ExceptionHandler(CustomerWishlistNotFoundException.class)
    public ResponseEntity<Object> handleCustomerWishlistNotFoundException(CustomerWishlistNotFoundException ex) {
        ApiErrorResponse apiErrorMessage = ApiErrorResponse
                .builder()
                .status(ex.getStatusCode())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorMessage);
    }

}
