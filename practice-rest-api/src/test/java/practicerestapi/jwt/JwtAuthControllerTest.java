package practicerestapi.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.HttpHeaders;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import practicerestapi.account.Account;
import practicerestapi.account.AccountForm;
import practicerestapi.account.AccountRole;
import practicerestapi.common.BaseTest;

class JwtAuthControllerTest extends BaseTest {

	@BeforeEach
	public void setUp() {
		accountRepository.deleteAll();
	}
	
	@Test
	void test() throws Exception {
		
		mockMvc.perform(post("/api/jwt")
							.header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
							.contentType(MediaType.APPLICATION_JSON)
							.accept(MediaTypes.HAL_JSON)
							.contentType(MediaType.APPLICATION_JSON)
						)
					.andExpect(status().isOk())
					.andDo(print());
		
		
			
	}
	
	private Account createAccount() {
		Account account = Account.builder().email(appProperties.getUserUsername()).password(appProperties.getUserPassword())
				.roles(Stream.of(AccountRole.ADMIN, AccountRole.USER).collect(Collectors.toSet())).build();

		
		return accountService.saveAccount(account);
	}
	
	private String getBearerToken(boolean needAccount) throws Exception {
		return "Bearer " + getAccessToken(needAccount);
	}


    private String getAccessToken(boolean needAccount) throws Exception {
    	
    	if (needAccount) {
    		createAccount();
    	}
    	
		ResultActions andExpect = mockMvc.perform(post("/oauth/token")
					.with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
					.param("username" , appProperties.getUserUsername())
					.param("password" , appProperties.getUserPassword())
					.param("grant_type" , "password"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("access_token").exists());
		
		String contentAsString = andExpect.andReturn().getResponse().getContentAsString();
		JSONTokener jsonTokener = new JSONTokener(contentAsString);
		JSONObject nextValue = (JSONObject) jsonTokener.nextValue();
		return nextValue.getString("access_token");
					
				
    }

}
