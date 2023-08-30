package com.coutomariel.wishlist.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "wishlist")
public class Wishlist {
    @Id
    private String id;
    private List<Product> products;
}
