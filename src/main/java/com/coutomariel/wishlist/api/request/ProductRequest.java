package com.coutomariel.wishlist.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductRequest {
    @NotBlank
    @Schema(description = "ID do produto.")
    private String id;
    @NotBlank
    @Schema(description = "Nome do produto.")
    private String name;
    @NotBlank
    @Schema(description = "Descrição do produto.")
    private String description;
}
