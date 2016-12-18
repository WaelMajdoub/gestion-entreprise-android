package com.gestionentreprise.bean;

import java.util.List;

import com.gestionentreprise.model.Client;
import com.gestionentreprise.model.MoyenDePaiement;
import com.gestionentreprise.model.TypePrestation;

public class DetailPrestationData {
    private List<TypePrestation> listeTypePrestation;
    private List<MoyenDePaiement> listeMoyenDePaiement;
    private List<Client> listeClient;

    public DetailPrestationData() {
	super();
    }

    public DetailPrestationData(List<TypePrestation> listeTypePrestation,
	    List<MoyenDePaiement> listeMoyenDePaiement,
	    List<Client> listeClient) {
	super();
	this.listeTypePrestation = listeTypePrestation;
	this.listeMoyenDePaiement = listeMoyenDePaiement;
	this.listeClient = listeClient;
    }

    public List<TypePrestation> getListeTypePrestation() {
	return listeTypePrestation;
    }

    public void setListeTypePrestation(List<TypePrestation> listeTypePrestation) {
	this.listeTypePrestation = listeTypePrestation;
    }

    public List<MoyenDePaiement> getListeMoyenDePaiement() {
	return listeMoyenDePaiement;
    }

    public void setListeMoyenDePaiement(List<MoyenDePaiement> listeMoyenDePaiement) {
	this.listeMoyenDePaiement = listeMoyenDePaiement;
    }

    public List<Client> getListeClient() {
	return listeClient;
    }

    public void setListeClient(List<Client> listeClient) {
	this.listeClient = listeClient;
    }

}
