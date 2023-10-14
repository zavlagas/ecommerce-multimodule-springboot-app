package com.agileactors.launcher.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import java.util.Map;

@Component
@Primary
public class DynamicTransactionManager implements PlatformTransactionManager {

    private final ApplicationContext applicationContext;
    private final Map<String, PlatformTransactionManager> transactionManagerHandler;

    public DynamicTransactionManager(ApplicationContext applicationContext,
                                     @Qualifier("transactionManagerHandler")
                                     Map<String, PlatformTransactionManager> transactionManagerHandler) {
        this.applicationContext = applicationContext;
        this.transactionManagerHandler = transactionManagerHandler;
    }

    private PlatformTransactionManager determineTransactionManager() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            for (Map.Entry<String, PlatformTransactionManager> entry : transactionManagerHandler.entrySet()) {
                if (element.getClassName().startsWith(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
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
