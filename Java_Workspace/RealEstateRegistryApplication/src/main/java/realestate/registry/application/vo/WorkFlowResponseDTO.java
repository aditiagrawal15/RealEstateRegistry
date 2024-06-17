package realestate.registry.application.vo;

public class WorkFlowResponseDTO {

	private Long workFlowId;
	private Integer userId;
	private String userRole;
	private String response;
	private String buyerComment;
	private String sellerComment;
	private String registrarComment;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	public String getBuyerComment() {
		return buyerComment;
	}
	public void setBuyerComment(String buyerComment) {
		this.buyerComment = buyerComment;
	}
	public String getSellerComment() {
		return sellerComment;
	}
	public void setSellerComment(String sellerComment) {
		this.sellerComment = sellerComment;
	}
	public String getRegistrarComment() {
		return registrarComment;
	}
	public void setRegistrarComment(String registrarComment) {
		this.registrarComment = registrarComment;
	}
	public Long getWorkFlowId() {
		return workFlowId;
	}
	public void setWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
	}
	
}
