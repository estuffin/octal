package octal;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		if (response.isCommitted()) {
            return;
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
        	DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        	String username = oidcUser.getAttribute("name");
        	String email = oidcUser.getAttribute("email");
            session.setAttribute("username", username);
            session.setAttribute("userEmail", email);
        }
        
        redirectStrategy.sendRedirect(request, response, "/");
    }
}