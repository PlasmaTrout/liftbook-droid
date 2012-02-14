package com.latchd;

import com.latchd.liftbook.data.LiftbookSettingsHelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class LiftbookSettingsActivity extends Activity {
	
	CheckBox useMetricSystem;
	CheckBox useWendlerMax;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		useMetricSystem = (CheckBox) this.findViewById(R.id.useMetricCheckBox);
		useWendlerMax = (CheckBox) this.findViewById(R.id.useWendlerMax);
		
		useMetricSystem.setChecked(LiftbookSettingsHelper.UseMetricSystem(this));
		useWendlerMax.setChecked(LiftbookSettingsHelper.UseWendlerMax(this));
		
		useMetricSystem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				LiftbookSettingsHelper.SetMetricSystem(LiftbookSettingsActivity.this, isChecked);
			}
		});
		
		useWendlerMax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				LiftbookSettingsHelper.SetWendlerMax(LiftbookSettingsActivity.this, isChecked);
			}
		});
		
		
	}
}
