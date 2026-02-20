package com.anurag.productapi.service;

import com.anurag.productapi.dto.request.ProductRequest;
import com.anurag.productapi.dto.response.ProductResponse;
import com.anurag.productapi.entity.Product;
import com.anurag.productapi.repository.ProductRepository;
import com.anurag.productapi.service.impl.ProductServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product mockProduct;

    @BeforeEach
    void setUp() {
        // Mock Security Context for auditing fields
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testuser", "password", new ArrayList<>())
        );

        mockProduct = Product.builder()
                .id(1)
                .productName("Gaming Laptop")
                .createdBy("testuser")
                .createdOn(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
    }

    @Test
    void createProduct_Success() {
        ProductRequest request = ProductRequest.builder()
                .productName("New Mouse")
                .items(new ArrayList<>())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        ProductResponse response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals("testuser", response.getCreatedBy());
        assertNotNull(response.getCreatedOn());
        assertEquals("Gaming Laptop", response.getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        ProductResponse response = productService.getProductById(1);

        assertNotNull(response);
        assertEquals("Gaming Laptop", response.getProductName());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductById_NotFound_ThrowsException() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getProductById(99));
        verify(productRepository, times(1)).findById(99);
    }

    @Test
    void updateProduct_Success() {
        ProductRequest request = ProductRequest.builder()
                .productName("Updated Laptop")
                .items(new ArrayList<>())
                .build();

        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        ProductResponse response = productService.updateProduct(1, request);

        assertNotNull(response);
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProductDTO_Success() {
        when(productRepository.existsById(1)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1);

        assertDoesNotThrow(() -> productService.deleteProduct(1));
        verify(productRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteProductDTO_NotFound_ThrowsException() {
        when(productRepository.existsById(99)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(99));
        verify(productRepository, times(1)).existsById(99);
    }
}