package com.gestionentreprise.model;


public class Adresse {

    private Long id;
    private String libelle1;
    private String libelle2;
    private String codePostal;
    private String ville;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getLibelle1() {
	return libelle1;
    }

    public void setLibelle1(String libelle1) {
	this.libelle1 = libelle1;
    }

    public String getLibelle2() {
	return libelle2;
    }

    public void setLibelle2(String libelle2) {
	this.libelle2 = libelle2;
    }

    public String getCodePostal() {
	return codePostal;
    }

    public void setCodePostal(String codePostal) {
	this.codePostal = codePostal;
    }

    public String getVille() {
	return ville;
    }

    public void setVille(String ville) {
	this.ville = ville;
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
	Adresse other = (Adresse) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }
}
