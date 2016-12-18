package com.gestionentreprise.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Depense implements ITotal {
    private Long id;
    private TypeDepense typeDepense;
    private Date date;
    private Double tarif;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public TypeDepense getTypeDepense() {
	return typeDepense;
    }

    public void setTypeDepense(TypeDepense typeDepense) {
	this.typeDepense = typeDepense;
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

    public Map<String, String> createMap() {
	Map<String, String> map = new HashMap<String, String>();
	if (id != null) {
	    map.put("id", id.toString());
	}
	map.put("typeDepense", typeDepense.getId().toString());
	map.put("date", String.valueOf((date.getTime()) / 1000));
	map.put("tarif", tarif.toString());
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
	Depense other = (Depense) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    @Override
    public String getCategorie() {
	return (typeDepense != null) ? typeDepense.getLibelle() : null;
    }

}
