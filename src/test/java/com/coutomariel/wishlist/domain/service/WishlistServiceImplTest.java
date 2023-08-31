package com.coutomariel.wishlist.domain.service;

import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;
import com.coutomariel.wishlist.domain.exception.CustomerWishlistNotFoundException;
import com.coutomariel.wishlist.domain.exception.ProductAlreadyExistsInCustomerWishlistException;
import com.coutomariel.wishlist.domain.exception.ProductNotFoundInWishlistCustomerException;
import com.coutomariel.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.coutomariel.wishlist.utils.MockUtils.*;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
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
        Product product = mockProduct();
        String customerId = randomId();

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
        Product product = mockProduct();
        String customerId = randomId();

        Wishlist mockWishlistFromRepository = Wishlist.builder()
                .id(customerId)
                .products(Collections.singletonList(product))
                .build();

        when(repository.findById(anyString())).thenReturn(Optional.of(mockWishlistFromRepository));

        Exception exception = assertThrows(ProductAlreadyExistsInCustomerWishlistException.class, () -> {
            service.add(customerId, product);
        });

        String expectedProductId = product.getProductId();
        String expectedMessage = format(
                "Produto ID: %s já existe na lista de desejos do cliente.", expectedProductId
        );
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    @DisplayName("Given a remove product request, whem wishlist just a one item and its match, then delete wihlist")
    void removeUniqueProductFromListAndDeleteIt() {
        String customerId = randomId();
        Product product = mockProduct();

        Wishlist mockWishlistFromRepository = Wishlist.builder()
                .id(customerId)
                .products(Collections.singletonList(product))
                .build();

        when(repository.findByIdAndProduct(anyString(), anyString()))
                .thenReturn(Optional.of(mockWishlistFromRepository));

        doNothing().when(repository).delete(any(Wishlist.class));

        service.remove(customerId, product.getProductId());

        verify(repository, times(1)).findByIdAndProduct(anyString(), anyString());
        verify(repository, times(1)).delete(any(Wishlist.class));
        verify(repository, times(0)).save(any(Wishlist.class));

    }

    @Test
    @DisplayName("Given a remove product request, whem wishlist has many items, then remove it from wishlist")
    void removeProductFroWishilistWithMoreItems() {
        String customerId = randomId();
        List<Product> products = mockProductList(2);
        Wishlist mockWishlistFromRepository = Wishlist.builder()
                .id(customerId)
                .products(products)
                .build();

        when(repository.findByIdAndProduct(anyString(), anyString()))
                .thenReturn(Optional.of(mockWishlistFromRepository));

        when(repository.save(any(Wishlist.class))).thenReturn(null);

        service.remove(customerId, customerId);

        verify(repository, times(1)).findByIdAndProduct(anyString(), anyString());
        verify(repository, times(0)).delete(any(Wishlist.class));
        verify(repository, times(1)).save(any(Wishlist.class));
    }

    @Test
    @DisplayName("Given a product not present in customer wishlist, when try to remove, then throws")
    void notFoundProdutToRemove() {
        Product product = mockProduct();
        String customerId = randomId();

        when(repository.findByIdAndProduct(anyString(), anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundInWishlistCustomerException.class, () -> {
            service.remove(customerId, product.getProductId());
        });

        String expectedMessage = format(
                "O produto ID:%s não está associado com uma lista do cliente ID:%s.",
                product.getProductId(), customerId
        );

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(repository, times(1)).findByIdAndProduct(anyString(), anyString());
        verify(repository, times(0)).delete(any(Wishlist.class));
        verify(repository, times(0)).save(any(Wishlist.class));

    }

    @Test
    @DisplayName("Given a search for exists wishlist by customerId, then returns it")
    void getCustomerWishlist() {
        Integer wishlistSize = 2;
        String customerId = randomId();
        List<Product> products = mockProductList(wishlistSize);
        Wishlist mockWishlistFromRepository = Wishlist.builder()
                .id(customerId)
                .products(products)
                .build();

        when(repository.findById(customerId)).thenReturn(Optional.of(mockWishlistFromRepository));

        Wishlist result = service.getCustomerWishlist(customerId);
        Assertions.assertEquals(customerId, result.getId());
        Assertions.assertEquals(wishlistSize, result.getProducts().size());

        verify(repository, times(1)).findById(customerId);
    }


    @Test
    @DisplayName("Given a search for a not exists wishlist by customerId, then throws")
    void notFoundCustomerWishlist() {
        String customerId = randomId();
        when(repository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerWishlistNotFoundException.class, () -> {
            service.getCustomerWishlist(customerId);
        });

        String expectedMessage = format("Não foi encontrada wishlist associada ao customerID $s", customerId);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(repository, times(1)).findById(customerId);
    }


    @Test
    @DisplayName("Given a search product exists in customer wishlist, when be found, then returns it")
    void getCustomerWishlistCheckingProduct() {
        String customerId = randomId();

        Integer wishlistSize = 2;
        String searchedProductId = "SEARCH";
        List<Product> products = mockProductList(wishlistSize);
        products.add(Product.builder()
                .productId(searchedProductId)
                .name("name").description("description").createdOn(LocalDateTime.now()).build()
        );

        Wishlist mockWishlistFromRepository = Wishlist.builder().id(customerId).products(products).build();
        when(repository.findById(customerId)).thenReturn(Optional.of(mockWishlistFromRepository));

        Optional<Product> result = service.getProductByIdInCustomerWishList(customerId, searchedProductId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(searchedProductId, result.get().getProductId());
        verify(repository, times(1)).findById(customerId);

    }    @Test
    @DisplayName("Given a search product exists in customer wishlist, when not found, then returns empty")
    void getCustomerWishlistCheckingProductNotExists() {
        String customerId = randomId();
        Integer wishlistSize = 2;
        String searchedProductId = "SEARCH";
        List<Product> products = mockProductList(wishlistSize);

        Wishlist mockWishlistFromRepository = Wishlist.builder().id(customerId).products(products).build();
        when(repository.findById(customerId)).thenReturn(Optional.of(mockWishlistFromRepository));

        Optional<Product> result = service.getProductByIdInCustomerWishList(customerId, searchedProductId);

        Assertions.assertTrue(result.isEmpty());
        verify(repository, times(1)).findById(customerId);

    }

    @Test
    @DisplayName("Given a check exists product in customer wishlist, when wishlist not found, then throws")
    void notFoundCustomerWishlistCheckingProduct() {
        String ramdomId = UUID.randomUUID().toString();
        when(repository.findById(ramdomId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerWishlistNotFoundException.class, () -> {
            service.getProductByIdInCustomerWishList(ramdomId, ramdomId);
        });

        String expectedMessage = format(
                "O produto ID:%s não está associado com uma lista do cliente ID:%s.", ramdomId, ramdomId
        );
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        verify(repository, times(1)).findById(ramdomId);
    }


}