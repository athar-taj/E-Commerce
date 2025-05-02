package com.example.product.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan(basePackages = {"com.example.product", "org.ecom.auth"})
public class AppConfig {
}