package com.latchd;

import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.liftbook.data.WorkoutItem;
import com.latchd.picker.DecimalSelector;
import com.latchd.picker.NumberSelector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class LiftbookViewSetActivity extends Activity {

	private int reps;
	private float weight;
	private int row;
	private String set;
	private WorkoutItem item;
	LiftbookDataHelper helper;
	NumberSelector repsPicker;
	DecimalSelector weightPicker;
	CheckBox box;
	TextView onerep;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_weight_dialog);
		
		Bundle b = getIntent().getExtras();
		set = b.getString(IntentExtraKeys.SET_KEY);
		row = b.getInt(IntentExtraKeys.ROWID_KEY);
		
		helper = new LiftbookDataHelper(this);
		item = helper.GetWorkoutItems(row,true);
		
		reps = item.getReps();
		weight = item.getWeight();
		item.isCompleted();
		
		setTitle("Liftbook - "+set+" Of "+item.getName());
		
		repsPicker = (NumberSelector) findViewById(R.id.reps_number_picker);
		weightPicker = (DecimalSelector) findViewById(R.id.weight_number_picker);
		box = (CheckBox) findViewById(R.id.checkBox1);
		Button save = (Button) findViewById(R.id.savebutton);
		Button cancel = (Button) findViewById(R.id.cancelbutton);
		onerep = (TextView) findViewById(R.id.onerepmaxlabel);
		
		repsPicker.setRange(0, 50);
		repsPicker.setCurrent(reps);
		
		weightPicker.setRange(5.0F,1000.0F, 2.5F);
		weightPicker.setCurrent(weight);
		
		onerep.setText("1RM "+item.GetEpleyOneRepMax());
		
		//box.setChecked(item.isCompleted());
		
		save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				helper.UpdateLiftDetail(LiftbookViewSetActivity.this,row, repsPicker.getCurrent(), weightPicker.getCurrent(), box.isChecked());
				
				Toast.makeText(getBaseContext(), "Edit Completed", Toast.LENGTH_SHORT);
				setResult(RESULT_OK);
				finish();
				//LiftbookViewSetActivity.this.finish();
				
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Edit Cancelled", Toast.LENGTH_SHORT);
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		
		
		repsPicker.setOnChangeListener(new com.latchd.picker.NumberSelector.OnChangedListener() {
			
			public void onChanged(NumberSelector picker, int oldVal, int newVal) {
				item.setReps(picker.getCurrent());
				onerep.setText("1RM "+item.GetEpleyOneRepMax());
			}
		});
		
		weightPicker.setOnChangeListener(new com.latchd.picker.DecimalSelector.OnChangedListener() {
			
			public void onChanged(DecimalSelector picker, float oldVal, float newVal) {
				item.setWeight(picker.getCurrent());
				onerep.setText("1RM "+item.GetEpleyOneRepMax());
			}
		});
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		item.setReps(savedInstanceState.getInt("Reps"));
		repsPicker.setCurrent(savedInstanceState.getInt("Reps"));
		weightPicker.setCurrent(savedInstanceState.getFloat("Weight"));
		
		//Log.d("State","Hydrated reps ("+savedInstanceState.getInt("Reps")+" and weight state");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("Reps", repsPicker.getCurrent());
		outState.putFloat("Weight", weightPicker.getCurrent());
		//Log.d("State", "Saved reps and weight state");
		super.onSaveInstanceState(outState);
	}
	
	

}
