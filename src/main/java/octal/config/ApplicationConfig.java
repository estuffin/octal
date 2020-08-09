package octal.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.annotation.SessionScope;

import octal.dao.DBService;
import octal.models.User;

@Configuration
public class ApplicationConfig {
	
	@Bean
	@SessionScope
	public User user(DBService db) {
		Object obj = null;
		DefaultOidcUser oidcUser = null;
		
		if (SecurityContextHolder.getContext() != null 
				&& SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
			
			obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if (!(obj instanceof String)) {
				oidcUser = (DefaultOidcUser)obj;
			}
		}
		
		if (oidcUser != null) {
			return db.findUserByEmail(oidcUser.getAttribute("email"));
		} else {
			return new User();
		}
	}
	
	@Bean
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
		return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	}
}
