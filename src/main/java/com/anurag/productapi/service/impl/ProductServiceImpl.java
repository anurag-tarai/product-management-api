package com.anurag.productapi.service.impl;


import com.anurag.productapi.dto.request.ProductRequest;
import com.anurag.productapi.dto.response.ProductResponse;
import com.anurag.productapi.entity.Product;
import com.anurag.productapi.mapper.ProductMapper;
import com.anurag.productapi.repository.ProductRepository;
import com.anurag.productapi.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public ProductResponse getProductByIdDTO(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return ProductMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProductDTO(ProductRequest request) {
        Product product = ProductMapper.toProductEntity(request);
        if (product.getItems() != null) {
            product.getItems().forEach(item -> item.setProduct(product));
        }
        product.setCreatedOn(LocalDateTime.now());
        product.setCreatedBy("SYSTEM");
        Product saved = productRepository.save(product);
        return ProductMapper.toProductResponse(saved);
    }
}