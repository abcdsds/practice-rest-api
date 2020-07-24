package practicerestapi.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import practicerestapi.account.Account;
import practicerestapi.account.AccountRole;
import practicerestapi.common.BaseControllerTest;

class AuthServerConfigTest extends BaseControllerTest {

	
	
	@Test
	public void getAuthToken() throws Exception {
		
//		Account account = Account.builder().email(appProperties.getUserUsername()).password(appProperties.getUserPassword())
//				.roles(Stream.of(AccountRole.ADMIN, AccountRole.USER).collect(Collectors.toSet())).build();
//
//		accountService.saveAccount(account);
		
		mockMvc.perform(post("/oauth/token")
				.with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
				.param("username" , appProperties.getUserUsername())
				.param("password" , appProperties.getUserPassword())
				.param("grant_type" , "password"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("access_token").exists());
	}

}
