package octal.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import octal.dao.DBService;
import octal.models.User;

@RestController
@RequestMapping(value = "/server")
public class ServersController {
	
	Logger logger = LoggerFactory.getLogger(NavController.class);
	
    @Autowired
    DBService db;
	
	@GetMapping("create")
	User createServer(HttpSession session, HttpServletRequest req) {
		String email = (String)session.getAttribute("userEmail");
		return db.findUserByEmail(email);
	}
}
