package cn.com.flaginfo;

import javax.servlet.Filter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@ServletComponentScan
@EnableScheduling
@MapperScan(value = { "cn.com.flaginfo.dao" })
public class AppWebApplication {
	@Bean
	public FilterRegistrationBean filterRegistrationBean () {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(AppSSOFilter());
		filterRegistrationBean.addInitParameter("index_url", "");
		filterRegistrationBean.addInitParameter("error_url", "http://zx.ums86.com");
		filterRegistrationBean.addInitParameter("key_cookie_login_flag", "_sso_user");
		filterRegistrationBean.addInitParameter("key_sso_session_user", "loginUser");
		filterRegistrationBean.addInitParameter("ignoreUrls","/css/**,/images/**,/img/**,js/**");
		filterRegistrationBean.addInitParameter("targetBeanName","appSSOFilter");
		filterRegistrationBean.addInitParameter("targetFilterLifecycle","true");
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}
	
	
	
	@Bean  
    public Filter AppSSOFilter() {  
        return new cn.com.flaginfo.filter.AppSSOFilter(); 
    }  
	
	public static void main(String[] args) {
		SpringApplication.run(AppWebApplication.class, args);
	}
}
