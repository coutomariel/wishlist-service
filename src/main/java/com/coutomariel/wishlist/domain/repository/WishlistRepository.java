package com.coutomariel.wishlist.domain.repository;

import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    @Query(value = "{'id' : ?0 ,'products' : {$elemMatch : {'productId' : ?1} }}")
    Optional<Wishlist> findByIdAndProduct(String customerId, String productId);

}
