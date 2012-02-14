package com.latchd.templates;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import com.latchd.IntentExtraKeys;
import com.latchd.R;
import com.latchd.liftbook.data.ILiftStorageFactory;
import com.latchd.liftbook.data.LiftStorageFactory;
import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.liftbook.data.LiftbookSettingsHelper;
import com.latchd.liftbook.data.ScheduledLift;
import com.latchd.picker.DecimalSelector;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class StrongLiftsBasicActivity extends Activity implements OnCheckedChangeListener {

	RadioButton wa;
	RadioButton wb;
	RadioGroup rg;
	DecimalSelector none;
	DecimalSelector ntwo;
	DecimalSelector nthree;
	TextView labelOne;
	TextView labelTwo;
	TextView labelThree;
	TextView selectedDate;
	String dateString;
	CheckBox box;
	int selectedDay;
	int selectedYear;
	int selectedMonth;
	
	
	private static final String WORK_A_TYPE_1 = "Back Squat";
	private static final String WORK_A_TYPE_2 = "Bench Press";
	private static final String WORK_A_TYPE_3 = "Barbell Rows";
	
	private static final String WORK_B_TYPE_1 = "Back Squat";
	private static final String WORK_B_TYPE_2 = "Overhead Press";
	private static final String WORK_B_TYPE_3 = "Deadlift";
	protected static final int DATE_DIALOG_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.stronglifts_lift);
		
		rg = (RadioGroup) this.findViewById(R.id.radioGroup1);
		wa = (RadioButton) this.findViewById(R.id.workouta_radio);
		wb = (RadioButton) this.findViewById(R.id.workoutb_radio);
		
		none = (DecimalSelector) this.findViewById(R.id.one_number_selector);
		ntwo = (DecimalSelector) this.findViewById(R.id.two_number_selector);
		nthree = (DecimalSelector) this.findViewById(R.id.three_number_selector);
		
		none.setRange(0.0F, 1000.0F, 2.5F);
		ntwo.setRange(0.0F,1000.0F,2.5F);
		nthree.setRange(0.0F,1000.0F, 2.5F);
		
		labelOne = (TextView) this.findViewById(R.id.one_label);
		labelTwo = (TextView) this.findViewById(R.id.two_label);
		labelThree = (TextView) this.findViewById(R.id.three_label);
		
		selectedDate = (TextView) this.findViewById(R.id.selecteddate);
		box = (CheckBox) this.findViewById(R.id.warmup_checkbox);
		
		Button bt = (Button) this.findViewById(R.id.button1);
		
		bt.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);				
			}
		});
		
		rg.setOnCheckedChangeListener(this);
		
		loadOneRepMaximums();
		
		TextView tv = (TextView) this.findViewById(R.id.link_text);
		tv.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("http://stronglifts.com/stronglifts-5x5-beginner-strength-training-program/"));
				startActivity(i);
			}
		});
		
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
				
		updateDisplay();
	}

	private void loadOneRepMaximums() {
		
		LiftbookDataHelper helper = new LiftbookDataHelper(this);
		
		float maxone;
		float maxtwo;
		float maxthree;
		float mid = 135;
		if(LiftbookSettingsHelper.UseMetricSystem(this)){
			mid=60;
		}
		
		if(wa.isChecked()){
			maxone = helper.GetRecentMax(WORK_A_TYPE_1);
			labelOne.setText("5x5 "+WORK_A_TYPE_1+ " (Best 1RM "+maxone+"):");
			maxtwo = helper.GetRecentMax(WORK_A_TYPE_2);
			labelTwo.setText("5x5 "+WORK_A_TYPE_2+ " (Best 1RM "+maxtwo+"):");
			maxthree = helper.GetRecentMax(WORK_A_TYPE_3);
			labelThree.setText("5x5 "+WORK_A_TYPE_3+ " (Best 1RM "+maxthree+"):");
		}else{
			maxone = helper.GetRecentMax(WORK_B_TYPE_1);
			labelOne.setText("5x5 "+WORK_B_TYPE_1+ " (Best 1RM "+maxone+"):");
			maxtwo = helper.GetRecentMax(WORK_B_TYPE_2);
			labelTwo.setText("5x5 "+WORK_B_TYPE_2+ " (Best 1RM "+maxtwo+"):");
			maxthree = helper.GetRecentMax(WORK_B_TYPE_3);
			labelThree.setText("1x5 "+WORK_B_TYPE_3+ " (Best 1RM "+maxthree+"):");
		}
		if(maxone > 0){
			none.setCurrent(CalcFiveRep(maxone)+5);
		}else{
			none.setCurrent(mid);
		}
		if(maxtwo > 0){
			ntwo.setCurrent(CalcFiveRep(maxtwo)+5);
		}else{
			ntwo.setCurrent(mid);
		}
		if(maxthree > 0){
			nthree.setCurrent(CalcFiveRep(maxthree)+5);
		}else{
			nthree.setCurrent(mid);
		}
		
		
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		loadOneRepMaximums();		
	}
	
	private float CalcFiveRep(float onerepmax){
		return CalculationHelper.CalcWeightForReps(5, onerepmax);
		/*int result = onerepmax;
		
		double conv = Math.ceil(onerepmax / 1.16665);
		Log.d("Calculation","Actual 5 Rep is "+conv);
		result = (int) Math.ceil(conv/5)*5;
		
		return result;*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.open_lift_menu,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case(R.id.save):
			GenerateLifts();
			this.finish();
			return true;
		case(R.id.cancel):
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	private void GenerateLifts(){
		
		ArrayList<ScheduledLift> coll = new ArrayList<ScheduledLift>();
		
		if(wa.isChecked()){
			coll.add(new ScheduledLift(selectedDate.getText().toString(), WORK_A_TYPE_1, 5, 5, none.getCurrent()));
			coll.add(new ScheduledLift(selectedDate.getText().toString(), WORK_A_TYPE_2, 5, 5, ntwo.getCurrent()));
			coll.add(new ScheduledLift(selectedDate.getText().toString(), WORK_A_TYPE_3, 5, 5, nthree.getCurrent()));
		}else{
			coll.add(new ScheduledLift(selectedDate.getText().toString(), WORK_B_TYPE_1, 5, 5, none.getCurrent()));
			coll.add(new ScheduledLift(selectedDate.getText().toString(), WORK_B_TYPE_2, 5, 5, ntwo.getCurrent()));
			coll.add(new ScheduledLift(selectedDate.getText().toString(), WORK_B_TYPE_3, 1, 5, nthree.getCurrent()));
		}
					
		ILiftStorageFactory factory = LiftStorageFactory.getFactory(LiftStorageFactory.SLFIVE_PROTOCOL);
		factory.Save(this, coll, box.isChecked());
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
	
}
