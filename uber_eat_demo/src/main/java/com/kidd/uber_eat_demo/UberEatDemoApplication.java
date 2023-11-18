package com.kidd.uber_eat_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class UberEatDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(UberEatDemoApplication.class, args);
		log.info("项目启动成功...");
	}

}
