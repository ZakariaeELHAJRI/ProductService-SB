package com.zakariae.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zakariae.productservice.dto.ProductRequest;
import com.zakariae.productservice.dto.ProductResponse;
import com.zakariae.productservice.model.Product;
import com.zakariae.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product created: {}", product);
        
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("Retrieving all products: {}", products);
        return products.stream().map(this::mapToProductResponse).toList();
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id).orElseThrow();
        log.info("Retrieving product by id: {}", product);
        return mapToProductResponse(product);
    }

    public void updateProduct(String id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        productRepository.save(product);
        log.info("Product updated: {}", product);
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow();
        productRepository.delete(product);
        log.info("Product deleted: {}", product);
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
