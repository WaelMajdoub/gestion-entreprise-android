package com.gestionentreprise.service.message;

public class Msg implements IMessage {
    private int code;
    private String libelle;

    public Msg(int code, String libelle) {
	this.code = code;
	this.libelle = libelle;
    }

    public int getCode() {
	return code;
    }

    public String getLibelle() {
	return libelle;
    }
}
