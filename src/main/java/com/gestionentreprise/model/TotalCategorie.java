package com.gestionentreprise.model;

public class TotalCategorie {
    private String total;
    private String categorie;

    public TotalCategorie() {
	super();
    }

    public TotalCategorie(String categorie, String total) {
	super();
	this.total = total;
	this.categorie = categorie;
    }

    public String getTotal() {
	return total;
    }

    public void setTotal(String total) {
	this.total = total;
    }

    public String getCategorie() {
	return categorie;
    }
}
