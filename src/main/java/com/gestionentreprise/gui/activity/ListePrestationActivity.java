package com.gestionentreprise.gui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gestionentreprise.R;
import com.gestionentreprise.bean.ListePrestationData;
import com.gestionentreprise.bean.criteria.ListePrestationCriteria;
import com.gestionentreprise.gui.handler.DataLoaderHandler;
import com.gestionentreprise.gui.listeners.DateFldTouchListener;
import com.gestionentreprise.model.Prestation;
import com.gestionentreprise.model.TotalCategorie;
import com.gestionentreprise.service.PrestationService;
import com.gestionentreprise.service.ServiceManager;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.service.message.IMessageListener;
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;
import com.gestionentreprise.utils.Action;
import com.gestionentreprise.utils.MessageUtils;

public class ListePrestationActivity extends GeneralActivity<ListePrestationData> implements IMessageListener {

    private EditText depuisLeFld;
    private EditText jusquaFld;
    private TableLayout totauxTable;
    private Button ajouterPrestationBtn;
    private Button rafraichirBtn;
    private Button accueilBtn;
    private TextView totalFld;
    private TableLayout prestationTable;

    private Prestation prestationToDelete;

    private PrestationService prestationService;
    private MessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_liste_prestation);

	depuisLeFld = (EditText) findViewById(R.id.depuisLeFld);
	jusquaFld = (EditText) findViewById(R.id.jusquaFld);
	totauxTable = (TableLayout) findViewById(R.id.totauxTable);
	ajouterPrestationBtn = (Button) findViewById(R.id.ajouterPrestationBtn);
	rafraichirBtn = (Button) findViewById(R.id.rafraichirBtn);
	totalFld = (TextView) findViewById(R.id.totalFld);
	prestationTable = (TableLayout) findViewById(R.id.prestationTable);
	accueilBtn = (Button) findViewById(R.id.alpAccueilBtn);

	dataLoader = new DataLoaderHandler<ListePrestationActivity, ListePrestationData>(this);

	initHandlers();

	initData();

	prestationService = ServiceManager.get(PrestationService.class);
	messageService = ServiceManager.get(MessageService.class);
	messageService.registerListener(this);
    }

    private void initHandlers() {
	depuisLeFld.setOnTouchListener(new DateFldTouchListener(depuisLeFld));
	jusquaFld.setOnTouchListener(new DateFldTouchListener(jusquaFld));

	ajouterPrestationBtn.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		saveContext();
		// Le premier paramètre est le nom de l'activité actuelle
		// Le second est le nom de l'activité de destination
		Intent secondeActivite = new Intent(ListePrestationActivity.this, DetailPrestationActivity.class);
		// On rajoute un extra
		// secondeActivite.putExtra(AGE, 31);
		// Puis on lance l'intent !
		startActivity(secondeActivite);
	    }
	});

	rafraichirBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		startServerCommunication(Action.READ);
	    }
	});

	accueilBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent secondeActivite = new Intent(ListePrestationActivity.this, AccueilActivity.class);
		startActivity(secondeActivite);
	    }
	});
    }

    private void initData() {
	initDates();

	if (getContext() != null && getContext().getListeTotalMoyenDePaiement() != null
	        && getContext().getListePrestation() != null && getContext().getListeMoyenDePaiement() != null) {
	    setData(buildListePrestationDataFromContext());
	} else {
	    startServerCommunication(Action.READ);
	}
    }

    private void initDates() {
	if (getContext() != null && getContext().getListePrestationCriteria() != null) {
	    depuisLeFld.setText(sdf.format(getContext().getListePrestationCriteria().getDepuisLe()));
	    jusquaFld.setText(sdf.format(getContext().getListePrestationCriteria().getJusqua()));
	} else {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    int year = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH);

	    Calendar depuisLeCal = Calendar.getInstance();
	    depuisLeCal.set(year, month, 1);

	    depuisLeFld.setText(sdf.format(depuisLeCal.getTime()));
	    jusquaFld.setText(sdf.format(cal.getTime()));
	}
    }

    @Override
    public void messageTriggered(Msg message) {
	MessageUtils.sendMessage(dataLoader, message.getCode(), message.getLibelle());
    }

    @Override
    public void serverCommunicationManagement(Handler receiver) throws Exception {
	if (getAction().equals(Action.READ)) {
	    String depuisLe = depuisLeFld.getText().toString();
	    String jusqua = jusquaFld.getText().toString();
	    ListePrestationData data = prestationService.getListePrestationData(depuisLe, jusqua);
	    updateContext(data);
	    MessageUtils.sendMessage(receiver, IMessage.SUCCES, data);
	} else {
	    if (prestationToDelete != null) {
		Boolean result = prestationService.delete(prestationToDelete.getId());
		if (result == Boolean.TRUE) {
		    Prestation prestationDeleted = null;
		    for (Prestation prestation : getContext().getListePrestation()) {
			if (prestation.equals(prestationToDelete)) {
			    prestationDeleted = prestation;
			    break;
			}
		    }
		    refreshPrestationIntoContext(prestationDeleted, null);
		    MessageUtils.sendMessage(receiver, IMessage.SUCCES, buildListePrestationDataFromContext());
		} else {
		    MessageUtils.sendMessage(receiver, IMessage.CONNECTION_EXCEPTION, null);
		}
	    } else {
		MessageUtils.sendMessage(receiver, IMessage.DATA_NOT_FOUND, null);
	    }
	}
    }

    private ListePrestationData buildListePrestationDataFromContext() {
	return new ListePrestationData(getContext().getListeTotalMoyenDePaiement(),
	                               getContext().getListeMoyenDePaiement(),
	                               getContext().getListePrestation());
    }

    @Override
    public void updateContext(ListePrestationData data) {
	if (getContext() == null)
	    initContext();
	getContext().setListeTotalMoyenDePaiement(data.getListeTotalMoyenDePaiement());
	getContext().setListePrestation(data.getListePrestation());
	getContext().setListeMoyenDePaiement(data.getListeMoyenDePaiement());
    }

    @Override
    public void saveContext() {
	try {
	    ListePrestationCriteria criteres = new ListePrestationCriteria();
	    criteres.setDepuisLe(sdf.parse(depuisLeFld.getText().toString()));
	    criteres.setJusqua(sdf.parse(jusquaFld.getText().toString()));
	    getContext().setListePrestationCriteria(criteres);
	} catch (ParseException e) {
	    Message msg = Message.obtain();
	    msg.arg1 = IMessage.PARSE_EXCEPTION;
	    msg.obj = e;
	    MessageUtils.logException(e, "Parse exception");
	    dataLoader.sendMessage(msg);
	}
    }

    @Override
    public void setData(ListePrestationData listePrestationData) {
	displayTotaux(listePrestationData.getListeTotalMoyenDePaiement());
	displayPrestations(listePrestationData.getListePrestation());
    }

    private void displayTotaux(List<TotalCategorie> listeTotalMoyenDePaiement) {
	int index = 1;
	double total = 0;
	int nbViews = totauxTable.getChildCount();
	if (nbViews > 2) {
	    totauxTable.removeViews(1, nbViews - 2); // On garde la derniere
		                                     // (Total)
	}
	for (TotalCategorie totalMoyenDePaiement : listeTotalMoyenDePaiement) {
	    TableRow row = new TableRow(this);
	    totauxTable.addView(row, index);

	    TextView moyenDePaiementFld = new TextView(this, null, R.style.rowStyle);
	    moyenDePaiementFld.setText(totalMoyenDePaiement.getCategorie());
	    row.addView(moyenDePaiementFld);

	    TextView totalStr = new TextView(this, null, R.style.rowStyle);
	    Double totalNumber = Double.parseDouble(totalMoyenDePaiement.getTotal());
	    totalStr.setText(df.format(totalNumber));
	    row.addView(totalStr);

	    total += totalNumber;

	    index++;
	}
	totalFld.setText(df.format(total));
    }

    private void displayPrestations(List<Prestation> listePrestation) {
	int index = 1;
	int nbRows = prestationTable.getChildCount();
	if (nbRows > 1) {
	    prestationTable.removeViews(1, nbRows - 1);
	}
	for (Prestation prestation : listePrestation) {
	    prestationTable.addView(buildRowPrestation(prestation), index);
	}
    }

    private TableRow buildRowPrestation(Prestation prestation) {
	TableRow row = new TableRow(this);
	SimpleDateFormat sdfListePrestation = new SimpleDateFormat("dd/MM", Locale.FRENCH);

	TextView dateFld = new TextView(this, null, R.style.rowStyle);
	dateFld.setText(sdfListePrestation.format(prestation.getDate()));
	row.addView(dateFld);

	TextView clientFld = new TextView(this, null, R.style.rowStyle);
	clientFld.setText(prestation.getClient().getNom());
	row.addView(clientFld);

	TextView tarifFld = new TextView(this, null, R.style.rowStyle);
	tarifFld.setText(df.format(prestation.getTarif()));
	row.addView(tarifFld);

	TextView moyenFld = new TextView(this, null, R.style.rowStyle);
	moyenFld.setText(prestation.getMoyenDePaiement().getLibelle().substring(0, 1));
	row.addView(moyenFld);

	ImageButton actionBtn = new ImageButton(this, null, android.R.style.Widget_Button_Small);
	actionBtn.setImageResource(android.R.drawable.ic_input_get);
	actionBtn.setOnClickListener(new ActionClickListener(this, prestation));
	row.addView(actionBtn);

	return row;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.liste_prestation, menu);
	return true;
    }

    private class ActionClickListener implements OnClickListener {
	private Prestation prestation;
	private ListePrestationActivity parent;

	public ActionClickListener(ListePrestationActivity parent, Prestation prestation) {
	    this.prestation = prestation;
	    this.parent = parent;
	}

	public void onClick(View arg0) {

	    Builder builder = new Builder(parent);
	    builder.setTitle("Choix de l'action à réaliser");
	    builder.setMessage("Prestation de " + prestation.getClient().getNom() + " à " + prestation.getTarif() + "€");
	    builder.setPositiveButton(R.string.modifier, new ModifierClickListener(parent, prestation));
	    builder.setNegativeButton(R.string.supprimer, new SupprimerClickListener(prestation));
	    builder.show();
	}
    }

    private class ModifierClickListener implements DialogInterface.OnClickListener {
	private ListePrestationActivity parent;
	private Prestation prestation;

	public ModifierClickListener(ListePrestationActivity parent, Prestation prestation) {
	    this.parent = parent;
	    this.prestation = prestation;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	    parent.saveContext();
	    Intent secondeActivite = new Intent(parent, DetailPrestationActivity.class);
	    // On rajoute un extra
	    secondeActivite.putExtra("idPrestation", prestation.getId());
	    // Puis on lance l'intent !
	    parent.startActivity(secondeActivite);
	}
    }

    private class SupprimerClickListener implements DialogInterface.OnClickListener {
	private Prestation prestationToDelete;

	public SupprimerClickListener(Prestation prestationToDelete) {
	    this.prestationToDelete = prestationToDelete;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	    ListePrestationActivity.this.prestationToDelete = this.prestationToDelete;
	    startServerCommunication(Action.DELETE);
	};
    }

}
