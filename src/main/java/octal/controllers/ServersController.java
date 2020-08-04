package octal.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import octal.dao.DBService;
import octal.models.Server;
import octal.models.User;

@RestController
@RequestMapping(value = "/server")
public class ServersController {
	
	Logger logger = LoggerFactory.getLogger(NavController.class);
	
    @Autowired
    DBService db;
	
    @GetMapping("{id}")
    Server one(@PathVariable Long id) {
    	return db.fetchServer(id);
    }
    
    @GetMapping("list")
    List<Server> getServers(HttpSession session) {
    	User user = (User)session.getAttribute("userObj");
    	return db.fetchUserServers(user.getUser_id());
    }
    
	@PostMapping("create")
	Server createServer(HttpSession session, HttpServletRequest req, @RequestBody Server data) {
		User user = (User)session.getAttribute("userObj");
		data.setUser_id(user.getUser_id());
		data.setCreate_date(new Date());
		return db.createServer(data);
	}
}
