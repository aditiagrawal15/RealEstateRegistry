package realestate.registry.application.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name="registry_user")
public class RegistryUser {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registry_user_seq")
	@SequenceGenerator(name = "registry_user_seq",sequenceName = "registry_user_seq", allocationSize = 1)
	private int id;
	
	@Column(name="name")
	private String name;

	@Column(name="dob")
	private Date dateOfBirth;
	
	@Column(name="aadhar_number")
	private Long aadharNumber;
	
	@Column(name="pan_number")
	private String panNumber;
	
	@Column(name="contact_number")
	private Long contactNumber;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="address")
	private String address;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="logged_in_role")
	private String loggedInRole;
	
	@Column(name="is_logged_in")
	private boolean isLoggedIn;
	
	@Column(name="is_first_login")
	private boolean isFirstLogin;
	
	@Column(name="is_registrar")
	private boolean isRegistrar;
	
	@Column(name="active_flag")
	private boolean activeFlag;
	
	@Column(name="login_time", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	@LastModifiedDate
	private Date loginTime;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public String getNameWithRole() {
		return name + " ( Role : " + this.loggedInRole + " )";
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(Long aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public Long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public boolean isActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getLoginTime() {
		return loginTime.toString();
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public String getLoggedInRole() {
		return loggedInRole;
	}

	public void setLoggedInRole(String loggedInRole) {
		this.loggedInRole = loggedInRole;
	}

	public boolean isRegistrar() {
		return isRegistrar;
	}

	public void setRegistrar(boolean isRegistrar) {
		this.isRegistrar = isRegistrar;
	}
	
}
