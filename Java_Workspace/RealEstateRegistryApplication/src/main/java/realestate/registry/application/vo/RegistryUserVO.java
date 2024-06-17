package realestate.registry.application.vo;

import java.util.Date;

public interface RegistryUserVO {

	public String getName();
	public Long getAadharNumber();
	public String getPanNumber();
	public Long getContactNumber();
	public char getGender();
	public String getAddress();
	public Date getDateOfBirth();
	public String getNameWithRole();
}
