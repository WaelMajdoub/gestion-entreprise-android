package com.gestionentreprise.service;

import java.text.ParseException;
import java.util.Map;

import org.json.JSONObject;

import com.gestionentreprise.model.Client;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;
import com.gestionentreprise.utils.JSONParser2;
import com.gestionentreprise.utils.ServerConnectionUtils;

public class ClientService implements IService {
    private MessageService messageService;

    public ClientService() {
	messageService = ServiceManager.get(MessageService.class);
    }

    public Client saveClient(Client client) throws ParseException, Exception {
	Map<String, String> mapClient = client.createMap();
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getParametrageServerUrl()
	        + "client/enregistrer", mapClient);
	messageService.sendMessage(new Msg(IMessage.IN_PROGRESS, "Décodage"));
	return JSONParser2.parseJSONForClient(messageService, new JSONObject(sb.toString()));
    }

}
