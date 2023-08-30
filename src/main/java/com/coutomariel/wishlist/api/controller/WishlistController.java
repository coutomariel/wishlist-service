package com.coutomariel.wishlist.api.controller;

import com.coutomariel.wishlist.api.mapper.WishlistMapper;
import com.coutomariel.wishlist.api.request.ProductRequest;
import com.coutomariel.wishlist.api.response.CheckedProductResponse;
import com.coutomariel.wishlist.api.response.ProductResponse;
import com.coutomariel.wishlist.api.response.WishlistResponse;
import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;
import com.coutomariel.wishlist.domain.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController implements WishlistControllerContract{

    private final WishlistService service;
    private final WishlistMapper mapper;

    private static final String ROUTE_ADD_PRODUCT = "/{customerId}/products";
    private static final String ROUTE_REMOVE_PRODUCT = "/{customerId}/products/{productId}";
    private static final String ROUTE_SEARCH_CUSTOMER_WISHLIST = "/{customerId}/products";
    private static final String ROUTE_CHECK_PRODUCT_IN_CUSTOMER_WISHLIST = "/{customer_id}/products/{product_id}";

    @Override
    @PostMapping(ROUTE_ADD_PRODUCT)
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistResponse addProduct(@PathVariable String customerId, @Valid @RequestBody ProductRequest request) {
        Wishlist wishlist = service.add(customerId, mapper.mapToProductEntity(request));
        return mapper.mapToWishlistResponse(wishlist);
    }

    @Override
    @DeleteMapping(ROUTE_REMOVE_PRODUCT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProduct(@PathVariable String customerId, @PathVariable String productId) {
        service.remove(customerId, productId);
    }

    @Override
    @GetMapping(ROUTE_SEARCH_CUSTOMER_WISHLIST)
    public List<ProductResponse> getCustomerWishlist(@PathVariable String customerId){
        return mapper.mapToProductResponseList(service.getCustomerWishlist(customerId));
    }

    @Override
    @GetMapping(ROUTE_CHECK_PRODUCT_IN_CUSTOMER_WISHLIST)
    public CheckedProductResponse checkProductInCustomerWishlist(
            @PathVariable String customerId, @PathVariable String productId
    ) {
        Optional<Product> product = service.getProductByIdInCustomerWishList(customerId, productId);
        Optional<ProductResponse> productResponse = product.map(mapper::mapToProductResponse);
        return CheckedProductResponse
                .builder()
                .exists(product.isPresent())
                .productResponse(productResponse.orElse(null))
                .build();
    }

}
