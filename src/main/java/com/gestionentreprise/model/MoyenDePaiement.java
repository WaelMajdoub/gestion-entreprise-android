package com.gestionentreprise.model;


public class MoyenDePaiement implements IServerData {
    private Long id;
    private String libelle;
    private Boolean defaut;

    public MoyenDePaiement() {
    }

    public MoyenDePaiement(Long id, String libelle, Boolean defaut) {
	super();
	this.id = id;
	this.libelle = libelle;
	this.defaut = defaut;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getLibelle() {
	return libelle;
    }

    public void setLibelle(String libelle) {
	this.libelle = libelle;
    }

    public Boolean getDefaut() {
	return defaut;
    }

    public void setDefaut(Boolean defaut) {
	this.defaut = defaut;
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
	MoyenDePaiement other = (MoyenDePaiement) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

}
