package com.coutomariel.wishlist.api.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckedProductResponse {
    private Boolean exists;
    private ProductResponse productResponse;
}
