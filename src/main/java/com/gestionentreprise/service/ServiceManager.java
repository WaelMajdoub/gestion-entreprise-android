package com.gestionentreprise.service;

import java.util.ArrayList;
import java.util.List;

import com.gestionentreprise.utils.MessageUtils;

import android.util.Log;

public class ServiceManager {

    private static List<IService> listService;

    private static ServiceManager instance;

    private ServiceManager() {
	listService = new ArrayList<IService>();
    }

    public static <T extends IService> T get(Class<T> classe) {
	if (instance == null)
	    instance = new ServiceManager();

	T serviceToReturn = null;
	for (IService service : listService) {
	    if (service.getClass().equals(classe)) {
		serviceToReturn = (T) service;
	    }
	}

	if (serviceToReturn == null) {
	    try {
	    	serviceToReturn = classe.newInstance();
	    } catch (IllegalArgumentException e) {
	    	MessageUtils.logException(e, "Illegal Argument exception");
	    } catch (InstantiationException e) {
	    	MessageUtils.logException(e, "Instantiation exception");
	    } catch (IllegalAccessException e) {
	    	MessageUtils.logException(e, "illegal Access exception");
	    }
	    listService.add((IService) serviceToReturn);
	}
	return serviceToReturn;
    }
}
