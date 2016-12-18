package com.gestionentreprise.bean;

import java.util.List;

import com.gestionentreprise.model.Depense;
import com.gestionentreprise.model.TotalCategorie;
import com.gestionentreprise.model.TypeDepense;

public class ListeDepenseData {
    private List<TotalCategorie> listeTotalTypeDepense;
    private List<TypeDepense> listeTypeDepense;
    private List<Depense> listeDepense;

    public ListeDepenseData() {
	super();
    }

    public ListeDepenseData(List<TotalCategorie> listeTotalTypeDepense, List<TypeDepense> listeTypeDepense, List<Depense> listeDepense) {
	super();
	this.listeTotalTypeDepense = listeTotalTypeDepense;
	this.listeTypeDepense = listeTypeDepense;
	this.listeDepense = listeDepense;
    }

    public List<TotalCategorie> getListeTotalTypeDepense() {
	return listeTotalTypeDepense;
    }

    public void setListeTotalTypeDepense(List<TotalCategorie> listeTotalTypeDepense) {
	this.listeTotalTypeDepense = listeTotalTypeDepense;
    }

    public List<TypeDepense> getListeTypeDepense() {
	return listeTypeDepense;
    }

    public void setListeTypeDepense(List<TypeDepense> listeTypeDepense) {
	this.listeTypeDepense = listeTypeDepense;
    }

    public List<Depense> getListeDepense() {
	return listeDepense;
    }

    public void setListeDepense(List<Depense> listeDepense) {
	this.listeDepense = listeDepense;
    }

}
