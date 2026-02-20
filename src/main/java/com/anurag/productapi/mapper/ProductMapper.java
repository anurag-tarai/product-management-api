package com.anurag.productapi.mapper;


import com.anurag.productapi.dto.request.ProductRequest;
import com.anurag.productapi.dto.response.ItemResponse;
import com.anurag.productapi.dto.response.ProductResponse;
import com.anurag.productapi.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    // Entity → Response
    public static ItemResponse toItemResponse(Item item) {
        if (item == null) return null;
        return ItemResponse.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        if (product == null) return null;
        List<ItemResponse> items = null;
        if (product.getItems() != null) {
            items = product.getItems().stream()
                    .map(ProductMapper::toItemResponse)
                    .collect(Collectors.toList());
        }
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .createdBy(product.getCreatedBy())
                .modifiedBy(product.getModifiedBy())
                .items(items)
                .build();
    }

    // Request → Entity
    public static Item toItemEntity(ProductRequest.ItemRequest dto) {
        if (dto == null) return null;
        return Item.builder()
                .quantity(dto.getQuantity())
                .build();
    }

    public static Product toProductEntity(ProductRequest dto) {
        if (dto == null) return null;
        List<Item> items = null;
        if (dto.getItems() != null) {
            items = dto.getItems().stream()
                    .map(ProductMapper::toItemEntity)
                    .collect(Collectors.toList());
        }
        return Product.builder()
                .productName(dto.getProductName())
                .items(items)
                .build();
    }
}