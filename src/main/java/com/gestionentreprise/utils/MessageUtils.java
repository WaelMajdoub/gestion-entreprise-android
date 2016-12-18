package com.gestionentreprise.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MessageUtils {
    public static void sendMessage(Handler receiver, int arg1, Object obj) {
	Message msg = Message.obtain();
	msg.arg1 = arg1;
	msg.obj = obj;
	receiver.sendMessage(msg);
    }
    
    public static void logException(Exception e, String defaultMessage) {
    	if (e != null && e.getMessage() != null) {
	    	Log.e("Gestion Entreprise", e.getMessage());	
	    } else {
	    	Log.e("Gestion Entreprise", defaultMessage);
	    }
    }
}
