package com.iastate.edu.coms309.sb4.Getit.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class GetitServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetitServerApplication.class, args);
	}

}
