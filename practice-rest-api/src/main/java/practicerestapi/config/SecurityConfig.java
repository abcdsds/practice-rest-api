package practicerestapi.config;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import lombok.RequiredArgsConstructor;
import practicerestapi.account.Account;
import practicerestapi.account.AccountRepository;
import practicerestapi.account.AccountRole;
import practicerestapi.account.AccountService;
import practicerestapi.commons.AppProperties;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final AccountService accountService;
	private final AppProperties appProperties;
	private final PasswordEncoder passwordEncoder;
	
	
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring()
				.mvcMatchers("/docs/index.html")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
		
	@Bean
	public ApplicationRunner applicationunner() {
		return new ApplicationRunner() {
			
			@Override
			public void run(ApplicationArguments args) throws Exception {
				// TODO Auto-generated method stub
				
				System.out.println(appProperties.getClientId());
				System.out.println(appProperties.getClientSecret());
				
				Account admin = Account.builder()
								.email(appProperties.getAdminUsername())
								.password(appProperties.getAdminPassword())
								.roles( Stream.of(AccountRole.ADMIN).collect(Collectors.toSet()) )
								.build();
				
				Account user = Account.builder()
						.email(appProperties.getUserUsername())
						.password(appProperties.getUserPassword())
						.roles( Stream.of(AccountRole.USER).collect(Collectors.toSet()) )
						.build();
				
				accountService.saveAccount(admin);
				accountService.saveAccount(user);
			}
		};
	}
}
