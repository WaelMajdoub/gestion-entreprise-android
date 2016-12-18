package com.gestionentreprise.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import com.gestionentreprise.bean.DetailDepenseData;
import com.gestionentreprise.bean.DetailPrestationData;
import com.gestionentreprise.bean.DetailRdvData;
import com.gestionentreprise.bean.ListeDepenseData;
import com.gestionentreprise.bean.ListePrestationData;
import com.gestionentreprise.bean.SuiviData;
import com.gestionentreprise.model.Adresse;
import com.gestionentreprise.model.Client;
import com.gestionentreprise.model.Depense;
import com.gestionentreprise.model.MoyenDePaiement;
import com.gestionentreprise.model.Prestation;
import com.gestionentreprise.model.SuiviElement;
import com.gestionentreprise.model.TotalCategorie;
import com.gestionentreprise.model.TypeDepense;
import com.gestionentreprise.model.TypePrestation;
import com.gestionentreprise.service.message.IMessage;

public class JSONParser {
    public static ListePrestationData parseJSONForListePrestationData(Handler receiver, JSONObject jsonObject)
	    throws JSONException {
	JSONObject jsonArrayCopy = jsonObject.getJSONObject("iterator").getJSONObject("arrayCopy");

	ListePrestationData data = new ListePrestationData();
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Total Moyen de Paiement");
	data.setListeTotalMoyenDePaiement(parseListeTotalMoyenDePaiement(jsonArrayCopy));
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Moyen de Paiement");
	data.setListeMoyenDePaiement(parseListeMoyenDePaiement(jsonArrayCopy));
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Prestations");
	data.setListePrestation(parseListePrestation(receiver, jsonArrayCopy));
	return data;
    }

    public static SuiviData parseJSONForSuiviData(Handler receiver, JSONObject jsonObject) throws JSONException {
	JSONObject jsonArrayCopy = jsonObject.getJSONObject("iterator").getJSONObject("arrayCopy");

	SuiviData data = new SuiviData();
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Suivi");
	data.setListeSuiviElement(parseListeSuiviElement(jsonArrayCopy));
	return data;
    }

    public static ListeDepenseData parseJSONForListeDepenseData(Handler receiver, JSONObject jsonObject)
	    throws JSONException {
	JSONObject jsonArrayCopy = jsonObject.getJSONObject("iterator").getJSONObject("arrayCopy");

	ListeDepenseData data = new ListeDepenseData();
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Total Type Depense");
	data.setListeTotalTypeDepense(parseListeTotalTypeDepense(jsonArrayCopy));
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Type Depense");
	data.setListeTypeDepense(parseListeTypeDepense(jsonArrayCopy));
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Depenses");
	data.setListeDepense(parseListeDepense(receiver, jsonArrayCopy));
	return data;
    }

    public static Prestation parseJSONForPrestation(Handler receiver, JSONObject jsonObject) throws JSONException {
	JSONObject jsonArrayCopy = jsonObject.getJSONObject("iterator").getJSONObject("arrayCopy");
	return parsePrestation(jsonArrayCopy.getJSONObject("prestation"));
    }

    public static Client parseJSONForClient(Handler receiver, JSONObject jsonObject) throws JSONException {
	JSONObject jsonArrayCopy = jsonObject.getJSONObject("iterator").getJSONObject("arrayCopy");
	return parseClient(jsonArrayCopy.getJSONObject("client"));
    }

    public static DetailPrestationData parseJSONForDetailPrestationData(Handler receiver, JSONObject jsonObject)
	    throws JSONException {
	JSONObject jsonArrayCopy = jsonObject.getJSONObject("iterator").getJSONObject("arrayCopy");

	DetailPrestationData data = new DetailPrestationData();
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Type de Prestation");
	data.setListeTypePrestation(parseListeTypePrestation(receiver, jsonArrayCopy));
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Moyen de Paiement");
	data.setListeMoyenDePaiement(parseListeMoyenDePaiement(jsonArrayCopy));
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Clients");
	data.setListeClient(parseListeClient(receiver, jsonArrayCopy));
	return data;
    }

