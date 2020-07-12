package octal.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import octal.models.Server;
import octal.models.User;

@Repository
public class DAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	public void createUser(User user) {
		entityManager.persist(user);
	}
	
	public void updateUser(User user) {
		entityManager.merge(user);
	}
	
	public User fetchUser(long id) {
		return entityManager.find(User.class, id);
	}
	
	public void deleteUser(long id) {
		User user = fetchUser(id);
		if (user != null) {
			entityManager.remove(user);
		}
	}
	
	public void createServer(Server server) {
		entityManager.persist(server);
	}
	
	public void updateServer(Server server) {
		entityManager.merge(server);
	}
	
	public Server fetchServer(long id) {
		return entityManager.find(Server.class, id);
	}
	
	public void deleteServer(long id) {
		Server server = fetchServer(id);
		if (server != null) {
			entityManager.remove(server);
		}
	}
}
