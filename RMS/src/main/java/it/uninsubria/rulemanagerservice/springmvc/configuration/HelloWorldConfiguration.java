package it.uninsubria.rulemanagerservice.springmvc.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "it.uninsubria.rulemanagerservice.springmvc;")
public class HelloWorldConfiguration extends WebMvcConfigurerAdapter{
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		registry.viewResolver(viewResolver);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}
	 @Bean
		public MessageSource messageSource() {
		    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		    messageSource.setBasename("messages");
		    return messageSource;
		}
	 
	 @Bean
	    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
	        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
	        c.setLocation(new ClassPathResource("application.properties"));
	        return c;
	    }
	    
	    /**Optional. It's only required when handling '.' in @PathVariables which otherwise ignore everything after last '.' in @PathVaidables argument.
	     * It's a known bug in Spring [https://jira.spring.io/browse/SPR-6164], still present in Spring 4.1.7.
	     * This is a workaround for this issue.
	     */
	    @Override
	    public void configurePathMatch(PathMatchConfigurer matcher) {
	        matcher.setUseRegisteredSuffixPatternMatch(true);
	    }

}