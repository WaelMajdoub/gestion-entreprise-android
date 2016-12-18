package com.gestionentreprise.gui.activity;

import java.text.ParseException;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.gestionentreprise.R;
import com.gestionentreprise.gui.handler.DataLoaderHandler;
import com.gestionentreprise.model.Adresse;
import com.gestionentreprise.model.Client;
import com.gestionentreprise.service.ClientService;
import com.gestionentreprise.service.ServiceManager;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.service.message.IMessageListener;
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;
import com.gestionentreprise.utils.Action;
import com.gestionentreprise.utils.MessageUtils;

public class DetailClientActivity extends GeneralActivity<String> implements IMessageListener {

    private EditText nomFld;
    private EditText prenomFld;
    private EditText telephoneFld;
    private EditText adresseFld;
    private EditText codePostalFld;
    private EditText villeFld;

    private boolean callingActivityDetailRdv;

    private ClientService clientService;
    private MessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_detail_client);

	nomFld = (EditText) findViewById(R.id.dcaNomFld);
	prenomFld = (EditText) findViewById(R.id.dcaPrenomFld);
	telephoneFld = (EditText) findViewById(R.id.dcaTelephoneFld);
	adresseFld = (EditText) findViewById(R.id.dcaAdresseFld);
	codePostalFld = (EditText) findViewById(R.id.dcaCodePostalFld);
	villeFld = (EditText) findViewById(R.id.dcaVilleFld);

	dataLoader = new DataLoaderHandler<DetailClientActivity, String>(this);

	Button fermerFenetreBtn = (Button) findViewById(R.id.dcaFermerFenetreBtn);
	fermerFenetreBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent secondeActivite;
		if (callingActivityDetailRdv) {
		    secondeActivite = new Intent(DetailClientActivity.this, DetailRdvActivity.class);
		} else {
		    secondeActivite = new Intent(DetailClientActivity.this, DetailPrestationActivity.class);
		}
		startActivity(secondeActivite);
	    }
	});

	Button enregistrerBtn = (Button) findViewById(R.id.dcaEnregistrerBtn);
	enregistrerBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		Boolean validationResult = validate();
		if (validationResult) {
		    startServerCommunication(Action.ADD);
		}
	    }
	});

	getDataFromIntent();

	clientService = ServiceManager.get(ClientService.class);
	messageService = ServiceManager.get(MessageService.class);
	messageService.registerListener(this);

	// Show the Up button in the action bar.
	setupActionBar();
    }

    private void getDataFromIntent() {
	Intent intent = getIntent();
	callingActivityDetailRdv = intent.getBooleanExtra("detailRdvActivity", false);
	String nouveauClientNom = intent.getStringExtra("nouveauClientNom");
	nomFld.setText(nouveauClientNom);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.detail_client, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    // This ID represents the Home or Up button. In the case of this
	    // activity, the Up button is shown. Use NavUtils to allow users
	    // to navigate up one level in the application structure. For
	    // more details, see the Navigation pattern on Android Design:
	    //
	    // http://developer.android.com/design/patterns/navigation.html#up-vs-back
	    //
	    NavUtils.navigateUpFromSameTask(this);
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public void messageTriggered(Msg message) {
	MessageUtils.sendMessage(dataLoader, message.getCode(), message.getLibelle());
    }

    @Override
    public void updateContext(String data) {
	// TODO Auto-generated method stub

    }

    @Override
    public void saveContext() {
	// TODO Auto-generated method stub

    }

    @Override
    public void setData(String data) {
	// TODO Auto-generated method stub

    }

    @Override
    public void serverCommunicationManagement(Handler receiver) throws Exception {
	Client clientFromServer = clientService.saveClient(getClientDisplayed());
	if (clientFromServer != null) {
	    getContext().getListeClient().add(clientFromServer);
	}
	MessageUtils.sendMessage(receiver, IMessage.SUCCES, null);
	Intent secondeActivite;
	if (callingActivityDetailRdv) {
	    secondeActivite = new Intent(DetailClientActivity.this, DetailRdvActivity.class);
	} else {
	    secondeActivite = new Intent(DetailClientActivity.this, DetailPrestationActivity.class);
	}
	if (clientFromServer != null)
	    secondeActivite.putExtra("idClient", clientFromServer.getId());
	startActivity(secondeActivite);
    }

    private Client getClientDisplayed() throws ParseException {
	Client client = new Client();
	client.setNom(nomFld.getText().toString());
	client.setPrenom(prenomFld.getText().toString());
	client.setTelephone(telephoneFld.getText().toString());

	Adresse adresse = new Adresse();
	adresse.setLibelle1(adresseFld.getText().toString());
	adresse.setCodePostal(codePostalFld.getText().toString());
	adresse.setVille(villeFld.getText().toString());
	client.setAdresse(adresse);

	return client;
    }

    private Boolean validate() {
	Boolean validationResult = Boolean.TRUE;
	if (nomFld.getText().toString().length() == 0) {
	    validationResult = Boolean.FALSE;
	    nomFld.setError("Le nom est obligatoire");
	}
	if (adresseFld.getText().toString().length() == 0) {
	    validationResult = Boolean.FALSE;
	    adresseFld.setError("L'adresse est obligatoire");
	}
	if (villeFld.getText().toString().length() == 0) {
	    validationResult = Boolean.FALSE;
	    villeFld.setError("La ville est obligatoire");
	}
	return validationResult;
    }

}
