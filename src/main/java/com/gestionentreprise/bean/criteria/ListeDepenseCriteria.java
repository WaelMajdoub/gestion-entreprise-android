package com.gestionentreprise.bean.criteria;

import java.util.Date;

public class ListeDepenseCriteria {
    private Date depuisLe;
    private Date jusqua;

    public Date getDepuisLe() {
	return depuisLe;
    }

    public void setDepuisLe(Date depuisLe) {
	this.depuisLe = depuisLe;
    }

    public Date getJusqua() {
	return jusqua;
    }

    public void setJusqua(Date jusqua) {
	this.jusqua = jusqua;
    }

}
