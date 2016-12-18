package com.gestionentreprise.service.message;

import java.util.ArrayList;
import java.util.List;

import com.gestionentreprise.service.IService;

public class MessageService implements IService {
    private List<IMessageListener> listeners;

    public MessageService() {
	this.listeners = new ArrayList<IMessageListener>();
    }

    public void registerListener(IMessageListener listener) {
	listeners.add(listener);
    }

    public void sendMessage(Msg message) {
	for (IMessageListener listener : listeners) {
	    listener.messageTriggered(message);
	}
    }
}
