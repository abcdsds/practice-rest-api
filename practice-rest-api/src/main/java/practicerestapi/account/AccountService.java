package practicerestapi.account;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService{

	
	private final AccountRepository accountRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	
	public Account saveAccount(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		return accountRepository.save(account);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Account account = accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
		
		return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
	}

	private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
		// TODO Auto-generated method stub
		return roles.stream().map(v -> new SimpleGrantedAuthority("ROLE_" + v.name())).collect(Collectors.toSet());
	}

}
