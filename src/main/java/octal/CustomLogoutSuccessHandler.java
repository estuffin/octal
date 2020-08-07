package octal;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import octal.dao.DBService;
import octal.models.User;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	@Autowired
	private DBService db;
	
	@Autowired
	User user;
	
	Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession     session = request.getSession();
        
        if (session != null){
        	String refererUrl = request.getHeader("Referer");
        	logger.info("{} ({}) logging out from: {}" + user.getName(), user.getEmail(), refererUrl);
        	
        	if (db != null && !user.isEmpty()) {
                user.setLast_ip(Utils.getClientIpAddress(request));
                user.setLast_login_date(new Date());
                db.updateUser(user);
        	}
        }
        
        redirectStrategy.sendRedirect(request, response, "/");
    }
}
