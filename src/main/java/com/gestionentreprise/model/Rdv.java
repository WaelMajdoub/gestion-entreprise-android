package com.gestionentreprise.model;

import java.util.Date;

public class Rdv {
    private Long id;
    private Client client;
    private TypePrestation typePrestation;
    private Date dateDebut;
    private Integer duree; // en minutes
    private String description;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Date getDateDebut() {
	return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
	this.dateDebut = dateDebut;
    }

    public Integer getDuree() {
	return duree;
    }

    public void setDuree(Integer duree) {
	this.duree = duree;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Client getClient() {
	return client;
    }

    public void setClient(Client client) {
	this.client = client;
    }

    public TypePrestation getTypePrestation() {
	return typePrestation;
    }

    public void setTypePrestation(TypePrestation typePrestation) {
	this.typePrestation = typePrestation;
    }
}
