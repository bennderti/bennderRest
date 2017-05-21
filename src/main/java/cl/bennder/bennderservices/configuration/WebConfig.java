package cl.bennder.bennderservices.configuration;

import cl.bennder.bennderservices.multitenancy.MultiTenancyInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.sql.DataSource;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "cl.bennder.bennderservices" })
@MapperScan("cl.bennder.bennderservices.mapper")
@EnableTransactionManagement
public class WebConfig extends WebMvcConfigurerAdapter{
	@Autowired
	MultiTenancyInterceptor multiTenancyInterceptor;

	@Bean
    public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver
                          = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(multiTenancyInterceptor).addPathPatterns("/**");
	}

	@Bean
	public DataSource dataSource() {
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		return lookup.getDataSource("jboss/datasources/bennderDS");
	}

	@Bean
	public DataSourceTransactionManager transactionManager()
	{
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception
	{
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		return sessionFactory.getObject();
	}
}