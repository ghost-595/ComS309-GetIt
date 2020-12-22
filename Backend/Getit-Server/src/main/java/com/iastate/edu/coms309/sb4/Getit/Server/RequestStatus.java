package com.iastate.edu.coms309.sb4.Getit.Server;



/**
 * @author Maxwell Smith
 *
 */
public class RequestStatus {

	/**
	 * Request Status is used to have a universal JSON response to request to the server. It returns a success status and the object
	 *
	 */
	
	private boolean success;
	private Object result;

	public RequestStatus(Boolean success, Object result) {
		this.setSuccess(success);
		this.setResult(result);
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
