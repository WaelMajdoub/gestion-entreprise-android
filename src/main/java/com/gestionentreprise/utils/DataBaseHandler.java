package com.gestionentreprise.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

    private String createStatement;
    private String dropStatement;

    public DataBaseHandler(Context context, String name, CursorFactory factory, int version, String createStatement, String dropStatement) {
	super(context, name, factory, version);
	this.createStatement = createStatement;
	this.dropStatement = dropStatement;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	db.execSQL(createStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL(dropStatement);
	onCreate(db);
    }

}
