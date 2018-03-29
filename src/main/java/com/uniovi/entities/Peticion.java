package com.uniovi.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Peticion {
	
	@Id @GeneratedValue public long id;
	
	@ManyToOne
	@JoinColumn(name = "user_envia")
	private User userEnvia;
	
	@ManyToOne
	@JoinColumn(name = "user_recibe")
	private User userRecibe;
	
	
	public Peticion(User envia,User recibe) {
		super();
		this.userEnvia=envia;
		this.userRecibe=recibe;
	}

	public Peticion() {	
	}	

	public User getUserEnvia() {
		return userEnvia;
	}

	public void setUserEnvia(User userEnvia) {
		this.userEnvia = userEnvia;
	}

	public User getUserRecibe() {
		return userRecibe;
	}

	public void setUserRecibe(User userRecibe) {
		this.userRecibe = userRecibe;
	}	

}

