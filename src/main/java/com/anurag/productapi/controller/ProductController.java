package com.anurag.productapi.controller;


import com.anurag.productapi.dto.request.ProductRequest;
import com.anurag.productapi.dto.response.ItemResponse;
import com.anurag.productapi.dto.response.ProductResponse;
import com.anurag.productapi.service.ProductService;
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

    // GET all products (paged)
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                productService.getAllProducts(PageRequest.of(page, size))
        );
    }

    // GET single product
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductByIdDTO(id));
    }

    // POST create product
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return new ResponseEntity<>(
                productService.createProductDTO(request),
                HttpStatus.CREATED
        );
    }

    // GET items of a product
    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemResponse>> getItems(@PathVariable Integer id) {
        ProductResponse productResponse = productService.getProductByIdDTO(id);
        return ResponseEntity.ok(productResponse.getItems());
    }

    // PUT update product
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> update(@PathVariable Integer id, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProductDTO(id, request));
    }

    // DELETE product
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}