package com.jinlailiao;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JinlailiaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JinlailiaoApplication.class, args);
	}
//
//	@Bean
//	public Starter taskRunner(){
//		return new Starter();
//	}


	// 加载YML格式自定义配置文件
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(new ClassPathResource("sensitive.yml"));//File引入
//		yaml.setResources(new ClassPathResource("youryml.yml"));//class引入
		configurer.setProperties(yaml.getObject());
		return configurer;
	}
}
