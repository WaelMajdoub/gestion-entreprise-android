package com.gestionentreprise.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.gestionentreprise.bean.DetailPrestationData;
import com.gestionentreprise.bean.ListePrestationData;
import com.gestionentreprise.model.Prestation;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;
import com.gestionentreprise.utils.JSONParser2;
import com.gestionentreprise.utils.ServerConnectionUtils;

public class PrestationService implements IService {
    private MessageService messageService;

    public PrestationService() {
	messageService = ServiceManager.get(MessageService.class);
    }

    public DetailPrestationData getDetailPrestationData() throws Exception {
	Map<String, String> parametres = new HashMap<String, String>();
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getComptabiliteServerUrl()
	        + "prestation/detail", parametres);
	messageService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage"));
	return JSONParser2.parseJSONForDetailPrestationData(messageService, new JSONObject(sb.toString()));
    }

    public Prestation savePrestation(Prestation prestation) throws ParseException, Exception {
	Map<String, String> mapPrestation = prestation.createMap();
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getComptabiliteServerUrl()
	        + "prestation/enregistrer", mapPrestation);
	messageService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage"));
	return JSONParser2.parseJSONForPrestation(messageService, new JSONObject(sb.toString()));
    }

    public ListePrestationData getListePrestationData(String depuisLe, String jusqua) throws Exception {
	Map<String, String> parametres = new HashMap<String, String>();
	parametres.put("depuisle", depuisLe);
	parametres.put("jusqua", jusqua);
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getComptabiliteServerUrl()
	        + "prestation/liste", parametres);

	messageService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage"));
	return JSONParser2.parseJSONForListePrestationData(messageService, new JSONObject(sb.toString()));
    }

    public Boolean delete(Long idPrestation) throws Exception {
	Map<String, String> parametres = new HashMap<String, String>();
	parametres.put("idPrestation", idPrestation.toString());
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getComptabiliteServerUrl()
	        + "prestation/supprimer", parametres);
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
