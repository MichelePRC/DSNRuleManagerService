 package it.uninsubria.rulemanagerservice.springmvc.model;


import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="USER")
public class User {
		
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idu", unique=true, nullable=false)
	private Integer idu;
	
		
	@NotEmpty
	@Column(name="modulus", nullable=false)
	private String modulus;
	
	@NotEmpty
	@Column(name="exponent", nullable=false)
	private String exponent;
		
	@NotEmpty
	@Column(name="secret", nullable=false)
	private String secret;
	
	public User (){}

	public Integer getIdu() {
		return idu;
	}

	public void setIdu(Integer idu) {
		this.idu = idu;
	}

	public String getModulus() {
		return modulus;
	}

	public void setModulus(String modulus) {
		this.modulus = modulus;
	}

	public String getExponent() {
		return exponent;
	}

	public void setExponent(String exponent) {
		this.exponent = exponent;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idu == null) ? 0 : idu.hashCode());
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
		User other = (User) obj;
		if (idu == null) {
			if (other.idu != null)
				return false;
		} else if (!idu.equals(other.idu))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [idu=" + idu + ", modulus=" + modulus + ", exponent=" + exponent + ", secret=" + secret + "]";
	};

	
	
	

}
