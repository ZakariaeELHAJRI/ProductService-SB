package com.zakariae.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zakariae.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
    
}
