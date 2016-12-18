package com.gestionentreprise.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Prestation implements ITotal {
    private Long id;
    private TypePrestation typePrestation;
    private MoyenDePaiement moyenDePaiement;
    private Client client;
    private Date date;
    private Double tarif;
    private Boolean comptabilise;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public TypePrestation getTypePrestation() {
	return typePrestation;
    }

    public void setTypePrestation(TypePrestation typePrestation) {
	this.typePrestation = typePrestation;
    }

    public MoyenDePaiement getMoyenDePaiement() {
	return moyenDePaiement;
    }

    public void setMoyenDePaiement(MoyenDePaiement moyenDePaiement) {
	this.moyenDePaiement = moyenDePaiement;
    }

    public Client getClient() {
	return client;
    }

    public void setClient(Client client) {
	this.client = client;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Double getTarif() {
	return tarif;
    }

    public void setTarif(Double tarif) {
	this.tarif = tarif;
    }

    public Boolean getComptabilise() {
	return comptabilise;
    }

    public void setComptabilise(Boolean comptabilise) {
	this.comptabilise = comptabilise;
    }

    public Map<String, String> createMap() {
	Map<String, String> map = new HashMap<String, String>();
	if (id != null) {
	    map.put("id", id.toString());
	}
	map.put("typePrestation", typePrestation.getId().toString());
	map.put("moyenDePaiement", moyenDePaiement.getId().toString());
	map.put("client", client.getId().toString());
	map.put("date", String.valueOf((date.getTime()) / 1000));
	map.put("tarif", tarif.toString());
	map.put("comptabilise", comptabilise ? "true" : "false");
	return map;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
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
	Prestation other = (Prestation) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    @Override
    public String getCategorie() {
	return (moyenDePaiement != null) ? moyenDePaiement.getLibelle() : null;
    }
}
