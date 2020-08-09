package octal.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Delete;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.Key;
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
    public Server one(@PathVariable Long id) {
    	return db.fetchServer(id);
    }
    
    @GetMapping("list")
    public List<Server> getServers(HttpSession session) {
    	return db.fetchUserServers(user.getUser_id());
    }
    
	@PostMapping("create")
	public Server createServer(HttpSession session, HttpServletRequest req, @RequestBody Server data) {
		data.setUser_id(user.getUser_id());
		data.setCreate_date(new Date());
		
		// create ssh key here?
		
		return db.createServer(data);
	}
	
	@GetMapping("start/{id}")
	public ResponseEntity<Object> startServer(HttpSession session, @PathVariable Long id) {
		logger.info("Starting server id: {}", id);
		
		
		String pattern = "yyyyMMdd-HH.mm.ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		
		Server server = db.fetchServer(id);
		String serverName = "octal-" + id.toString() + "-" + server.getName().replace(" ", "-") + "-" + date;
		
		logger.info("Staring server: {}", serverName);
		
		DigitalOcean client = user.getDoClient();
		Droplet newDrop = new Droplet();
		newDrop.setName(serverName);
		newDrop.setSize("512mb");
		newDrop.setImage(new Image(47384041));
//		newDrop.setRegion(new Region(server.getDo_region()));
		newDrop.setRegion(new Region("nyc1"));
		newDrop.setEnableBackup(false);
		newDrop.setEnableIpv6(false);
		newDrop.setEnablePrivateNetworking(false);
		
//		List<Key> keys = new ArrayList<>();
//		keys.add(new Key());
//		newDrop.setKeys(keys);
		
		Droplet d;
		try {
			 d = client.createDroplet(newDrop); // save droplet into db (specially the id)
			 logger.info(d.toString());
			 
			 server.setDo_server_id(d.getId());
			 db.updateServer(server);
		} catch (DigitalOceanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RequestUnsuccessfulException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@GetMapping("stop/{id}")
	public ResponseEntity<Object> stopServer(HttpSession session, @PathVariable Long id) {
		logger.info("Stopping server id: {}", id);
		
		Server server = db.fetchServer(id);
		Delete result;
		try {
			result = user.getDoClient().deleteDroplet(server.getDo_server_id());
			logger.info(result.toString());
		} catch (DigitalOceanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RequestUnsuccessfulException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Object>(HttpStatus.OK); 
	}
}
