package in.kpmg.auro.project.dtos;

public class ApiResponse2<T> {
    private Boolean status;
    private String message;
    private Object result;
    private Integer statusCode;

    public ApiResponse2(Boolean status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

	public ApiResponse2(Boolean status, String message, Object result, Integer statusCode) {
		this.status = status;
		this.message = message;
		this.result = result;
		this.statusCode=statusCode;
	}


    public ApiResponse2(String ticketString) {
        this.statusCode = 500;
        this.status = false;
        this.message = "Error Occured during processing Request$" + ticketString;
    }

    public ApiResponse2(Boolean status, Object result) {
        this.status = status;
        this.result = result;
    }


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Integer getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}


	public Object getResult() {
		return result;
	}


	public void setResult(Object result) {
		this.result = result;
	}


}
