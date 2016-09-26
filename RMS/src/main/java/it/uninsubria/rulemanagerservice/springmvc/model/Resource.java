 package it.uninsubria.rulemanagerservice.springmvc.model;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="RESOURCE")
public class Resource {
	
	@Id 
	@Column(name="idR", nullable=false, unique=true)
	private int idR;
	
	
	@ManyToOne 
	@JoinColumn(name="idu", nullable=false)
	private User owner;	
		
	
	
	@Column(name="sharingDepth", nullable=true)
	private int sharingDepth;	

	
	public Resource (){}

	

	public int getIdR() {
		return idR;
	}



	public void setIdR(int idR) {
		this.idR = idR;
	}


	



	public User getOwner() {
		return owner;
	}



	public void setOwner(User owner) {
		this.owner = owner;
	}



	public int getSharingDepth() {
		return sharingDepth;
	}



	public void setSharingDepth(int sharingDepth) {
		this.sharingDepth = sharingDepth;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idR;
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
		Resource other = (Resource) obj;
		if (idR != other.idR)
			return false;
		return true;
	}



	







		


		
	
}
