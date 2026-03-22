package com.sarangsvkm.portfolio_api.apiuser;


public class ApiUserResponse {
	
    private ApiUser apiUser;
	
    private String errorMessage;

	public ApiUser getApiUser() {
		return apiUser;
	}

	public void setApiUser(ApiUser apiUser) {
		this.apiUser = apiUser;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ApiUserResponse(ApiUser apiUser, String errorMessage) {
		
		this.apiUser = apiUser;
		this.errorMessage = errorMessage;
	}
    
    

}

