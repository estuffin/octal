package octal.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.pojo.Keys;
import com.myjeeva.digitalocean.pojo.Regions;
import com.myjeeva.digitalocean.pojo.Sizes;

import octal.models.User;

@RestController
@RequestMapping(value = "do")
public class DoController {
	
	private Logger logger = LoggerFactory.getLogger(DoController.class);
	
	@Autowired
	User user; 
	
    @GetMapping("sizes")
    public Sizes getSizes() {
    	Sizes sizes = null;
    	try {
			sizes = user.getDoClient().getAvailableSizes(0);
			logger.info("{} ({}) getting sizes. Rate limit: {}", user.getName(), user.getUser_id(), sizes.getRateLimit().toString());
		} catch (DigitalOceanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequestUnsuccessfulException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return sizes;
    }
    
    @GetMapping("keys")
    public Keys getKeys() {
    	Keys keys = null;
    	try {
			keys = user.getDoClient().getAvailableKeys(0);
		} catch (DigitalOceanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequestUnsuccessfulException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return keys;
    }
	
    @GetMapping("regions")
    public Regions getRegions() {
    	Regions regions = null;
    	try {
    		regions = user.getDoClient().getAvailableRegions(0);
    		logger.info("{} ({}) getting regions. Rate limit: {}", user.getName(), user.getUser_id(), regions.getRateLimit().toString());
		} catch (DigitalOceanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequestUnsuccessfulException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return regions;
    }
}
