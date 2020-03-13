package br.senai.sp.odonto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.senai.sp.odonto.repository.UserRepository;

// Assinatura parar o spring saber que pode usar
@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		br.senai.sp.odonto.model.User user = userRepository.findByUsername(username);
		
		UserDetails userDetails = new User(
				username, 
				user.getPassword(), 
				AuthorityUtils.createAuthorityList(user.getRole())
				);
		
		return userDetails;
		
	}

	
}
