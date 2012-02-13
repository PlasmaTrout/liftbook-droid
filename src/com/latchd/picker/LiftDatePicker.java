package com.latchd.picker;

import java.sql.Date;
import java.util.Calendar;

import com.latchd.R;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LiftDatePicker extends LinearLayout {

	protected static final int DATE_DIALOG_ID = 1;
	int selectedDay;
	int selectedYear;
	int selectedMonth;
	TextView selectedDate;
	
	public LiftDatePicker(Context context) {
		super(context);
		SetupControl(context);
	}
	
	public LiftDatePicker(Context context, AttributeSet attrs){
		super(context,attrs);
		SetupControl(context);
	}

	private void SetupControl(Context context){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.lift_date_control, this, true);
		
		if(!v.isInEditMode()){
			
			Button bt = (Button) this.findViewById(R.id.button1);
		    selectedDate = (TextView) this.findViewById(R.id.selecteddate);
			
			setDate("");
			
			updateDisplay();
			
			bt.setOnClickListener(new OnClickListener() {
				
				DatePickerDialog.OnDateSetListener mDateSetListener =  new DatePickerDialog.OnDateSetListener() {

					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						selectedDay = dayOfMonth;
						selectedMonth = monthOfYear;
						selectedYear = year;
						updateDisplay();
						
					}
				};
				
				public void onClick(View v) {
					DatePickerDialog d =  new DatePickerDialog(v.getContext(),mDateSetListener,selectedYear,selectedMonth,selectedDay);
					d.show();
				}

				
			});
			
			
		}
	}
	
	public void setDate(String dateString){
		
		if(dateString != null && dateString.length() > 0){
        	Date date = Date.valueOf(dateString);
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
		
	}
	
	public String getDate(){
		return selectedDate.getText().toString();
	}
	
	private void updateDisplay(){
		StringBuilder sb = new StringBuilder();
		sb.append(selectedYear);
		sb.append("-");
		sb.append(String.format("%02d", selectedMonth+1));
		sb.append("-");
		sb.append(String.format("%02d", selectedDay));
		selectedDate.setText(sb.toString());
	}
	
	
}
