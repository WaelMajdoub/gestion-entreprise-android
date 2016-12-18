package com.gestionentreprise.bean;

import java.util.List;

import com.gestionentreprise.model.SuiviElement;

public class SuiviData {
    private List<SuiviElement> listeSuiviElement;

    public SuiviData() {
	super();
    }

    public SuiviData(List<SuiviElement> listeSuiviElement) {
	super();
	this.listeSuiviElement = listeSuiviElement;
    }

    public List<SuiviElement> getListeSuiviElement() {
	return listeSuiviElement;
    }

    public void setListeSuiviElement(List<SuiviElement> listeSuiviElement) {
	this.listeSuiviElement = listeSuiviElement;
    }

}
