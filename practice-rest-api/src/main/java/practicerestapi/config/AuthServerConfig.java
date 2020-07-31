package practicerestapi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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
	private final AppProperties appProperties;
	private final AccountService accountService;
	

	
	@Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
	
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
				.authorizedGrantTypes("authorization_code", "password" , "client_credentials", "referesh_token")
				.scopes("read", "write")
				.secret(passwordEncoder.encode(appProperties.getClientSecret()))
				.accessTokenValiditySeconds(60 * 60)
				.refreshTokenValiditySeconds(60 * 60 * 24);
	}
	
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// TODO Auto-generated method stub
		endpoints.tokenStore(tokenStore())
					.authenticationManager(authenticationManager)
					.accessTokenConverter(accessTokenConverter())
					.userDetailsService(accountService);
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("test1111");
		//converter.setVerifierKey("123");
		// final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new
		// ClassPathResource("mytest.jks"), "mypass".toCharArray());
		// converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
		return converter;
	}
	
}
