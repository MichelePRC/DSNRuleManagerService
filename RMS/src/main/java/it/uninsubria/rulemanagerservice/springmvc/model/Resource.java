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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idR", nullable=false)
	private Integer idR;
	
	
	@ManyToOne 
	@JoinColumn(name="idu", nullable=false)
	private Integer id_user_own;	
	
	
	@Column(name="nameR", nullable=false)
	private String nameR;
	
	
	@Column(name="sharingDepth", nullable=false)
	private int sharingDepth;	

	
	public Resource (){}

	

	public Integer getIdR() {
		return idR;
	}



	public void setIdR(Integer idR) {
		this.idR = idR;
	}



	public Integer getId_user_own() {
		return id_user_own;
	}



	public void setId_user_own(Integer id_user_own) {
		this.id_user_own = id_user_own;
	}




	public String getNameR() {
		return nameR;
	}

	
	

	public void setNameR(String nameR) {
		this.nameR = nameR;
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
		result = prime * result + ((idR == null) ? 0 : idR.hashCode());
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
		if (idR == null) {
			if (other.idR != null)
				return false;
		} else if (!idR.equals(other.idR))
			return false;
		return true;
	}







		


		
	
}
