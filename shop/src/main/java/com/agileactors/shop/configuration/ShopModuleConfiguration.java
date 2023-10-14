package com.agileactors.shop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ShopModuleDataSourceConfiguration.class)
public class ShopModuleConfiguration {
}
