package com.gestionentreprise.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;

public class JSONParser2 {
    public static ListePrestationData parseJSONForListePrestationData(MessageService msgService, JSONObject jsonObject)
	    throws JSONException, ParseException {
	ListePrestationData data = new ListePrestationData();
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Total Moyen de Paiement"));
	data.setListeTotalMoyenDePaiement(parseListeTotalMoyenDePaiement(jsonObject));
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Moyen de Paiement"));
	data.setListeMoyenDePaiement(parseListeMoyenDePaiement(jsonObject));
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Prestations"));
	data.setListePrestation(parseListePrestation(msgService, jsonObject));
	return data;
    }

    public static SuiviData parseJSONForSuiviData(MessageService msgService, JSONObject jsonObject)
	    throws JSONException {
	SuiviData data = new SuiviData();
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Suivi"));
	data.setListeSuiviElement(parseListeSuiviElement(jsonObject));
	return data;
    }

    public static ListeDepenseData parseJSONForListeDepenseData(MessageService msgService, JSONObject jsonObject)
	    throws JSONException, ParseException {
	ListeDepenseData data = new ListeDepenseData();
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Total Type Depense"));
	data.setListeTotalTypeDepense(parseListeTotalTypeDepense(jsonObject));
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Type Depense"));
	data.setListeTypeDepense(parseListeTypeDepense(jsonObject));
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Depenses"));
	data.setListeDepense(parseListeDepense(msgService, jsonObject));
	return data;
    }

    public static Prestation parseJSONForPrestation(MessageService msgService, JSONObject jsonObject)
	    throws JSONException, ParseException {
	return parsePrestation(jsonObject.getJSONObject("prestation"));
    }

    public static Client parseJSONForClient(MessageService msgService, JSONObject jsonObject) throws JSONException {
	return parseClient(jsonObject.getJSONObject("client"));
    }

    public static DetailPrestationData parseJSONForDetailPrestationData(MessageService msgService, JSONObject jsonObject)
	    throws JSONException {
	DetailPrestationData data = new DetailPrestationData();
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Type de Prestation"));
	data.setListeTypePrestation(parseListeTypePrestation(msgService, jsonObject));
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Moyen de Paiement"));
	data.setListeMoyenDePaiement(parseListeMoyenDePaiement(jsonObject));
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Clients"));
	data.setListeClient(parseListeClient(msgService, jsonObject));
	return data;
    }

    public static DetailRdvData parseJSONForDetailRdvData(MessageService msgService, JSONObject jsonObject)
	    throws JSONException {
	DetailRdvData data = new DetailRdvData();
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Type de Prestation"));
	data.setListeTypePrestation(parseListeTypePrestation(msgService, jsonObject));
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Clients"));
	data.setListeClient(parseListeClient(msgService, jsonObject));
	return data;
    }

    public static Depense parseJSONForDepense(MessageService msgService, JSONObject jsonObject) throws JSONException, ParseException {
	return parseDepense(jsonObject.getJSONObject("depense"));
    }

    public static DetailDepenseData parseJSONForDetailDepenseData(MessageService msgService, JSONObject jsonObject)
	    throws JSONException {
	DetailDepenseData data = new DetailDepenseData();
	msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Type de Depense"));
	data.setListeTypeDepense(parseListeTypeDepense(jsonObject));
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
    	
	JSONArray keys = jsonListeTotalCategorie.names();
	List<TotalCategorie> listeTotalTypeDepense = new ArrayList<TotalCategorie>();
	for (int i = 0; i < keys.length(); i++) {
	    listeTotalTypeDepense.add(new TotalCategorie(keys.getString(i), jsonListeTotalCategorie.getString(keys.getString(i))));
	}
	return listeTotalTypeDepense;
    }

    private static List<SuiviElement> parseListeSuiviElement(JSONObject jsonArrayCopy) throws JSONException {
	JSONArray jsonDateTab = jsonArrayCopy.getJSONArray("dateTab");
	JSONArray jsonPrestationTab = jsonArrayCopy.getJSONArray("prestationTab");
	JSONArray jsonPrestationHorsRSITab = jsonArrayCopy.getJSONArray("prestationHorsRSITab");
	JSONArray jsonDepenseTab = jsonArrayCopy.getJSONArray("depenseTab");

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

    private static List<Double> parseListeDouble(JSONArray values) throws JSONException {
	List<Double> listeDouble = new ArrayList<Double>();
	for (int i = 0; i < values.length(); i++) {
	    listeDouble.add(Double.valueOf(values.getString(i)));
	}
	return listeDouble;
    }

    private static List<Date> parseListeDate(JSONArray values) throws JSONException {
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

    private static List<Prestation> parseListePrestation(MessageService msgService, JSONObject jsonArrayCopy)
	    throws JSONException, ParseException {
	JSONArray jsonListePrestation = jsonArrayCopy.getJSONArray("listePrestation");
	List<Prestation> listePrestation = new ArrayList<Prestation>();
	for (int i = 0; i < jsonListePrestation.length(); i++) {
	    msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Prestations " + (i + 1) + "/"
		    + jsonListePrestation.length()));
	    listePrestation.add(parsePrestation(jsonListePrestation.getJSONObject(i)));
	}
	return listePrestation;
    }

