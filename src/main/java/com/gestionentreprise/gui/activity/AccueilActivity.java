package com.gestionentreprise.gui.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.gestionentreprise.R;
import com.gestionentreprise.utils.ServerConnectionUtils;

public class AccueilActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_accueil);

	Button prestationBtn = (Button) findViewById(R.id.aaPrestationBtn);
	Button depenseBtn = (Button) findViewById(R.id.aaDepenseBtn);
	Button suiviBtn = (Button) findViewById(R.id.aaSuiviBtn);
	Button calendrierBtn = (Button) findViewById(R.id.aaCalendrierBtn);

	prestationBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		Intent secondeActivite = new Intent(AccueilActivity.this, ListePrestationActivity.class);
		startActivity(secondeActivite);
	    }
	});

	depenseBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		Intent secondeActivite = new Intent(AccueilActivity.this, ListeDepenseActivity.class);
		startActivity(secondeActivite);
	    }
	});

	suiviBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		Intent secondeActivite = new Intent(AccueilActivity.this, SuiviActivity.class);
		startActivity(secondeActivite);
	    }
	});

	calendrierBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent secondeActivite = new Intent(AccueilActivity.this, CalendarActivity.class);
		startActivity(secondeActivite);
	    }
	});

	initAssets();
    }

    private void initAssets() {
	// Initialisation des assets
	Properties properties = new Properties();
	InputStream in = null;
	try {
	    in = getAssets().open("server.properties");
	} catch (IOException e) {
	    Toast.makeText(AccueilActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
	}
	try {
	    if (in != null)
		properties.load(in);
	} catch (IOException e) {
	    Toast.makeText(AccueilActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
	}
	ServerConnectionUtils.setProperties(properties);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.accueil, menu);
	return true;
    }

}
