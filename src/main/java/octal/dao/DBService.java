package octal.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.myjeeva.digitalocean.pojo.Key;

import octal.CustDigitalOceanClient;
import octal.Utils;
import octal.models.Server;
import octal.models.User;
import octal.models.UserKey;

@Service
public class DBService extends OidcUserService  {
	
	Logger logger = LoggerFactory.getLogger(DBService.class);
	
	@Autowired
	private DAO dao;
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private User user;
	
	public User findUserByEmail(String email) {
		List<User> foundUsers = dao.findUserByEmail(email);
		if (foundUsers.isEmpty()) {
			return null;
		} else {
			return foundUsers.get(0); 
		}
	}
	
	public void createUser(User user) {
		dao.createUser(user);
	}
	
	public void updateUser(User user) {
		if (user != null && !user.isEmpty()) {
			dao.updateUser(user);
		}
	}
	
	public User fetchUser(long id) {
		return dao.fetchUser(id);
	}
	
	public void deleteUser(long id) {
		dao.deleteUser(id);
	}
	
	public OidcUser loadUser(OidcUserRequest userReq) throws OAuth2AuthenticationException {
		OidcUser oUser      = super.loadUser(userReq);
		Map      attributes = oUser.getAttributes();
		String   email      = (String)attributes.get("email");
		User     foundUser  = findUserByEmail(email);
		boolean  userExists = foundUser != null;
		
		if (userExists) {
			foundUser.setUpdate_date(new Date());
			foundUser.setCurr_login_date(new Date());
			foundUser.setLogin_count(foundUser.getLogin_count() + 1);
			dao.updateUser(foundUser);
			Long userId = foundUser.getUser_id();
			if (dao.findUserKeysByUserId(userId).isEmpty()
					&& foundUser.getDo_api_key() != null
					&& !foundUser.getDo_api_key().isEmpty()) {
				
				createDoSshKey(userId, foundUser.getDo_api_key());
			}
		} else {
			User user = new User();
			user.setEmail(email);
			user.setG_id((String)attributes.get("sub"));
			user.setPicture((String)attributes.get("picture"));
			user.setName((String)attributes.get("name"));
			user.setCreate_date(new Date());
			user.setCurr_login_date(new Date());
			user.setLogin_count(0);
			dao.createUser(user);
		}
		
		return oUser;
	}
	
	public Server createServer(Server server) {
		return dao.createServer(server);
	}
	
	public void updateServer(Server server) {
		dao.updateServer(server);
	}
	
	public Server fetchServer(long id) {
		return dao.fetchServer(id);
	}
	
	public void deleteServer(long id) {
		dao.deleteServer(id);
	}
	
	public List<Server> fetchUserServers(long userId) {
		return dao.fetchUserServers(userId);
	}
	
	public void createUserKey(UserKey key) {
		dao.createUserKey(key);
	}
	
	public void updateUserKey(UserKey key) {
		dao.updateUserKey(key);
	}
	
	public UserKey fetchUserKey(Integer id) {
		return dao.fetchUserKey(id);
	}
	
	public List<UserKey> findUserKeysByUserId(Long id) {
		return dao.findUserKeysByUserId(id);
	}
	
	public void deleteUserKey(Integer id) {
		dao.deleteUserKey(id);
	}
	
	public void createDoSshKey(Long userId, String apiKey) {
		Key doSshKey = utils.createDoSshKey(apiKey);
		UserKey uKey = new UserKey();
		uKey.setKey_id(doSshKey.getId());
		uKey.setName(doSshKey.getName());
		uKey.setFingerprint(doSshKey.getFingerprint());
		uKey.setPublic_key(doSshKey.getPublicKey());
		uKey.setUser_id(userId);
		uKey.setCreate_date(new Date());
		uKey.setIsInternal('Y');
		dao.createUserKey(uKey);
	}
}
