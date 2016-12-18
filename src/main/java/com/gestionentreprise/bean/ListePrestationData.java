package com.gestionentreprise.bean;

import java.util.List;

import com.gestionentreprise.model.MoyenDePaiement;
import com.gestionentreprise.model.Prestation;
import com.gestionentreprise.model.TotalCategorie;

public class ListePrestationData {
    private List<TotalCategorie> listeTotalMoyenDePaiement;
    private List<MoyenDePaiement> listeMoyenDePaiement;
    private List<Prestation> listePrestation;

    public ListePrestationData() {
	super();
    }

    public ListePrestationData(List<TotalCategorie> listeTotalMoyenDePaiement, List<MoyenDePaiement> listeMoyenDePaiement, List<Prestation> listePrestation) {
	super();
	this.listeTotalMoyenDePaiement = listeTotalMoyenDePaiement;
	this.listeMoyenDePaiement = listeMoyenDePaiement;
	this.listePrestation = listePrestation;
    }

    public List<TotalCategorie> getListeTotalMoyenDePaiement() {
	return listeTotalMoyenDePaiement;
    }

    public void setListeTotalMoyenDePaiement(List<TotalCategorie> listeTotalMoyenDePaiement) {
	this.listeTotalMoyenDePaiement = listeTotalMoyenDePaiement;
    }

    public List<MoyenDePaiement> getListeMoyenDePaiement() {
	return listeMoyenDePaiement;
    }

    public void setListeMoyenDePaiement(List<MoyenDePaiement> listeMoyenDePaiement) {
	this.listeMoyenDePaiement = listeMoyenDePaiement;
    }

    public List<Prestation> getListePrestation() {
	return listePrestation;
    }

    public void setListePrestation(List<Prestation> listePrestation) {
	this.listePrestation = listePrestation;
    }

}
