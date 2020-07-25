package octal.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import octal.models.Server;
import octal.models.User;

@Transactional
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
	
	public List<User> findUserByEmail(String email) {
		Query q = entityManager.createQuery("FROM User WHERE email = :email");
		q.setParameter("email", email);
		List<User> result = (List<User>) q.getResultList();
		return result;
	}
	
	public void deleteUser(long id) {
		User user = fetchUser(id);
		if (user != null) {
			entityManager.remove(user);
		}
	}
	
	public Server createServer(Server server) {
		entityManager.persist(server);
		return server;
	}
	
	public void updateServer(Server server) {
		entityManager.merge(server);
	}
	
	public Server fetchServer(long id) {
		return entityManager.find(Server.class, id);
	}
	
	public List<Server> fetchUserServers(long userId) {
		Query q = entityManager.createQuery("FROM Server WHERE user_id = :user_id");
		q.setParameter("user_id", userId);
		List<Server> result = (List<Server>) q.getResultList();
		return result;
	}
	
	public void deleteServer(long id) {
		Server server = fetchServer(id);
		if (server != null) {
			entityManager.remove(server);
		}
	}
}
