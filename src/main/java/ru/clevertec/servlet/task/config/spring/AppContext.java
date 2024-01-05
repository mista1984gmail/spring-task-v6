package ru.clevertec.servlet.task.config.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class AppContext {

	private static AppContext instance;

	public AnnotationConfigApplicationContext context;

	public AppContext() {
		this.context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
	}

	public static AppContext getInstance() {
		if (instance == null) {
			instance = new AppContext();
		}
		return instance;
	}
}
