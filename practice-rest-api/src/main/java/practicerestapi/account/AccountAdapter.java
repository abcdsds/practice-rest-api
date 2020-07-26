package practicerestapi.account;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AccountAdapter extends User {

	private Account account;
	
	public AccountAdapter(Account account) {
		super(account.getEmail() , account.getPassword(), authorities(account.getRoles()) );
		this.account = account;
	}

	private static Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
		// TODO Auto-generated method stub
		return roles.stream().map(v -> new SimpleGrantedAuthority("ROLE_" + v.name())).collect(Collectors.toSet());
	}

	public Account getAccount() {
		return account;
	}
}
