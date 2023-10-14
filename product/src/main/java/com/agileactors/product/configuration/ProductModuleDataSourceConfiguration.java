package com.agileactors.product.configuration;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;
import java.util.Optional;

@Configuration
@PropertySource("classpath:product-datasource.properties")
@EnableTransactionManagement
@EnableMongoRepositories(basePackages = "com.agileactors.product",
        mongoTemplateRef = "productMongoTemplate")
public class ProductModuleDataSourceConfiguration extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoDbUri;
    @Value("${product.mongodb.database}")
    private String databaseName;

    private Optional<Map<String, PlatformTransactionManager>> transactionManagerHandler = Optional.empty();


    @Autowired(required = false)
    public void ProductModuleDataSourceConfiguration(@Qualifier("transactionManagerHandler") Map<
            String,
            PlatformTransactionManager> transactionManagerHandler) {
        this.transactionManagerHandler = Optional.ofNullable(transactionManagerHandler);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean(name = "productMongoClient")
    @Override
    public MongoClient mongoClient() {
        return super.mongoClient();
    }

    @Bean(name = "productMongoTemplate")
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    @Bean(name = "productTransactionManager")
    public MongoTransactionManager transactionManager() {
        final MongoTransactionManager mongoTransactionManager = new MongoTransactionManager(
                new SimpleMongoClientDatabaseFactory(mongoClient(), getDatabaseName()));
        transactionManagerHandler.
                ifPresent(transactionManagerHandler -> transactionManagerHandler.put("com.agileactors.product", mongoTransactionManager));
        return mongoTransactionManager;
    }
}
