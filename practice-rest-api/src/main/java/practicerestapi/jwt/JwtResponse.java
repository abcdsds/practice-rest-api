package practicerestapi.jwt;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class JwtResponse {
	
	private String token;
	
	private String type = "Bearer";

}