package practicerestapi.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practicerestapi.account.AccountAdapter;
import practicerestapi.account.AccountService;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter{
	
	private final JwtProvider provider;
	private final AccountService accountService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {
			String jwt = null;
			String username = null;
			
			final String authHeader = request.getHeader("Authorization");
			
			log.info("[[[[[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]{}" , authHeader);
			
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				
				
				jwt = authHeader.substring(7);
												
				username = provider.getUserNameFromJwtToken(jwt).split("user_name=")[1].split(",")[0];
				
				AccountAdapter accountAdapter = (AccountAdapter)accountService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(accountAdapter.getAccount(), accountAdapter.getPassword(), accountAdapter.getAuthorities());
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Can NOT set user authentication -> Message: {}", e);
		}
		
		filterChain.doFilter(request, response);
		
	}

}

