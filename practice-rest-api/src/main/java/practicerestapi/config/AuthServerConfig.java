package practicerestapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practicerestapi.account.AccountService;
import practicerestapi.commons.AppProperties;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{

	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final TokenStore tokenStore;
	private final AccountService accountService;
	private final AppProperties appProperties;
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// TODO Auto-generated method stub
		security.passwordEncoder(passwordEncoder);
	}
	
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// TODO Auto-generated method stub

		log.info("clientSecret == {}" , appProperties.getClientId());
		log.info("clientSecret == {}" , appProperties.getClientSecret());
		
		clients.inMemory()
				.withClient(appProperties.getClientId())
				.authorizedGrantTypes("password" , "referesh_token")
				.scopes("read", "write")
				.secret(passwordEncoder.encode(appProperties.getClientSecret()))
				.accessTokenValiditySeconds(60 * 60)
				.refreshTokenValiditySeconds(60 * 60 * 24);
		
	}
	
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// TODO Auto-generated method stub
		endpoints.authenticationManager(authenticationManager)
				.tokenStore(tokenStore)
				.userDetailsService(accountService);
	}
	
}
