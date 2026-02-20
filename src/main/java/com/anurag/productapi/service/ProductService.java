package com.anurag.productapi.service;

import com.anurag.productapi.dto.request.ProductRequest;
import com.anurag.productapi.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing products and their related items.
 * Provides CRUD operations for Product entities.
 */
public interface ProductService {

    /**
     * Retrieves a paginated list of all products.
     *
     * @param pageable pagination information (page number, size, sorting)
     * @return a Page of ProductResponse DTOs representing all products
     */
    Page<ProductResponse> getAllProducts(Pageable pageable);

    /**
     * Retrieves a single product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return ProductResponse DTO of the requested product
     * @throws jakarta.persistence.EntityNotFoundException if the product with the given ID does not exist
     */
    ProductResponse getProductById(Integer id);

    /**
     * Creates a new product along with its associated items.
     * Automatically sets audit fields such as createdBy and createdOn.
     *
     * @param request ProductRequest DTO containing product and item details
     * @return ProductResponse DTO of the newly created product
     */
    ProductResponse createProduct(ProductRequest request);

    /**
     * Updates an existing product and its items.
     * Replaces old items with new ones and updates audit fields like modifiedBy and modifiedOn.
     *
     * @param id the ID of the product to update
     * @param request ProductRequest DTO containing updated product and item details
     * @return ProductResponse DTO of the updated product
     * @throws jakarta.persistence.EntityNotFoundException if the product with the given ID does not exist
     */
    ProductResponse updateProduct(Integer id, ProductRequest request);

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @throws jakarta.persistence.EntityNotFoundException if the product with the given ID does not exist
     */
    void deleteProduct(Integer id);
}