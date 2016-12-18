package com.gestionentreprise.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gestionentreprise.utils.DataBaseHandler;

public abstract class DAOBase {
    protected final static int VERSION = 3;
    // Le nom du fichier qui représente ma base
    protected final static String NOM = "database.db";
    protected SQLiteDatabase database = null;
    protected DataBaseHandler databaseHandler = null;

    public DAOBase(Context context, String createStatement, String dropStatement) {
	this.databaseHandler = new DataBaseHandler(context, NOM, null, VERSION, createStatement, dropStatement);
    }

    public SQLiteDatabase open() {
	// Pas besoin de fermer la dernière base puisque
	// getWritableDatabase s'en charge
	database = databaseHandler.getWritableDatabase();
	return database;
    }

    public void close() {
	database.close();
    }

    public SQLiteDatabase getDb() {
	return database;
    }
}
