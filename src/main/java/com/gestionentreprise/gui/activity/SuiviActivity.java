package com.gestionentreprise.gui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.gestionentreprise.bean.SuiviData;
import com.gestionentreprise.gui.chart.Chart;
import com.gestionentreprise.gui.handler.DataLoaderHandler;
import com.gestionentreprise.gui.listeners.DateFldTouchListener;
import com.gestionentreprise.model.SuiviElement;
import com.gestionentreprise.service.ServiceManager;
import com.gestionentreprise.service.SuiviService;
import com.gestionentreprise.service.message.IMessage;
import com.gestionentreprise.service.message.IMessageListener;
import com.gestionentreprise.service.message.MessageService;
import com.gestionentreprise.service.message.Msg;
import com.gestionentreprise.utils.Action;
import com.gestionentreprise.utils.MessageUtils;

public class SuiviActivity extends GeneralActivity<SuiviData> implements IMessageListener {
    private Chart chart;
    private EditText deFld;
    private EditText aFld;
    private Button suiviBtn;
    private Button accueilBtn;

    private SuiviService suiviService;
    private MessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_suivi);

	deFld = (EditText) findViewById(R.id.asDeFld);
	aFld = (EditText) findViewById(R.id.asAFld);
	suiviBtn = (Button) findViewById(R.id.asSuiviBtn);
	accueilBtn = (Button) findViewById(R.id.asAccueilBtn);

	dataLoader = new DataLoaderHandler<SuiviActivity, SuiviData>(this);

	initHandlers();

	initDates();

	chart = new Chart(SuiviActivity.this);
	chart.onCreateAction();

	suiviService = ServiceManager.get(SuiviService.class);
	messageService = ServiceManager.get(MessageService.class);
	messageService.registerListener(this);

	// Show the Up button in the action bar.
	setupActionBar();
    }

    private void initHandlers() {
	deFld.setOnTouchListener(new DateFldTouchListener(deFld));
	aFld.setOnTouchListener(new DateFldTouchListener(aFld));

	suiviBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		startServerCommunication(Action.READ);
	    }
	});

	accueilBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent secondeActivite = new Intent(SuiviActivity.this, AccueilActivity.class);
		startActivity(secondeActivite);
	    }
	});
    }

    private void initDates() {
	Calendar cal = Calendar.getInstance();
	cal.setTime(new Date());
	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);

	Calendar depuisLeCal = Calendar.getInstance();
	depuisLeCal.set(year, month, 1);
	depuisLeCal.add(Calendar.MONTH, -12);

	deFld.setText(sdf.format(depuisLeCal.getTime()));
	aFld.setText(sdf.format(cal.getTime()));
    }

    @Override
    protected void onResume() {
	super.onResume();
	if (chart != null) {
	    chart.onResumeAction();
	}
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
	getMenuInflater().inflate(R.menu.suivi, menu);
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
    public void updateContext(SuiviData data) {
	// Nothing
    }

    @Override
    public void saveContext() {
	// Nothing
    }

    @Override
    public void setData(SuiviData data) {
	String[] titles = new String[] { "CA", "CA - charges (RSI)", "Salaire Net" };
	double[] x = new double[data.getListeSuiviElement().size()];
	List<String> date = new ArrayList<String>(data.getListeSuiviElement().size());
	double[] chiffreAffaire = new double[data.getListeSuiviElement().size()];
	double[] chiffreAffaireHorsRSI = new double[data.getListeSuiviElement().size()];
	double[] salaireNet = new double[data.getListeSuiviElement().size()];
	int index = 0;
	for (SuiviElement suiviElement : data.getListeSuiviElement()) {
	    x[index] = index + 1;
	    chiffreAffaire[index] = suiviElement.getChiffreAffaireBrut();
	    chiffreAffaireHorsRSI[index] = suiviElement.getChiffreAffaireHorsRSI();
	    salaireNet[index] = suiviElement.getChiffreAffaireNet();
	    date.add(sdfMonth.format(suiviElement.getDate()));
	    index++;
	}

	List<double[]> values = new ArrayList<double[]>();
	values.add(chiffreAffaire);
	values.add(chiffreAffaireHorsRSI);
	values.add(salaireNet);

	chart.setData(titles, date, values);
    }

    @Override
    public void messageTriggered(Msg message) {
	MessageUtils.sendMessage(dataLoader, message.getCode(), message.getLibelle());
    }

    @Override
    public void serverCommunicationManagement(Handler receiver) throws Exception {
	String depuisLe = deFld.getText().toString();
	String jusqua = aFld.getText().toString();
	SuiviData data = suiviService.getSuiviData(depuisLe, jusqua);
	MessageUtils.sendMessage(receiver, IMessage.SUCCES, data);
    }

}
