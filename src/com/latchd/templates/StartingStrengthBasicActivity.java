package com.latchd.templates;


import com.latchd.IntentExtraKeys;
import com.latchd.R;
import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.picker.DecimalSelector;
import com.latchd.picker.LiftDatePicker;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class StartingStrengthBasicActivity extends Activity implements OnCheckedChangeListener {
	
	RadioButton wa;
	RadioButton wb;
	RadioGroup rg;
	DecimalSelector none;
	DecimalSelector ntwo;
	DecimalSelector nthree;
	TextView labelOne;
	TextView labelTwo;
	TextView labelThree;
	CheckBox box;
	LiftDatePicker picker;
	
	
	private static final String WORK_A_TYPE_1 = "Back Squat";
	private static final String WORK_A_TYPE_2 = "Bench Press";
	private static final String WORK_A_TYPE_3 = "Deadlift";
	
	private static final String WORK_B_TYPE_1 = "Back Squat";
	private static final String WORK_B_TYPE_2 = "Overhead Press";
	private static final String WORK_B_TYPE_3 = "Power Clean";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.starting_strength);
		
		rg = (RadioGroup) this.findViewById(R.id.radioGroup1);
		wa = (RadioButton) this.findViewById(R.id.workouta_radio);
		wb = (RadioButton) this.findViewById(R.id.workoutb_radio);
		box = (CheckBox) this.findViewById(R.id.warmup_checkbox);
		
		none = (DecimalSelector) this.findViewById(R.id.one_number_selector);
		ntwo = (DecimalSelector) this.findViewById(R.id.two_number_selector);
		nthree = (DecimalSelector) this.findViewById(R.id.three_number_selector);
		
		none.setRange(0.0F, 1000.0F, 2.5F);
		ntwo.setRange(0.0F,1000.0F,2.5F);
		nthree.setRange(0.0F,1000.0F, 2.5F);
		
		labelOne = (TextView) this.findViewById(R.id.one_label);
		labelTwo = (TextView) this.findViewById(R.id.two_label);
		labelThree = (TextView) this.findViewById(R.id.three_label);
		
		picker = (LiftDatePicker) this.findViewById(R.id.datePicker);
		
		rg.setOnCheckedChangeListener(this);
		
		loadOneRepMaximums();
		
		Bundle b = getIntent().getExtras();
		
		if(b != null){
			String dateString = b.getString(IntentExtraKeys.DATE_KEY);
			picker.setDate(dateString);
		}
		
		TextView tv = (TextView) this.findViewById(R.id.link_text);
		tv.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("http://startingstrength.wikia.com/wiki/FAQ:The_Program"));
				startActivity(i);
			}
		});
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		loadOneRepMaximums();		
	}
	
	private void loadOneRepMaximums() {
		
		LiftbookDataHelper helper = new LiftbookDataHelper(this);
		
		float maxone;
		float maxtwo;
		float maxthree;
		
		if(wa.isChecked()){
			maxone = helper.GetRecentMax(WORK_A_TYPE_1);
			labelOne.setText("3x5 "+WORK_A_TYPE_1+ " (Best 1RM "+maxone+"):");
			maxtwo = helper.GetRecentMax(WORK_A_TYPE_2);
			labelTwo.setText("3x5 "+WORK_A_TYPE_2+ " (Best 1RM "+maxtwo+"):");
			maxthree = helper.GetRecentMax(WORK_A_TYPE_3);
			labelThree.setText("1x5 "+WORK_A_TYPE_3+ " (Best 1RM "+maxthree+"):");
		}else{
			maxone = helper.GetRecentMax(WORK_B_TYPE_1);
			labelOne.setText("3x5 "+WORK_B_TYPE_1+ " (Best 1RM "+maxone+"):");
			maxtwo = helper.GetRecentMax(WORK_B_TYPE_2);
			labelTwo.setText("3x5 "+WORK_B_TYPE_2+ " (Best 1RM "+maxtwo+"):");
			maxthree = helper.GetRecentMax(WORK_B_TYPE_3);
			labelThree.setText("5x3 "+WORK_B_TYPE_3+ " (Best 1RM "+maxthree+"):");
		}
		if(maxone > 0){
			none.setCurrent(CalculationHelper.CalcWeightForReps(5, maxone)+5);
		}else{
			none.setCurrent(135);
		}
		if(maxtwo > 0){
			ntwo.setCurrent(CalculationHelper.CalcWeightForReps(5, maxtwo)+5);
		}else{
			ntwo.setCurrent(135);
		}
		if(maxthree > 0){
			nthree.setCurrent(CalculationHelper.CalcWeightForReps(5, maxthree));
		}else{
			nthree.setCurrent(135);
		}
		
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inf = this.getMenuInflater();
		inf.inflate(R.menu.open_lift_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	
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

	public void generateLifts(){
		
		Context ctx = this.getBaseContext();
		final String protocol = "Starting Strength";
		
		LiftbookDataHelper helper = new LiftbookDataHelper(ctx);
		
		if(wa.isChecked()){
			
			if(box.isChecked()){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_1+" Warmup", 5, 45, protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_1+" Warmup", 5, 45, protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_1+" Warmup", 5,
						CalculationHelper.CalcWeightForPct(0.40,none.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_1+" Warmup", 3,
						CalculationHelper.CalcWeightForPct(0.60,none.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_1+" Warmup", 2,
						CalculationHelper.CalcWeightForPct(0.80,none.getCurrent(),true), protocol);
			}
			
			for(int i=0; i < 3;i++){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_1, 5, none.getCurrent(), protocol);
			}
			
			if(box.isChecked()){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_2+" Warmup", 5, 45, protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_2+" Warmup", 5, 45, protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_2+" Warmup", 3,
						CalculationHelper.CalcWeightForPct(0.50,ntwo.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_2+" Warmup", 3,
						CalculationHelper.CalcWeightForPct(0.70,ntwo.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_2+" Warmup", 2,
						CalculationHelper.CalcWeightForPct(0.90,ntwo.getCurrent(),true), protocol);
			}
			
			for(int i=0; i < 3;i++){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_2, 5, ntwo.getCurrent(), protocol);
			}
			
			if(box.isChecked()){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_3+" Warmup", 5,
						CalculationHelper.CalcWeightForPct(0.40,nthree.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_3+" Warmup", 5,
						CalculationHelper.CalcWeightForPct(0.40,nthree.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_3+" Warmup", 3,
						CalculationHelper.CalcWeightForPct(0.60,nthree.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_3+" Warmup", 2,
						CalculationHelper.CalcWeightForPct(0.85,nthree.getCurrent(),true), protocol);
			}
			
			helper.StoreScheduledSet(ctx, picker.getDate(), WORK_A_TYPE_3, 5, nthree.getCurrent(), protocol);
			
		}else{
			
			if(box.isChecked()){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_1+" Warmup", 5, 45, protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_1+" Warmup", 5, 45, protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_1+" Warmup", 5,
						CalculationHelper.CalcWeightForPct(0.40,none.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_1+" Warmup", 3,
						CalculationHelper.CalcWeightForPct(0.60,none.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_1+" Warmup", 2,
						CalculationHelper.CalcWeightForPct(0.80,none.getCurrent(),true), protocol);
			}
			
			for(int i=0; i < 3;i++){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_1, 5, none.getCurrent(), protocol);
			}
			
			if(box.isChecked()){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_2+" Warmup", 5, 45, protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_2+" Warmup", 5, 45, protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_2+" Warmup", 3,
						CalculationHelper.CalcWeightForPct(0.55,ntwo.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_2+" Warmup", 3,
						CalculationHelper.CalcWeightForPct(0.70,ntwo.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_2+" Warmup", 2,
						CalculationHelper.CalcWeightForPct(0.85,ntwo.getCurrent(),true), protocol);
			}
			
			for(int i=0; i < 3;i++){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_2, 5, ntwo.getCurrent(), protocol);
			}
			
			if(box.isChecked()){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_3+" Warmup", 5, 45, protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_3+" Warmup", 5, 45, protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_3+" Warmup", 5,
						CalculationHelper.CalcWeightForPct(0.55,nthree.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_3+" Warmup", 3,
						CalculationHelper.CalcWeightForPct(0.70,nthree.getCurrent(),true), protocol);
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_3+" Warmup", 2,
						CalculationHelper.CalcWeightForPct(0.85,nthree.getCurrent(),true), protocol);
			}
			
			for(int i=0;i < 5;i++){
				helper.StoreScheduledSet(ctx, picker.getDate(), WORK_B_TYPE_3, 3, nthree.getCurrent(), protocol);
			}
			
		}
	}
	
	
}
