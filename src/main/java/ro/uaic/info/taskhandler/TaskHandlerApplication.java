package ro.uaic.info.taskhandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableEurekaClient
public class TaskHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskHandlerApplication.class, args);
	}

}
