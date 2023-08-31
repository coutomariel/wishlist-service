package com.coutomariel.wishlist.api.controller;

import com.coutomariel.wishlist.api.request.ProductRequest;
import com.coutomariel.wishlist.domain.entity.Product;
import com.coutomariel.wishlist.domain.entity.Wishlist;
import com.coutomariel.wishlist.domain.exception.ProductAlreadyExistsInCustomerWishlistException;
import com.coutomariel.wishlist.domain.exception.ProductNotFoundInWishlistCustomerException;
import com.coutomariel.wishlist.domain.service.WishlistServiceImpl;
import com.coutomariel.wishlist.utils.MockUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class WishlistControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MongoClient mongoClient;

    @MockBean
    private WishlistServiceImpl service;
    @InjectMocks
    private WishlistControllerTest wishlistControllerTest;


    private static final String ROUTE_ADD_PRODUCT = "/wishlist/{customerId}/products";
    private static final String ROUTE_REMOVE_PRODUCT = "/wishlist/{customerId}/products/{productId}";

    @Test
    @DisplayName("give a valid request to add product in customer wishlist, then return 201 created")
    void addProductSuccessfully() throws Exception {
        String customerId = UUID.randomUUID().toString();
        Product product = MockUtils.mockProduct();

        Wishlist mockWishlistFromRepository = Wishlist.builder()
                .id(customerId)
                .products(Collections.singletonList(product))
                .build();

        when(service.add(anyString(), any(Product.class))).thenReturn(mockWishlistFromRepository);

        ProductRequest request = MockUtils.mockProductRequest();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mvc.perform(post(ROUTE_ADD_PRODUCT, customerId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("give a request without mandatory fields to add product in customer wishlist, then throws 400")
    void addProductWhithoutmandatoryFields() throws Exception {
        String customerId = UUID.randomUUID().toString();

        ProductRequest requestWithoutMandatoryFields = ProductRequest.builder().build();
        String jsonRequest = objectMapper.writeValueAsString(requestWithoutMandatoryFields);

        mvc.perform(post(ROUTE_ADD_PRODUCT, customerId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @DisplayName("give a request without with product already exists in customer wishlist, then throws 422")
    void addProductAlreadyExistsInCustomerWishList() throws Exception {
        String customerId = UUID.randomUUID().toString();

        ProductRequest requestWithoutMandatoryFields = MockUtils.mockProductRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestWithoutMandatoryFields);

        when(service.add(anyString(), any(Product.class)))
                .thenThrow(ProductAlreadyExistsInCustomerWishlistException.class);

        mvc.perform(post(ROUTE_ADD_PRODUCT, customerId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("given a product remove request, when exists by product and wishlist, then successfully")
    void removeProductFromCustomerWishlist() throws Exception {
        String randomId = UUID.randomUUID().toString();
        doNothing().when(service).remove(randomId, randomId);

        mvc.perform(delete(ROUTE_REMOVE_PRODUCT, randomId, randomId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("given a product remove request, when does not found, then throw exception")
    void removeProductThatNotExistsInCustomerWishlist() throws Exception {
        String randomId = UUID.randomUUID().toString();
        doThrow(new ProductNotFoundInWishlistCustomerException("mensagem de erro"))
                .when(service).remove(randomId, randomId);

        mvc.perform(delete(ROUTE_REMOVE_PRODUCT, randomId, randomId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("mensagem de erro"));
    }

}