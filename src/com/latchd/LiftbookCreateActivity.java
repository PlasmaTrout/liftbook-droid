package com.latchd;

import java.sql.Date;
import java.util.ArrayList;

import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.liftbook.data.ScheduleEntry;
import com.latchd.liftbook.data.WorkoutItem;

import android.app.ListActivity;
import android.app.ProgressDialog;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class LiftbookCreateActivity extends ListActivity {
    /** Called when the activity is first created. */
	
	private ProgressDialog progressDialog = null; 
	ScheduleEntryAdapter adapter;
	ArrayList<ScheduleEntry> entries;
	private Runnable viewSchedule;
	private int currentIndex;
	private String clause = "date >= date('now','-3 day') and date <= date('now','+3 day')";
	private boolean allComplete;
	LiftbookDataHelper helper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       this.getListView().setOnItemClickListener(new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			TextView view = (TextView) arg1.findViewById(R.id.toptext);
			Toast t = Toast.makeText(getApplicationContext(), view.getText(), Toast.LENGTH_SHORT);
			
			t.show();
			
			Intent intent = new Intent(getApplicationContext(),LiftbookDisplayDayActivity.class);
			intent.putExtra("Date",view.getTag().toString());
			startActivity(intent);
			
		}
       });
        
        this.setTitle("Liftbook - Show Week (-3,Today,+3)");
        registerForContextMenu(this.getListView());
    }
    
    private void RefreshList(){
    	
    	entries = new ArrayList<ScheduleEntry>();
        
        //ListView list = (ListView) this.findViewById(R.layout.schedule_list_item);
        adapter = new ScheduleEntryAdapter(this,R.layout.row,entries);
        setListAdapter(adapter);
    	
    	viewSchedule = new Runnable(){
        	public void run(){
        		GetSchedules();
        	}
        };
        
       Thread t = new Thread(null,viewSchedule,"MagentoBackground");
       t.start();
       progressDialog = ProgressDialog.show(LiftbookCreateActivity.this, "Please wait...", "Retrieving data ...", true);
       
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	MenuInflater inf = getMenuInflater();
    	inf.inflate(R.menu.main_menu, menu);
    	
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case R.id.addlift:
    		break;
    	case R.id.openprotocol:
    		//Intent openIntent = new Intent(this,LiftbookCreateLiftActivity.class);
    		Intent openIntent = new Intent("com.latchd.CREATEOPENLIFT");
    		startActivity(openIntent);
    		break;
    	case R.id.slfivebyfive:
    		Intent slIntent = new Intent("com.latchd.CREATESTRONGLIFT");
    		//slIntent.addCategory("com.latchd.WORKOUTS");
    		startActivity(slIntent);
    		break;
    	case R.id.wendler:
    		Intent wIntent = new Intent("com.latchd.CREATEWENDLERLIFT");
    		startActivity(wIntent);
    		break;
    	case R.id.ssbasic:
    		Intent ssIntent = new Intent("com.latchd.CREATESSBASICLIFT");
    		startActivity(ssIntent);
    		break;
    	case R.id.addrun:
    		startActivity(new Intent("com.latchd.CREATERUN"));
    		break;
    	case R.id.today:
    		this.getListView().setSelection(currentIndex);
    		clause = "date=date('"+new Date(System.currentTimeMillis()).toString()+"')";
    		RefreshList();   
    		this.setTitle("LiftBook - "+item.getTitle());
    		break;
    	case R.id.yearview:
    		clause = "strftime('%Y',date)=strftime('%Y',date('now'))";
    		RefreshList();
    		this.setTitle("LiftBook - "+item.getTitle());
    		break;
    	case R.id.month:
    		clause = "date >= date('now','start of month') and date < date('now','start of month','+1 month')";
    		//clause = "";
    		RefreshList();
    		this.setTitle("Liftbook - "+item.getTitle());
    		break;
    	case R.id.lastsevendays:
    		clause = "date >= date('now','-3 day') and date <= date('now','+3 day')";
    		RefreshList();
    		this.setTitle("LiftBook - "+item.getTitle());
    		break;
    	case R.id.showall:
    		clause = "";
    		RefreshList();
    		this.setTitle("LiftBook - "+item.getTitle());
    		break;
    	case R.id.settings:
    		startActivity(new Intent("com.latchd.liftbook.SETTINGS"));
    		break;
    	default:
    		Toast t = Toast.makeText(this, item.getTitle(),Toast.LENGTH_SHORT);
        	t.show();
    	}
    	
    	//this.setTitle("LiftBook - "+item.getTitle());
    	
    	return true;
    }
    
    private Runnable returnRes = new Runnable(){
    	public void run(){
    		if(entries != null && entries.size() > 0){
    			adapter.notifyDataSetChanged();
    			for(int i=0;i < entries.size();i++){
    				adapter.add(entries.get(i));
    			}
    			
    			Toast t = Toast.makeText(LiftbookCreateActivity.this, entries.size()+" entries found",Toast.LENGTH_SHORT);
    			t.show();
    			
    		}else{
    			
    			Toast t = Toast.makeText(LiftbookCreateActivity.this, "You have nothing scheduled this week, please use the menu to add some items",Toast.LENGTH_LONG);
    			t.show();
    		}
    		progressDialog.dismiss();
    		adapter.notifyDataSetChanged();
    	}
    };
    
    private void GetSchedules(){
    	 helper = new LiftbookDataHelper(this);
    	 entries = helper.GetSchedules(clause);
    	 
    	 
    	 //try {
			//Thread.sleep(1000);
		//} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
    	 
    	 runOnUiThread(returnRes);
    }
    
     private class ScheduleEntryAdapter extends ArrayAdapter<ScheduleEntry> {

    	private ArrayList<ScheduleEntry> items;
    	public ScheduleEntryAdapter(LiftbookCreateActivity context,
    			int scheduleListItem, ArrayList<ScheduleEntry> items) {
    		super(context,scheduleListItem,items);
    		this.items = items;
    		
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent){
    		
    		LayoutInflater inf = getLayoutInflater();
    		View v = null;
    		
    		if (convertView == null) {
    			v = inf.inflate(R.layout.row, parent,false);
            }else{
            	v = convertView;
            }

    		ScheduleEntry entry = items.get(position);
    		
    		if(entry != null){
    			v.setTag(entry.getDate());
    			
    			Date d = new Date(System.currentTimeMillis());
    			if(d.toString().equals(entry.getDate())){
    				currentIndex = position;
    			}
    			
    			TextView tt = (TextView) v.findViewById(R.id.toptext);
    			TextView bl1 = (TextView) v.findViewById(R.id.bottomlabel1);
    			TextView bt1 = (TextView) v.findViewById(R.id.bottomtext1);
    			TextView bl2 = (TextView) v.findViewById(R.id.bottomlabel2);
    			TextView bt2 = (TextView) v.findViewById(R.id.bottomtext2);
    			TextView bt3 = (TextView) v.findViewById(R.id.bottomtext3);
    			CheckBox box = (CheckBox) v.findViewById(R.id.checkBox1);
    			
    			ArrayList<WorkoutItem> items = helper.GetWorkoutItems(entry.getDate());
    			allComplete = LiftbookDataHelper.CheckWorkoutsForCompletion(items);
    		
    			if(bt1 != null){
    		
    				if(entry.getLiftCount() == 0){
    					bl1.setVisibility(View.GONE);
    					bt1.setVisibility(View.GONE);
    				}else{
    					bl1.setVisibility(View.VISIBLE);
    					bt1.setVisibility(View.VISIBLE);
    					if(entry.getLiftCount() == 1){
    						bl1.setText("Lift Set");
    					}else{
    						bl1.setText("Lift Sets");
    					}
    				}
    				
    				if(entry.getRunCount() == 0){
    					bl2.setVisibility(View.GONE);
    					bt2.setVisibility(View.GONE);
    				}else{
    					bl2.setVisibility(View.VISIBLE);
    					bt2.setVisibility(View.VISIBLE);
    				}
    				
    				String bottomString1 = String.valueOf(entry.getLiftCount());
    				String bottomString2 = String.valueOf(entry.getRunCount());
    				String bottomString3 = entry.getProtocol();
    				
    				box.setChecked(allComplete);
    				tt.setText(ScheduleDateHelper.GetReadableScheduleDate(entry.getDate()));
    				tt.setTag(entry.getDate());
    				bt1.setText(bottomString1);
    				bt2.setText(bottomString2);
    				bt3.setText(bottomString3);
    			}
    		
    		}
    		return v;
    	}
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//RefreshList();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		RefreshList();
		super.onStart();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
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
			//WorkoutItem item1 = (WorkoutItem) adapter.getItem(info.position);
			ScheduleEntry entry = (ScheduleEntry) adapter.getItem(info.position);
			
			if(entry != null){
				helper.DeleteAll(entry.getDate());
			}
			
			RefreshList();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

}