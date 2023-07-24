package ourtine.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// exclude : Security 기능 구현 전이라, 인증 미구현으로 인한 에러 방지를 위해 추가
@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
