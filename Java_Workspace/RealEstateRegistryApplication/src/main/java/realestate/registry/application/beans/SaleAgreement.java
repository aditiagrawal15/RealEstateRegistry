package realestate.registry.application.beans;

import java.util.Date;

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
@Table(name="sale_agreement")
public class SaleAgreement {

	@Id
	@Column(name="agreement_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_agreement_seq")
	@SequenceGenerator(name = "sale_agreement_seq",sequenceName = "sale_agreement_seq", allocationSize = 1)
	private int agreementId;
	
	@Column(name="tentative_date")
	private Date tentativeDate;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="purchaser_user_fk", referencedColumnName = "id")
	private RegistryUser purchaserUserFk;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="seller_user_fk", referencedColumnName = "id")
	private RegistryUser sellerUserFk;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="property_fk", referencedColumnName = "property_id")
	private PropertyOwnership propertyFk;
	
	@Column(name="active_flag")
	private boolean activeFlag;

	public int getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(int agreementId) {
		this.agreementId = agreementId;
	}

	public Date getTentativeDate() {
		return tentativeDate;
	}

	public void setTentativeDate(Date tentativeDate) {
		this.tentativeDate = tentativeDate;
	}

	public PropertyOwnership getPropertyFk() {
		return propertyFk;
	}

	public void setPropertyFk(PropertyOwnership propertyFk) {
		this.propertyFk = propertyFk;
	}

	public RegistryUser getPurchaserUserFk() {
		return purchaserUserFk;
	}

	public void setPurchaserUserFk(RegistryUser purchaserUserFk) {
		this.purchaserUserFk = purchaserUserFk;
	}

	public RegistryUser getSellerUserFk() {
		return sellerUserFk;
	}

	public void setSellerUserFk(RegistryUser sellerUserFk) {
		this.sellerUserFk = sellerUserFk;
	}

	public boolean isActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}


}
