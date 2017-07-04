package cn.com.flaginfo.config;

import com.alibaba.druid.pool.DruidDataSource;

import cn.com.flaginfo.utils.SystemConfig;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@Order(2)
public class MybatisConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DruidDataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(SystemConfig.getString("spring.datasource.url"));
		dataSource.setDriverClassName(SystemConfig.getString("spring.datasource.driver-class-name"));
        dataSource.setUsername(SystemConfig.getString("spring.datasource.username"));
        dataSource.setPassword(SystemConfig.getString("spring.datasource.password"));
		dataSource.setTimeBetweenEvictionRunsMillis(60000);
		dataSource.setMaxWait(60000);
		return dataSource;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:cn/com/flaginfo/dao/mapper/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
