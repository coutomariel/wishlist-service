package com.coutomariel.wishlist.api.controller;

import com.coutomariel.wishlist.api.request.ProductRequest;
import com.coutomariel.wishlist.api.response.CheckedProductResponse;
import com.coutomariel.wishlist.api.response.ProductResponse;
import com.coutomariel.wishlist.api.response.WishlistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "/wishlist", description = "Grupo de apis para gerenciamento de wishlists de cliente")
public interface WishlistControllerContract {

    @Operation(description = "API para adicionar um produto na wishlist de um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorno Ok com produto adicionado."),
            @ApiResponse(responseCode = "400", description = "Requisição com parâmentros inválidos"),
            @ApiResponse(responseCode = "422", description = "Produto já estava presente na wishlist do cliente")
    })
    @Parameters(value = {
            @Parameter(name = "customerId", in = ParameterIn.PATH, description = "ID do cliente", required = true),
            @Parameter(name = "productId", in = ParameterIn.PATH, description = "ID do produto", required = true)}
    )
    WishlistResponse addProduct(String customerid, ProductRequest request);

    @Operation(description = "API para remover um produto da wishlist de um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Retorno Ok, produto removido e response sem conteúdo."),
            @ApiResponse(responseCode = "404", description = "Produto a ser removido não foi encontrado na wishlist")
    })
    @Parameters(value = {
            @Parameter(name = "customerId", in = ParameterIn.PATH, description = "ID do cliente", required = true),
            @Parameter(name = "productId", in = ParameterIn.PATH, description = "ID do produto", required = true)}
    )
    void removeProduct(String customerId, String productId);

    @Operation(description = "API para listar os produtos na wishlist de um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno Ok com produto adicionado.")
    })
    @Parameters(value = {
            @Parameter(name = "customerId", in = ParameterIn.PATH, description = "ID do cliente", required = true)
    })
    List<ProductResponse> getCustomerWishlist(String customerId);

    @Operation(description = "API para verificar se um produto existe na wishlist de um cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornando true ou false para produto procurado.")
    })
    @Parameters(value = {
            @Parameter(name = "customerId", in = ParameterIn.PATH, description = "ID do cliente", required = true),
            @Parameter(name = "productId", in = ParameterIn.PATH, description = "ID do produto", required = true)}
    )
    CheckedProductResponse checkProductInCustomerWishlist(String customerId, String productId);
}
