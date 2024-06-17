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
@Table(name="workflow")
public class WorkFlow {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workflow_seq")
	@SequenceGenerator(name = "workflow_seq",sequenceName = "workflow_seq", allocationSize = 1)
	private Long id;
	
	@Column(name="details")
	private String details;
	
	@Column(name="buyer_comments")
	private String buyerComments;
	
	@Column(name="seller_comments")
	private String sellerComments;
	
	@Column(name="registrar_comments")
	private String registrarComments;
	
	@Column(name="status")
	private String status;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="seller_approver", referencedColumnName = "id")
	private RegistryUser sellerApprover;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="registrar_approver", referencedColumnName = "id")
	private RegistryUser registrarApprover;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="raised_by", referencedColumnName = "id")
	private RegistryUser raisedBy;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="pending_with", referencedColumnName = "id")
	private RegistryUser pendingWith;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="sale_agreement_fk", referencedColumnName = "agreement_id")
	private SaleAgreement saleAgreementFk;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="registry_fk", referencedColumnName = "reg_no")
	private RegistryNagarpalikaRecord registry;
	
	@Column(name="approver_role")
	private String approverRole;
	
	@Column(name="raised_role")
	private String raisedRole;
	
	@Column(name="active_flag")
	private boolean activeFlag;

	@Column(name="created", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Date created;
	
	@Column(name="updated", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Date updated;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getBuyerComments() {
		if(this.buyerComments != null)
			return buyerComments;
		else
			return "No Buyer Comments found.";
	}

	public void setBuyerComments(String buyerComments) {
		this.buyerComments = buyerComments;
	}

	public String getSellerComments() {
		if(this.sellerComments != null)
			return sellerComments;
		else
			return "No Seller Comments found.";
	}

	public void setSellerComments(String sellerComments) {
		this.sellerComments = sellerComments;
	}

	public String getRegistrarComments() {
		if(this.registrarComments != null)
			return registrarComments;
		else
			return "No Registrar Comments found.";
	}

	public void setRegistrarComments(String registrarComments) {
		this.registrarComments = registrarComments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public RegistryUser getRaisedBy() {
		return raisedBy;
	}
	
	public String getRaisedByNameWithAadhar() {
		if(this.raisedBy!=null)
			return this.raisedBy.getName()+" (Aadhar No : "+this.raisedBy.getAadharNumber() + ')';
		else
			return null;
	}
	
	public String getSellerApproverByNameWithAadhar() {
		if(this.sellerApprover!=null)
			return this.sellerApprover.getName()+" (Aadhar No : "+this.sellerApprover.getAadharNumber() + ')';
		else
			return "No Seller assigned.";
	}

	public String getRegistrarApproverByNameWithAadhar() {
		if(this.registrarApprover!=null)
			return this.registrarApprover.getName()+" (Aadhar No : "+this.registrarApprover.getAadharNumber() + ')';
		else
			return "No Registrar assigned.";
	}
	
	public String getPendingWithNameWithAadhar() {
		if(this.pendingWith!=null)
			return this.pendingWith.getName()+" (Aadhar No : "+this.pendingWith.getAadharNumber() + ')';
		else if(this.status.equalsIgnoreCase("APPROVED"))
			return "Workflow has been successfully closed.";
		else if(this.status.equalsIgnoreCase("REJECTED"))
			return "Workflow has been rejected & closed.";
		else
			return null;
	}
	
	public void setRaisedBy(RegistryUser raisedBy) {
		this.raisedBy = raisedBy;
	}

	public String getApproverRole() {
		return approverRole;
	}

	public void setApproverRole(String approverRole) {
		this.approverRole = approverRole;
	}

	public String getRaisedRole() {
		return raisedRole;
	}

	public void setRaisedRole(String raisedRole) {
		this.raisedRole = raisedRole;
	}

	public boolean isActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public RegistryUser getSellerApprover() {
		return sellerApprover;
	}

	public void setSellerApprover(RegistryUser sellerApprover) {
		this.sellerApprover = sellerApprover;
	}

	public RegistryUser getRegistrarApprover() {
		return registrarApprover;
	}

	public void setRegistrarApprover(RegistryUser registrarApprover) {
		this.registrarApprover = registrarApprover;
	}

	public SaleAgreement getSaleAgreementFk() {
		return saleAgreementFk;
	}

	public void setSaleAgreementFk(SaleAgreement saleAgreementFk) {
		this.saleAgreementFk = saleAgreementFk;
	}

	public String getRaisedByName() {
		if(this.raisedBy!=null)
			return this.raisedBy.getName();
		else
			return null;
	}
	
	public String getSellerApproverName() {
		if(this.sellerApprover!=null)
			return this.sellerApprover.getName();
		else
			return null;
	}
	
	public String getRegistrarApproverName() {
		if(this.registrarApprover!=null)
			return this.registrarApprover.getName();
		else
			return null;
	}
	
	public Integer getRaisedByID() {
		if(this.raisedBy!=null)
			return this.raisedBy.getId();
		else
			return null;
	}
	
	public Integer getSellerApproverID() {
		if(this.sellerApprover!=null)
			return this.sellerApprover.getId();
		else
			return null;
	}
	
	public Integer getRegistrarApproverID() {
		if(this.registrarApprover!=null)
			return this.registrarApprover.getId();
		else
			return null;
	}
	
	public Integer getSaleAgreementId() {
		if(this.saleAgreementFk!=null) {
			return this.saleAgreementFk.getAgreementId();
		}
		else
			return null;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	public String getCreatedDate() {
		if(this.created!=null)
		return created.toString().split(" ")[0];
		else
			return null;
	}
	
	public String getUpdatedDate() {
		if(this.updated!=null)
		return updated.toString().split(" ")[0];
		else
			return null;
	}

	public RegistryUser getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(RegistryUser pendingWith) {
		this.pendingWith = pendingWith;
	}

	public RegistryNagarpalikaRecord getRegistry() {
		return registry;
	}

	public void setRegistry(RegistryNagarpalikaRecord registry) {
		this.registry = registry;
	}

	public Integer getRegistrationNo() {
		if(this.registry!=null)
			return this.registry.getRegNumber();
		else
			return null;
	}
}
