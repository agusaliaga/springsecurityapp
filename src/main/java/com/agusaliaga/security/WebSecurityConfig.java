package com.agusaliaga.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.agusaliaga.security.jwt.AuthEntryPointJwt;
import com.agusaliaga.security.jwt.AuthTokenFilter;
import com.agusaliaga.security.services.UserDetailsServiceImpl;


/*It provides HttpSecurity configurations to configure cors, csrf, session management, 
 * rules for protected resources. 
 * We can also extend and customize the default configuration that contains the elements below.*/
@Configuration
@EnableWebSecurity //allows Spring to find and automatically apply the class to the global Web Security.
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true) // provides AOP security on methods. It enables @PreAuthorize, @PostAuthorize
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/*Interface has a method to load User by userName and returns a UserDetails object 
	 * that Spring Security can use for authentication and validation.
	 UserDetails contains necessary information (such as: userName, password, authorities) 
	 to build an Authentication object.
	 Spring Security will load User details to perform authentication & authorization. 
	 So it has UserDetailsService interface that we need to implement.
	 he implementation of UserDetailsService will be used for configuring DaoAuthenticationProvider 
	 by AuthenticationManagerBuilder.userDetailsService() method.*/
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	/*will catch unauthorized error and return a 401 when Clients access protected resources without authentication.*/
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	/* AuthenticationManager has a DaoAuthenticationProvider (with help of UserDetailsService & PasswordEncoder) 
	 * to validate UsernamePasswordAuthenticationToken object. If successful, AuthenticationManager returns 
	 * a fully populated Authentication object (including granted authorities).*/
	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	/*We need a PasswordEncoder for the DaoAuthenticationProvider. If we don’t specify, it will use plain text.*/
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*It tells Spring Security how we configure CORS and CSRF, when we want to require all users to be 
	 * authenticated or not, which filter (AuthTokenFilter) and when we want it to work (filter before 
	 * UsernamePasswordAuthenticationFilter), which Exception Handler is chosen (AuthEntryPointJwt).*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().antMatchers("/api/auth/**").permitAll()
			.antMatchers(AUTH_WHITELIST).permitAll()
			.antMatchers("/api/test/**").permitAll()
			.antMatchers("/h2_console/**").permitAll()
			.anyRequest().authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
            // other public endpoints of your API may be appended to this array
    };
}
