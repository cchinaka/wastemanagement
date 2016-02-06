package com.milcomsolutions.commons;

/**
 * This is a standard class to hold all default configurations that will be loaded on the system
 *
 * @author Jeff Amachree
 * @version 1.0
 */
import java.io.File;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;


@Component("configurationManager")
public class ConfigurationManager extends PropertySourcesPlaceholderConfigurer implements InitializingBean {

    static final Log LOG = LogFactory.getLog(ConfigurationManager.class);

    private Properties props;


    public ConfigurationManager() {
        String path = System.getenv("PROJECT_HOME") + File.separator + "config/application.properties";
        this.setLocation(new FileSystemResource(path));
    }


    public String getProperty(String key) {
        return StringUtils.defaultIfEmpty(props.getProperty(key), System.getenv(key));
    }


    public Properties getAllProperties() {
        return props;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        props = this.mergeProperties();
        props.put("currency.symbol", "&#x20A6;");
    }


    public String getProperty(String key, String defaultValue) {
        return StringUtils.defaultIfEmpty(getProperty(key), defaultValue);
    }
}
