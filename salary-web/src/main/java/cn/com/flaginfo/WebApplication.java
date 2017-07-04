package cn.com.flaginfo;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.Filter;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class WebApplication {

	protected static Logger logger = LoggerFactory.getLogger(WebApplication.class);

	public @PostConstruct void init() { }

	@PreDestroy
	public void dostory() { }
	
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(SSOFilter());
		filterRegistrationBean.addInitParameter("index_url", "");
		filterRegistrationBean.addInitParameter("error_url", "http://zx.ums86.com");
		filterRegistrationBean.addInitParameter("key_cookie_login_flag", "yxt_u");
		filterRegistrationBean.addInitParameter("key_sso_session_user", "loginUser");
		filterRegistrationBean.addInitParameter("exclude_paths","/static,/user/login,/user/reg,/index.jsp");
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}
	
	@Bean  
    public Filter SSOFilter() {  
        return new cn.com.flaginfo.user.auth.sdk.SSOFilter(); 
    }
	
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}
