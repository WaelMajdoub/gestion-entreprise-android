package com.gestionentreprise.gui.activity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.gestionentreprise.R;
import com.gestionentreprise.bean.DetailRdvData;
import com.gestionentreprise.bean.GestionEntrepriseContext;
import com.gestionentreprise.dao.RdvDAO;
import com.gestionentreprise.gui.adapter.AdapterFactory;
import com.gestionentreprise.gui.adapter.ServerDataAdapter;
import com.gestionentreprise.gui.handler.DataLoaderHandler;
import com.gestionentreprise.gui.listeners.DateFldTouchListener;
import com.gestionentreprise.gui.listeners.TimeFldTouchListener;
import com.gestionentreprise.model.Client;
import com.gestionentreprise.model.Rdv;
import com.gestionentreprise.model.TypePrestation;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.utils.Action;
import com.gestionentreprise.utils.JSONParser;
import com.gestionentreprise.utils.MessageUtils;
import com.gestionentreprise.utils.ServerConnectionUtils;

public class DetailRdvActivity extends GeneralActivity<DetailRdvData> {

    private EditText dateFld;
    private EditText heureFld;
    private AutoCompleteTextView clientFld;
    private Spinner typePrestationFld;
    private EditText descriptionFld;
    private EditText dureeFld;

    private ServerDataAdapter<Client> clientAdapter;
    private ServerDataAdapter<TypePrestation> typePrestationAdapter;

    private Rdv rdv;
    private Client clientFromIntent;

