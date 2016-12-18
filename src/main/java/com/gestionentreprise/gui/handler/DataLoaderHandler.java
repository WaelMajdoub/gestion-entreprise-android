package com.gestionentreprise.gui.handler;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.gestionentreprise.gui.activity.GeneralActivity;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.utils.MessageUtils;

public class DataLoaderHandler<T extends GeneralActivity<K>, K> extends Handler {
    private T parent;

    public DataLoaderHandler(T parent) {
	super();
	this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public void handleMessage(Message msg) {

	Builder builder;
	switch (msg.arg1) {
	case IMessage.IN_PROGRESS:
	    parent.setConnectionLoadingMessage((String) msg.obj);
	    break;
	case IMessage.SUCCES:
	    parent.dismissConnectionLoadingDialog();
	    if (msg.obj != null)
		parent.setData((K) msg.obj);
	    break;
	case IMessage.CONNECTION_EXCEPTION:
	    parent.dismissConnectionLoadingDialog();
	    Exception e = (Exception) msg.obj;
	    MessageUtils.logException(e, "Connection exception");
	    Toast.makeText(parent, "Echec de connection", Toast.LENGTH_SHORT).show();
	    break;
	case IMessage.PARSE_EXCEPTION:
	    parent.dismissConnectionLoadingDialog();
	    e = (Exception) msg.obj;
	    MessageUtils.logException(e, "Parse exception");
	    Toast.makeText(parent, "Echec de parsing", Toast.LENGTH_LONG).show();
	case IMessage.DATA_NOT_FOUND:
	    parent.dismissConnectionLoadingDialog();
	    Toast.makeText(parent, "Donnée inconnue", Toast.LENGTH_LONG).show();
	default:
	    parent.dismissConnectionLoadingDialog();
	    Exception e2 = (Exception) msg.obj;
	    MessageUtils.logException(e2, "Default Dataloader Handler exception");
	    builder = new AlertDialog.Builder(parent);
	    builder.setTitle("Echec INCONNU");
	    builder.setMessage("Echec INCONNU");
	    builder.show();
	    break;

	}
    }
}
