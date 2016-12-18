package com.gestionentreprise.model;

import java.util.HashMap;
import java.util.Map;

public class Client implements IServerData {
    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private String telephone2;
    private String email;
    private Adresse adresse;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getNom() {
	return nom;
    }

    public void setNom(String nom) {
	this.nom = nom;
    }

    public String getPrenom() {
	return prenom;
    }

    public void setPrenom(String prenom) {
	this.prenom = prenom;
    }

    public String getTelephone() {
	return telephone;
    }

    public void setTelephone(String telephone) {
	this.telephone = telephone;
    }

    public String getTelephone2() {
	return telephone2;
    }

    public void setTelephone2(String telephone2) {
	this.telephone2 = telephone2;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Adresse getAdresse() {
	return adresse;
    }

    public void setAdresse(Adresse adresse) {
	this.adresse = adresse;
    }

    public Map<String, String> createMap() {
	Map<String, String> map = new HashMap<String, String>();
	map.put("nom", nom);
	map.put("prenom", prenom);
	map.put("telephone", telephone);
	map.put("adresse", adresse.getLibelle1());
	map.put("codePostal", adresse.getCodePostal());
	map.put("ville", adresse.getVille());
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
	Client other = (Client) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    @Override
    public String getLibelle() {
	StringBuilder sb = new StringBuilder(getNom());
	if (getPrenom() != null) {
	    sb.append(" ");
	    sb.append(getPrenom());
	}
	return sb.toString();
    }
}
