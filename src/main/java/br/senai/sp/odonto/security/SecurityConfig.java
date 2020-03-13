package br.senai.sp.odonto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtAuthenticationService jwtAuthenticationService;
	
	@Autowired
	private UserDetailServiceImpl userDetailService;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*
		 MODO BASIC
		 
		 * http
			.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.httpBasic()
			.and().csrf().disable();
		
			
		MODO HARD
		
		sessionManagement = Serve para falar que não vamos guardar os dados na sessão
		
		antMatches = Para falar as rotas e as Roles
		
		*/
		
		http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/odonto/auth/login").permitAll()
				.antMatchers(HttpMethod.GET, "/odonto/dentistas/**").hasAnyRole("USER", "ADMIN")
				.antMatchers(HttpMethod.POST, "/odonto/dentistas/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT, "/odonto/dentistas/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/odonto/dentistas/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/odonto/dentistas/foto/**").hasAnyRole("USER", "ADMIN")
				.anyRequest().authenticated()
			.and()
				.apply(new JwtAuthenticationConfigurer(jwtAuthenticationService));
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		/* Roles: serve para você indicar qual a permissão. */
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		
		auth.userDetailsService(userDetailService).passwordEncoder(encoder);
		
		/*
		 
		 * auth
			.inMemoryAuthentication()
			.passwordEncoder(encoder)
			.withUser("jean")
			.password(encoder.encode("123"))
			.roles("USER")
			.and()
			.passwordEncoder(encoder)
			.withUser("admin")
			.password(encoder.encode("admin"))
			.roles("ADMIN")
			.and()
			.passwordEncoder(encoder)
			.withUser("dentista")
			.password(encoder.encode("123"))
			.roles("DENTISTA")
			.and()
			.passwordEncoder(encoder)
			.withUser("paciente")
			.password(encoder.encode("123"))
			.roles("PACIENTE");
		*/
		
	}

}
