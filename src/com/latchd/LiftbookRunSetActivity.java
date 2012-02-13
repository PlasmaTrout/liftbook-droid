package com.latchd;

import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.liftbook.data.WorkoutItem;
import com.latchd.picker.NumberPicker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LiftbookRunSetActivity extends Activity {

	WorkoutItem entry;
	EditText miles;
	NumberPicker hours;
	NumberPicker minutes;
	NumberPicker seconds;
	CheckBox complete;
	int rowId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_run);
		
		Bundle b = getIntent().getExtras();
		rowId = b.getInt(IntentExtraKeys.ROWID_KEY);
		
		miles = (EditText) this.findViewById(R.id.miles_number_picker);
		hours = (NumberPicker) this.findViewById(R.id.hours_number_picker);
		minutes = (NumberPicker) this.findViewById(R.id.min_number_picker);
		seconds = (NumberPicker) this.findViewById(R.id.sec_number_picker);
		complete = (CheckBox) this.findViewById(R.id.check_box);
		
		Button save = (Button) this.findViewById(R.id.save_button);
		Button cancel = (Button) this.findViewById(R.id.cancel_button);
		
		save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				LiftbookDataHelper helper = new LiftbookDataHelper(LiftbookRunSetActivity.this);
				helper.UpdateRunDetail(LiftbookRunSetActivity.this,
						rowId,
						hours.getCurrent(),
						minutes.getCurrent(),
						seconds.getCurrent(),
						Float.parseFloat(miles.getText().toString()),
						complete.isChecked());
				LiftbookRunSetActivity.this.finish();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				LiftbookRunSetActivity.this.finish();				
			}
		});
		
	}
	@Override
	protected void onStart() {
		super.onStart();
		Log.d("Activity","Starting up Run Set with rowId "+rowId);
		LiftbookDataHelper helper = new LiftbookDataHelper(this);
		entry = helper.GetWorkoutItems(rowId, false);
		setupUI();
		
	}
	
	private void setupUI(){
		Log.d("Activity","Miles had value "+entry.getMiles());
		miles.setText(Float.toString(entry.getMiles()));
		hours.setCurrent(entry.getHours());
		minutes.setCurrent(entry.getMinutes());
		seconds.setCurrent(entry.getSeconds());
		complete.setChecked(entry.isCompleted());
	}

}
