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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LiftbookDisplayDayActivity extends ListActivity{

	protected static final int EDIT_RUN_DIALOG = 1;
	
	private String selectedDate;
	ArrayList<WorkoutItem> items;
	private ProgressDialog progressDialog = null; 
	WorkoutItemEntryAdapter adapter;
	private Runnable viewSchedule;
	//private GestureDetector dt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day_workout);
		
		//dt = new GestureDetector(this);
					
		Bundle b = getIntent().getExtras();
		selectedDate = b.getString("Date");
		
		this.setTitle("Liftbook - Displaying Day "+ScheduleDateHelper.GetReadableScheduleDate(selectedDate));
		
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView view = (TextView) arg1.findViewById(R.id.toptext);
				Toast t = Toast.makeText(getApplicationContext(), "Opening "+view.getText(), Toast.LENGTH_SHORT);
				t.show();
				
				WorkoutItem entry = (WorkoutItem) arg1.getTag();
				
				if(entry.isLift()){
					Intent intent = new Intent(LiftbookDisplayDayActivity.this,LiftbookDisplayExerciseActivity.class);
					intent.putExtra(IntentExtraKeys.DATE_KEY, selectedDate);
					intent.putExtra(IntentExtraKeys.EXERCISE_KEY,view.getText());
					startActivity(intent);
				}else{
					Intent intent = new Intent("com.latchd.EDITRUNACTIVITY");
					intent.putExtra(IntentExtraKeys.ROWID_KEY, entry.getRowId());
					startActivityForResult(intent,EDIT_RUN_DIALOG);		
				}
				
			}
	       });
		
		registerForContextMenu(this.getListView());
		//RefreshList();
	}
	
	private void RefreshList(){
    	
    	items = new ArrayList<WorkoutItem>();
        
        //ListView list = (ListView) this.findViewById(R.id.workout_day_list_view);
        adapter = new WorkoutItemEntryAdapter(this,R.layout.day_lift_row,items);
        setListAdapter(adapter);
    	
    	viewSchedule = new Runnable(){
        	public void run(){
        		GetSchedules();
        	}
        };
        
       Thread t = new Thread(null,viewSchedule,"MagentoBackground");
       t.start();
       progressDialog = ProgressDialog.show(LiftbookDisplayDayActivity.this, "Please wait...", "Retrieving data ...", true);
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
	
	public void GetSchedules(){
   	 	LiftbookDataHelper helper = new LiftbookDataHelper(this);
   	 	items = helper.GetWorkoutItems(selectedDate);
   	 	
   	 	/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}*/
 	 
		runOnUiThread(returnRes);
	}
	
	private class WorkoutItemEntryAdapter extends ArrayAdapter<WorkoutItem> {

		ArrayList<WorkoutItem> items;
		
		public WorkoutItemEntryAdapter(Context context, int workoutListItem,
				ArrayList<WorkoutItem> items) {
			super(context, workoutListItem, items);
			this.items = items;
			
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inf = getLayoutInflater();
    		View v = null;
    		
    		if (convertView == null) {
    			v = inf.inflate(R.layout.day_lift_row, parent,false);
            }else{
            	v = convertView;
            }

    		WorkoutItem entry = items.get(position);
    		v.setTag(entry);
    		
    		if(entry != null){
    			
    			TextView tt = (TextView) v.findViewById(R.id.toptext);
    			TextView sets = (TextView) v.findViewById(R.id.setslabel);
    			TextView onerm = (TextView) v.findViewById(R.id.onereptotallabel);
    			TextView donerm = (TextView) v.findViewById(R.id.dayonereptotallabel);
    			TextView ton = (TextView) v.findViewById(R.id.tonnagelabel);
    			TextView tonl = (TextView) v.findViewById(R.id.textView3);
    			CheckBox box = (CheckBox) v.findViewById(R.id.checkBox1);
    			
    			if(entry.isLift()){
    				tt.setText(entry.getName());
    				sets.setText(Integer.toString(entry.getSets())+(entry.getSets() > 1 ? " Sets": " Set"));
    				onerm.setText("All Time 1RM "+Float.toString(entry.getOneRepMax())+" ("+CalculationHelper.GetNearestBarWeight(entry.getOneRepMax())+")");
    				donerm.setText("1RM "+Float.toString(entry.getTodayOneRepMax())+" ("+CalculationHelper.GetNearestBarWeight(entry.getTodayOneRepMax())+")");
    				ton.setText(Float.toString(entry.getTonnage()));
    				box.setChecked(entry.isCompleted());
    			}else{
    				tt.setText(entry.getName());
    				sets.setText(Float.toString(entry.getMiles())+" miles");
    				onerm.setText(Float.toString(entry.getMph())+" mph");
    				//onerm.setVisibility(TextView.INVISIBLE);
    				donerm.setText(Float.toString(entry.getPace())+" minute miles");
    				ton.setText(Integer.toString(entry.getHours())+":"+Integer.toString(entry.getMinutes())+":"+Integer.toString(entry.getSeconds()));
    				tonl.setText("Time:");
    				//ton.setText(Float.toString(entry.getPace())+" mm");
    				//box.setVisibility(CheckBox.GONE);
    				box.setChecked(entry.isCompleted());
    			}
    			
    		}
    		
    		return v;
		}
		
	}

	@Override
	protected void onStart() {
		//Log.d("Activity","On Start Called");
		RefreshList();
		super.onStart();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Extended Options");
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.context_menu, menu);
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case (R.id.delete):
			LiftbookDataHelper helper = new LiftbookDataHelper(this);
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			WorkoutItem item1 = (WorkoutItem) adapter.getItem(info.position);
			
			if(item1.isLift()){
				helper.DeleteLift(item1.getDate(),item1.getName());
			}else{
				helper.DeleteLift(item1.getRowId());
			}
			RefreshList();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.sub_additions_menu, menu);
		return super.onCreateOptionsMenu(menu);	
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case(R.id.addlift):
			Intent t = new Intent("com.latchd.CREATEOPENLIFT");
			t.putExtra(IntentExtraKeys.DATE_KEY, selectedDate);
			startActivity(t);
			return true;
		case(R.id.addrun):
			Intent rt = new Intent("com.latchd.CREATERUN");
			rt.putExtra(IntentExtraKeys.DATE_KEY, selectedDate);
			startActivity(rt);
			return true;
		case (R.id.delete):
			LiftbookDataHelper helper = new LiftbookDataHelper(this);
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			WorkoutItem item1 = (WorkoutItem) adapter.getItem(info.position);
			if(item1.isLift()){
				helper.DeleteLift(item1.getDate(),item1.getName());
			}else{
				helper.DeleteLift(item1.getRowId());
			}
			RefreshList();
			return true;
		default:
			return true;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		RefreshList();
		super.onActivityResult(requestCode, resultCode, data);
	}

	

	
}
