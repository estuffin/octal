package octal.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.Region;
import com.myjeeva.digitalocean.pojo.Size;

import octal.dao.DBService;
import octal.models.Server;
import octal.models.User;

@RestController
@RequestMapping(value = "/server")
public class ServersController {
	
	Logger logger = LoggerFactory.getLogger(ServersController.class);
	
    @Autowired
    DBService db;
    
    @Autowired
    User user;
	
    @GetMapping("{id}")
    Server one(@PathVariable Long id) {
    	return db.fetchServer(id);
    }
    
    @GetMapping("list")
    List<Server> getServers(HttpSession session) {
    	return db.fetchUserServers(user.getUser_id());
    }
    
	@PostMapping("create")
	Server createServer(HttpSession session, HttpServletRequest req, @RequestBody Server data) {
		data.setUser_id(user.getUser_id());
		data.setCreate_date(new Date());
		return db.createServer(data);
	}
	
	@GetMapping("start/{id}")
	ResponseEntity startServer(HttpSession session, @PathVariable Long id) {
		logger.info("Starting server id: {}", id);
		
		Server server = db.fetchServer(id);
		
		
		// create the form first for creating servers with the correct do parameters
//		DigitalOcean client = new DigitalOceanClient("v2", user.getDo_api_key());
//		Droplet newDrop = new Droplet();
//		newDrop.setName("octal-" + id.toString() + "-" + server.getName() + "-" + (new Date()).toString());
//		newDrop.setRegion(new Region(server.getDo_region()));
//		newDrop.setSize("");
//		newDrop.setImage(new Image(47384041));
		
		
		
		return new ResponseEntity(HttpStatus.OK);
	}
}
