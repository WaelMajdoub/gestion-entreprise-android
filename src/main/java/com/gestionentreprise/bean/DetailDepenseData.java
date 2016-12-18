package com.gestionentreprise.bean;

import java.util.List;

import com.gestionentreprise.model.TypeDepense;

public class DetailDepenseData {
    private List<TypeDepense> listeTypeDepense;

    public DetailDepenseData() {
	super();
    }

    public DetailDepenseData(List<TypeDepense> listeTypeDepense) {
	super();
	this.listeTypeDepense = listeTypeDepense;
    }

    public List<TypeDepense> getListeTypeDepense() {
	return listeTypeDepense;
    }

    public void setListeTypeDepense(List<TypeDepense> listeTypeDepense) {
	this.listeTypeDepense = listeTypeDepense;
    }

}
