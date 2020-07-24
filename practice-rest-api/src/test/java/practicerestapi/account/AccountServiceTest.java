package practicerestapi.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

	
	@Autowired
	AccountService accountService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	void findByUsername() {

		String password = "aaabb";
		// Given
		Account account = Account.builder().email("222@naver.com").password(password)
				.roles(Stream.of(AccountRole.ADMIN, AccountRole.USER).collect(Collectors.toSet())).build();

		accountService.saveAccount(account);
		
		// When
		UserDetailsService userDetailsService = (UserDetailsService) accountService;
		UserDetails userDetails = userDetailsService.loadUserByUsername("222@naver.com");

		// Then
		assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
	}

	@Test
	public void findByusernameFail() {

		assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername("blahblah"));
	}
}
