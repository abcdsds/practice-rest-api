package practicerestapi.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.jsonwebtoken.Jwts;

@SpringBootTest
@ActiveProfiles("test")
public class jwtTest {

	@Test
	void test() {
		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTYxNTgwMDQsInVzZXJfbmFtZSI6InVzZXJAZW1haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI0MDFhYjliNS1jMzAzLTRmNTUtODVjZi0yZTk1YmIzMWY3MTEiLCJjbGllbnRfaWQiOiJteUFwcCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.M14dMgaPcPpNeAtYPMJQfhiyQFWE7qXuWnZlIuuFmKI";
		
		System.out.println("aaaaaaaaaaaaaaaaaa");
		System.out.println(Jwts.parser().setSigningKey("test1111".getBytes()).parse(token).getBody());
		System.out.println("aaaaaaaaaaaaaaaaaa");
	}
}