    public static DetailRdvData parseJSONForDetailRdvData(Handler receiver, JSONObject jsonObject) throws JSONException {
	JSONObject jsonArrayCopy = jsonObject.getJSONObject("iterator").getJSONObject("arrayCopy");

	DetailRdvData data = new DetailRdvData();
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Type de Prestation");
	data.setListeTypePrestation(parseListeTypePrestation(receiver, jsonArrayCopy));
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Clients");
	data.setListeClient(parseListeClient(receiver, jsonArrayCopy));
	return data;
    }

    public static Depense parseJSONForDepense(Handler receiver, JSONObject jsonObject) throws JSONException {
	JSONObject jsonArrayCopy = jsonObject.getJSONObject("iterator").getJSONObject("arrayCopy");
	return parseDepense(jsonArrayCopy.getJSONObject("depense"));
    }

    public static DetailDepenseData parseJSONForDetailDepenseData(Handler receiver, JSONObject jsonObject)
	    throws JSONException {
	JSONObject jsonArrayCopy = jsonObject.getJSONObject("iterator").getJSONObject("arrayCopy");

	DetailDepenseData data = new DetailDepenseData();
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Type de Depense");
	data.setListeTypeDepense(parseListeTypeDepense(jsonArrayCopy));
	return data;
    }

    private static List<TotalCategorie> parseListeTotalMoyenDePaiement(JSONObject jsonArrayCopy) throws JSONException {
	JSONObject jsonListeTotalMoyenDePaiement = jsonArrayCopy.getJSONObject("listeTotalMoyenDePaiement");
	return parseListeTotalCategorie(jsonListeTotalMoyenDePaiement);
    }

    private static List<TotalCategorie> parseListeTotalTypeDepense(JSONObject jsonArrayCopy) throws JSONException {
	JSONObject jsonListeTotalTypeDepense = jsonArrayCopy.getJSONObject("listeTotalDepense");
	return parseListeTotalCategorie(jsonListeTotalTypeDepense);
    }

    private static List<TotalCategorie> parseListeTotalCategorie(JSONObject jsonListeTotalCategorie)
	    throws JSONException {
	JSONArray keys = jsonListeTotalCategorie.getJSONArray("keys");
	JSONArray values = jsonListeTotalCategorie.getJSONArray("values");
	List<TotalCategorie> listeTotalTypeDepense = new ArrayList<TotalCategorie>();
	for (int i = 0; i < keys.length(); i++) {
	    listeTotalTypeDepense.add(new TotalCategorie(keys.getString(i), values.getString(i)));
	}
	return listeTotalTypeDepense;
    }

    private static List<SuiviElement> parseListeSuiviElement(JSONObject jsonArrayCopy) throws JSONException {
	JSONObject jsonDateTab = jsonArrayCopy.getJSONObject("dateTab");
	JSONObject jsonPrestationTab = jsonArrayCopy.getJSONObject("prestationTab");
	JSONObject jsonPrestationHorsRSITab = jsonArrayCopy.getJSONObject("prestationHorsRSITab");
	JSONObject jsonDepenseTab = jsonArrayCopy.getJSONObject("depenseTab");

	List<Date> listeDate = parseListeDate(jsonDateTab);
	List<Double> listePrestation = parseListeDouble(jsonPrestationTab);
	List<Double> listePrestationHorsRSI = parseListeDouble(jsonPrestationHorsRSITab);
	List<Double> listeDepense = parseListeDouble(jsonDepenseTab);

	List<SuiviElement> dataOut = new ArrayList<SuiviElement>();
	for (int i = 0; i < listeDate.size(); i++) {
	    SuiviElement suiviElement = new SuiviElement();
	    suiviElement.setDate(listeDate.get(i));
	    suiviElement.setChiffreAffaireBrut(listePrestation.get(i));
	    suiviElement.setChiffreAffaireHorsRSI(listePrestationHorsRSI.get(i));
	    suiviElement.setChiffreAffaireNet(listePrestationHorsRSI.get(i) - listeDepense.get(i));
	    dataOut.add(suiviElement);
	}
	return dataOut;
    }

