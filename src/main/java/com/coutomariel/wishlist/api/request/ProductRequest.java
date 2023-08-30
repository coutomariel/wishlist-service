package com.coutomariel.wishlist.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequest {
    @NotBlank
    @Schema(description = "ID do produto.")
    private String productId;
    @NotBlank
    @Schema(description = "Nome do produto.")
    private String name;
    @NotBlank
    @Schema(description = "Descrição do produto.")
    private String description;
}