    private static Prestation parsePrestation(JSONObject jsonPrestation) throws JSONException, ParseException {
	Prestation prestation = new Prestation();
	prestation.setId(jsonPrestation.getLong("id"));
	prestation.setTypePrestation(parseTypePrestation(jsonPrestation.getJSONObject("type_prestation")));
	prestation.setMoyenDePaiement(parseMoyenDePaiement(jsonPrestation.getJSONObject("moyen_de_paiement")));
	prestation.setTarif(jsonPrestation.getDouble("tarif"));
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	prestation.setDate(dateFormat.parse(jsonPrestation.getString("date")));
	prestation.setClient(parseClient(jsonPrestation.getJSONObject("client")));
	prestation.setComptabilise(jsonPrestation.getBoolean("comptabilise"));
	return prestation;
    }

    private static List<Depense> parseListeDepense(MessageService msgService, JSONObject jsonArrayCopy)
	    throws JSONException, ParseException {
	JSONArray jsonListeDepense = jsonArrayCopy.getJSONArray("listeDepense");
	List<Depense> listeDepense = new ArrayList<Depense>();
	for (int i = 0; i < jsonListeDepense.length(); i++) {
	    msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Depense " + (i + 1) + "/"
		    + jsonListeDepense.length()));
	    listeDepense.add(parseDepense(jsonListeDepense.getJSONObject(i)));
	}
	return listeDepense;
    }

    private static Depense parseDepense(JSONObject jsonPrestation) throws JSONException, ParseException {
	Depense depense = new Depense();
	depense.setId(jsonPrestation.getLong("id"));
	depense.setTypeDepense(parseTypeDepense(jsonPrestation.getJSONObject("type_depense")));
	depense.setTarif(jsonPrestation.getDouble("tarif"));	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	depense.setDate(dateFormat.parse(jsonPrestation.getString("date")));
	return depense;
    }

    private static List<TypePrestation> parseListeTypePrestation(MessageService msgService, JSONObject jsonArrayCopy)
	    throws JSONException {
	JSONArray jsonListeTypePrestation = jsonArrayCopy.getJSONArray("listeTypePrestation");
	List<TypePrestation> listeTypePrestation = new ArrayList<TypePrestation>();
	for (int i = 0; i < jsonListeTypePrestation.length(); i++) {
	    msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Type de Prestation " + (i + 1) + "/"
		    + jsonListeTypePrestation.length()));
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

    private static List<Client> parseListeClient(MessageService msgService, JSONObject jsonArrayCopy)
	    throws JSONException {
	JSONArray jsonListeClient = jsonArrayCopy.getJSONArray("listeClient");
	List<Client> listeClient = new ArrayList<Client>();
	for (int i = 0; i < jsonListeClient.length(); i++) {
	    msgService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage - Client " + (i + 1) + "/"
		    + jsonListeClient.length()));
	    listeClient.add(parseClient(jsonListeClient.getJSONObject(i)));
	}
	return listeClient;
    }

    private static Client parseClient(JSONObject jsonClient) throws JSONException {
	Client client = new Client();
	client.setId(jsonClient.getLong("id"));
	client.setPrenom(valueOf(jsonClient, "prenom"));
	client.setNom(valueOf(jsonClient, "nom"));
	client.setAdresse(parseAdresse(jsonClient.getJSONObject("adresse")));
	client.setEmail(valueOf(jsonClient, "email"));
	client.setTelephone(valueOf(jsonClient, "telephone"));
	client.setTelephone2(valueOf(jsonClient, "telephone2"));
	return client;
    }

    private static Adresse parseAdresse(JSONObject jsonAdresse) throws JSONException {
	Adresse adresse = new Adresse();
	adresse.setId(jsonAdresse.getLong("id"));
	adresse.setLibelle1(valueOf(jsonAdresse, "libelle1"));
	adresse.setLibelle2(valueOf(jsonAdresse, "libelle2"));
	adresse.setCodePostal(valueOf(jsonAdresse, "code_postal"));
	adresse.setVille(valueOf(jsonAdresse, "ville"));
	return adresse;
    }

    private static String valueOf(JSONObject jsonObject, String key) {
	String value = null;
	try {
		value = jsonObject.getString(key);
	} catch (JSONException e) {
		value = null;
	} finally {
		if (value != null) {
			if (value.contains("null"))
			    value = null;
			
		}
	}
	return value;
	

    }

}