    private static List<Double> parseListeDouble(JSONObject jsonObject) throws JSONException {
	JSONArray values = jsonObject.getJSONArray("values");
	List<Double> listeDouble = new ArrayList<Double>();
	for (int i = 0; i < values.length(); i++) {
	    listeDouble.add(Double.valueOf(values.getString(i)));
	}
	return listeDouble;
    }

    private static List<Date> parseListeDate(JSONObject jsonObject) throws JSONException {
	JSONArray values = jsonObject.getJSONArray("values");
	List<Date> listeDate = new ArrayList<Date>();
	for (int i = 0; i < values.length(); i++) {
	    Long dateTime = Long.valueOf(values.getString(i)) * 1000;
	    listeDate.add(new Date(dateTime));
	}
	return listeDate;
    }

    private static List<MoyenDePaiement> parseListeMoyenDePaiement(JSONObject jsonArrayCopy) throws JSONException {
	JSONArray jsonListeMoyenDePaiement = jsonArrayCopy.getJSONArray("listeMoyenDePaiement");
	List<MoyenDePaiement> listeMoyenDePaiement = new ArrayList<MoyenDePaiement>();
	for (int i = 0; i < jsonListeMoyenDePaiement.length(); i++) {
	    JSONObject jsonMoyenDePaiement = jsonListeMoyenDePaiement.getJSONObject(i);
	    listeMoyenDePaiement.add(parseMoyenDePaiement(jsonMoyenDePaiement));
	}
	return listeMoyenDePaiement;
    }

    private static MoyenDePaiement parseMoyenDePaiement(JSONObject jsonMoyenDePaiement) throws JSONException {
	return new MoyenDePaiement(jsonMoyenDePaiement.getLong("id"),
	                           jsonMoyenDePaiement.getString("libelle"),
	                           jsonMoyenDePaiement.getBoolean("defaut"));
    }

