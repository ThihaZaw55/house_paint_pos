package com.thz.house_paint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.thz.house_paint.model.BaseRepositoryImpl;


@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class HousePaintApplication {

	public static void main(String[] args) {
		SpringApplication.run(HousePaintApplication.class, args);
	}

}
