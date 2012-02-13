package com.latchd.templates;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import com.latchd.IntentExtraKeys;
import com.latchd.R;
import com.latchd.liftbook.data.ILiftStorageFactory;
import com.latchd.liftbook.data.LiftStorageFactory;
import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.liftbook.data.ScheduledLift;
import com.latchd.picker.DecimalSelector;

public class WendlerBasicActivity extends Activity {

	protected static final int DATE_DIALOG_ID = 1;
	
	int selectedDay;
	int selectedYear;
	int selectedMonth;
	TextView selectedDate;
	String dateString;
	Spinner mainLiftSpinner;
	Spinner assist1Spinner;
	Spinner assist2Spinner;
	DecimalSelector mainSelector;
	DecimalSelector selector1;
	DecimalSelector selector2;
	TextView mainLiftDetails;
	TextView moreInfoLink;
	RadioButton week1Radio;
	RadioButton week2Radio;
	RadioButton week3Radio;
	RadioButton week4Radio;
	RadioGroup radioGroup;
	TextView assist1Text;
	TextView assist1TextWeight;
	TextView assist2Text;
	TextView assist2TextWeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wendler_lift);
		
	    Button bt = (Button) this.findViewById(R.id.button1);
	    selectedDate = (TextView) this.findViewById(R.id.selecteddate);
	    mainLiftSpinner = (Spinner) this.findViewById(R.id.mainliftspinner);
	    assist1Spinner = (Spinner) this.findViewById(R.id.spinner1);
	    assist2Spinner = (Spinner) this.findViewById(R.id.spinner2);
	    
	    mainSelector = (DecimalSelector) this.findViewById(R.id.main_number_selector);
	    selector1 = (DecimalSelector) this.findViewById(R.id.assistance1_number_selector);
	    selector2 = (DecimalSelector) this.findViewById(R.id.assistance2_number_selector);
	    
	    mainSelector.setRange(0.0F, 1000.0F, 2.5F);
	    selector1.setRange(0.0F,1000.0F,2.5F);
	    selector2.setRange(0.0F,1000.0F, 2.5F);
	    
	    mainLiftDetails = (TextView) this.findViewById(R.id.mainliftdetails);
	    moreInfoLink = (TextView) this.findViewById(R.id.moreinfolink);
	    week1Radio = (RadioButton) this.findViewById(R.id.week1radio);
	    week2Radio = (RadioButton) this.findViewById(R.id.week2radio);
	    week3Radio = (RadioButton) this.findViewById(R.id.week3radio);
	    week4Radio = (RadioButton) this.findViewById(R.id.week4radio);
	    radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup1);
	    assist1Text = (TextView) this.findViewById(R.id.textAssist1);
	    assist2Text = (TextView) this.findViewById(R.id.testAssist2);
	    assist1TextWeight = (TextView) this.findViewById(R.id.testAssist1Weight);
	    assist2TextWeight = (TextView) this.findViewById(R.id.textAssist2Weight);
	    radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(week4Radio.isChecked()){
					selector1.setVisibility(Spinner.GONE);
					selector2.setVisibility(Spinner.GONE);
					assist1Spinner.setVisibility(Spinner.GONE);
					assist2Spinner.setVisibility(Spinner.GONE);
					assist1Text.setVisibility(Spinner.GONE);
					assist1TextWeight.setVisibility(Spinner.GONE);
					assist2Text.setVisibility(Spinner.GONE);
					assist2TextWeight.setVisibility(Spinner.GONE);
				}else{
					selector1.setVisibility(Spinner.VISIBLE);
					selector2.setVisibility(Spinner.VISIBLE);
					assist1Spinner.setVisibility(Spinner.VISIBLE);
					assist2Spinner.setVisibility(Spinner.VISIBLE);
					assist1Text.setVisibility(Spinner.VISIBLE);
					assist1TextWeight.setVisibility(Spinner.VISIBLE);
					assist2Text.setVisibility(Spinner.VISIBLE);
					assist2TextWeight.setVisibility(Spinner.VISIBLE);
				}
			}
		});
	    
	    moreInfoLink.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.flexcart.com/members/elitefts/default.asp?cid=114&m=PD&pid=2976");
				Intent i = new Intent(android.content.Intent.ACTION_VIEW,uri);
				startActivity(i);
				
			}
		});
	   	    
	    selector1.setCurrent(135);
	    selector2.setCurrent(135);
		
	    SetDate();
	    
		bt.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);				
			}
		});
		
		ArrayAdapter<CharSequence> mainAdapter = ArrayAdapter.createFromResource(this,R.array.wendler_main_lifts,android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.wendler_assistance_lifts,android.R.layout.simple_spinner_item);
		
		mainAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		adapter.sort(new Comparator<CharSequence>(){
			public int compare(CharSequence object1, CharSequence object2) {
				return object1.toString().compareTo(object2.toString());
			}});
		
		mainLiftSpinner.setAdapter(mainAdapter);
		assist1Spinner.setAdapter(adapter);
		assist2Spinner.setAdapter(adapter);
		
		mainSelector.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				EditText box = (EditText) v;
				float result = Float.parseFloat(box.getText().toString());
				float finalValue = CalculationHelper.CalcWeightForPct(0.90, result, false);
				mainLiftDetails.setText("Selected 1RM Is "+box.getText().toString()+" lbs. Using "+Float.toString(finalValue)+" lbs (90%).");
			}
		});
		
		setupSpinners();
		
		updateDisplay();
	}

	private void setupSpinners() {
		mainLiftSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String result = mainLiftSpinner.getSelectedItem().toString();
				
				LiftbookDataHelper helper = new LiftbookDataHelper(WendlerBasicActivity.this);
				float max = helper.GetRecentMax(result);
				mainSelector.setCurrent(CalculationHelper.GetNearestBarWeight(max));
				
				//Toast.makeText(WendlerBasicActivity.this, "Making "+result+" The Main Lift. Your Best 1RM Is "+Integer.toString(max), Toast.LENGTH_SHORT).show();
				
				float finalValue = CalculationHelper.CalcWeightForPct(0.90, max,false);
				mainLiftDetails.setText("Best 1RM Was "+Float.toString(max)+". Using "+Float.toString(finalValue)+" (90%).");
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				mainLiftSpinner.setSelection(0);				
			}
		});
		
		assist1Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String result = assist1Spinner.getSelectedItem().toString();
				LiftbookDataHelper helper = new LiftbookDataHelper(WendlerBasicActivity.this);
				float max = helper.GetRecentMax(result);
				float finalValue = CalculationHelper.CalcWeightForPct(0.60, max);
				if(finalValue != 0){
					selector1.setCurrent(CalculationHelper.GetNearestBarWeight(finalValue));
				}else{
					selector1.setCurrent(135.0F);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				assist1Spinner.setSelection(0);				
			}
		});
		
		assist2Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String result = assist2Spinner.getSelectedItem().toString();
				LiftbookDataHelper helper = new LiftbookDataHelper(WendlerBasicActivity.this);
				float max = helper.GetRecentMax(result);
				float finalValue = CalculationHelper.CalcWeightForPct(0.60, max);
				if(finalValue != 0){
					selector2.setCurrent(CalculationHelper.GetNearestBarWeight(finalValue));
				}else{
					selector2.setCurrent(135);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				assist2Spinner.setSelection(0);	
			}
		});
	}
	
	private void SetDate(){
		Bundle b = getIntent().getExtras();
		
		if(b != null){
			dateString = b.getString(IntentExtraKeys.DATE_KEY);
		}
		
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
	
	private void updateDisplay(){
		StringBuilder sb = new StringBuilder();
		sb.append(selectedYear);
		sb.append("-");
		sb.append(String.format("%02d", selectedMonth+1));
		sb.append("-");
		sb.append(String.format("%02d", selectedDay));
		selectedDate.setText(sb.toString());
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this,mDateSetListener,selectedYear,selectedMonth,selectedDay);
		default:
			return super.onCreateDialog(id);
		}
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.open_lift_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case (R.id.save):
			generateLifts();
			this.finish();
			return true;
		case (R.id.cancel):
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

	private void generateLifts() {
		
		int week = 4;
		
		if(week1Radio.isChecked()){
			week = 1;
		}else if(week2Radio.isChecked()){
			week = 2;
		}else if(week3Radio.isChecked()){
			week = 3;
		}
		
		ILiftStorageFactory factory = LiftStorageFactory.getFactory(LiftStorageFactory.WENDLER_PROTOCOL);
		ArrayList<ScheduledLift> list = new ArrayList<ScheduledLift>();
		float max = mainSelector.getCurrent();
		float finalValue = CalculationHelper.CalcWeightForPct(0.90, max,false);
		
		ScheduledLift mainLift = new ScheduledLift(selectedDate.getText().toString(),
				mainLiftSpinner.getSelectedItem().toString(),3,0,
				finalValue);
		mainLift.setWeek(week);
		list.add(mainLift);
		
		if(week != 4){
		ScheduledLift assistanceLift1 = new ScheduledLift(selectedDate.getText().toString(),
				assist1Spinner.getSelectedItem().toString()+" Assistance", 5, 10, selector1.getCurrent());
		assistanceLift1.setWeek(week);
		
		ScheduledLift assistanceLift2 = new ScheduledLift(selectedDate.getText().toString(),
				assist2Spinner.getSelectedItem().toString()+" Assistance", 5, 10, selector2.getCurrent());
		assistanceLift2.setWeek(week);
		
		list.add(assistanceLift1);
		list.add(assistanceLift2);
		
		}
		factory.Save(this, list, true);	
				
				
	}

}
