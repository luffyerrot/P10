package fr.pierre.batch.entities;

public class RequestAndCopy {
	
	private Long requestId;
	
	private Long userId;
	private String email;
	private String username;
	
	private Long copyId;
	private String etat;
	
	public RequestAndCopy(Long requestId, Long userId, String email, String username, 
			Long copyId, String etat) {
		this.requestId = requestId;
		this.userId = userId;
		this.email = email;
		this.username = username;
		this.copyId = copyId;
		this.etat = etat;
	}
	
	@Override
	public String toString() {
		return "[" + "requestId = " + requestId + ", userId = " + userId + ", email = " + email + ", username = " + username + ", copyId = " + copyId + ", etat = " + etat + "]";
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getCopyId() {
		return copyId;
	}

	public void setCopyId(Long copyId) {
		this.copyId = copyId;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}
}
