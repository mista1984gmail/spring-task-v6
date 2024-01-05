package ru.clevertec.servlet.task.config.spring;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import ru.clevertec.servlet.task.mapper.ClientMapper;
import ru.clevertec.servlet.task.mapper.ClientMapperImpl;
import ru.clevertec.servlet.task.mapper.UserCredentialMapper;
import ru.clevertec.servlet.task.mapper.UserCredentialMapperImpl;

@Configuration
@PropertySource(value = "classpath:application.properties")
@ComponentScan(value={"ru.clevertec.servlet.task"})
public class SpringConfiguration {

	@Bean
	public ClientMapper clientMapper() {
		return new ClientMapperImpl();
	}

	@Bean
	public UserCredentialMapper userCredentialMapper() {
		return new UserCredentialMapperImpl();
	}

	@Bean
	@Primary
	public ObjectMapper objectMapper() {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		objectMapper.findAndRegisterModules();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		return objectMapper;
	}

	@Bean
	public XmlMapper xmlMapper() {

		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		xmlMapper.findAndRegisterModules();
		xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		return xmlMapper;
	}

}
