package org.runcity.mvc.config;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.runcity.mvc.config.util.CommonAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebMvcSecurity
@PropertySource("/WEB-INF/conf/app.properties")
@EnableGlobalMethodSecurity(securedEnabled = true)
@ComponentScan({"org.runcity.db.service", "org.runcity.db.service.impl"})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDetailsService userDetailsService;

	@Resource
	private Environment env;

	@Autowired
	public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/secure/**").authenticated()
				.antMatchers("/api/**").authenticated()
				.antMatchers("/api/**").authenticated()
			.and()
				.formLogin()
					.defaultSuccessUrl("/home")
					.loginPage("/login")
					.failureUrl("/login?state=error")
					.loginProcessingUrl("/j_spring_security_check")
					.usernameParameter("j_username")
					.passwordParameter("j_password")
					.permitAll()
			.and()
				.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout")
			.and()
				.csrf()
			.and()
				.rememberMe()
					.tokenRepository(persistentTokenRepository())
					.tokenValiditySeconds(new Integer(env.getRequiredProperty("runcity.rememberme_time")))
			.and()
				.exceptionHandling().accessDeniedHandler(new CommonAccessDeniedHandler("/403"))
			.and()
				.headers().xssProtection().contentTypeOptions().frameOptions()
			.and()
				.sessionManagement()
					.maximumSessions(new Integer(env.getRequiredProperty("runcity.max_sessions")))
					.sessionRegistry(sessionRegistry())
					.expiredUrl("/login?state=expired");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}
	
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }
    
    @Bean
    public ApplicationListener<InteractiveAuthenticationSuccessEvent> loginListener() {
    	return new ApplicationListener<InteractiveAuthenticationSuccessEvent>() {
    		@Autowired
    		private HttpSession httpSession;
    		
			@Override
			public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
				if (RememberMeAuthenticationFilter.class.equals(event.getGeneratedBy())) {
					sessionRegistry().registerNewSession(httpSession.getId(), event.getAuthentication().getPrincipal());
				}
			}
		};
    }
}
