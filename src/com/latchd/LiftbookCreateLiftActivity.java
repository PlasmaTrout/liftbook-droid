package com.latchd;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import com.latchd.liftbook.data.ILiftStorageFactory;
import com.latchd.liftbook.data.LiftStorageFactory;
import com.latchd.liftbook.data.ScheduledLift;
import com.latchd.picker.DecimalSelector;
import com.latchd.picker.NumberSelector;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class LiftbookCreateLiftActivity extends Activity {
	
	private int selectedDay;
	private int selectedMonth;
	private int selectedYear;
	private TextView dateLabel;
	Spinner s;
	Button b;
	static final int DATE_DIALOG_ID = 0;
	NumberSelector repsPicker;
	DecimalSelector weightPicker;
	NumberSelector setsPicker;
	RadioGroup g;
	String checked;
	String selectedDate;
	
	DatePickerDialog.OnDateSetListener mDateSetListener =  new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			selectedDay = dayOfMonth;
			selectedMonth = monthOfYear;
			selectedYear = year;
			updateDisplay();
			
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_lift);
        
        
        
        s = (Spinner) findViewById(R.id.spinner1);
        b = (Button) findViewById(R.id.button1);
        dateLabel = (TextView) findViewById(R.id.selecteddate);
        repsPicker = (NumberSelector) findViewById(R.id.reps_number_picker);
        weightPicker = (DecimalSelector) findViewById(R.id.weight_number_picker);
        setsPicker = (NumberSelector) findViewById(R.id.sets_number_picker);
        g = (RadioGroup) findViewById(R.id.radioGroup1);
        
        
        
        Bundle bundle = getIntent().getExtras();
        
        if(bundle != null){
        	selectedDate = bundle.getString(IntentExtraKeys.DATE_KEY);
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
        
        
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.open_lift_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.sort(new Comparator<CharSequence>(){
			public int compare(CharSequence object1, CharSequence object2) {
				return object1.toString().compareTo(object2.toString());
			}});
        
        if(s != null){
        	s.setAdapter(adapter);
        }
        
        b.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
        
        g.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton box = (RadioButton) findViewById(checkedId);
				checked = box.getText().toString();
				
				if(checked.equals("Main") || checked.equals("null")){
					checked = "";
				}
			}
		});
	
        repsPicker.setRange(1, 50);
        repsPicker.setCurrent(10);
        
        weightPicker.setRange(5, 1000,5);
        weightPicker.setCurrent(135);
        
        setsPicker.setRange(1,10);
        setsPicker.setCurrent(3);
        
        
	
        updateDisplay();
	}
	
	
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case DATE_DIALOG_ID:
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
		MenuInflater m = getMenuInflater();
		m.inflate(R.menu.open_lift_menu, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.cancel:
				this.finish();
				return true;
			case R.id.save:
				saveNewLift();
				this.finish();
			    return true;
			default:
				return true;
		}
	}
	
	private void saveNewLift(){
		ArrayList<ScheduledLift> list = new ArrayList<ScheduledLift>();
		
		
		String date = selectedYear+"-"+String.format("%02d",(selectedMonth+1))+"-"+String.format("%02d",selectedDay);
		String chosen = s.getAdapter().getItem(s.getSelectedItemPosition()).toString();
		
		if(checked != null){
			chosen = chosen + " " + checked;
		}
		
		ScheduledLift lift = new ScheduledLift(date, chosen, setsPicker.getCurrent(), repsPicker.getCurrent(), weightPicker.getCurrent());
		list.add(lift);
		
		ILiftStorageFactory factory = LiftStorageFactory.getFactory(LiftStorageFactory.OPEN_PROTOCOL);
		factory.Save(this, list,false);
	}
	
	
}
