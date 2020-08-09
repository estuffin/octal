package octal;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.pojo.Key;

import octal.models.User;

@Component
public class Utils {
	
	@Autowired
	User user;
	
	public String getClientIpAddress(HttpServletRequest request) {
	    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
	    
	    if (xForwardedForHeader == null) {
	        return request.getRemoteAddr();
	    } else {
	        return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
	    }
	}
	
	public String resourceAsString(String path) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
	    Resource resource = resourceLoader.getResource(path);
	    
        try (Reader reader = new InputStreamReader(resource.getInputStream(), "UTF-8")) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
	
	public Key createDoSshKey() {
		return doKeyCreation(user.getDoClient());
	}
	
	public Key createDoSshKey(String apiKey) {
		return doKeyCreation(new CustDigitalOceanClient("v2", apiKey));
	}
	
	private Key doKeyCreation(DigitalOcean client) {
		Key key = null;
		try {
			String pubKey = resourceAsString("id_rsa.pub");
			String keyName = "octal-public-key";
			key = client.createKey(new Key(keyName, pubKey));
		} catch (DigitalOceanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequestUnsuccessfulException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}
	
}
