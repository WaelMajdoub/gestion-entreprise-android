package com.gestionentreprise.bean;

import java.util.List;

import com.gestionentreprise.bean.criteria.ListeDepenseCriteria;
import com.gestionentreprise.bean.criteria.ListePrestationCriteria;
import com.gestionentreprise.model.Client;
import com.gestionentreprise.model.Depense;
import com.gestionentreprise.model.MoyenDePaiement;
import com.gestionentreprise.model.Prestation;
import com.gestionentreprise.model.TotalCategorie;
import com.gestionentreprise.model.TypeDepense;
import com.gestionentreprise.model.TypePrestation;

public class GestionEntrepriseContext {
    private List<TotalCategorie> listeTotalMoyenDePaiement;
    private List<TotalCategorie> listeTotalTypeDepense;
    private List<MoyenDePaiement> listeMoyenDePaiement;
    private List<Prestation> listePrestation;
    private List<Depense> listeDepense;
    private List<TypePrestation> listeTypePrestation;
    private List<TypeDepense> listeTypeDepense;
    private ListePrestationCriteria listePrestationCriteria;
    private ListeDepenseCriteria listeDepenseCriteria;
    private List<Client> listeClient;

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

    public List<TypePrestation> getListeTypePrestation() {
	return listeTypePrestation;
    }

    public void setListeTypePrestation(List<TypePrestation> listeTypePrestation) {
	this.listeTypePrestation = listeTypePrestation;
    }

    public List<TypeDepense> getListeTypeDepense() {
	return listeTypeDepense;
    }

    public void setListeTypeDepense(List<TypeDepense> listeTypeDepense) {
	this.listeTypeDepense = listeTypeDepense;
    }

    public ListePrestationCriteria getListePrestationCriteria() {
	return listePrestationCriteria;
    }

    public void setListePrestationCriteria(ListePrestationCriteria listePrestationCriteria) {
	this.listePrestationCriteria = listePrestationCriteria;
    }

    public List<Client> getListeClient() {
	return listeClient;
    }

    public void setListeClient(List<Client> listeClient) {
	this.listeClient = listeClient;
    }

    public List<TotalCategorie> getListeTotalTypeDepense() {
	return listeTotalTypeDepense;
    }

    public void setListeTotalTypeDepense(List<TotalCategorie> listeTotalTypeDepense) {
	this.listeTotalTypeDepense = listeTotalTypeDepense;
    }

    public List<Depense> getListeDepense() {
	return listeDepense;
    }

    public void setListeDepense(List<Depense> listeDepense) {
	this.listeDepense = listeDepense;
    }

    public ListeDepenseCriteria getListeDepenseCriteria() {
	return listeDepenseCriteria;
    }

    public void setListeDepenseCriteria(ListeDepenseCriteria listeDepenseCriteria) {
	this.listeDepenseCriteria = listeDepenseCriteria;
    }
}
