package realestate.registry.application.exception;

public class RERException{

	private String errorMessage;
	private String recoveryMessage;
	private String type;
	
	public RERException(String errorMessage, String recoveryMessage, String type) {
		super();
		this.errorMessage = errorMessage;
		this.recoveryMessage = recoveryMessage;
		this.type = type;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getRecoveryMessage() {
		return recoveryMessage;
	}

	public void setRecoveryMessage(String recoveryMessage) {
		this.recoveryMessage = recoveryMessage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
