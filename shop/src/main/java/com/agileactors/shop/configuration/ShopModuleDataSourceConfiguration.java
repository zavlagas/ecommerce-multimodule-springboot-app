package com.agileactors.shop.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:shop-datasource.properties")
@EnableJpaRepositories(basePackages = "com.agileactors.shop",
        entityManagerFactoryRef = "shopEntityManagerFactory",
        transactionManagerRef = "shopTransactionManager")
public class ShopModuleDataSourceConfiguration {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;


    private Optional<Map<String, PlatformTransactionManager>> transactionManagerHandler = Optional.empty();

    @Autowired(required = false)
    public void setTransactionManagerHandler(@Qualifier("transactionManagerHandler") Map<String, PlatformTransactionManager> transactionManagerHandler) {
        this.transactionManagerHandler = Optional.ofNullable(transactionManagerHandler);
    }

    @Bean("shopEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "shopDataSource")
    public DataSource shopModuleDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }


    @Bean(name = "shopEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        return entityManagerFactoryBuilder()
                .dataSource(shopModuleDataSource())
                .packages("com.agileactors.shop")
                .persistenceUnit("shop")
                .build();
    }

    @Bean(name = "shopTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("shopEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}