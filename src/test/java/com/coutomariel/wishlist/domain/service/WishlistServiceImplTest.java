package com.coutomariel.wishlist.domain.service;

import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;
import com.coutomariel.wishlist.domain.exception.ProductAlreadyExistsInCustomerWishlistException;
import com.coutomariel.wishlist.domain.repository.WishlistRepository;
import com.coutomariel.wishlist.utils.MockUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @Mock
    private WishlistRepository repository;
    @InjectMocks
    private WishlistServiceImpl service;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(service, "wishlistLimitProducts", 20);
    }

    @Test
    @DisplayName("Given a valid product to add and customer without wishlist, then create a new wishlist")
    void createNewCustomerWishlistSuccessfully() {
        Product product = MockUtils.mockProduct();
        String customerId = UUID.randomUUID().toString();

        Wishlist mockSavedWishlist = Wishlist.builder()
                .id(customerId)
                .products(Collections.singletonList(product))
                .build();

        when(repository.findById(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Wishlist.class))).thenReturn(mockSavedWishlist);

        Wishlist result = service.add(customerId, product);
        List<Product> products = result.getProducts();

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals(customerId, result.getId());
        Assertions.assertEquals(customerId, result.getId());

        verify(repository, times(1)).findById(anyString());
        verify(repository, times(1)).save(any(Wishlist.class));

    }

    @Test
    @DisplayName("Given a product already exists in customer wishlist, then throws")
    void addProductAlreadyExistsThenThrows() {
        Product product = MockUtils.mockProduct();
        String customerId = UUID.randomUUID().toString();

        Wishlist mockWishlistFromRepository = Wishlist.builder()
                .id(customerId)
                .products(Collections.singletonList(product))
                .build();

        when(repository.findById(anyString())).thenReturn(Optional.of(mockWishlistFromRepository));

        Exception exception = assertThrows(ProductAlreadyExistsInCustomerWishlistException.class, () -> {
            service.add(customerId, product);
        });

        String expectedProductId = product.getProductId();
        String expectedMessage = String.format(
                "Produto ID: %s já existe na lista de desejos do cliente.",expectedProductId
        );
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }




}