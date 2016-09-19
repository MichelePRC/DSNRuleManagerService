package it.uninsubria.rulemanagerservice.springmvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.uninsubria.rulemanagerservice.springmvc.model.User;

@Entity
@Table(name="UPLOADREQUEST")
public class UploadRequest {

	
	@Id
	@Column(name="id", nullable=true, unique=true)
	private Integer token;
	
	
	@Column(name="nonce", nullable=true)
	private Integer nonce;
	
	
	@ManyToOne 	
	@JoinColumn(name ="idu", nullable=false)
	private User utente;
	
		
	public UploadRequest(){}


	public Integer getToken() {
		return token;
	}


	public void setToken(Integer token) {
		this.token = token;
	}


	public Integer getNonce() {
		return nonce;
	}


	public void setNonce(Integer nonce) {
		this.nonce = nonce;
	}


	public User getUtente() {
		return utente;
	}


	public void setUtente(User utente) {
		this.utente = utente;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UploadRequest other = (UploadRequest) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "UploadRequest [token=" + token + ", nonce=" + nonce + ", utente=" + utente + "]";
	};
	
	
	

	
	
}
