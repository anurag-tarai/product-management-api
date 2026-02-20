package com.anurag.productapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// For receiving create/update requests
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank(message = "Product name cannot be blank")
    private String productName;

    @Valid
    private List<ItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemRequest {
        @Min(1)
        private Integer quantity;
    }
}