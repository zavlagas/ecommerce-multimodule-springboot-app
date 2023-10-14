package com.agileactors.customer.configuration;

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

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
@PropertySource("classpath:customer-datasource.properties")
@EnableJpaRepositories(basePackages = "com.agileactors.customer",
        entityManagerFactoryRef = "customerEntityManagerFactory",
        transactionManagerRef = "customerTransactionManager")
public class CustomerModuleDataSourceConfiguration {

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
    public void CustomerModuleDataSourceConfiguration(
            @Qualifier("transactionManagerHandler") Map<String, PlatformTransactionManager> transactionManagerHandler) {
        this.transactionManagerHandler = Optional.ofNullable(transactionManagerHandler);
    }


    @Bean("customerEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "customerDataSource")
    public DataSource customerModuleDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    @Bean(name = "customerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        return entityManagerFactoryBuilder()
                .dataSource(customerModuleDataSource())
                .packages("com.agileactors.customer")
                .persistenceUnit("customer")
                .build();
    }

    @Bean(name = "customerTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("customerEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory);
        transactionManagerHandler.ifPresent(transactionManagerHandler -> transactionManagerHandler.put("com.agileactors.customer", jpaTransactionManager));
        return jpaTransactionManager;
    }

}
