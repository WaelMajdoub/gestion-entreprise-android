package com.gestionentreprise.gui.listeners;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import android.app.TimePickerDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TimePicker;

public class TimeFldTouchListener implements OnTouchListener {
    private EditText field;
    private TimePickerDialog dialog = null;

    public TimeFldTouchListener(EditText field) {
	this.field = field;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
	Calendar dtTxt = null;

	String preExistingDate = (String) field.getText().toString();

	if (preExistingDate != null && !preExistingDate.equals("")) {
	    StringTokenizer st = new StringTokenizer(preExistingDate, ":");
	    String initialHour = st.nextToken();
	    String initialMinutes = st.nextToken();
	    if (dialog == null)
		dialog = new TimePickerDialog(v.getContext(),
		                              new PickDate(field),
		                              Integer.parseInt(initialHour),
		                              Integer.parseInt(initialMinutes),
		                              true);

	    dialog.updateTime(Integer.parseInt(initialHour), Integer.parseInt(initialMinutes));

	} else {
	    dtTxt = Calendar.getInstance();
	    if (dialog == null)
		dialog = new TimePickerDialog(v.getContext(),
		                              new PickDate(field),
		                              dtTxt.getTime().getHours(),
		                              dtTxt.getTime().getMinutes(),
		                              true);

	    dialog.updateTime(dtTxt.getTime().getHours(), dtTxt.getTime().getMinutes());
	}

	dialog.show();
	return true;

    }

    public class PickDate implements TimePickerDialog.OnTimeSetListener {
	private EditText field;

	public PickDate(EditText field) {
	    this.field = field;
	}

	@Override
	public void onTimeSet(TimePicker view, int hour, int minutes) {
	    DecimalFormat twoNombers = new DecimalFormat("00");
	    // view.updateDate(year, monthOfYear, dayOfMonth);
	    field.setText(twoNombers.format(hour) + ":" + twoNombers.format(minutes));
	    dialog.hide();
	}
    }
}