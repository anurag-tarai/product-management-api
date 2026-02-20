package com.anurag.productapi.controller;

import com.anurag.productapi.dto.request.ProductRequest;
import com.anurag.productapi.dto.response.ItemResponse;
import com.anurag.productapi.dto.response.ProductResponse;
import com.anurag.productapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of products retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(
            @Parameter(description = "Page number (starts from 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                productService.getAllProducts(PageRequest.of(page, size))
        );
    }

    @Operation(summary = "Get a single product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(
            @Parameter(description = "ID of the product to retrieve") @PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(
            @Parameter(description = "Product details") @Valid @RequestBody ProductRequest request) {
        return new ResponseEntity<>(
                productService.createProduct(request),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Get all items of a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemResponse>> getItems(
            @Parameter(description = "ID of the product") @PathVariable Integer id) {
        ProductResponse productResponse = productService.getProductById(id);
        return ResponseEntity.ok(productResponse.getItems());
    }

    @Operation(summary = "Update an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> update(
            @Parameter(description = "ID of the product to update") @PathVariable Integer id,
            @Parameter(description = "Updated product details") @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Operation(summary = "Delete a product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the product to delete") @PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}