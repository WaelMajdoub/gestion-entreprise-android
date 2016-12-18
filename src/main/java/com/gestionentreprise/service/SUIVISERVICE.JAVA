package com.gestionentreprise.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.gestionentreprise.bean.SuiviData;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;
import com.gestionentreprise.utils.JSONParser2;
import com.gestionentreprise.utils.ServerConnectionUtils;

public class SuiviService implements IService {
    private MessageService messageService;

    public SuiviService() {
	messageService = ServiceManager.get(MessageService.class);
    }

    public SuiviData getSuiviData(String depuisLe, String jusqua) throws Exception {
	Map<String, String> parametres = new HashMap<String, String>();
	parametres.put("depuisle", depuisLe);
	parametres.put("jusqua", jusqua);
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getComptabiliteServerUrl()
	        + "suivi", parametres);

	messageService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage"));
	return JSONParser2.parseJSONForSuiviData(messageService, new JSONObject(sb.toString()));
    }
}
