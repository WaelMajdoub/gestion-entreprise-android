package com.gestionentreprise.gui.activity;

import java.text.ParseException;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.gestionentreprise.R;
import com.gestionentreprise.bean.DetailPrestationData;
import com.gestionentreprise.bean.GestionEntrepriseContext;
import com.gestionentreprise.gui.adapter.AdapterFactory;
import com.gestionentreprise.gui.adapter.ServerDataAdapter;
import com.gestionentreprise.gui.handler.DataLoaderHandler;
import com.gestionentreprise.gui.listeners.DateFldTouchListener;
import com.gestionentreprise.model.Client;
import com.gestionentreprise.model.MoyenDePaiement;
import com.gestionentreprise.model.Prestation;
import com.gestionentreprise.model.TypePrestation;
import com.gestionentreprise.service.PrestationService;
import com.gestionentreprise.service.ServiceManager;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.service.message.IMessageListener;
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;
import com.gestionentreprise.utils.Action;
import com.gestionentreprise.utils.MessageUtils;

public class DetailPrestationActivity extends GeneralActivity<DetailPrestationData> implements IMessageListener {

    private EditText dateFld;
    private AutoCompleteTextView clientFld;
    private EditText tarifFld;
    private Spinner typePrestationFld;
    private Spinner moyenDePaiementFld;
    private CheckBox comptabiliseFld;

    private ServerDataAdapter<Client> clientAdapter;
    private ServerDataAdapter<MoyenDePaiement> moyenDePaiementAdapter;
    private ServerDataAdapter<TypePrestation> typePrestationAdapter;

    private Prestation prestation;
    private Client clientFromIntent;

    private PrestationService prestationService;
    private MessageService messageService;

