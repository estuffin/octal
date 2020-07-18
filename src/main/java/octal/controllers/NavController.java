package octal.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import octal.Utils;
import octal.dao.DBService;

@Controller
public class NavController {
	
	Logger logger = LoggerFactory.getLogger(NavController.class);
	
	@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    DBService db;
	
	@GetMapping("/")
	ModelAndView home(Model model, OAuth2AuthenticationToken authentication, HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		
		if (authentication != null) {
			String ip = Utils.getClientIpAddress(req);
			OAuth2User user = authentication.getPrincipal();
			
            for (Map.Entry<String, Object> o : user.getAttributes().entrySet()) {
            	logger.info("Key = " + o.getKey() + " | " + o.getValue());
            }
            
//            model.addAttribute("name", authentication.getPrincipal().getAttribute("name"));
		}
		
		return mv;
	}
	
	@GetMapping("login")
	String login(Model model) {
		String authorizationRequestBaseUri = "oauth2/authorization";  
		Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
		
		Iterable<ClientRegistration> clientRegistrations = null;
	    ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
	    
	    if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
	        clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
	    }
	 
	    clientRegistrations.forEach(registration -> 
	    	oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
	    model.addAttribute("urls", oauth2AuthenticationUrls);
	 
	    return "login";
	}
	
	@GetMapping("servers")
	ModelAndView servers() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("servers");
		return mv;
	}

}
