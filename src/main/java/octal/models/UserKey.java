package octal.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "oct_user_keys")
public class UserKey {
	
	@Id
	private Integer key_id;
	private String name;
	private String fingerprint;
	@Column(length = 500)
	private String public_key;
	private Long user_id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;
	private Character isInternal;
	
	public Integer getKey_id() {
		return key_id;
	}
	public void setKey_id(Integer key_id) {
		this.key_id = key_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFingerprint() {
		return fingerprint;
	}
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
	public String getPublic_key() {
		return public_key;
	}
	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
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
	public Character getIsInternal() {
		return isInternal;
	}
	public void setIsInternal(Character isInternal) {
		this.isInternal = isInternal;
	}
	@Override
	public String toString() {
		return "UserKey [key_id=" + key_id + ", name=" + name + ", fingerprint=" + fingerprint + ", public_key="
				+ public_key + ", user_id=" + user_id + ", create_date=" + create_date + ", update_date=" + update_date
				+ ", isInternal=" + isInternal + "]";
	}
}
