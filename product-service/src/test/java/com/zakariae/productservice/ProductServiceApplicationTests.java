package com.zakariae.productservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zakariae.productservice.dto.ProductRequest;
import com.zakariae.productservice.repository.ProductRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3"); //starts a mongodb container

	@Autowired 
	private MockMvc mockMvc; //mocks the http requests 

	@Autowired
	private ObjectMapper objectMapper; //converts java object to json and vice versa

	@Autowired
	private ProductRepository productRepository; //repository to access the mongodb database

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl); //sets the mongodb url to the container url
	}

	@Test
	void CreateProduct() throws Exception {
		ProductRequest productRequest= getProductRequest();
		String productString = objectMapper.writeValueAsString(productRequest); //converts the productRequest to json
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productString)) 
				.andExpect(status().isCreated()); //checks if the response is "Product created successfully"
		Assertions.assertEquals(1, productRepository.findAll().size()); //checks if the product is added to the database
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Product 1")
				.description("Product 1 description")
				.price(10.0)
				.build();
	}

}
