package com.coutomariel.wishlist.domain.repository;

import com.coutomariel.wishlist.domain.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {
}
