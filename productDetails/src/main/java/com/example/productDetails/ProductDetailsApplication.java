package com.example.productDetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableElasticsearchRepositories
public class ProductDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductDetailsApplication.class, args);
	}

}
