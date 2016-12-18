package com.gestionentreprise.gui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.gestionentreprise.model.IServerData;

public class AdapterFactory {
    public static ArrayAdapter<String> buildArrayAdapter(Context context, List<String> liste) {
	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, liste);
	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	return dataAdapter;
    }

    @SuppressWarnings("unchecked")
    public static <T extends IServerData> ServerDataAdapter<?> buildServerDataAdapter(Context context, boolean autoCompleteTextView) {
	ServerDataAdapter<T> serverDataAdapter;
	if (autoCompleteTextView) {
	    serverDataAdapter = (ServerDataAdapter<T>) new ServerDataAdapter<IServerData>(context, android.R.layout.simple_dropdown_item_1line);
	    serverDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
	} else {
	    serverDataAdapter = (ServerDataAdapter<T>) new ServerDataAdapter<IServerData>(context, android.R.layout.simple_spinner_item);
	    serverDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	return serverDataAdapter;
    }

}