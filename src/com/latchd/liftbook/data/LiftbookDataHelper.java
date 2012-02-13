package com.latchd.liftbook.data;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class LiftbookDataHelper {
	
	Context context;
	LiftbookDataOpenHelper dbHelper;
	
	public LiftbookDataHelper(Context context){
		this.context = context;
		dbHelper = new LiftbookDataOpenHelper(context);
	}
	
	public ArrayList<WorkoutItem> GetWorkoutItems(String date){
		SQLiteDatabase read = dbHelper.getReadableDatabase();
		Cursor c = read.query(LiftbookDataOpenHelper.LIFTS_BY_NAME_VIEW_NAME,null,"date=?",new String[]{ date },null,null,"id");
		ArrayList<WorkoutItem> items = new ArrayList<WorkoutItem>();
		
		if(c.moveToFirst()){
			do{
				WorkoutItem item = new WorkoutItem();
				item.setDate(c.getString(0));
				item.setName(c.getString(1));
				item.setTonnage(c.getInt(2));
				item.setTodayOneRepMax((float) c.getDouble(3));
				item.setOneRepMax((float) c.getDouble(4));
				item.setSets(c.getInt(5));
				item.setCompleted(c.getInt(6) > 0);
				item.setLift(true);
				items.add(item);
				
			}while(c.moveToNext());
		}
		c.close();
		
		Cursor c2 = read.query(LiftbookDataOpenHelper.RUNS_BY_TYPE_VIEW_NAME, null, "date=?", new String[]{ date }, null, null, "type");
		
		if(c2.moveToFirst()){
			do{
				WorkoutItem item = new WorkoutItem();
				item.setLift(false);
				item.setRowId(c2.getInt(0));
				item.setDate(c2.getString(1));
				item.setName(c2.getString(2));
				item.setHours(c2.getInt(3));
				item.setMinutes(c2.getInt(4));
				item.setSeconds(c2.getInt(5));
				item.setCompleted(c2.getInt(7) > 0);
				item.setMph(c2.getFloat(9));
				item.setPace(c2.getFloat(8));
				item.setMiles(c2.getFloat(6));
				items.add(item);
			}while(c2.moveToNext());
		}
		c2.close();
		
		read.close();
		return items;
	}
	
	public ArrayList<WorkoutItem> GetWorkoutItems(String date, String name){
		SQLiteDatabase read = dbHelper.getReadableDatabase();
		Cursor c = read.query(LiftbookDataOpenHelper.LIFT_VIEW_NAME,null,"date=? and liftname=?",new String[]{ date,name },null,null,"rowid");
		ArrayList<WorkoutItem> items = new ArrayList<WorkoutItem>();
		int i=1;
		if(c.moveToFirst()){
			do{
				
				WorkoutItem item = new WorkoutItem();
				item.setSets(i);
				item.setRowId(c.getInt(0));
				item.setDate(c.getString(1));
				item.setProtocol(c.getString(2));
				item.setName(c.getString(3));
				item.setCompleted(c.getInt(4) > 0);
				item.setReps(c.getInt(5));
				item.setWeight((float) c.getDouble(6));
				item.setTonnage((float) c.getDouble(8));
				item.setTodayOneRepMax((float) c.getDouble(9));
				//item.setOneRepMax(c.getInt(4));
				//item.setSets(c.getInt(5));
				item.setLift(true);
				
				items.add(item);
				i++;
			}while(c.moveToNext());
		}
		c.close();
		read.close();
		return items;
	}
	
	public WorkoutItem GetWorkoutItems(int rowId,boolean isLift){
		WorkoutItem item = new WorkoutItem();
		SQLiteDatabase read = dbHelper.getReadableDatabase();
		
		if(isLift){
			Log.d("SQL", "Finding rowId "+rowId+". Should be a lift.");
			extractLift(rowId, item, read);
		}else{
			Log.d("SQL", "Finding rowId "+rowId+". Should be a run.");
			extractRun(rowId, item, read);
		}
		
		read.close();
		return item;
	}

	private void extractRun(int rowId, WorkoutItem item,
			SQLiteDatabase read) {
		Cursor c2 = read.query(LiftbookDataOpenHelper.RUNS_BY_TYPE_VIEW_NAME, null, "id=?", new String[]{ String.valueOf(rowId) }, null, null, null);
		
		if(c2.moveToFirst()){
			do{
				Log.d("SQL","Loading up run item.");
				//item = new WorkoutItem();
				item.setLift(false);
				item.setRowId(c2.getInt(0));
				item.setDate(c2.getString(1));
				item.setName(c2.getString(2));
				item.setHours(c2.getInt(3));
				item.setMinutes(c2.getInt(4));
				item.setSeconds(c2.getInt(5));
				item.setCompleted(c2.getInt(7) > 0);
				item.setMph(c2.getFloat(9));
				item.setPace(c2.getFloat(8));
				item.setMiles(c2.getFloat(6));
				//items.add(item);
			}while(c2.moveToNext());
		}
		c2.close();
		
	}

	private void extractLift(int rowId, WorkoutItem item, SQLiteDatabase read) {
		Cursor c = read.query(LiftbookDataOpenHelper.LIFT_VIEW_NAME,null,"id=?",new String[]{ String.valueOf(rowId) },null,null,null);
		
		
		if(c.moveToFirst()){
			do{
				Log.d("SQL","Loading up lift item.");
				item.setRowId(c.getInt(0));
				item.setDate(c.getString(1));
				item.setProtocol(c.getString(2));
				item.setName(c.getString(3));
				item.setCompleted(c.getInt(4) > 0);
				item.setReps(c.getInt(5));
				item.setWeight((float) c.getDouble(6));
				item.setTonnage((float) c.getDouble(8));
				item.setTodayOneRepMax((float) c.getDouble(9));
				item.setLift(true);
				
			}while(c.moveToNext());
		}
		c.close();
	}
	
	public ArrayList<ScheduleEntry> GetSchedules(){
		ArrayList<ScheduleEntry> list = new ArrayList<ScheduleEntry>();
		Cursor c = null;
		SQLiteDatabase read = dbHelper.getReadableDatabase();
		c = read.query(LiftbookDataOpenHelper.MASTER_SCHEDULE_VIEW_NAME, null, null, null, null, null, "Date");
		if(c.moveToFirst()){
			do{
				ScheduleEntry entry = new ScheduleEntry();
				entry.setDate(c.getString(0));
				
				String protocol = c.getString(2);
				String runType = c.getString(3);
								
				if(protocol == null){
					entry.setProtocol(runType);
				}else{
					if(runType != null){
						entry.setProtocol(protocol+" + "+runType);
					}else{
						entry.setProtocol(protocol);
					}
				}
				
				
				entry.setLiftCount(c.getInt(1));
				entry.setRunCount(c.getInt(4));
				list.add(entry);
			}while(c.moveToNext());
		}
		c.close();
		read.close();
		return list;
	}
	
	public ArrayList<ScheduleEntry> GetSchedules(String clause){
		ArrayList<ScheduleEntry> list = new ArrayList<ScheduleEntry>();
		Cursor c = null;
		SQLiteDatabase read = dbHelper.getReadableDatabase();
		c = read.query(LiftbookDataOpenHelper.MASTER_SCHEDULE_VIEW_NAME,null,clause, null, null, null, "Date");
		
		if(c.moveToFirst()){
			do{
				ScheduleEntry entry = new ScheduleEntry();
				entry.setDate(c.getString(0));
				
				String protocol = c.getString(2);
				String runType = c.getString(3);
								
				if(protocol == null){
					entry.setProtocol(runType);
				}else{
					if(runType != null){
						entry.setProtocol(protocol+" + "+runType);
					}else{
						entry.setProtocol(protocol);
					}
				}
				
				
				entry.setLiftCount(c.getInt(1));
				entry.setRunCount(c.getInt(4));
				list.add(entry);
			}while(c.moveToNext());
		}
		c.close();
		read.close();
		return list;
	}
	
	public ArrayList<String> GetScheduleStrings(){
		ArrayList<String> list = new ArrayList<String>();
		Cursor c = null;
		SQLiteDatabase read = dbHelper.getReadableDatabase();
		c = read.query(LiftbookDataOpenHelper.MASTER_SCHEDULE_VIEW_NAME, null, null, null, null, null, "Date");
		if(c.moveToFirst()){
			do{	
				list.add(c.getString(0));
			}while(c.moveToNext());
		}
		c.close();
		read.close();
		return list;
	}

	public int StoreScheduledSet(Context context, String date, String liftName,
			int reps,float weight, String protocol){
		String insertStmt = LiftbookDataOpenHelper.INSERT_LIFT;
		LiftbookDataOpenHelper helper = new LiftbookDataOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteStatement stmt = db.compileStatement(insertStmt);
		
		stmt.bindString(1, date);
		stmt.bindString(2, protocol);
		stmt.bindString(3, liftName);
		stmt.bindLong(4, 0);
		stmt.bindLong(5, reps);
		stmt.bindDouble(6, weight);
		stmt.bindLong(7, 0);
		
		long result = stmt.executeInsert();
		stmt.close();
		db.close();
		return (int) result;
	}
	
	public int StoredScheduledLift(Context context, ScheduledLift lift,String protocol){
		String insertStmt = LiftbookDataOpenHelper.INSERT_LIFT;
		LiftbookDataOpenHelper helper = new LiftbookDataOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteStatement stmt = db.compileStatement(insertStmt);
		
		
		stmt.bindString(1, lift.getDate());
		stmt.bindString(2, protocol);
		stmt.bindString(3, lift.getName());
		stmt.bindLong(4, 0);
		stmt.bindLong(5, lift.getReps());
		stmt.bindDouble(6, lift.getWeight());
		stmt.bindLong(7, lift.getWeek());
		
		long result = stmt.executeInsert();
		stmt.close();
		db.close();
		return (int) result;
	}
	
	public int StoreScheduledRun(Context context, String date, String type, float miles, int hours, int minutes, int seconds){
		String insertStmt = LiftbookDataOpenHelper.INSERT_RUN;
		LiftbookDataOpenHelper helper = new LiftbookDataOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteStatement stmt = db.compileStatement(insertStmt);
		
		stmt.bindString(1, date);
		stmt.bindString(2, type);
		stmt.bindLong(3, hours);
		stmt.bindLong(4, minutes);
		stmt.bindLong(5, seconds);
		stmt.bindDouble(6, miles);
		
		
		long result = stmt.executeInsert();
		stmt.close();
		db.close();
		return (int) result;
	}
	
	public void UpdateLiftDetail(Context context, int rowId, int reps, float weight, boolean isCompleted){
		String updateStmt = LiftbookDataOpenHelper.UPDATE_LIFT;
		LiftbookDataOpenHelper helper = new LiftbookDataOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteStatement stmt = db.compileStatement(updateStmt);
		
		stmt.bindLong(1, reps);
		stmt.bindDouble(2, weight);
		stmt.bindLong(3, isCompleted ? 1 : 0);
		stmt.bindLong(4, rowId);
		
		stmt.execute();
		stmt.close();
		db.close();
	}
	
	public void UpdateRunDetail(Context contact, int rowId, int hours, int minutes, int seconds, float miles, boolean isCompleted){
		String updateStmt = LiftbookDataOpenHelper.UPDATE_RUN;
		LiftbookDataOpenHelper helper = new LiftbookDataOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteStatement stmt = db.compileStatement(updateStmt);
		
		stmt.bindLong(1, hours);
		stmt.bindLong(2, minutes);
		stmt.bindLong(3, seconds);
		stmt.bindLong(4, isCompleted ? 1 : 0);
		stmt.bindDouble(5, miles);
		stmt.bindLong(6, rowId);

		stmt.execute();
		stmt.close();
		db.close();
	}

	public void DeleteLift(int rowId){
		String updateStmt = LiftbookDataOpenHelper.DELETE_LIFT;
		String updateRunStmt = LiftbookDataOpenHelper.DELETE_RUN;
		
		LiftbookDataOpenHelper helper = new LiftbookDataOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteStatement stmt = db.compileStatement(updateStmt);
		SQLiteStatement stmt2 = db.compileStatement(updateRunStmt);
		
		stmt.bindLong(1, rowId);
		stmt2.bindLong(1, rowId);
		
		stmt.execute();
		stmt2.execute();
		stmt.close();
		stmt2.close();
		db.close();
	}
	
	public void DeleteLift(String date, String exercise){
		String updateStmt = LiftbookDataOpenHelper.DELETE_LIFT2;
		LiftbookDataOpenHelper helper = new LiftbookDataOpenHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteStatement stmt = db.compileStatement(updateStmt);
		stmt.bindString(1, date);
		stmt.bindString(2, exercise);
		stmt.execute();
		stmt.close();
		db.close();
	}
	
	public void DeleteAll(String date){
		String updateStmt = LiftbookDataOpenHelper.DELETE_LIFT3;
		String updateStmt2 = LiftbookDataOpenHelper.DELETE_RUN2;
		
		LiftbookDataOpenHelper helper = new LiftbookDataOpenHelper(context);
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		SQLiteStatement stmt = db.compileStatement(updateStmt);
		stmt.bindString(1, date);
		stmt.execute();
		stmt.close();
		
		SQLiteStatement stmt2 = db.compileStatement(updateStmt2);
		stmt2.bindString(1, date);
		stmt2.execute();
		stmt2.close();
		
		db.close();
	}

	public static boolean CheckWorkoutsForCompletion(ArrayList<WorkoutItem> items){
		boolean result = true;
		
		for(WorkoutItem item : items){
			if(!item.isCompleted()){
				result = false;
				break;
			}
		}
		
		return result;
	}

	public float GetRecentMax(String exercise){
		float result = 0;
		SQLiteDatabase read = dbHelper.getReadableDatabase();
		Cursor c = read.query(LiftbookDataOpenHelper.LIFTS_BY_NAME_VIEW_NAME, new String[] {"baronerep"},"liftname=?", new String[] { exercise }, null, null,"date desc" );
		
		if(c.moveToFirst()){
			result = (float) c.getDouble(0);
		}
		c.close();
		read.close();
		return result;
	}
}
