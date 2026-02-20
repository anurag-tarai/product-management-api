package com.anurag.productapi.controller;

import com.anurag.productapi.dto.request.ProductRequest;
import com.anurag.productapi.dto.response.ProductResponse;
import com.anurag.productapi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import org.hamcrest.Matchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createProduct_ReturnsCreatedStatus() throws Exception {
        ProductRequest.ItemRequest item = ProductRequest.ItemRequest.builder()
                .quantity(5)
                .build();

        ProductRequest request = ProductRequest.builder()
                .productName("Mechanical Keyboard")
                .items(List.of(item))
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Mechanical Keyboard"));
    }

    @Test
    void getProducts_WithoutAuthentication_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getProducts_WithAuthentication_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getProducts_WithPagination_ReturnsPagedResult() throws Exception {
        // Setup: Ensure at least one product exists
        ProductRequest createRequest = ProductRequest.builder()
                .productName("Paged Product")
                .items(List.of())
                .build();
        productService.createProduct(createRequest);

        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verifies the response follows Spring Data Page structure
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(Matchers.greaterThanOrEqualTo(1)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateProduct_ReturnsOk() throws Exception {
        // Setup: Create a product first to ensure it exists
        ProductRequest createRequest = ProductRequest.builder()
                .productName("Original Product")
                .items(List.of())
                .build();
        ProductResponse createdProduct = productService.createProduct(createRequest);

        ProductRequest updateRequest = ProductRequest.builder()
                .productName("Updated Product")
                .items(List.of())
                .build();

        mockMvc.perform(put("/api/v1/products/{id}", createdProduct.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProduct_WithoutAuthentication_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/products/{id}", 1)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteProduct_ReturnsNoContent() throws Exception {
        // Setup: Create a product first
        ProductRequest createRequest = ProductRequest.builder()
                .productName("To Be Deleted")
                .items(List.of())
                .build();
        ProductResponse createdProduct = productService.createProduct(createRequest);

        mockMvc.perform(delete("/api/v1/products/{id}", createdProduct.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getProductItems_ReturnsItemsList() throws Exception {
        // Setup: Create product with items
        ProductRequest.ItemRequest item = ProductRequest.ItemRequest.builder()
                .quantity(10)
                .build();
        ProductRequest createRequest = ProductRequest.builder()
                .productName("Product with Items")
                .items(List.of(item))
                .build();
        ProductResponse createdProduct = productService.createProduct(createRequest);

        mockMvc.perform(get("/api/v1/products/{id}/items", createdProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].quantity").value(10));
    }
}