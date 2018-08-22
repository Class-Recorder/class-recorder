package com.classrecorder.teacherserver.server.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security configuration. In this class can be configured several aspects
 * related to security:
 * <ul>
 * <li>Security behavior: Login method, session management, CSRF, etc..</li>
 * <li>Authentication provider: Responsible to authenticate users. In this
 * example, we use an instance of UserRepositoryAuthProvider, that authenticate
 * users stored in a Spring Data database.</li>
 * <li>URL Access Authorization: Access to http URLs depending on Authenticated
 * vs anonymous users and also based on user role.</li>
 * </ul>
 * 
 * NOTE: The only part of this class intended for app developer customization is
 * the method <code>configureUrlAuthorization</code>. App developer should
 * decide what URLs are accessible by what user role.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//	public UserRepositoryAuthProvider userRepoAuthProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//H2 Console
        http.authorizeRequests().antMatchers("/console/**").permitAll();

		http.headers().frameOptions().disable();
		// H2 Console
		
		configureUrlAuthorization(http);

		// Disable CSRF protection (it is difficult to implement with ng2)
		http.csrf().disable();

		// Use Http Basic Authentication
		http.httpBasic();

		// Do not redirect when logout
		http.logout().logoutSuccessHandler((rq, rs, a) -> {
		});
	}

	private void configureUrlAuthorization(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/").permitAll();
		http.authorizeRequests().antMatchers("/api/logIn").permitAll();
		http.authorizeRequests().antMatchers("/api/logOut").permitAll();
        http.authorizeRequests().antMatchers( "/api/**").hasRole("TEACHER");
		
	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//		// Database authentication provider
//		auth.authenticationProvider(userRepoAuthProvider);
//	}
}

