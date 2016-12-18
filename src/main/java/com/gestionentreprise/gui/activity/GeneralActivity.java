package com.gestionentreprise.gui.activity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.gestionentreprise.bean.GestionEntrepriseContext;
import com.gestionentreprise.model.Depense;
import com.gestionentreprise.model.ITotal;
import com.gestionentreprise.model.Prestation;
import com.gestionentreprise.model.TotalCategorie;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.utils.Action;
import com.gestionentreprise.utils.MessageUtils;

public abstract class GeneralActivity<T> extends Activity {
    private static GestionEntrepriseContext context;
    private static ProgressDialog connectionLoading;
    protected static SimpleDateFormat sdf;
    protected static SimpleDateFormat sdfMonth;
    protected static SimpleDateFormat sdfTime;
    public static DecimalFormat df;

    protected Handler dataLoader;
    private Action action;

    public GeneralActivity() {
	sdf = new SimpleDateFormat("dd/MM/yyyy");
	sdfMonth = new SimpleDateFormat("MM");
	sdfTime = new SimpleDateFormat("HH:mm");

	DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
	otherSymbols.setDecimalSeparator('.');
	df = new DecimalFormat("#####0.00", otherSymbols);
	df.setGroupingUsed(false);
    }

    public static GestionEntrepriseContext initContext() {
	context = new GestionEntrepriseContext();
	return context;
    }

    public static GestionEntrepriseContext getContext() {
	return context;
    }

    protected Action getAction() {
	return action;
    }

    /**
     * This method save the data retrieved from server
     * 
     * @param data
     */
    public abstract void updateContext(T data);

    /**
     * This method save the criteria of the screen
     * 
     * @throws ParseException
     */
    public abstract void saveContext();

    public abstract void setData(T data);

    public void startServerCommunication(Action action) {
	this.action = action;
	if (connectionLoading == null) {
	    connectionLoading = new ProgressDialog(this);
	    connectionLoading.setCancelable(true);
	}
	connectionLoading = ProgressDialog.show(this, "Attente serveur", "Connection serveur");
	connection(dataLoader);
    }

    public void setConnectionLoadingMessage(String message) {
		if (connectionLoading != null)
    		connectionLoading.setMessage(message);
		else 
			Toast.makeText(this, "Boite de dialogue introuvable", Toast.LENGTH_SHORT).show();
    }

    public void dismissConnectionLoadingDialog() {
    	if (connectionLoading != null)
    		connectionLoading.dismiss();
    	else 
			Toast.makeText(this, "Boite de dialogue introuvable", Toast.LENGTH_SHORT).show();
    }

    public abstract void serverCommunicationManagement(final Handler receiver) throws Exception;

    private void connection(final Handler receiver) {
	new Thread() {
	    public void run() {
		try {
		    serverCommunicationManagement(receiver);
		} catch (Exception e) {
		    Message msg = Message.obtain();
		    msg.arg1 = IMessage.CONNECTION_EXCEPTION;
		    msg.obj = e;
		    MessageUtils.logException(e, "Connection exception");
		    receiver.sendMessage(msg);
		}
	    }
	}.start();

    }

    protected void refreshPrestationIntoContext(Prestation prestationToRemove, Prestation prestationToSave) {
	refreshTotalCategorieIntoContext(prestationToRemove, prestationToSave, context.getListeTotalMoyenDePaiement());
	refreshElementIntoList(context.getListePrestation(), prestationToRemove, prestationToSave);
    }

    protected void refreshDepenseIntoContext(Depense depenseToRemove, Depense depenseToSave) {
	refreshTotalCategorieIntoContext(depenseToRemove, depenseToSave, context.getListeTotalTypeDepense());
	refreshElementIntoList(context.getListeDepense(), depenseToRemove, depenseToSave);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void refreshElementIntoList(List list, ITotal elementToRemove, ITotal elementToSave) {
	int position = 0;
	position = list.indexOf(elementToRemove);
	if (elementToRemove != null) {
	    list.remove(elementToRemove);
	}
	if (elementToSave != null) {
	    if (position != -1) {
		list.add(position, elementToSave);
	    } else {
		list.add(elementToSave);
	    }
	}
    }

    private void refreshTotalCategorieIntoContext(ITotal elementToRemove, ITotal elementToSave,
	    List<TotalCategorie> listeTotalCategorie) {
	int position;
	List<TotalCategorie> listeFromContext = new ArrayList<TotalCategorie>();
	for (TotalCategorie totalCategorie : listeTotalCategorie) {
	    listeFromContext.add(totalCategorie);
	}
	for (TotalCategorie totalCategorie : listeFromContext) {
	    if (elementToRemove != null && elementToRemove.getCategorie().equals(totalCategorie.getCategorie())) {
		Double total = Double.valueOf(totalCategorie.getTotal());
		total = total - elementToRemove.getTarif();
		totalCategorie.setTotal(total.toString());
	    }
	    if (elementToSave != null && elementToSave.getCategorie().equals(totalCategorie.getCategorie())) {
		Double total = Double.valueOf(totalCategorie.getTotal());
		total = total + elementToSave.getTarif();
		totalCategorie.setTotal(total.toString());
	    }
	    position = listeTotalCategorie.indexOf(totalCategorie);
	    listeTotalCategorie.remove(totalCategorie);
	    listeTotalCategorie.add(position, totalCategorie);
	}
    }

}
