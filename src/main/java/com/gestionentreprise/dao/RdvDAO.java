package com.gestionentreprise.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gestionentreprise.model.Client;
import com.gestionentreprise.model.Rdv;
import com.gestionentreprise.model.TypePrestation;

public class RdvDAO extends DAOBase {
    public static final String RDV_ID = "ID";
    public static final String RDV_CLIENT_ID = "ID_CLIENT";
    public static final String RDV_CLIENT_NOM = "NOM_CLIENT";
    public static final String RDV_CLIENT_PRENOM = "PRENOM_CLIENT";
    public static final String RDV_TYPE_PRESTATION_ID = "ID_TYPE_PRESTATION";
    public static final String RDV_TYPE_PRESTATION_LIBELLE = "ID_TYPE_PRESTATION_LIBELLE";
    public static final String RDV_DATEDEBUT = "DATE_DEBUT";
    public static final String RDV_DUREE = "DUREE";
    public static final String RDV_DESCRIPTION = "DESCRIPTION";
    public static final String RDV_TABLE_NAME = "RDV";
    public static final String RDV_TABLE_CREATE = "CREATE TABLE " + RDV_TABLE_NAME + " (" + RDV_ID
	    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + RDV_CLIENT_ID + " INTEGER, " + RDV_CLIENT_NOM + " TEXT, "
	    + RDV_CLIENT_PRENOM + " TEXT, " + RDV_TYPE_PRESTATION_ID + " INTEGER, " + RDV_TYPE_PRESTATION_LIBELLE
	    + " TEXT, " + RDV_DATEDEBUT + " INTEGER, " + RDV_DUREE + " INTEGER, " + RDV_DESCRIPTION + " TEXT);";

    public static final String RDV_TABLE_DROP = "DROP TABLE IF EXISTS " + RDV_TABLE_NAME + ";";

    public RdvDAO(Context context) {
	super(context, RDV_TABLE_CREATE, RDV_TABLE_DROP);
    }

    public long add(Rdv rdv) {
	open();
	ContentValues value = new ContentValues();
	value.put(RDV_CLIENT_ID, rdv.getClient().getId());
	value.put(RDV_CLIENT_NOM, rdv.getClient().getNom());
	value.put(RDV_CLIENT_PRENOM, rdv.getClient().getPrenom());
	value.put(RDV_TYPE_PRESTATION_ID, rdv.getTypePrestation().getId());
	value.put(RDV_TYPE_PRESTATION_LIBELLE, rdv.getTypePrestation().getLibelle());
	value.put(RDV_DATEDEBUT, rdv.getDateDebut().getTime());
	value.put(RDV_DUREE, rdv.getDuree());
	value.put(RDV_DESCRIPTION, rdv.getDescription());
	long id = database.insert(RDV_TABLE_NAME, null, value);
	close();
	return id;
    }

    public void delete(Long id) {
	open();
	database.delete(RDV_TABLE_NAME, RDV_ID + " = ?", new String[] { String.valueOf(id) });
	close();
    }

    public void update(Rdv rdv) {
	open();
	ContentValues value = new ContentValues();
	value.put(RDV_CLIENT_ID, rdv.getClient().getId());
	value.put(RDV_CLIENT_NOM, rdv.getClient().getNom());
	value.put(RDV_CLIENT_PRENOM, rdv.getClient().getPrenom());
	value.put(RDV_TYPE_PRESTATION_ID, rdv.getTypePrestation().getId());
	value.put(RDV_TYPE_PRESTATION_LIBELLE, rdv.getTypePrestation().getLibelle());
	value.put(RDV_DATEDEBUT, rdv.getDateDebut().getTime());
	value.put(RDV_DUREE, rdv.getDuree());
	value.put(RDV_DESCRIPTION, rdv.getDescription());
	database.update(RDV_TABLE_NAME, value, RDV_ID + " =?", new String[] { String.valueOf(rdv.getId()) });
	close();
    }

