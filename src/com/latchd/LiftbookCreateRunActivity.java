package com.latchd;

import java.sql.Date;
import java.util.Calendar;

import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.picker.NumberPicker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class LiftbookCreateRunActivity extends Activity {

	static final int DATE_DIALOG_ID = 0;
	
	EditText miles;
	NumberPicker hours;
	NumberPicker minutes;
	NumberPicker seconds;
	TextView dateLabel;
	String selectedType;
	Button dateButton;
	String selectedDate;
	int selectedDay;
	int selectedMonth;
	int selectedYear;
	float milesValue ;
	int hoursValue;
	int minutesValue;
	int secondsValue;
	Spinner spinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.create_run);
		
		miles = (EditText) this.findViewById(R.id.miles_number_picker);
		hours = (NumberPicker) this.findViewById(R.id.hours_number_picker);
		minutes = (NumberPicker) this.findViewById(R.id.min_number_picker);
		seconds = (NumberPicker) this.findViewById(R.id.sec_number_picker);
		dateButton = (Button) this.findViewById(R.id.button1);
		spinner = (Spinner) this.findViewById(R.id.spinner1);
		dateLabel = (TextView) this.findViewById(R.id.selecteddate);
		
		setupDate();
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cardio_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		miles.setHint("2.5");
		
		minutes.setRange(0, 59);
		seconds.setRange(0, 59);
		
		dateButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);				
			}
		});
		
		updateDisplay();
	}
	
	DatePickerDialog.OnDateSetListener mDateSetListener =  new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			selectedDay = dayOfMonth;
			selectedMonth = monthOfYear;
			selectedYear = year;
			updateDisplay();
			
		}
	};

	private void setupDate() {
		
		Bundle b = getIntent().getExtras();
		
		if(b != null){
			selectedDate = b.getString(IntentExtraKeys.DATE_KEY);
		}
		
		if(selectedDate != null && selectedDate.length() > 0){
        	Date date = Date.valueOf(selectedDate);
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(date);
        	selectedDay = cal.get(Calendar.DAY_OF_MONTH);
            selectedMonth = cal.get(Calendar.MONTH);
            selectedYear = cal.get(Calendar.YEAR);
        	
        }else{
        	Calendar cal = Calendar.getInstance();
            selectedDay = cal.get(Calendar.DAY_OF_MONTH);
            selectedMonth = cal.get(Calendar.MONTH);
            selectedYear = cal.get(Calendar.YEAR);
        }
		
	
		miles.setText(Float.toString(milesValue));
		hours.setCurrent(hoursValue);
		minutes.setCurrent(minutesValue);
		seconds.setCurrent(secondsValue);
	}

	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case(DATE_DIALOG_ID):
			return new DatePickerDialog(this,mDateSetListener,selectedYear,selectedMonth,selectedDay);
		default:
			return null;
		}
	}
	
	private void updateDisplay(){
		StringBuilder sb = new StringBuilder();
		sb.append(selectedYear);
		sb.append("-");
		sb.append(String.format("%02d", selectedMonth+1));
		sb.append("-");
		sb.append(String.format("%02d", selectedDay));
		dateLabel.setText(sb.toString());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.open_lift_menu, menu);
		return true;
	}

	private void SaveResults(){
		
		String type = spinner.getSelectedItem().toString();
		String date = dateLabel.getText().toString();
		
		LiftbookDataHelper helper = new LiftbookDataHelper(this);
		helper.StoreScheduledRun(this, date, type, Float.parseFloat(miles.getText().toString()), hours.getCurrent(), minutes.getCurrent(), seconds.getChildCount());
		
		
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case(R.id.save):
			SaveResults();
			finish();
			return true;
		case(R.id.cancel):
			finish();
			return true;
		default:
			return false;
		}
	}

}
