package com.gestionentreprise.model;

import java.util.Date;

public class SuiviElement {
    private Date date;
    private Double chiffreAffaireBrut;
    private Double chiffreAffaireHorsRSI;
    private Double chiffreAffaireNet;

    public SuiviElement() {
	super();
    }

    public SuiviElement(Date date, Double chiffreAffaireBrut, Double chiffreAffaireHorsRSI, Double chiffreAffaireNet) {
	super();
	this.date = date;
	this.chiffreAffaireBrut = chiffreAffaireBrut;
	this.chiffreAffaireHorsRSI = chiffreAffaireHorsRSI;
	this.chiffreAffaireNet = chiffreAffaireNet;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Double getChiffreAffaireBrut() {
	return chiffreAffaireBrut;
    }

    public void setChiffreAffaireBrut(Double chiffreAffaireBrut) {
	this.chiffreAffaireBrut = chiffreAffaireBrut;
    }

    public Double getChiffreAffaireHorsRSI() {
	return chiffreAffaireHorsRSI;
    }

    public void setChiffreAffaireHorsRSI(Double chiffreAffaireHorsRSI) {
	this.chiffreAffaireHorsRSI = chiffreAffaireHorsRSI;
    }

    public Double getChiffreAffaireNet() {
	return chiffreAffaireNet;
    }

    public void setChiffreAffaireNet(Double chiffreAffaireNet) {
	this.chiffreAffaireNet = chiffreAffaireNet;
    }
}
