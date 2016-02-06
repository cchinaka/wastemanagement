package com.milcomsolutions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import com.milcomsolutions.commons.ConfigurationManager;


@SpringBootApplication
@EnableAutoConfiguration(exclude = { VelocityAutoConfiguration.class, FreeMarkerAutoConfiguration.class })
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource("file:${PROJECT_HOME}/config/application.properties")
@EntityScan({ "com.milcomsolutions.entity" })
public class BaseWebApplication extends SpringBootServletInitializer {

    @Value("${tile.layout:classpath**:src/main/webapp/WEB-INF/layouts/layouts.xml}")
    String tileLayout;

    @Value("${tile.view:classpath*:src/main/webapp/WEB-INF/pages/**/views.xml}")
    String tileView;


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BaseWebApplication.class);
    }


    @Bean
    public UrlBasedViewResolver tilesViewResolver() {
        UrlBasedViewResolver tilesViewResolver = new UrlBasedViewResolver();
        tilesViewResolver.setViewClass(TilesView.class);
        tilesViewResolver.setOrder(1);
        return tilesViewResolver;
    }


    @Bean
    public TilesConfigurer tilesConfigurer() {
        String[] definitions = new String[] { tileLayout, tileView };
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(definitions);
        tilesConfigurer.setCompleteAutoload(false);
        return tilesConfigurer;
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurationManager = new ConfigurationManager();
        return configurationManager;
    }
}
