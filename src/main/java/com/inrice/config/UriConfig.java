package com.inrice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xurongmao
 */
@Configuration
@ConfigurationProperties(prefix = "inrice")
@Data
public class UriConfig {

    private Map<String, String> urls = new HashMap<>();

    public String getUriByCode (String code) {

        if (CollectionUtils.isEmpty(urls)) {
            throw new RuntimeException("调用服务不存在！");
        }
        String uri = urls.get(code);
        if (uri == null) {
            throw new RuntimeException("调用服务不存在！");
        }
        return uri;
    }
}
