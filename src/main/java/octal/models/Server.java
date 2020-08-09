package octal.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Eric Sutphen
 *
 */
@Entity
@Table(name = "oct_servers")
public class Server {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Long server_id;
	private Long user_id;
	private String name;
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;
	private String domain;
	private String ssh_key;
	private String do_region;
	private String do_size;
	private String do_snapshot_id;
	private Integer do_server_id;
	
	public Long getServer_id() {
		return server_id;
	}
	public void setServer_id(Long server_id) {
		this.server_id = server_id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getSsh_key() {
		return ssh_key;
	}
	public void setSsh_key(String ssh_key) {
		this.ssh_key = ssh_key;
	}
	public String getDo_region() {
		return do_region;
	}
	public void setDo_region(String do_region) {
		this.do_region = do_region;
	}
	public String getDo_size() {
		return do_size;
	}
	public void setDo_size(String do_size) {
		this.do_size = do_size;
	}
	public String getDo_snapshot_id() {
		return do_snapshot_id;
	}
	public void setDo_snapshot_id(String do_snapshot_id) {
		this.do_snapshot_id = do_snapshot_id;
	}
	public Integer getDo_server_id() {
		return do_server_id;
	}
	public void setDo_server_id(Integer do_server_id) {
		this.do_server_id = do_server_id;
	}
	
	@Override
	public String toString() {
		return "Server [server_id=" + server_id + ", user_id=" + user_id + ", name=" + name + ", create_date="
				+ create_date + ", update_date=" + update_date + ", domain=" + domain + ", ssh_key=" + ssh_key
				+ ", do_region=" + do_region + ", do_size=" + do_size + ", do_snapshot_id=" + do_snapshot_id
				+ ", do_server_id=" + do_server_id + "]";
	}
}
