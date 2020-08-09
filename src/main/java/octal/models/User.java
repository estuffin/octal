package octal.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.context.annotation.SessionScope;

import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;

/**
 * @author Eric Sutphen
 *
 */
@Entity
@Table(name = "oct_users")
public class User {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Long user_id;
	private String g_id;
	private String email;
	private String name;
	private Integer login_count;
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_login_date;
	@Temporal(TemporalType.TIMESTAMP)
	private Date curr_login_date;
	private String curr_ip;
	private String last_ip;
	private String do_api_key;
	private String picture;
	@Transient
	private DigitalOcean doClient;
	
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getG_id() {
		return g_id;
	}
	public void setG_id(String g_id) {
		this.g_id = g_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getLogin_count() {
		return login_count;
	}
	public void setLogin_count(int login_count) {
		this.login_count = login_count;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public Date getLast_login_date() {
		return last_login_date;
	}
	public void setLast_login_date(Date last_login_date) {
		this.last_login_date = last_login_date;
	}
	public Date getCurr_login_date() {
		return curr_login_date;
	}
	public void setCurr_login_date(Date curr_login_date) {
		this.curr_login_date = curr_login_date;
	}
	public String getCurr_ip() {
		return curr_ip;
	}
	public void setCurr_ip(String curr_ip) {
		this.curr_ip = curr_ip;
	}
	public String getLast_ip() {
		return last_ip;
	}
	public void setLast_ip(String last_ip) {
		this.last_ip = last_ip;
	}
	public String getDo_api_key() {
		return do_api_key;
	}
	public void setDo_api_key(String do_api_key) {
		this.do_api_key = do_api_key;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DigitalOcean getDoClient() {
		if (doClient == null) {
			if (!do_api_key.isEmpty()) {
				doClient =  new DigitalOceanClient("v2", do_api_key);
            }
		}
		return doClient;
	}
	public void setDoClient(DigitalOcean doClient) {
		this.doClient = doClient;
	}
	
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", g_id=" + g_id + ", email=" + email + ", name=" + name + ", login_count="
				+ login_count + ", create_date=" + create_date + ", update_date=" + update_date + ", last_login_date="
				+ last_login_date + ", curr_login_date=" + curr_login_date + ", curr_ip=" + curr_ip + ", last_ip="
				+ last_ip + ", do_api_key=" + do_api_key + ", picture=" + picture + "]";
	}
	
	public Boolean isEmpty() {
		if (user_id == null 
				&& g_id == null
				&& email == null
				&& name == null
				&& login_count == null
				&& create_date == null
				&& update_date == null
				&& last_login_date == null
				&& curr_login_date == null
				&& curr_ip == null
				&& last_ip == null
				&& do_api_key == null
				&& picture == null)
			return true;
		else 
			return false;
	}
}
