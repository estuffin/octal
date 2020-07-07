package octal.db;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Eric Sutphen
 *
 */
@Entity
@Table(name = "oct_users")
public class User {

	@Id
	private Long user_id;
	private String email;
	private int login_count;
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
	
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
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

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", email=" + email + ", login_count=" + login_count + ", create_date="
				+ create_date + ", update_date=" + update_date + ", last_login_date=" + last_login_date
				+ ", curr_login_date=" + curr_login_date + ", curr_ip=" + curr_ip + ", last_ip=" + last_ip
				+ ", do_api_key=" + do_api_key + "]";
	}
}
