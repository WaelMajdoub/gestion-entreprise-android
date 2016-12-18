package com.gestionentreprise.gui.activity;

import java.text.ParseException;
import java.util.Date;

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
import android.widget.Spinner;

import com.gestionentreprise.R;
import com.gestionentreprise.bean.DetailDepenseData;
import com.gestionentreprise.bean.GestionEntrepriseContext;
import com.gestionentreprise.gui.adapter.AdapterFactory;
import com.gestionentreprise.gui.adapter.ServerDataAdapter;
import com.gestionentreprise.gui.handler.DataLoaderHandler;
import com.gestionentreprise.gui.listeners.DateFldTouchListener;
import com.gestionentreprise.model.Depense;
import com.gestionentreprise.model.TypeDepense;
import com.gestionentreprise.service.DepenseService;
import com.gestionentreprise.service.ServiceManager;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.service.message.IMessageListener;
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;
import com.gestionentreprise.utils.Action;
import com.gestionentreprise.utils.MessageUtils;

public class DetailDepenseActivity extends GeneralActivity<DetailDepenseData> implements IMessageListener {

    private EditText dateFld;
    private EditText tarifFld;
    private Spinner typeDepenseFld;

    private ServerDataAdapter<TypeDepense> typeDepenseAdapter;

    private Depense depense;

    private DepenseService depenseService;
    private MessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_detail_depense);

	dateFld = (EditText) findViewById(R.id.addDateFld);
	tarifFld = (EditText) findViewById(R.id.addTarifFld);
	typeDepenseFld = (Spinner) findViewById(R.id.addTypeDepenseFld);

	dataLoader = new DataLoaderHandler<DetailDepenseActivity, DetailDepenseData>(this);

	initAdapter();

	initHandlers();

	initData();

	depenseService = ServiceManager.get(DepenseService.class);
	messageService = ServiceManager.get(MessageService.class);
	messageService.registerListener(this);

	// Show the Up button in the action bar.
	setupActionBar();
    }

    private void initHandlers() {
	dateFld.setOnTouchListener(new DateFldTouchListener(dateFld));

	Button fermerFenetreBtn = (Button) findViewById(R.id.addFermerBtn);
	fermerFenetreBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		returnToListeDepenseActivity();
	    }
	});

	Button enregistrerBtn = (Button) findViewById(R.id.addEnregistrerBtn);
	enregistrerBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		if (validate()) {
		    if (depense != null) {
			startServerCommunication(Action.UPDATE);
		    } else {
			startServerCommunication(Action.ADD);
		    }
		}
	    }
	});

    }

    private void returnToListeDepenseActivity() {
	Intent secondeActivite = new Intent(DetailDepenseActivity.this, ListeDepenseActivity.class);
	startActivity(secondeActivite);
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
	typeDepenseAdapter = (ServerDataAdapter<TypeDepense>) AdapterFactory.buildServerDataAdapter(this, false);
	typeDepenseFld.setAdapter(typeDepenseAdapter);
    }

    private void initData() {
	getDataFromIntent();

	if (getContext() != null && getContext().getListeTypeDepense() != null) {
	    setData(new DetailDepenseData(getContext().getListeTypeDepense()));
	} else {
	    startServerCommunication(Action.READ);
	}
    }

    private void getDataFromIntent() {
	Intent intent = getIntent();
	Long idDepense = intent.getLongExtra("idDepense", -1);
	depense = searchDepenseFromContext(idDepense);
    }

    private Depense searchDepenseFromContext(Long idDepense) {
	GestionEntrepriseContext context = getContext();
	Depense depenseFound = null;
	for (int i = 0; depenseFound == null && i < context.getListeDepense().size(); i++) {
	    if (context.getListeDepense().get(i).getId().equals(idDepense)) {
		depenseFound = context.getListeDepense().get(i);
	    }
	}
	return depenseFound;
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
	getMenuInflater().inflate(R.menu.detail_depense, menu);
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
    public void updateContext(DetailDepenseData data) {
	if (getContext() == null)
	    initContext();
	getContext().setListeTypeDepense(data.getListeTypeDepense());
    }

    @Override
    public void saveContext() {
	// Nothing : no criteria
    }

    @Override
    public void setData(DetailDepenseData data) {
	typeDepenseAdapter.setData(data.getListeTypeDepense());

	if (depense != null) {
	    dateFld.setText(sdf.format(depense.getDate()));
	    tarifFld.setText(df.format(depense.getTarif()));
	    typeDepenseFld.setSelection(typeDepenseAdapter.getPosition(depense.getTypeDepense()));
	} else {
	    dateFld.setText(sdf.format(new Date()));
	}
    }

    @Override
    public void messageTriggered(Msg message) {
	MessageUtils.sendMessage(dataLoader, message.getCode(), message.getLibelle());
    }

    @Override
    public void serverCommunicationManagement(Handler receiver) throws Exception {
	if (getAction().equals(Action.READ)) {
	    DetailDepenseData data = depenseService.getDetailDepenseData();
	    updateContext(data);
	    MessageUtils.sendMessage(receiver, IMessage.SUCCES, data);
	} else {
	    Depense depenseFromServer = depenseService.saveDepense(getDepenseDisplayed());
	    refreshDepenseIntoContext(depense, depenseFromServer);
	    depense = depenseFromServer;
	    MessageUtils.sendMessage(receiver, IMessage.SUCCES, null);
	    returnToListeDepenseActivity();
	}
    }

    private Depense getDepenseDisplayed() throws ParseException {
	Depense depenseAffiche = new Depense();
	if (depense != null) {
	    depenseAffiche.setId(depense.getId());
	}
	depenseAffiche.setDate(sdf.parse(dateFld.getText().toString()));
	depenseAffiche.setTypeDepense(typeDepenseAdapter.getData(typeDepenseFld.getSelectedItemPosition()));
	depenseAffiche.setTarif(Double.valueOf(tarifFld.getText().toString()));
	return depenseAffiche;
    }

    private Boolean validate() {
	Boolean validationResult = Boolean.TRUE;
	if (dateFld.getText().toString().length() == 0) {
	    validationResult = Boolean.FALSE;
	    dateFld.setError("La date est obligatoire");
	}
	if (tarifFld.getText().toString().length() == 0) {
	    validationResult = Boolean.FALSE;
	    tarifFld.setError("Le tarif est obligatoire");
	}
	return validationResult;
    }

}
