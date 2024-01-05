package ru.clevertec.servlet.task.config.cache;

import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.servlet.task.aspect.CacheAspect;

@Configuration
public class CacheConfig {

	@Bean
	public CacheAspect cacheAspect(){

		return Aspects.aspectOf(CacheAspect.class);

	}
}
