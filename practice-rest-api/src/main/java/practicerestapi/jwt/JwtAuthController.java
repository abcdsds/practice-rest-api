package practicerestapi.jwt;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import practicerestapi.account.Account;
import practicerestapi.account.CurrentUser;

@RequestMapping(value = "/api" , produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class JwtAuthController {

	private final AuthenticationManager manager;
	
	private final JwtProvider jwtProvier;
	
	@PostMapping("/jwt")
	public ResponseEntity<?> createAuthenticationToken(@CurrentUser Account account) throws Exception {
		
		if (account == null) {
			return ResponseEntity.badRequest().build();
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String generateJwtToken = jwtProvier.generateJwtToken(authentication);
		
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setToken(generateJwtToken);
		
		return ResponseEntity.ok(jwtResponse);
		
	}

}
