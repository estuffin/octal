package octal.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import octal.CustDigitalOceanClient;
import octal.dao.DBService;
import octal.models.User;

@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	DBService db;
	
	@Autowired
	User user;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		if (response.isCommitted()) {
            return;
        }
		
        HttpSession session = request.getSession(false);
        if (session != null && user.isEmpty()) {
        	DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        	String email = oidcUser.getAttribute("email");
        	User temp = db.findUserByEmail(email); 
            user.setCreate_date(temp.getCreate_date());
            user.setCurr_ip(temp.getCurr_ip());
            user.setCurr_login_date(temp.getCurr_login_date());
            user.setDo_api_key(temp.getDo_api_key());
            user.setEmail(temp.getEmail());
            user.setG_id(temp.getG_id());
            user.setLast_ip(temp.getLast_ip());
            user.setLast_login_date(temp.getLast_login_date());
            user.setLogin_count(temp.getLogin_count());
            user.setName(temp.getName());
            user.setPicture(temp.getPicture());
            user.setUpdate_date(temp.getUpdate_date());
            user.setUser_id(temp.getUser_id());
            
            if (user.getDo_api_key() != null && !user.getDo_api_key().isEmpty()) {
            	user.setDoClient(new CustDigitalOceanClient("v2", user.getDo_api_key()));
            }
        }
        
        redirectStrategy.sendRedirect(request, response, "/servers");
    }
}
