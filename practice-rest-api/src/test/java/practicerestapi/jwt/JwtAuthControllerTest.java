package practicerestapi.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import practicerestapi.account.Account;
import practicerestapi.account.AccountRole;
import practicerestapi.common.BaseTest;

class JwtAuthControllerTest extends BaseTest {

	@BeforeEach
	public void setUp() {
		accountRepository.deleteAll();
	}
	
	@Test
	void test() throws Exception {
		Account createAccount = createAccount();
		
		
		mockMvc.perform(post("/api/jwt")
							.content(objectMapper.writeValueAsString(createAccount))
						)
					.andExpect(status().isOk())
					.andDo(print());
		
		
			
	}
	
	private Account createAccount() {
		Account account = Account.builder().email(appProperties.getUserUsername()).password(appProperties.getUserPassword())
				.roles(Stream.of(AccountRole.ADMIN, AccountRole.USER).collect(Collectors.toSet())).build();

		
		return accountService.saveAccount(account);
	}

}
