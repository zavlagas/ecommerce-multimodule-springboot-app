package com.agileactors.customer.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CustomerModuleDataSourceConfiguration.class)
public class CustomerModuleConfiguration {


}
