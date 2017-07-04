package cn.com.flaginfo.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import cn.com.flaginfo.utils.SystemConfig;



@Configuration
@Order(2)
public class ServletConfig {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
    	return new EmbeddedServletContainerCustomizer() {
            @Override 
            public void customize(ConfigurableEmbeddedServletContainer container) {
            	System.out.println("端口号："+SystemConfig.getString("web_port"));
                container.setPort(Integer.valueOf(SystemConfig.getString("web_port")));
            }
        };
    }
}