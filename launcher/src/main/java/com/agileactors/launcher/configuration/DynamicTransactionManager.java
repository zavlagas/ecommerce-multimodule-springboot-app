package com.agileactors.launcher.configuration;

import com.agileactors.launcher.dto.Modules;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Primary
public class DynamicTransactionManager implements PlatformTransactionManager {
    private final Map<String, PlatformTransactionManager> platformTransactionManagerBeans;
    private final Map<String, Object> configurationClasses;
    private final Modules modules;
    private ApplicationContext applicationContext;
    private Map<String, PlatformTransactionManager> transactionManagers;

    public DynamicTransactionManager(ApplicationContext applicationContext,
                                     Modules modules,
                                     Map<String, PlatformTransactionManager> platformTransactionManagerBeans) {
        this.applicationContext = applicationContext;
        this.modules = modules;
        this.platformTransactionManagerBeans = platformTransactionManagerBeans;
        this.configurationClasses = this.applicationContext.getBeansWithAnnotation(Configuration.class);
        this.transactionManagers = new HashMap<>();
    }

    private PlatformTransactionManager determineTransactionManager() {
        if (transactionManagers.isEmpty()) {
            findTransactionManagers();
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            for (Map.Entry<String, PlatformTransactionManager> entry : transactionManagers.entrySet()) {
                if (element.getClassName().startsWith(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    private void findTransactionManagers() {
        final ConfigurableListableBeanFactory beanFactory = ((AnnotationConfigServletWebServerApplicationContext) applicationContext).getBeanFactory();
        final Map<String, PlatformTransactionManager> platformTransactionManagers = new HashMap<>();
        for (Map.Entry<String, PlatformTransactionManager> platformTransactionManagerEntry : platformTransactionManagerBeans.entrySet()) {
            String transactionManagerName = platformTransactionManagerEntry.getKey();
            PlatformTransactionManager platformTransactionManager = platformTransactionManagerEntry.getValue();
            final BeanDefinition beanDefinition = beanFactory.getBeanDefinition(transactionManagerName);
            if (beanDefinition.getFactoryBeanName() != null) {
                final Object configurationClass = configurationClasses.get(beanDefinition.getFactoryBeanName());
                final String pathOfConfigurationClass = configurationClass.getClass().getPackage().getName();
                final Optional<String> module = modules.findModule(pathOfConfigurationClass);
                module.ifPresent(path -> this.transactionManagers.put(path, platformTransactionManager));
            }
        }
    }


    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        return determineTransactionManager().getTransaction(definition);
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        determineTransactionManager().commit(status);
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        determineTransactionManager().rollback(status);
    }
}
