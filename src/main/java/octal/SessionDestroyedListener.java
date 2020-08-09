package octal;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import octal.dao.DBService;
import octal.models.User;

@Component
public class SessionDestroyedListener implements ApplicationListener<HttpSessionDestroyedEvent> {
	
	Logger logger = LoggerFactory.getLogger(SessionDestroyedListener.class);
	
	@Autowired
	User user;
	
	@Autowired
	private DBService db;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	Utils utils;
	
	public void onApplicationEvent(HttpSessionDestroyedEvent evt) {
		String refererUrl = request.getHeader("Referer");
    	logger.info("{} ({}) logging out from: {}", user.getName(), user.getUser_id(), refererUrl);
    	
    	if (db != null && !user.isEmpty()) {
    		User temp = new User();
    		temp.setCreate_date(user.getCreate_date());
    		temp.setCurr_ip(user.getCurr_ip());
    		temp.setCurr_login_date(user.getCurr_login_date());
            temp.setDo_api_key(user.getDo_api_key());
            temp.setEmail(user.getEmail());
            temp.setG_id(user.getG_id());
            temp.setLogin_count(user.getLogin_count());
            temp.setName(user.getName());
            temp.setPicture(user.getPicture());
            temp.setUpdate_date(user.getUpdate_date());
            temp.setUser_id(user.getUser_id());
            temp.setLast_ip(utils.getClientIpAddress(request));
            temp.setLast_login_date(new Date());
            db.updateUser(temp);
    	}
    }
}
