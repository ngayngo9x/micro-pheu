package com.pheu.micro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class HelloClientController {
	
	private static final String HELLO_SERVICE = "hello-service";
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private GoodbyeClient feignClient;
	
	@RequestMapping("/greeting")
	@HystrixCommand(fallbackMethod = "defaultGreeting")
	public String greeting(@RequestParam("name") String name) {
		ResponseEntity<String> exchange = restTemplate.exchange(
				String.format("http://%s/hello?name={name}", HELLO_SERVICE), 
				HttpMethod.GET, 
				null, 
				new ParameterizedTypeReference<String>() {}, 
				name);
		return exchange.getBody();
	}
	
	@SuppressWarnings("unused")
	private String defaultGreeting(String username) {
        return "defaultGreeting";
    }
	
	@RequestMapping("/goodbye")
	public String goodbye(@RequestParam("name") String name) {
		return feignClient.sayGoodbye(name);
	}
	
	@FeignClient("http://"+ HELLO_SERVICE)
	interface GoodbyeClient {
		@RequestMapping(method = RequestMethod.GET, value = "/goodbye")
		String sayGoodbye(@RequestParam("name") String name);
	}
	
	@LoadBalanced
	@Bean
	private RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
}
