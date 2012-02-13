package com.latchd;

import java.util.ArrayList;


import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.liftbook.data.WorkoutItem;
import com.latchd.templates.CalculationHelper;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class LiftbookDisplayExerciseActivity extends ListActivity  {

	private String selectedDate;
	private String exercise;
	private ProgressDialog progressDialog = null; 
	WorkoutItemAdapter adapter;
	private Runnable viewSchedule;
	ArrayList<WorkoutItem> items;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_display);
		
		Bundle b = getIntent().getExtras();
		selectedDate = b.getString(IntentExtraKeys.DATE_KEY);
		exercise = b.getString(IntentExtraKeys.EXERCISE_KEY);
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				WorkoutItem tag = (WorkoutItem) arg1.getTag();
				TextView v = (TextView) arg1.findViewById(R.id.setlabel);
				Intent intent = new Intent(LiftbookDisplayExerciseActivity.this,LiftbookViewSetActivity.class);
				//intent.putExtra(IntentExtraKeys.DATE_KEY, selectedDate);
				//intent.putExtra(IntentExtraKeys.EXERCISE_KEY, exercise);
				intent.putExtra(IntentExtraKeys.ROWID_KEY, tag.getRowId());
				intent.putExtra(IntentExtraKeys.SET_KEY, v.getText());
				startActivityForResult(intent,1);
				
			}
		});
		
		
		
		setTitle("Liftbook - "+ScheduleDateHelper.GetReadableScheduleDate(selectedDate)+" "+exercise+" Sets");
		registerForContextMenu(this.getListView());
		RefreshWorkout();
		
	}
	
	private void RefreshWorkout(){
		
		items = new ArrayList<WorkoutItem>();
		//ArrayList<WorkoutItem> items = helper.GetWorkoutItems(selectedDate, exercise);
		adapter = new WorkoutItemAdapter(this,R.layout.workout_view_row,items);
		setListAdapter(adapter);
		
		viewSchedule = new Runnable(){
        	public void run(){
        		GetDbValues();
        	}
        };
        
       Thread t = new Thread(null,viewSchedule,"MagentoBackground");
       t.start();
       progressDialog = ProgressDialog.show(LiftbookDisplayExerciseActivity.this, "Please wait...", "Retrieving data ...", true);
		
	}
	
	private Runnable returnRes = new Runnable(){
    	public void run(){
    		if(items != null && items.size() > 0){
    			adapter.notifyDataSetChanged();
    			for(int i=0;i < items.size();i++){
    				adapter.add(items.get(i));
    			}
    		}
    		progressDialog.dismiss();
    		adapter.notifyDataSetChanged();
    	}
	};
	
	private void GetDbValues(){
		LiftbookDataHelper helper = new LiftbookDataHelper(this);
		items = helper.GetWorkoutItems(selectedDate, exercise);
		runOnUiThread(returnRes);
	}
	
	private class WorkoutItemAdapter extends ArrayAdapter<WorkoutItem>{

		public WorkoutItemAdapter(Context context, int textViewResourceId,
				ArrayList<WorkoutItem> objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
		}

		ArrayList<WorkoutItem> items;
		
		public WorkoutItemAdapter(Context context, int resource,
				int textViewResourceId, ArrayList<WorkoutItem> objects) {
			super(context, resource, textViewResourceId, objects);
			this.items = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inf = getLayoutInflater();
    		View v = null;
    		
    		if (convertView == null) {
    			v = inf.inflate(R.layout.workout_view_row, parent,false);
            }else{
            	v = convertView;
            }
			
    		
    		
    		WorkoutItem item = items.get(position);
    		
    		if(item != null){
    			TextView exercise = (TextView) v.findViewById(R.id.exerciselabel);
    			TextView sets = (TextView) v.findViewById(R.id.setlabel);
    			TextView reps = (TextView) v.findViewById(R.id.reps_count_placeholder);
    			TextView weight = (TextView) v.findViewById(R.id.weight_count_placeholder);
    			TextView rm = (TextView) v.findViewById(R.id.prlabel);
    			CheckBox box = (CheckBox) v.findViewById(R.id.completed_check_box);
    			
    			
    			v.setTag(item);
    			exercise.setText(item.getName());
    			sets.setText("Set "+item.getSets());
    			reps.setText(String.valueOf(item.getReps()));
    			weight.setText(String.valueOf(item.getWeight())+" ("+CalculationHelper.GetNearestBarWeight(item.getWeight())+")");
    			exercise.setTag(item.getRowId());
    			rm.setText("1RM "+item.getTodayOneRepMax()+" ("+CalculationHelper.GetNearestBarWeight(item.getTodayOneRepMax())+")");
    			box.setChecked(item.isCompleted());
    			
    			//registerForContextMenu(exercise);
    			
    		}
			
			return v;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		RefreshWorkout();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Extended Options");
		//Toast.makeText(this, "Context Menu Activated", Toast.LENGTH_SHORT).show();
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.context_menu, menu);
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case(R.id.delete):
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			WorkoutItem item1 = (WorkoutItem) adapter.getItem((int) info.id);
			if(item !=  null){
				LiftbookDataHelper helper = new LiftbookDataHelper(this);
				helper.DeleteLift(item1.getRowId());
				RefreshWorkout();
			}
			return true;
		default:
			return super.onContextItemSelected(item);
		}
		
	}
	
}
