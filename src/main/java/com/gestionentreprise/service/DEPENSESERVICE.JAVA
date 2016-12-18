package com.gestionentreprise.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.gestionentreprise.bean.DetailDepenseData;
import com.gestionentreprise.bean.ListeDepenseData;
import com.gestionentreprise.model.Depense;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;
import com.gestionentreprise.utils.JSONParser2;
import com.gestionentreprise.utils.ServerConnectionUtils;

public class DepenseService implements IService {
    private MessageService messageService;

    public DepenseService() {
	messageService = ServiceManager.get(MessageService.class);
    }

    public DetailDepenseData getDetailDepenseData() throws Exception {
	Map<String, String> parametres = new HashMap<String, String>();
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getComptabiliteServerUrl()
	        + "prestation/detail", parametres);
	messageService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage"));
	return JSONParser2.parseJSONForDetailDepenseData(messageService, new JSONObject(sb.toString()));
    }

    public Depense saveDepense(Depense depense) throws ParseException, Exception {
	Map<String, String> mapDepense = depense.createMap();
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getComptabiliteServerUrl()
	        + "depense/enregistrer", mapDepense);
	messageService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage"));
	return JSONParser2.parseJSONForDepense(messageService, new JSONObject(sb.toString()));
    }

    public ListeDepenseData getListeDepenseData(String depuisLe, String jusqua) throws Exception {
	Map<String, String> parametres = new HashMap<String, String>();
	parametres.put("depuisle", depuisLe);
	parametres.put("jusqua", jusqua);
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getComptabiliteServerUrl()
	        + "depense/liste", parametres);
	messageService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage"));

	return JSONParser2.parseJSONForListeDepenseData(messageService, new JSONObject(sb.toString()));
    }

    public Boolean deleteDepense(Long idDepense) throws Exception {
	Map<String, String> parametres = new HashMap<String, String>();
	parametres.put("idDepense", idDepense.toString());
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getComptabiliteServerUrl()
	        + "depense/supprimer", parametres);
	messageService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage"));
	Boolean result = null;
	if (sb != null && sb.toString() != null && sb.toString().equals("\"OK\"")) {
	    result = Boolean.TRUE;
	} else {
	    result = Boolean.FALSE;
	}
	return result;
    }
}
