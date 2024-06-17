package realestate.registry.application.beans;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="registry_nagarpalika")
public class RegistryNagarpalikaRecord {

	@Id
	@Column(name="reg_no")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registry_user_seq")
	@SequenceGenerator(name = "registry_user_seq",sequenceName = "registry_user_seq", allocationSize = 1)
	private int regNumber;
	
	@Column(name="buy_type")
	private String buyType;
	
	@Column(name="stamp_value")
	private Long stampValue;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="owner_fk", referencedColumnName = "id")
	private RegistryUser ownerFk;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="property_fk", referencedColumnName = "property_id")
	private PropertyOwnership propertyFk;
	
	@Column(name="active_flag")
	private boolean activeFlag;

	public int getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(int regNumber) {
		this.regNumber = regNumber;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public Long getStampValue() {
		return stampValue;
	}

	public void setStampValue(Long stampValue) {
		this.stampValue = stampValue;
	}

	public RegistryUser getOwnerFk() {
		return ownerFk;
	}

	public void setOwnerFk(RegistryUser ownerFk) {
		this.ownerFk = ownerFk;
	}

	public boolean isActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public PropertyOwnership getPropertyFk() {
		return propertyFk;
	}

	public void setPropertyFk(PropertyOwnership propertyFk) {
		this.propertyFk = propertyFk;
	}
	
	
}
