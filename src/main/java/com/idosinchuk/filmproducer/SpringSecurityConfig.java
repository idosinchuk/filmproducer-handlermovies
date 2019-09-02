package com.idosinchuk.filmproducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	@Autowired
	private Environment env;

	private String readProperty(String param) {
		return env.getProperty(param);
	}

	private PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	private static final String[] AUTH_WHITELIST = {

			// -- swagger ui
			"/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')").anyRequest()
				.authenticated().and().httpBasic().authenticationEntryPoint(authEntryPoint);

		http.csrf().disable().authorizeRequests().antMatchers("/api/**")
				.access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')").anyRequest().authenticated().and().httpBasic()
				.authenticationEntryPoint(authEntryPoint);

		http.headers().cacheControl().disable(); // Spring Security invalidated it in the response, must use this line
													// to disable default cache control from Spring Security

		// Allows swagger
		http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication().withUser(readProperty("spring.security.user.name"))
				.password(encoder.encode(readProperty("spring.security.user.password"))).roles("USER").and()
				.withUser(readProperty("spring.security.useradmin.name"))
				.password(encoder.encode(readProperty("spring.security.useradmin.password"))).roles("ADMIN");
	}

}
