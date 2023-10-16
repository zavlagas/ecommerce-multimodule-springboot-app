package com.agileactors.launcher.configuration;

import com.agileactors.launcher.LauncherApplication;
import com.agileactors.launcher.dto.Modules;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class ModulesContextConfig {

    private final static String LAUNCHER_MODULE_PATH = LauncherApplication.class.getPackage().getName();
    private final ApplicationContext applicationContext;

    public ModulesContextConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Bean
    public Modules modules() throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(ComponentScan.class));
        Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents("./");
        List<String> modulePaths = new ArrayList<>();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            final String relativePath = beanDefinition.getBeanClassName();
            final Class<?> aClass = Class.forName(relativePath);
            final String selectedPath = aClass.getPackage().getName();
            if (!LAUNCHER_MODULE_PATH.equals(selectedPath)) {
                modulePaths.add(selectedPath);
            }
        }
        return new Modules(modulePaths);
    }
}
