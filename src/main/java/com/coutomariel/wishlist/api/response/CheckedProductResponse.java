package com.coutomariel.wishlist.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckedProductResponse {
    private Boolean exists;
    private ProductResponse product;
}