    private static List<Prestation> parseListePrestation(Handler receiver, JSONObject jsonArrayCopy)
	    throws JSONException {
	JSONArray jsonListePrestation = jsonArrayCopy.getJSONArray("listePrestation");
	List<Prestation> listePrestation = new ArrayList<Prestation>();
	for (int i = 0; i < jsonListePrestation.length(); i++) {
	    MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Prestations " + (i + 1) + "/"
		    + jsonListePrestation.length());
	    listePrestation.add(parsePrestation(jsonListePrestation.getJSONObject(i)));
	}
	return listePrestation;
    }

    private static Prestation parsePrestation(JSONObject jsonPrestation) throws JSONException {
	Prestation prestation = new Prestation();
	prestation.setId(jsonPrestation.getLong("id"));
	prestation.setTypePrestation(parseTypePrestation(jsonPrestation.getJSONObject("typePrestation")));
	prestation.setMoyenDePaiement(parseMoyenDePaiement(jsonPrestation.getJSONObject("moyenDePaiement")));
	prestation.setTarif(jsonPrestation.getDouble("tarif"));
	Long dateTime = jsonPrestation.getJSONObject("date").getLong("timestamp") * 1000;
	prestation.setDate(new Date(dateTime));
	prestation.setClient(parseClient(jsonPrestation.getJSONObject("client")));
	return prestation;
    }

    private static List<Depense> parseListeDepense(Handler receiver, JSONObject jsonArrayCopy) throws JSONException {
	JSONArray jsonListeDepense = jsonArrayCopy.getJSONArray("listeDepense");
	List<Depense> listeDepense = new ArrayList<Depense>();
	for (int i = 0; i < jsonListeDepense.length(); i++) {
	    MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Depense " + (i + 1) + "/"
		    + jsonListeDepense.length());
	    listeDepense.add(parseDepense(jsonListeDepense.getJSONObject(i)));
	}
	return listeDepense;
    }

    private static Depense parseDepense(JSONObject jsonPrestation) throws JSONException {
	Depense depense = new Depense();
	depense.setId(jsonPrestation.getLong("id"));
	depense.setTypeDepense(parseTypeDepense(jsonPrestation.getJSONObject("typeDepense")));
	depense.setTarif(jsonPrestation.getDouble("tarif"));
	Long dateTime = jsonPrestation.getJSONObject("date").getLong("timestamp") * 1000;
	depense.setDate(new Date(dateTime));
	return depense;
    }

    private static List<TypePrestation> parseListeTypePrestation(Handler receiver, JSONObject jsonArrayCopy)
	    throws JSONException {
	JSONArray jsonListeTypePrestation = jsonArrayCopy.getJSONArray("listeTypePrestation");
	List<TypePrestation> listeTypePrestation = new ArrayList<TypePrestation>();
	for (int i = 0; i < jsonListeTypePrestation.length(); i++) {
	    MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Type de Prestation " + (i + 1) + "/"
		    + jsonListeTypePrestation.length());
	    listeTypePrestation.add(parseTypePrestation(jsonListeTypePrestation.getJSONObject(i)));
	}
	return listeTypePrestation;
    }

    private static TypePrestation parseTypePrestation(JSONObject jsonTypePrestation) throws JSONException {
	TypePrestation typePrestation = new TypePrestation();
	typePrestation.setId(jsonTypePrestation.getLong("id"));
	typePrestation.setLibelle(jsonTypePrestation.getString("libelle"));
	typePrestation.setTarif(jsonTypePrestation.getDouble("tarif"));
	return typePrestation;
    }

    private static List<TypeDepense> parseListeTypeDepense(JSONObject jsonArrayCopy) throws JSONException {
	JSONArray jsonListeTypeDepense = jsonArrayCopy.getJSONArray("listeTypeDepense");
	List<TypeDepense> listeTypeDepense = new ArrayList<TypeDepense>();
	for (int i = 0; i < jsonListeTypeDepense.length(); i++) {
	    listeTypeDepense.add(parseTypeDepense(jsonListeTypeDepense.getJSONObject(i)));
	}
	return listeTypeDepense;
    }

    private static TypeDepense parseTypeDepense(JSONObject jsonTypePrestation) throws JSONException {
	TypeDepense typeDepense = new TypeDepense();
	typeDepense.setId(jsonTypePrestation.getLong("id"));
	typeDepense.setLibelle(jsonTypePrestation.getString("libelle"));
	return typeDepense;
    }

    private static List<Client> parseListeClient(Handler receiver, JSONObject jsonArrayCopy) throws JSONException {
	JSONArray jsonListeClient = jsonArrayCopy.getJSONArray("listeClient");
	List<Client> listeClient = new ArrayList<Client>();
	for (int i = 0; i < jsonListeClient.length(); i++) {
	    MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage - Client " + (i + 1) + "/"
		    + jsonListeClient.length());
	    listeClient.add(parseClient(jsonListeClient.getJSONObject(i)));
	}
	return listeClient;
    }

    private static Client parseClient(JSONObject jsonClient) throws JSONException {
	Client client = new Client();
	client.setId(jsonClient.getLong("id"));
	client.setPrenom(valueOf(jsonClient.getString("prenom")));
	client.setNom(valueOf(jsonClient.getString("nom")));
	client.setAdresse(parseAdresse(jsonClient.getJSONObject("adresse")));
	client.setEmail(valueOf(jsonClient.getString("email")));
	client.setTelephone(valueOf(jsonClient.getString("telephone")));
	client.setTelephone2(valueOf(jsonClient.getString("telephone2")));
	return client;
    }

    private static Adresse parseAdresse(JSONObject jsonAdresse) throws JSONException {
	Adresse adresse = new Adresse();
	adresse.setId(jsonAdresse.getLong("id"));
	adresse.setLibelle1(valueOf(jsonAdresse.getString("libelle1")));
	adresse.setLibelle2(valueOf(jsonAdresse.getString("libelle2")));
	adresse.setCodePostal(valueOf(jsonAdresse.getString("codePostal")));
	adresse.setVille(valueOf(jsonAdresse.getString("ville")));
	return adresse;
    }

    private static String valueOf(String str) {
	String out = str;
	if (str.contains("null"))
	    out = null;
	return out;

    }

}
