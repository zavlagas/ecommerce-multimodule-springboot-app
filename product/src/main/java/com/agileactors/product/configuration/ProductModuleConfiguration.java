package com.agileactors.product.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
@Import(ProductModuleDataSourceConfiguration.class)
public class ProductModuleConfiguration {

}
