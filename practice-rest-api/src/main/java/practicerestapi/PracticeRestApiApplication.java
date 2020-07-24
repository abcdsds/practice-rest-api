package practicerestapi;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import practicerestapi.account.Account;
import practicerestapi.account.AccountRole;

@SpringBootApplication
public class PracticeRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticeRestApiApplication.class, args);
	}

	
}