    private RdvDAO rdvDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_detail_rdv);

	dateFld = (EditText) findViewById(R.id.adrDateFld);
	heureFld = (EditText) findViewById(R.id.adrHeureFld);
	clientFld = (AutoCompleteTextView) findViewById(R.id.adrClientFld);
	typePrestationFld = (Spinner) findViewById(R.id.adrTypePrestationFld);
	descriptionFld = (EditText) findViewById(R.id.adrDescriptionFld);
	dureeFld = (EditText) findViewById(R.id.adrDureeFld);

	dataLoader = new DataLoaderHandler<DetailRdvActivity, DetailRdvData>(this);

	rdvDAO = new RdvDAO(this);
	initAdapter();

	initHandlers();

	initData();

	// Show the Up button in the action bar.
	setupActionBar();
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
	getMenuInflater().inflate(R.menu.detail_rdv, menu);
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

    private void initHandlers() {
	dateFld.setOnTouchListener(new DateFldTouchListener(dateFld));
	heureFld.setOnTouchListener(new TimeFldTouchListener(heureFld));

	Button fermerFenetreBtn = (Button) findViewById(R.id.adrFermerBtn);
	fermerFenetreBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		returnToCalendrierActivity();
	    }
	});

	Button enregistrerBtn = (Button) findViewById(R.id.adrEnregistrerBtn);
	enregistrerBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		if (validate()) {
		    if (rdv != null) {
			startServerCommunication(Action.UPDATE);
		    } else {
			startServerCommunication(Action.ADD);
		    }
		}
	    }
	});

	Button supprimerBtn = (Button) findViewById(R.id.adrSupprimerBtn);
	supprimerBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		if (rdv != null) {
		    startServerCommunication(Action.DELETE);
		}
	    }
	});

	Button nouveauClientBtn = (Button) findViewById(R.id.adrNewClientBtn);
	nouveauClientBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		Intent secondeActivite = new Intent(DetailRdvActivity.this, DetailClientActivity.class);
		secondeActivite.putExtra("nouveauClientNom", clientFld.getText().toString());
		secondeActivite.putExtra("detailRdvActivity", true);
		startActivity(secondeActivite);
	    }
	});
    }

    private Rdv getRdvDisplayed() throws ParseException {
	Rdv rdvAffiche = new Rdv();
	if (rdv != null) {
	    rdvAffiche.setId(rdv.getId());
	}
	String[] temps = heureFld.getText().toString().split(":");
	Calendar cal = Calendar.getInstance();
	Date date = sdf.parse(dateFld.getText().toString());
	cal.setTime(date);
	cal.set(Calendar.HOUR, Integer.parseInt(temps[0]));
	cal.set(Calendar.MINUTE, Integer.parseInt(temps[1]));
	rdvAffiche.setDateDebut(cal.getTime());
	rdvAffiche.setClient(clientAdapter.getData(clientAdapter.getPosition(clientFld.getText().toString())));
	rdvAffiche.setTypePrestation(typePrestationAdapter.getData(typePrestationFld.getSelectedItemPosition()));
	rdvAffiche.setDuree(Integer.valueOf(dureeFld.getText().toString()));
	rdvAffiche.setDescription(descriptionFld.getText().toString());
	return rdvAffiche;
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
	clientAdapter = (ServerDataAdapter<Client>) AdapterFactory.buildServerDataAdapter(this, true);
	clientFld.setAdapter(clientAdapter);
	typePrestationAdapter = (ServerDataAdapter<TypePrestation>) AdapterFactory.buildServerDataAdapter(this, false);
	typePrestationFld.setAdapter(typePrestationAdapter);
    }

    private void getDataFromIntent() {
	Intent intent = getIntent();
	Long idRdv = intent.getLongExtra("idRdv", -1);
	rdv = rdvDAO.get(idRdv);
	if (getContext() != null && getContext().getListeClient() != null) {
	    Long idClient = intent.getLongExtra("idClient", -1);
	    clientFromIntent = searchClientFromContext(idClient);
	}
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

    private void initData() {
	getDataFromIntent();

	if (getContext() != null && getContext().getListeClient() != null
	        && getContext().getListeTypePrestation() != null) {
	    setData(new DetailRdvData(getContext().getListeTypePrestation(), getContext().getListeClient()));
	} else {
	    startServerCommunication(Action.READ);
	}
    }

    private void returnToCalendrierActivity() {
	Intent secondeActivite = new Intent(DetailRdvActivity.this, CalendarActivity.class);
	startActivity(secondeActivite);
    }

    @Override
    public void updateContext(DetailRdvData data) {
	if (getContext() == null)
	    initContext();
	getContext().setListeTypePrestation(data.getListeTypePrestation());
	getContext().setListeClient(data.getListeClient());
    }

    @Override
    public void saveContext() {
	// Nothing : no criteria
    }

    @Override
    public void setData(DetailRdvData detailRdvData) {
	clientAdapter.setData(detailRdvData.getListeClient());
	typePrestationAdapter.setData(detailRdvData.getListeTypePrestation());

	if (clientFromIntent != null) {
	    clientFld.setText(clientFromIntent.getLibelle());
	}
	if (rdv != null) {
	    String date = sdf.format(rdv.getDateDebut());
	    String heure = sdfTime.format(rdv.getDateDebut());
	    dateFld.setText(date);
	    heureFld.setText(heure);
	    clientFld.setText(rdv.getClient().getLibelle());
	    typePrestationFld.setSelection(typePrestationAdapter.getPosition(rdv.getTypePrestation()));
	    dureeFld.setText(rdv.getDuree().toString());
	    descriptionFld.setText(rdv.getDescription());
	} else {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    cal.set(Calendar.HOUR_OF_DAY, 12);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    dateFld.setText(sdf.format(cal.getTime()));
	    heureFld.setText(sdfTime.format(cal.getTime()));
	}
    }

    @Override
    public void serverCommunicationManagement(Handler receiver) throws Exception {
	switch (getAction()) {
	case READ:
	    DetailRdvData data = retrieveDataFromServer(receiver);
	    updateContext(data);
	    MessageUtils.sendMessage(receiver, IMessage.SUCCES, data);
	    break;
	case ADD:
	case UPDATE:
	    rdv = saveRdv(getRdvDisplayed());
	    MessageUtils.sendMessage(receiver, IMessage.SUCCES, null);
	    returnToCalendrierActivity();
	    break;
	case DELETE:
	    if (rdv != null) {
		rdvDAO.delete(rdv.getId());
		MessageUtils.sendMessage(receiver, IMessage.SUCCES, null);
		returnToCalendrierActivity();
	    }
	default:
	    break;
	}
    }

    private Rdv saveRdv(Rdv rdv) {
	if (rdv.getId() != null) {
	    rdvDAO.update(rdv);
	} else {
	    long idRdv = rdvDAO.add(rdv);
	    rdv.setId(idRdv);
	}
	return rdv;
    }

    private DetailRdvData retrieveDataFromServer(final Handler receiver) throws Exception {
	Map<String, String> parametres = new HashMap<String, String>();
	StringBuilder sb = ServerConnectionUtils.executeRequest(ServerConnectionUtils.getComptabiliteServerUrl()
	        + "rdv/detail", parametres);
	MessageUtils.sendMessage(receiver, IMessage.IN_PROGRESS, "Décodage");
	return JSONParser.parseJSONForDetailRdvData(receiver, new JSONObject(sb.toString()));
    }

    private Boolean validate() {
	Boolean validationResult = Boolean.TRUE;
	if (dateFld.getText().toString().length() == 0) {
	    validationResult = Boolean.FALSE;
	    dateFld.setError("La date est obligatoire");
	}
	if (heureFld.getText().toString().length() == 0) {
	    validationResult = Boolean.FALSE;
	    heureFld.setError("L'heure est obligatoire");
	}
	return validationResult;
    }
}
