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
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import octal.dao.DBService;
import octal.models.User;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	@Autowired
	private DBService db;
	
	Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

	@Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession     session = request.getSession();
        DefaultOidcUser user    = (DefaultOidcUser)authentication.getPrincipal();
		String          name    = user.getAttribute("name");
		String          email   = user.getAttribute("email");
        
        if (session != null){
        	String refererUrl = request.getHeader("Referer");
        	logger.info(name + " (" + email + ")" + " logging out from: " + refererUrl);
        	
        	if (db != null) {
        		User foundUser = db.findUserByEmail(email);
                foundUser.setLast_ip(Utils.getClientIpAddress(request));
                foundUser.setLast_login_date(new Date());
                db.updateUser(foundUser);
        	}
        	 
            session.removeAttribute("username");
            session.removeAttribute("userEmail");
        }
    }
}