    public Rdv get(Long id) {
	open();
	Rdv rdv = null;
	Cursor cursor = database.rawQuery("select " + RDV_ID + ", " + RDV_CLIENT_ID + ", " + RDV_CLIENT_NOM + ", "
	        + RDV_CLIENT_PRENOM + ", " + RDV_TYPE_PRESTATION_ID + ", " + RDV_TYPE_PRESTATION_LIBELLE + ", "
	        + RDV_DATEDEBUT + ", " + RDV_DUREE + ", " + RDV_DESCRIPTION + " from " + RDV_TABLE_NAME + " where "
	        + RDV_ID + " = ?", new String[] { id.toString() });
	while (cursor.moveToNext()) {
	    rdv = buildRdvFromCursor(cursor);
	}
	cursor.close();

	close();
	return rdv;
    }

    private Rdv buildRdvFromCursor(Cursor cursor) {
	open();
	Rdv rdv;
	rdv = new Rdv();
	rdv.setId(cursor.getLong(0));
	Client client = new Client();
	client.setId(cursor.getLong(1));
	client.setNom(cursor.getString(2));
	client.setPrenom(cursor.getString(3));
	rdv.setClient(client);

	TypePrestation typePrestation = new TypePrestation();
	typePrestation.setId(cursor.getLong(4));
	typePrestation.setLibelle(cursor.getString(5));
	rdv.setTypePrestation(typePrestation);
	rdv.setDateDebut(new Date(cursor.getLong(6)));
	rdv.setDuree(cursor.getInt(7));
	rdv.setDescription(cursor.getString(8));
	close();
	return rdv;
    }

    public List<Rdv> getAll() {
	open();
	List<Rdv> listeRdv = new ArrayList<Rdv>();
	Cursor cursor = database.rawQuery("select " + RDV_ID + ", " + RDV_CLIENT_ID + ", " + RDV_CLIENT_NOM + ", "
	        + RDV_CLIENT_PRENOM + ", " + RDV_TYPE_PRESTATION_ID + ", " + RDV_TYPE_PRESTATION_LIBELLE + ", "
	        + RDV_DATEDEBUT + ", " + RDV_DUREE + ", " + RDV_DESCRIPTION + " from " + RDV_TABLE_NAME + " order by "
	        + RDV_DATEDEBUT + " asc", null);
	while (cursor.moveToNext()) {
	    listeRdv.add(buildRdvFromCursor(cursor));
	}
	close();
	return listeRdv;
    }

    public List<Rdv> getAllByPeriode(Date dateDebut, Date dateFin) {
	open();
	Calendar cal = Calendar.getInstance();
	cal.setTime(dateDebut);
	cal.set(Calendar.AM_PM, Calendar.AM);
	cal.set(Calendar.HOUR, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	dateDebut = cal.getTime();
	cal.setTime(dateFin);
	cal.set(Calendar.AM_PM, Calendar.AM);
	cal.set(Calendar.HOUR, 23);
	cal.set(Calendar.MINUTE, 59);
	cal.set(Calendar.SECOND, 59);
	dateFin = cal.getTime();

	List<Rdv> listeRdv = new ArrayList<Rdv>();
	Cursor cursor = database.rawQuery("select " + RDV_ID + ", " + RDV_CLIENT_ID + ", " + RDV_CLIENT_NOM + ", "
	        + RDV_CLIENT_PRENOM + ", " + RDV_TYPE_PRESTATION_ID + ", " + RDV_TYPE_PRESTATION_LIBELLE + ", "
	        + RDV_DATEDEBUT + ", " + RDV_DUREE + ", " + RDV_DESCRIPTION + " from " + RDV_TABLE_NAME + " where "
	        + RDV_DATEDEBUT + " between " + dateDebut.getTime() + " and " + dateFin.getTime() + " order by "
	        + RDV_DATEDEBUT + " asc", null);
	while (cursor.moveToNext()) {
	    listeRdv.add(buildRdvFromCursor(cursor));
	}
	close();
	return listeRdv;
    }
}
