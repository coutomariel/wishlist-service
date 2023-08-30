package com.coutomariel.wishlist.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(of="productId")
public class Product {
    private String productId;
    private String name;
    private String description;
    private LocalDateTime createdOn;
}