    // TODO : trouver autre chose pour bloquer le listener à l'init
    private boolean initTypePrestation = false;
    private boolean initMoyenDePaiement = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_detail_prestation);

	dateFld = (EditText) findViewById(R.id.dateFld);
	clientFld = (AutoCompleteTextView) findViewById(R.id.clientFld);
	tarifFld = (EditText) findViewById(R.id.tarifFld);
	typePrestationFld = (Spinner) findViewById(R.id.typePrestationFld);
	moyenDePaiementFld = (Spinner) findViewById(R.id.detailPrestationMoyenDePaiementFld);
	comptabiliseFld = (CheckBox) findViewById(R.id.comptabiliseFld);

	dataLoader = new DataLoaderHandler<DetailPrestationActivity, DetailPrestationData>(this);

	initAdapter();

	initHandlers();

	initData();

	prestationService = ServiceManager.get(PrestationService.class);
	messageService = ServiceManager.get(MessageService.class);
	messageService.registerListener(this);
    }

    private void initHandlers() {
	dateFld.setOnTouchListener(new DateFldTouchListener(dateFld));

	typePrestationFld.setOnItemSelectedListener(new OnItemSelectedListener() {
	    @Override
	    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (!initTypePrestation) {
		    TypePrestation typePrestation = typePrestationAdapter.getData(position);
		    tarifFld.setText(df.format(typePrestation.getTarif()));
		} else {
		    initTypePrestation = false;
		}

	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
	    }
	});

	moyenDePaiementFld.setOnItemSelectedListener(new OnItemSelectedListener() {
	    @Override
	    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (!initMoyenDePaiement) {
		    MoyenDePaiement moyenDePaiement = moyenDePaiementAdapter.getData(position);
		    // Si Chèque
		    if (moyenDePaiement.getId() == 1) {
			comptabiliseFld.setChecked(true);
		    } else {
			comptabiliseFld.setChecked(false);
		    }
		} else {
		    initMoyenDePaiement = false;
		}
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
	    }
	});

	Button fermerFenetreBtn = (Button) findViewById(R.id.fermerBtn);
	fermerFenetreBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		returnToListePrestationActivity();
	    }
	});

	Button enregistrerBtn = (Button) findViewById(R.id.detailPrestationEnregistrerBtn);
	enregistrerBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		if (validate()) {
		    if (prestation != null) {
			startServerCommunication(Action.UPDATE);
		    } else {
			startServerCommunication(Action.ADD);
		    }
		}
	    }
	});

	Button nouveauClientBtn = (Button) findViewById(R.id.newClientBtn);
	nouveauClientBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		Intent secondeActivite = new Intent(DetailPrestationActivity.this, DetailClientActivity.class);
		secondeActivite.putExtra("nouveauClientNom", clientFld.getText().toString());
		startActivity(secondeActivite);
	    }
	});
    }

    private Prestation getPrestationDisplayed() throws ParseException {
	Prestation prestationAffiche = new Prestation();
	if (prestation != null) {
	    prestationAffiche.setId(prestation.getId());
	}
	prestationAffiche.setDate(sdf.parse(dateFld.getText().toString()));
	prestationAffiche.setClient(clientAdapter.getData(clientAdapter.getPosition(clientFld.getText().toString())));
	prestationAffiche.setTypePrestation(typePrestationAdapter.getData(typePrestationFld.getSelectedItemPosition()));
	prestationAffiche.setMoyenDePaiement(moyenDePaiementAdapter.getData(moyenDePaiementFld.getSelectedItemPosition()));
	prestationAffiche.setTarif(Double.valueOf(tarifFld.getText().toString()));
	prestationAffiche.setComptabilise(comptabiliseFld.isChecked());
	return prestationAffiche;
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
	clientAdapter = (ServerDataAdapter<Client>) AdapterFactory.buildServerDataAdapter(this, true);
	clientFld.setAdapter(clientAdapter);
	moyenDePaiementAdapter = (ServerDataAdapter<MoyenDePaiement>) AdapterFactory.buildServerDataAdapter(this, false);
	moyenDePaiementFld.setAdapter(moyenDePaiementAdapter);
	typePrestationAdapter = (ServerDataAdapter<TypePrestation>) AdapterFactory.buildServerDataAdapter(this, false);
	typePrestationFld.setAdapter(typePrestationAdapter);
    }

    private void initData() {
	initTypePrestation = true;
	initMoyenDePaiement = true;
	getDataFromIntent();

	if (getContext() != null && getContext().getListeClient() != null
	        && getContext().getListeTypePrestation() != null && getContext().getListeMoyenDePaiement() != null) {
	    setData(new DetailPrestationData(getContext().getListeTypePrestation(),
		                             getContext().getListeMoyenDePaiement(),
		                             getContext().getListeClient()));
	} else {
	    startServerCommunication(Action.READ);
	}
    }

    private void getDataFromIntent() {
	Intent intent = getIntent();
	Long idPrestation = intent.getLongExtra("idPrestation", -1);
	prestation = searchPrestionFromContext(idPrestation);
	if (getContext().getListeClient() != null) {
	    Long idClient = intent.getLongExtra("idClient", -1);
	    clientFromIntent = searchClientFromContext(idClient);
	}
    }

    @Override
    public void messageTriggered(Msg message) {
	MessageUtils.sendMessage(dataLoader, message.getCode(), message.getLibelle());
    }

    private Prestation searchPrestionFromContext(Long idPrestation) {
	GestionEntrepriseContext context = getContext();
	Prestation prestation = null;
	for (int i = 0; prestation == null && i < context.getListePrestation().size(); i++) {
	    if (context.getListePrestation().get(i).getId().equals(idPrestation)) {
		prestation = context.getListePrestation().get(i);
	    }
	}
	return prestation;
    }

    private Client searchClientFromContext(Long idClient) {
	GestionEntrepriseContext context = getContext();
	Client client = null;
	for (int i = 0; client == null && i < context.getListeClient().size(); i++) {
	    if (context.getListeClient().get(i).getId().equals(idClient)) {
		client = context.getListeClient().get(i);
	    }
	}
	return client;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.detail_prestation, menu);
	return true;
    }

    @Override
    public void serverCommunicationManagement(Handler receiver) throws Exception {
	if (getAction().equals(Action.READ)) {
	    DetailPrestationData data = prestationService.getDetailPrestationData();
	    updateContext(data);
	    MessageUtils.sendMessage(receiver, IMessage.SUCCES, data);
	} else {
	    Prestation prestationFromServer = prestationService.savePrestation(getPrestationDisplayed());
	    refreshPrestationIntoContext(prestation, prestationFromServer);
	    prestation = prestationFromServer;
	    MessageUtils.sendMessage(receiver, IMessage.SUCCES, null);
	    returnToListePrestationActivity();
	}
    }

    private void returnToListePrestationActivity() {
	Intent secondeActivite = new Intent(DetailPrestationActivity.this, ListePrestationActivity.class);
	startActivity(secondeActivite);
    }

    @Override
    public void setData(DetailPrestationData detailPrestationData) {
	clientAdapter.setData(detailPrestationData.getListeClient());
	typePrestationAdapter.setData(detailPrestationData.getListeTypePrestation());
	moyenDePaiementAdapter.setData(detailPrestationData.getListeMoyenDePaiement());

	if (clientFromIntent != null) {
	    clientFld.setText(clientFromIntent.getLibelle());
	}
	if (prestation != null) {
	    dateFld.setText(sdf.format(prestation.getDate()));
	    clientFld.setText(prestation.getClient().getLibelle());
	    tarifFld.setText(df.format(prestation.getTarif()));
	    comptabiliseFld.setChecked(prestation.getComptabilise());
	    moyenDePaiementFld.setSelection(moyenDePaiementAdapter.getPosition(prestation.getMoyenDePaiement()));
	    typePrestationFld.setSelection(typePrestationAdapter.getPosition(prestation.getTypePrestation()));

	} else {
	    dateFld.setText(sdf.format(new Date()));
	    comptabiliseFld.setChecked(true);
	}
    }

    @Override
    public void updateContext(DetailPrestationData data) {
	if (getContext() == null)
	    initContext();
	getContext().setListeTypePrestation(data.getListeTypePrestation());
	getContext().setListeMoyenDePaiement(data.getListeMoyenDePaiement());
	getContext().setListeClient(data.getListeClient());
    }

    @Override
    public void saveContext() {
	// Nothing : no criteria
    }

    private Boolean validate() {
	Boolean validationResult = Boolean.TRUE;
	if (dateFld.getText().toString().length() == 0) {
	    validationResult = Boolean.FALSE;
	    dateFld.setError("La date est obligatoire");
	}
	if (clientFld.getText().toString().length() == 0
	        || clientAdapter.getPosition(clientFld.getText().toString()) < 0) {
	    validationResult = Boolean.FALSE;
	    clientFld.setError("Le client est obligatoire (il doit être sélectionné)");
	}
	if (tarifFld.getText().toString().length() == 0) {
	    validationResult = Boolean.FALSE;
	    tarifFld.setError("Le tarif est obligatoire");
	}
	return validationResult;
    }
}
