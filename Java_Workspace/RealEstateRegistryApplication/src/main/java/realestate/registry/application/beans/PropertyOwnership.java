package realestate.registry.application.beans;

import java.text.NumberFormat;
import java.util.Locale;

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
@Table(name="property_ownership")
public class PropertyOwnership {

	@Id
	@Column(name="property_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_ownership_seq")
	@SequenceGenerator(name = "property_ownership_seq",sequenceName = "property_ownership_seq", allocationSize = 1)
	private int propertyId;
	
	@Column(name="property_type")
	private String propertyType;
	
	@Column(name="property_location")
	private String propertyLocation;
	
	@Column(name="plot_no")
	private String plotNo;
	
	@Column(name="police_station")
	private String policeStation;
	
	@Column(name="district")
	private String district;
	
	@Column(name="property_area")
	private Integer propertyArea;
	
	@Column(name="property_value")
	private Long propertyValue;
	
	@Column(name="property_pincode")
	private int propertyPincode;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="previous_owner_fk", referencedColumnName = "id")
	private RegistryUser previousOwnerFk;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="current_owner_fk", referencedColumnName = "id")
	private RegistryUser currentOwnerFk;

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyLocation() {
		return propertyLocation;
	}

	public void setPropertyLocation(String propertyLocation) {
		this.propertyLocation = propertyLocation;
	}

	public int getPropertyArea() {
		return propertyArea;
	}

	public void setPropertyArea(int propertyArea) {
		this.propertyArea = propertyArea;
	}

	public Long getPropertyValue() {
		return propertyValue;
	}

	public String getPropertyValueWithComma() {
		if(this.propertyValue==null)
			return null;
		return NumberFormat.getNumberInstance(Locale.CHINA).format(this.propertyValue)+"/-";
	}
	
	public void setPropertyValue(Long propertyValue) {
		this.propertyValue = propertyValue;
	}

	public int getPropertyPincode() {
		return propertyPincode;
	}

	public void setPropertyPincode(int propertyPincode) {
		this.propertyPincode = propertyPincode;
	}

	public RegistryUser getPreviousOwnerFk() {
		return previousOwnerFk;
	}

	public void setPreviousOwnerFk(RegistryUser previousOwnerFk) {
		this.previousOwnerFk = previousOwnerFk;
	}

	public RegistryUser getCurrentOwnerFk() {
		return currentOwnerFk;
	}

	public void setCurrentOwnerFk(RegistryUser currentOwnerFk) {
		this.currentOwnerFk = currentOwnerFk;
	}

	public String getPlotNo() {
		return plotNo;
	}

	public void setPlotNo(String plotNo) {
		this.plotNo = plotNo;
	}

	public String getPoliceStation() {
		return policeStation;
	}

	public void setPoliceStation(String policeStation) {
		this.policeStation = policeStation;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public void setPropertyArea(Integer propertyArea) {
		this.propertyArea = propertyArea;
	}
	
}
