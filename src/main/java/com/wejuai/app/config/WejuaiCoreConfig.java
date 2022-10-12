package com.wejuai.app.config;

import com.wejuai.app.support.WejuaiCoreClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author ZM.Wang
 */
@Configuration
@EnableConfigurationProperties(WejuaiCoreConfig.Properties.class)
public class WejuaiCoreConfig {

    private final Properties properties;

    public WejuaiCoreConfig(Properties properties) {
        this.properties = properties;
    }

    @Bean
    WejuaiCoreClient wejuaiCoreClient() {
        return new WejuaiCoreClient(properties.getUrl());
    }

//    @Autowired
//    void originTest() {
//        wejuaiCoreClient().originTest();
//    }

    @Validated
    @ConfigurationProperties(prefix = "wejuai-core")
    public static class Properties {
        @NotBlank
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
