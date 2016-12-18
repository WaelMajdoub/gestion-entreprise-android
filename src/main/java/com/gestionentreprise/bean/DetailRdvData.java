package com.gestionentreprise.bean;

import java.util.List;

import com.gestionentreprise.model.Client;
import com.gestionentreprise.model.TypePrestation;

public class DetailRdvData {
    private List<TypePrestation> listeTypePrestation;
    private List<Client> listeClient;

    public DetailRdvData() {
	super();
    }

    public DetailRdvData(List<TypePrestation> listeTypePrestation, List<Client> listeClient) {
	super();
	this.listeTypePrestation = listeTypePrestation;
	this.listeClient = listeClient;
    }

    public List<TypePrestation> getListeTypePrestation() {
	return listeTypePrestation;
    }

    public void setListeTypePrestation(List<TypePrestation> listeTypePrestation) {
	this.listeTypePrestation = listeTypePrestation;
    }

    public List<Client> getListeClient() {
	return listeClient;
    }

    public void setListeClient(List<Client> listeClient) {
	this.listeClient = listeClient;
    }

}
