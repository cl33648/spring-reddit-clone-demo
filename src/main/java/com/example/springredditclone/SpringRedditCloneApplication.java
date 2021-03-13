package com.example.springredditclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication 	//when run, it will automatically scan the components in the current package and its sub-packages
@EnableAsync 			//In the real world situation we should handle the Email Sending functionality Asynchronously.
public class SpringRedditCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRedditCloneApplication.class, args);
	}

}
