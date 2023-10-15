package com.webapp.socialmedia.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class CloudConfig {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Cloudinary cloudBean() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dxpie6xvj",
                "api_key", "127665533459997",
                "api_secret", "6UzOJvAV6Gz_V2cuvzrihH5saAo",
                "secure", true));
    }
}