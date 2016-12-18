package com.gestionentreprise.gui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.gestionentreprise.model.IServerData;

public class ServerDataAdapter<T extends IServerData> extends ArrayAdapter<String> {
    private List<T> listeElement;

    public ServerDataAdapter(
	    Context context, int textResourceId) {
	super(context, textResourceId);
    }

    public void setData(List<T> listeElement) {
	this.listeElement = listeElement;
	registerToArrayAdapter(listeElement);
    }

    private void registerToArrayAdapter(List<T> listeElement) {
	for (T element : listeElement) {
	    add(element.getLibelle());
	}
    }

    public int getPosition(T element) {
	return getPosition(element.getLibelle());
    }

    public T getData(int position) {
	String libelle = getItem(position);
	T out = null;
	for (T element : listeElement) {
	    if (element.getLibelle().equals(libelle)) {
		out = element;
		break;
	    }
	}
	return out;
    }
}
