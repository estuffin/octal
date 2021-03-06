package octal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomLogoutSuccessHandler logoutSuccessHandler;
	@Autowired
	private AuthSuccessHandler authSuccessHandler;
	

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        		.antMatchers("/", "/login")
        		.permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .successHandler(authSuccessHandler)
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*");

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}