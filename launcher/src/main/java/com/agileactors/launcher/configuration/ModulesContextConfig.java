package com.agileactors.launcher.configuration;

import com.agileactors.customer.configuration.CustomerModuleConfiguration;
import com.agileactors.product.configuration.ProductModuleConfiguration;
import com.agileactors.shop.configuration.ShopModuleConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Import({ProductModuleConfiguration.class, CustomerModuleConfiguration.class, ShopModuleConfiguration.class})
public class ModulesContextConfig {

    private final ApplicationContext applicationContext;

    public ModulesContextConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean("transactionManagerHandler")
    public Map<String, PlatformTransactionManager> transactionManagerHandler() {
        return new HashMap<>();
    }


}
