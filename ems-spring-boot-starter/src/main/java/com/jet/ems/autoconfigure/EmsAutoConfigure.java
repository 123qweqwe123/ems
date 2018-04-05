package com.jet.ems.autoconfigure;

import com.jet.ems.filter.ErrorMsgFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * <pre>
 * </pre>
 *
 * @author javahuang
 */
@Configuration
@EnableConfigurationProperties(EmsProperties.class)
public class EmsAutoConfigure {

    @Autowired
    private EmsProperties properties;

    @Bean
    public ErrorMsgFilter emsFilter() {
        return new ErrorMsgFilter(properties);
    }

    @Bean
    public FilterRegistrationBean operationFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(emsFilter());
        registration.addUrlPatterns("/*");
        registration.setEnabled(properties.getActive());
        return registration;
    }
}
