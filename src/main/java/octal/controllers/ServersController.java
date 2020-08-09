package octal.controllers;

import java.text.SimpleDateFormat;
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
import com.myjeeva.digitalocean.pojo.Delete;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Droplets;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.LinkAction;
import com.myjeeva.digitalocean.pojo.Region;

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
    	List<Server> servers = db.fetchUserServers(user.getUser_id());
    	
    	Droplets d = null;
    	try {
			d = user.getDoClient().getAvailableDroplets(0, 25);
		} catch (DigitalOceanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequestUnsuccessfulException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	List<Droplet> droplets = d.getDroplets();
    	if (droplets == null || droplets.size() == 0) {
    		for (Server server : servers) {
    			if (server.getDo_server_id() == null) {
    				server.setDo_server_status(null);
    				db.updateServer(server);
    			}
    		}
    	} else {
        	for (Server server : servers) {
        		for (Droplet droplet : droplets) {
        			if (server.getDo_server_id().equals(droplet.getId())) {
        				server.setDo_server_status(droplet.getStatus().name());
        				server.setUpdate_date(new Date());
        				db.updateServer(server);
        			}
        		}
        	}
    	}
    	
    	return servers;
    }
    
	@PostMapping("create")
	public Server createServer(HttpSession session, HttpServletRequest req, @RequestBody Server data) {
		logger.info("{} ({}) creating a new server: {}", user.getName(), user.getUser_id(), data.toString());
		
		data.setUser_id(user.getUser_id());
		data.setCreate_date(new Date());
		
		// create ssh key here?
		
		return db.createServer(data);
	}
	
	@GetMapping("delete/{id}")
	public ResponseEntity<Object> deleteServer(@PathVariable Long id) {
		logger.info("{} ({}) deleting server: {}", user.getName(), user.getUser_id(), id);
		
		Server server = db.fetchServer(id);
		Integer doId = server.getDo_server_id();
		Droplet droplet = null;
		DigitalOcean client = user.getDoClient();
		
		if (doId != null) {
			try {
				droplet = client.getDropletInfo(doId);
			} catch (DigitalOceanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (RequestUnsuccessfulException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			if (droplet != null) {
				try {
					client.deleteDroplet(doId);
				} catch (DigitalOceanException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
				} catch (RequestUnsuccessfulException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		
		db.deleteServer(id);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@GetMapping("start/{id}")
	public ResponseEntity<Object> startServer(HttpSession session, @PathVariable Long id) {
		logger.info("Starting server id: {}", id);
		
		
		String pattern = "yyyyMMdd-HH.mm.ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		
		Server server = db.fetchServer(id);
		String serverName = "octal-" + id.toString() + "-" + server.getName().replace(" ", "-") + "-" + date;
		
		logger.info("Starting server: {}", serverName);
		
		DigitalOcean client = user.getDoClient();
		Droplet newDrop = new Droplet();
		newDrop.setName(serverName);
		newDrop.setSize(server.getDo_size());
		newDrop.setImage(new Image(47384041));
		newDrop.setRegion(new Region(server.getDo_region()));
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
			 
			 String links = "";
			 for (LinkAction action : d.getLinks().getActions()) {
				 links += action.getHref() + ",";
			 }
			 links = links.substring(0, links.length() - 1);
			 
			 server.setAction_link(links);
			 server.setDo_server_id(d.getId());
			 server.setUpdate_date(new Date());
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
		
		server.setAction_link(null);
		server.setDo_server_id(null);
		server.setUpdate_date(new Date());
		db.updateServer(server);
		
		return new ResponseEntity<Object>(HttpStatus.OK); 
	}
}
