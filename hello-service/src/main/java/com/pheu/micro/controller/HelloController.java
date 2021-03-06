package com.pheu.micro.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/")
public class HelloController {

	@Value("${hello.name}")
	private String nameConfig;
	
	@Value("${server.port}")
	private String portConfig;

	@RequestMapping("/")
	public String sayHello() {
		return "Hello, " + nameConfig + ", from " + portConfig;
	}
	
	@RequestMapping("/hello")
	public String sayHello(@RequestParam("name") String name) {
		return "Hello, " + name + ", from " + portConfig;
	}
	
	@RequestMapping("/goodbye")
	public String sayGoodbye(@RequestParam("name") String name) {
		return "Goodbye, " + name + ", from " + portConfig;
	}
	
}
