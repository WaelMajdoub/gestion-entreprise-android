package com.gestionentreprise.gui.listeners;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import android.app.DatePickerDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;

import com.gestionentreprise.gui.activity.IOnDateSetCalendarListener;

public class DateFldTouchListener implements OnTouchListener {
    private EditText field;
    private DatePickerDialog dialog = null;
    private IOnDateSetCalendarListener dateSetListener = null;

    public DateFldTouchListener(EditText field) {
	this.field = field;
    }

    public DateFldTouchListener(EditText field, IOnDateSetCalendarListener dateSetListener) {
	this(field);
	this.dateSetListener = dateSetListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
	Calendar dtTxt = null;

	String preExistingDate = (String) field.getText().toString();

	if (preExistingDate != null && !preExistingDate.equals("")) {
	    StringTokenizer st = new StringTokenizer(preExistingDate, "/");
	    String initialDate = st.nextToken();
	    String initialMonth = st.nextToken();
	    String initialYear = st.nextToken();
	    if (dialog == null)
		dialog = new DatePickerDialog(v.getContext(),
		                              new PickDate(field, dateSetListener),
		                              Integer.parseInt(initialYear),
		                              Integer.parseInt(initialMonth) - 1,
		                              Integer.parseInt(initialDate));
	    dialog.updateDate(Integer.parseInt(initialYear),
		              Integer.parseInt(initialMonth) - 1,
		              Integer.parseInt(initialDate));

	} else {
	    dtTxt = Calendar.getInstance();
	    if (dialog == null)
		dialog = new DatePickerDialog(v.getContext(),
		                              new PickDate(field, dateSetListener),
		                              dtTxt.getTime().getYear(),
		                              dtTxt.getTime().getMonth(),
		                              dtTxt.getTime().getDay());
	    dialog.updateDate(dtTxt.getTime().getYear(), dtTxt.getTime().getMonth(), dtTxt.getTime().getDay());
	}

	dialog.show();
	return true;

    }

    public class PickDate implements DatePickerDialog.OnDateSetListener {
	private EditText field;
	private IOnDateSetCalendarListener dateSetListener;

	public PickDate(EditText field, IOnDateSetCalendarListener dateSetListener) {
	    this.field = field;
	    this.dateSetListener = dateSetListener;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	    DecimalFormat twoNombers = new DecimalFormat("00");
	    DecimalFormat fourNombers = new DecimalFormat("0000");
	    view.updateDate(year, monthOfYear, dayOfMonth);
	    field.setText(twoNombers.format(dayOfMonth) + "/" + twoNombers.format((monthOfYear + 1)) + "/"
		    + fourNombers.format(year));
	    dialog.hide();
	    if (this.dateSetListener != null) {
		this.dateSetListener.onCalendarDateSet(year, monthOfYear, dayOfMonth);
	    }
	}
    }
}