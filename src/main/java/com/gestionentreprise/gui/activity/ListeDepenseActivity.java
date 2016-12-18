package com.gestionentreprise.gui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gestionentreprise.R;
import com.gestionentreprise.bean.ListeDepenseData;
import com.gestionentreprise.bean.criteria.ListeDepenseCriteria;
import com.gestionentreprise.gui.handler.DataLoaderHandler;
import com.gestionentreprise.gui.listeners.DateFldTouchListener;
import com.gestionentreprise.model.Depense;
import com.gestionentreprise.model.TotalCategorie;
import com.gestionentreprise.service.DepenseService;
import com.gestionentreprise.service.ServiceManager;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.service.message.IMessageListener;
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;
import com.gestionentreprise.utils.Action;
import com.gestionentreprise.utils.MessageUtils;

public class ListeDepenseActivity extends GeneralActivity<ListeDepenseData> implements IMessageListener {

    private EditText depuisLeFld;
    private EditText jusquaFld;
    private TableLayout totauxTable;
    private Button ajouterDepenseBtn;
    private Button rafraichirBtn;
    private Button accueilBtn;
    private TextView totalFld;
    private TableLayout depenseTable;

    private Depense depenseToDelete;

    private DepenseService depenseService;
    private MessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_liste_depense);

	depuisLeFld = (EditText) findViewById(R.id.aldDepuisLeFld);
	jusquaFld = (EditText) findViewById(R.id.aldJusquaFld);
	totauxTable = (TableLayout) findViewById(R.id.totauxDepenseTable);
	ajouterDepenseBtn = (Button) findViewById(R.id.aldAjouterDepenseBtn);
	rafraichirBtn = (Button) findViewById(R.id.aldRafraichirBtn);
	accueilBtn = (Button) findViewById(R.id.aldAccueilBtn);
	totalFld = (TextView) findViewById(R.id.aldTotalFld);
	depenseTable = (TableLayout) findViewById(R.id.aldDepenseTable);

	dataLoader = new DataLoaderHandler<ListeDepenseActivity, ListeDepenseData>(this);

	initHandlers();

	initData();

	depenseService = ServiceManager.get(DepenseService.class);
	messageService = ServiceManager.get(MessageService.class);
	messageService.registerListener(this);

	// Show the Up button in the action bar.
	setupActionBar();
    }

    private void getDataFromIntent() {
	Intent intent = getIntent();
	if (intent.getStringExtra("depuisLe") != null) {
	    depuisLeFld.setText(intent.getStringExtra("depuisLe"));
	}
	if (intent.getStringExtra("jusqua") != null) {
	    jusquaFld.setText(intent.getStringExtra("jusqua"));
	}
    }

    private void initData() {
	initDates();

	getDataFromIntent();

	if (getContext() != null && getContext().getListeTotalTypeDepense() != null
	        && getContext().getListeDepense() != null && getContext().getListeTypeDepense() != null) {
	    setData(buildListeDepenseDataFromContext());
	} else {
	    startServerCommunication(Action.READ);
	}
    }

    private void initDates() {
	if (getContext() != null && getContext().getListeDepenseCriteria() != null) {
	    depuisLeFld.setText(sdf.format(getContext().getListeDepenseCriteria().getDepuisLe()));
	    jusquaFld.setText(sdf.format(getContext().getListeDepenseCriteria().getJusqua()));
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

    private ListeDepenseData buildListeDepenseDataFromContext() {
	return new ListeDepenseData(getContext().getListeTotalTypeDepense(),
	                            getContext().getListeTypeDepense(),
	                            getContext().getListeDepense());
    }

    private void initHandlers() {
	depuisLeFld.setOnTouchListener(new DateFldTouchListener(depuisLeFld));
	jusquaFld.setOnTouchListener(new DateFldTouchListener(jusquaFld));

	ajouterDepenseBtn.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		saveContext();
		// Le premier paramètre est le nom de l'activité actuelle
		// Le second est le nom de l'activité de destination
		Intent secondeActivite = new Intent(ListeDepenseActivity.this, DetailDepenseActivity.class);
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
	    public void onClick(View arg0) {
		Intent secondeActivite = new Intent(ListeDepenseActivity.this, AccueilActivity.class);
		startActivity(secondeActivite);
	    }
	});

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
	getMenuInflater().inflate(R.menu.liste_depense, menu);
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
    public void updateContext(ListeDepenseData data) {
	if (getContext() == null)
	    initContext();
	getContext().setListeTotalTypeDepense(data.getListeTotalTypeDepense());
	getContext().setListeDepense(data.getListeDepense());
	getContext().setListeTypeDepense(data.getListeTypeDepense());
    }

    @Override
    public void saveContext() {
	try {
	    ListeDepenseCriteria criteres = new ListeDepenseCriteria();
	    criteres.setDepuisLe(sdf.parse(depuisLeFld.getText().toString()));
	    criteres.setJusqua(sdf.parse(jusquaFld.getText().toString()));
	    getContext().setListeDepenseCriteria(criteres);
	} catch (ParseException e) {
	    Message msg = Message.obtain();
	    msg.arg1 = IMessage.PARSE_EXCEPTION;
	    msg.obj = e;
	    MessageUtils.logException(e, "Parse exception");
	    dataLoader.sendMessage(msg);
	}
    }

    @Override
    public void setData(ListeDepenseData data) {
	displayTotaux(data.getListeTotalTypeDepense());
	displayDepenses(data.getListeDepense());
    }

    private void displayTotaux(List<TotalCategorie> listeTotalTypeDepense) {
	int index = 1;
	double total = 0;
	int nbViews = totauxTable.getChildCount();
	if (nbViews > 2) {
	    totauxTable.removeViews(1, nbViews - 2); // On garde la derniere
		                                     // (Total)
	}
	for (TotalCategorie totalTypeDepense : listeTotalTypeDepense) {
	    TableRow row = new TableRow(this);
	    totauxTable.addView(row, index);

	    TextView typeDepenseFld = new TextView(this, null, R.style.rowStyle);
	    typeDepenseFld.setText(totalTypeDepense.getCategorie());
	    row.addView(typeDepenseFld);

	    TextView totalStr = new TextView(this, null, R.style.rowStyle);
	    Double totalNumber = Double.parseDouble(totalTypeDepense.getTotal());
	    totalStr.setText(df.format(totalNumber));
	    row.addView(totalStr);

	    total += totalNumber;

	    index++;
	}
	totalFld.setText(df.format(total));
    }

    private void displayDepenses(List<Depense> listeDepense) {
	int index = 1;
	int nbRows = depenseTable.getChildCount();
	if (nbRows > 1) {
	    depenseTable.removeViews(1, nbRows - 1);
	}
	for (Depense depense : listeDepense) {
	    depenseTable.addView(buildRowDepense(depense), index);
	}
    }

    private TableRow buildRowDepense(Depense depense) {
	TableRow row = new TableRow(this);
	SimpleDateFormat sdfListeDepense = new SimpleDateFormat("dd/MM", Locale.FRENCH);

	TextView dateFld = new TextView(this, null, R.style.rowStyle);
	dateFld.setText(sdfListeDepense.format(depense.getDate()));
	row.addView(dateFld);

	TextView typeFld = new TextView(this, null, R.style.rowStyle);
	typeFld.setText(depense.getTypeDepense().getLibelle());
	row.addView(typeFld);

	TextView tarifFld = new TextView(this, null, R.style.rowStyle);
	tarifFld.setText(df.format(depense.getTarif()));
	row.addView(tarifFld);

	ImageButton actionBtn = new ImageButton(this, null, android.R.style.Widget_Button_Small);
	actionBtn.setImageResource(android.R.drawable.ic_input_get);
	actionBtn.setOnClickListener(new ActionClickListener(this, depense));
	row.addView(actionBtn);

	return row;
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
	    ListeDepenseData data = depenseService.getListeDepenseData(depuisLe, jusqua);
	    updateContext(data);
	    MessageUtils.sendMessage(receiver, IMessage.SUCCES, data);
	} else {

	    if (depenseToDelete != null) {
		Boolean result = depenseService.deleteDepense(depenseToDelete.getId());
		if (result == Boolean.TRUE) {
		    Depense depenseDeleted = null;
		    for (Depense depense : getContext().getListeDepense()) {
			if (depense.equals(depenseToDelete)) {
			    depenseDeleted = depense;
			    break;
			}
		    }
		    refreshDepenseIntoContext(depenseDeleted, null);
		    MessageUtils.sendMessage(receiver, IMessage.SUCCES, buildListeDepenseDataFromContext());
		} else {
		    MessageUtils.sendMessage(receiver, IMessage.CONNECTION_EXCEPTION, null);
		}
	    } else {
		MessageUtils.sendMessage(receiver, IMessage.DATA_NOT_FOUND, null);
	    }

	}
    }

    private class ActionClickListener implements OnClickListener {
	private Depense depense;
	private ListeDepenseActivity parent;

	public ActionClickListener(ListeDepenseActivity parent, Depense depense) {
	    this.depense = depense;
	    this.parent = parent;
	}

	public void onClick(View arg0) {

	    Builder builder = new Builder(parent);
	    builder.setTitle("Choix de l'action à réaliser");
	    builder.setMessage("Dépense de " + depense.getTypeDepense().getLibelle() + " à " + depense.getTarif() + "€");
	    builder.setPositiveButton(R.string.modifier, new ModifierClickListener(parent, depense));
	    builder.setNegativeButton(R.string.supprimer, new SupprimerClickListener(depense));
	    builder.show();
	}
    }

    private class ModifierClickListener implements DialogInterface.OnClickListener {
	private ListeDepenseActivity parent;
	private Depense depense;

	public ModifierClickListener(ListeDepenseActivity parent, Depense depense) {
	    this.parent = parent;
	    this.depense = depense;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	    parent.saveContext();
	    Intent secondeActivite = new Intent(parent, DetailDepenseActivity.class);
	    // On rajoute un extra
	    secondeActivite.putExtra("idDepense", depense.getId());
	    // Puis on lance l'intent !
	    parent.startActivity(secondeActivite);
	}
    }

    private class SupprimerClickListener implements DialogInterface.OnClickListener {
	private Depense depenseToDelete;

	public SupprimerClickListener(Depense prestationToDelete) {
	    this.depenseToDelete = prestationToDelete;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	    ListeDepenseActivity.this.depenseToDelete = this.depenseToDelete;
	    startServerCommunication(Action.DELETE);
	};
    }
}
