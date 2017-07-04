package cn.com.flaginfo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import cn.com.flaginfo.utils.CustomPropertyPlaceholderConfigurer;

@Configuration
@Order(1)
public class PropertyConfig {
	@Bean
	public CustomPropertyPlaceholderConfigurer customProperty (){
		CustomPropertyPlaceholderConfigurer configurer = new CustomPropertyPlaceholderConfigurer();
		configurer.setMode("cinfigCenter");
		//configurer.setMode("local");
		//configurer.setLocations(new ClassPathResource("application.properties"));
		Map<String, String> cinfigCenterDatas = new HashMap<String, String>();
		cinfigCenterDatas.put("salary_manage", "crm");
		configurer.setCinfigCenterDatas(cinfigCenterDatas);
		return configurer;
	}
}
