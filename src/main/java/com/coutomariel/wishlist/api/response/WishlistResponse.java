package com.coutomariel.wishlist.api.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WishlistResponse {
    private String customerId;
    private List<ProductResponse> products;
}
