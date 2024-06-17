package realestate.registry.application.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("rawtypes")
public class RERResponseEntity extends ResponseEntity{

	@SuppressWarnings("unchecked")
	public <T> RERResponseEntity(T body, HttpStatus status) {
		super(body, status);
	}
}
