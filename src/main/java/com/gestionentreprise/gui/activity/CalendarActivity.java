package com.gestionentreprise.gui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gestionentreprise.R;
import com.gestionentreprise.dao.RdvDAO;
import com.gestionentreprise.gui.listeners.DateFldTouchListener;
import com.gestionentreprise.model.Client;
import com.gestionentreprise.model.Rdv;

public class CalendarActivity extends Activity implements IOnDateSetCalendarListener {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
    private RdvDAO rdvDAO;
    private EditText dateFld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_calendar);
	// Show the Up button in the action bar.
	setupActionBar();

	rdvDAO = new RdvDAO(this);
	dateFld = (EditText) findViewById(R.id.acDateFld);
	dateFld.setFocusable(false);

	initHandlers();

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	    initScrollViewForAndroid4();
	}

	initData();
    }

    private void initData() {
	Date now = new Date();

	dateFld.setText(sdf.format(now));

	displayWeeklyRdvForDate(now);
    }

    private void retrieveRdvFromDatabaseAndDisplay(Date dateDebut, Date dateFin) {
	Calendar cal = Calendar.getInstance();
	List<Rdv> listeRdv = rdvDAO.getAllByPeriode(dateDebut, dateFin);
	// TODO : A SUPPRIMER
	// if (listeRdv.size() == 0) {
	// Client client = new Client();
	// client.setNom("Ponsolle");
	// cal.set(2013, 05, 24, 10, 00);
	// rdvDAO.add(buildRdv(client, cal.getTime(), 240));
	//
	// Client client2 = new Client();
	// client2.setNom("Francis");
	// cal.set(2013, 05, 25, 13, 15);
	// rdvDAO.add(buildRdv(client2, cal.getTime(), 120));
	//
	// Client client3 = new Client();
	// client3.setNom("Coppins");
	// cal.set(2013, 05, 26, 11, 00);
	// rdvDAO.add(buildRdv(client3, cal.getTime(), 150));
	//
	// Client client4 = new Client();
	// client4.setNom("Azoulay");
	// cal.set(2013, 05, 27, 8, 30);
	// rdvDAO.add(buildRdv(client4, cal.getTime(), 90));
	//
	// Client client5 = new Client();
	// client5.setNom("Ferriez");
	// cal.set(2013, 05, 27, 13, 30);
	// rdvDAO.add(buildRdv(client5, cal.getTime(), 150));
	//
	// Client client6 = new Client();
	// client6.setNom("Leveque");
	// cal.set(2013, 05, 28, 14, 30);
	// rdvDAO.add(buildRdv(client6, cal.getTime(), 240));
	//
	// Client client7 = new Client();
	// client7.setNom("Banks");
	// cal.set(2013, 05, 30, 11, 00);
	// rdvDAO.add(buildRdv(client7, cal.getTime(), 90));
	//
	// Client client8 = new Client();
	// client8.setNom("Lamblot");
	// cal.set(2013, 05, 30, 12, 30);
	// rdvDAO.add(buildRdv(client8, cal.getTime(), 60));
	//
	// Client client9 = new Client();
	// client9.setNom("Gardeau");
	// cal.set(2013, 05, 30, 13, 00);
	// rdvDAO.add(buildRdv(client9, cal.getTime(), 90));
	// listeRdv = rdvDAO.getAll();
	//
	// }

	for (Rdv rdv : listeRdv) {
	    displayRdv(rdv);
	}
    }

    private Rdv buildRdv(Client client, Date dateDebut, Integer duree) {
	Rdv rdv = new Rdv();
	rdv.setClient(client);
	rdv.setDateDebut(dateDebut);
	rdv.setDuree(duree);
	return rdv;
    }

    private void displayWeeklyRdvForDate(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

	setCalendarHeaderCell(R.id.acMondayNumValue, cal);
	Date dateDebut = cal.getTime();
	cal.add(Calendar.HOUR, 24);
	setCalendarHeaderCell(R.id.acTuesdayNumValue, cal);
	cal.add(Calendar.HOUR, 24);
	setCalendarHeaderCell(R.id.acWednesdayNumValue, cal);
	cal.add(Calendar.HOUR, 24);
	setCalendarHeaderCell(R.id.acThursdayNumValue, cal);
	cal.add(Calendar.HOUR, 24);
	setCalendarHeaderCell(R.id.acFridayNumValue, cal);
	cal.add(Calendar.HOUR, 24);
	setCalendarHeaderCell(R.id.acSaturdayNumValue, cal);
	cal.add(Calendar.HOUR, 24);
	setCalendarHeaderCell(R.id.acSundayNumValue, cal);
	Date dateFin = cal.getTime();

	retrieveRdvFromDatabaseAndDisplay(dateDebut, dateFin);
    }

    private void setCalendarHeaderCell(int textViewId, Calendar cal) {
	TextView cellNumValue = (TextView) findViewById(textViewId);
	cellNumValue.setText(String.valueOf(cal.get(Calendar.DATE)));
    }

    @Override
    public void onCalendarDateSet(int year, int monthOfYear, int dayOfMonth) {
	Calendar cal = Calendar.getInstance();
	cal.set(year, monthOfYear, dayOfMonth);
	clearWeekView();
	displayWeeklyRdvForDate(cal.getTime());
    }

    private void initHandlers() {
	Button accueilBtn = (Button) findViewById(R.id.acAccueilBtn);
	accueilBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		Intent secondeActivite = new Intent(CalendarActivity.this, AccueilActivity.class);
		startActivity(secondeActivite);
	    }
	});

	RelativeLayout dimancheLayout = (RelativeLayout) findViewById(R.id.acDimancheRelativeLayout);
	dimancheLayout.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Toast.makeText(CalendarActivity.this, "Layout", Toast.LENGTH_LONG).show();
	    }
	});

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	    initHandlersAndroid3();
	}

	dateFld.setOnTouchListener(new DateFldTouchListener(dateFld, this));

	Button suivantBtn = (Button) findViewById(R.id.acSuivantBtn);
	suivantBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		try {
		    addDaysToDateFld(7);
		} catch (Exception e) {
		    Toast.makeText(CalendarActivity.this, "Parse Date Error", Toast.LENGTH_SHORT).show();
		}

	    }
	});
	Button precedentBtn = (Button) findViewById(R.id.acPrecedentBtn);
	precedentBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		try {
		    addDaysToDateFld(-7);
		} catch (Exception e) {
		    Toast.makeText(CalendarActivity.this, "Parse Date Error", Toast.LENGTH_SHORT).show();
		}
	    }
	});

	Button nouveauBtn = (Button) findViewById(R.id.acNouveauBtn);
	nouveauBtn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		Intent secondeActivite = new Intent(CalendarActivity.this, DetailRdvActivity.class);
		startActivity(secondeActivite);
	    }
	});
    }

    private void addDaysToDateFld(int value) throws ParseException {
	Date date = sdf.parse(dateFld.getText().toString());
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(Calendar.DATE, value);
	dateFld.setText(sdf.format(cal.getTime()));
	onCalendarDateSet(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initHandlersAndroid3() {
	RelativeLayout relativeLayoutGeneral = (RelativeLayout) findViewById(R.id.relativeLayout3);
	relativeLayoutGeneral.setOnDragListener(new OnDragListener() {
	    @Override
	    public boolean onDrag(View arg0, DragEvent arg1) {
		Toast.makeText(CalendarActivity.this, "Drag", Toast.LENGTH_SHORT).show();
		return false;
	    }
	});

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initScrollViewForAndroid4() {
	ScrollView scrollView = (ScrollView) findViewById(R.id.acScrollView);
	scrollView.setScrollY(400);
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
	getMenuInflater().inflate(R.menu.calendar, menu);
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

    private void displayRdv(Rdv rdv) {
	TextView textView = new TextView(this);
	textView.setBackgroundResource(R.color.cal_rdv);
	textView.setText(rdv.getClient().getNom());
	textView.setTextAppearance(this, android.R.attr.textAppearanceSmall);
	textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.tableborderrdv));
	textView.setOnClickListener(new RdvClickListener(rdv.getId()));

	RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
	                                                                  computeHeight(rdv.getDuree()));
	lay.setMargins(0, computeMarginTop(rdv.getDateDebut()), 0, 0);

	Calendar cal = Calendar.getInstance();
	cal.setTime(rdv.getDateDebut());
	RelativeLayout layout = getDayRelativeLayout(cal);
	layout.addView(textView, lay);
    }

    private void clearWeekView() {
	LinearLayout weekRelativeLayout = (LinearLayout) findViewById(R.id.acSemaineLinearLayout);
	for (int i = 0; i < weekRelativeLayout.getChildCount(); i++) {
	    RelativeLayout dayRelativeLayout = (RelativeLayout) weekRelativeLayout.getChildAt(i);
	    dayRelativeLayout.removeViews(1, dayRelativeLayout.getChildCount() - 1);
	}
    }

    private RelativeLayout getDayRelativeLayout(Calendar cal) {
	RelativeLayout layout = null;
	switch (cal.get(Calendar.DAY_OF_WEEK)) {
	case Calendar.MONDAY:
	    layout = (RelativeLayout) findViewById(R.id.acLundiRelativeLayout);
	    break;
	case Calendar.TUESDAY:
	    layout = (RelativeLayout) findViewById(R.id.acMardiRelativeLayout);
	    break;
	case Calendar.WEDNESDAY:
	    layout = (RelativeLayout) findViewById(R.id.acMercrediRelativeLayout);
	    break;
	case Calendar.THURSDAY:
	    layout = (RelativeLayout) findViewById(R.id.acJeudiRelativeLayout);
	    break;
	case Calendar.FRIDAY:
	    layout = (RelativeLayout) findViewById(R.id.acVendrediRelativeLayout);
	    break;
	case Calendar.SATURDAY:
	    layout = (RelativeLayout) findViewById(R.id.acSamediRelativeLayout);
	    break;
	case Calendar.SUNDAY:
	    layout = (RelativeLayout) findViewById(R.id.acDimancheRelativeLayout);
	    break;
	}
	return layout;
    }

    /**
     * @param duree
     *            en minutes
     */
    private int computeHeight(int duree) {
	int height = duree * 2 / 3;
	return convertDpToPixel(height, this);
    }

    private int computeMarginTop(Date date) {
	int marginTop = 0;
	int heure = date.getHours();
	int minute = date.getMinutes();

	marginTop = ((heure - 5) * 40) + (minute * 10 / 15);

	return convertDpToPixel(marginTop, this);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density.
     * 
     * @param dp
     *            A value in dp (density independent pixels) unit. Which we need
     *            to convert into pixels
     * @param context
     *            Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on
     *         device density
     */
    private static int convertDpToPixel(int dp, Context context) {
	Resources resources = context.getResources();
	DisplayMetrics metrics = resources.getDisplayMetrics();
	int px = (int) (dp * (metrics.densityDpi / 160f));
	return px;
    }

    /**
     * This method converts device specific pixels to density independent
     * pixels.
     * 
     * @param px
     *            A value in px (pixels) unit. Which we need to convert into db
     * @param context
     *            Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    private static int convertPixelsToDp(int px, Context context) {
	Resources resources = context.getResources();
	DisplayMetrics metrics = resources.getDisplayMetrics();
	int dp = (int) (px / (metrics.densityDpi / 160f));
	return dp;
    }

    private class RdvClickListener implements OnClickListener {
	private Long id;

	public RdvClickListener(Long id) {
	    this.id = id;
	}

	@Override
	public void onClick(View v) {
	    Intent secondeActivite = new Intent(CalendarActivity.this, DetailRdvActivity.class);
	    secondeActivite.putExtra("idRdv", id);
	    startActivity(secondeActivite);
	}
    }
}
