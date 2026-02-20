package com.anurag.productapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// For sending item info to client
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {
    private Integer id;
    private Integer quantity;
}