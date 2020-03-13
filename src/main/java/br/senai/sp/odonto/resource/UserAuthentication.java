package br.senai.sp.odonto.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.odonto.dto.UserAccountCredential;
import br.senai.sp.odonto.model.User;
import br.senai.sp.odonto.repository.UserRepository;
import br.senai.sp.odonto.security.JwtAuthenticationService;

@RestController
@CrossOrigin
@RequestMapping("/odonto")
public class UserAuthentication {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtAuthenticationService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/auth/login")
	public ResponseEntity<Map<Object, Object>> signIn(@RequestBody UserAccountCredential credential) {
		
		System.out.println("*************** AUTENTICANDO *******************");
		
		UsernamePasswordAuthenticationToken userCredential = new UsernamePasswordAuthenticationToken(
				credential.getUsername(), 
				credential.getPassword()
				);
		
		authenticationManager.authenticate(userCredential);
		
		// Pegando o user para pegar o role dele;
		User user = userRepository.findByUsername(credential.getUsername());
		
		List<String> roles = new ArrayList<>();
		roles.add(user.getRole());
		
		String token = jwtService.createToken(credential.getUsername(), roles);
		
		Map<Object, Object> jsonReponse = new HashMap<>();
		
		jsonReponse.put("username", credential.getUsername());
		jsonReponse.put("token", token);
		
		return ResponseEntity.ok(jsonReponse);
		
	}
	
}
