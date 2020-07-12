package octal.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NavController {
	
	Logger logger = LoggerFactory.getLogger(NavController.class);
	
	@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
	
	@GetMapping("/")
	ModelAndView home(Model model, OAuth2AuthenticationToken authentication) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		
		if (authentication != null) {
			OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

			if (client != null) {
				String userInfoEndpointUri = client.getClientRegistration()
			            .getProviderDetails()
			            .getUserInfoEndpoint()
			            .getUri();

		        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
		            RestTemplate restTemplate = new RestTemplate();
		            HttpHeaders headers = new HttpHeaders();
		            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());

		            HttpEntity<String> entity = new HttpEntity<String>("", headers);
		            ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
		            Map userAttributes = response.getBody();
		            model.addAttribute("name", userAttributes.get("name"));
		            
		            logger.info("Logged-in user's attributes");
		            for (Object key : userAttributes.keySet()) {
		            	logger.info("Key = " + key + " | " + userAttributes.get(key));
		            }
		        }
			}
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
