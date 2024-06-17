package realestate.registry.application.vo;

public interface WorkFlowVO {

	public Long getId();
	public String getDetails();
	public String getBuyerComments();
	public String getSellerComments() ;
	public String getRegistrarComments();
	public String getStatus();
	public String getRaisedRole();
	public String getRaisedByName();
	public String getSellerApproverName();
	public String getRegistrarApproverName();
	public Integer getRaisedByID();
	public Integer getSellerApproverID();
	public Integer getRegistrarApproverID();
	public Integer getSaleAgreementId();
	public String getCreatedDate();
	public String getUpdatedDate();
	public String getRaisedByNameWithAadhar() ;
	public String getSellerApproverByNameWithAadhar();
	public String getRegistrarApproverByNameWithAadhar() ;
	public String getPendingWithNameWithAadhar();
	public Integer getRegistrationNo() ;
}
