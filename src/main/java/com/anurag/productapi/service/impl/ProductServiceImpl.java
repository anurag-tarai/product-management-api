package com.anurag.productapi.service.impl;


import com.anurag.productapi.dto.request.ProductRequest;
import com.anurag.productapi.dto.response.ProductResponse;
import com.anurag.productapi.entity.Item;
import com.anurag.productapi.entity.Product;
import com.anurag.productapi.exception.ResourceNotFoundException;
import com.anurag.productapi.mapper.ProductMapper;
import com.anurag.productapi.repository.ProductRepository;
import com.anurag.productapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductMapper::toProductResponse);
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ProductMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Product product = ProductMapper.toProductEntity(request);
        if (product.getItems() != null) {
            product.getItems().forEach(item -> item.setProduct(product));
        }
        product.setCreatedOn(LocalDateTime.now());

        product.setCreatedBy(username);
        Product saved = productRepository.save(product);
        return ProductMapper.toProductResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existingProduct.setProductName(request.getProductName());

        // Update audit fields
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        existingProduct.setModifiedBy(username);
        existingProduct.setModifiedOn(LocalDateTime.now());

        // Replace old items with new ones to trigger orphan removal
        existingProduct.getItems().clear();
        if (request.getItems() != null) {
            List<Item> newItems = request.getItems().stream()
                    .map(ProductMapper::toItemEntity)
                    .peek(item -> item.setProduct(existingProduct))
                    .toList();
            existingProduct.getItems().addAll(newItems);
        }

        Product updated = productRepository.save(existingProduct);
        return ProductMapper.toProductResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.deleteById(id);
    }
}