package octal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import octal.models.Server;
import octal.models.User;

@Transactional
public class DBService {
	
	@Autowired
	private DAO dao;
	
	public void createUser(User user) {
		dao.createUser(user);
	}
	
	public void updateUser(User user) {
		dao.updateUser(user);
	}
	
	public User fetchUser(long id) {
		return dao.fetchUser(id);
	}
	
	public void deleteUser(long id) {
		dao.deleteUser(id);
	}
	
	public void createServer(Server server) {
		dao.createServer(server);
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
}
