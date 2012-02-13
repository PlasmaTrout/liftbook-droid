package com.latchd.liftbook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LiftbookDataOpenHelper extends SQLiteOpenHelper {

    static final int DB_VERSION = 5;
	private static final String DB_NAME="Liftbook";
	private static final String LIFT_TABLE_NAME = "Lifts";
	public static final String LIFT_VIEW_NAME = "V_Lifts";
	public static final String SCHEDULE_VIEW_NAME = "V_Schedule";
	public static final String MASTER_SCHEDULE_VIEW_NAME = "V_MasterSchedule";
	public static final String LIFTS_BY_NAME_VIEW_NAME = "V_LiftsByLiftname";
	public static final String RUNS_BY_TYPE_VIEW_NAME = "V_RunsByType";
	private static final String RUN_TABLE_NAME = "Runs";
	private static final String LIFT_TABLE_CREATE = "CREATE TABLE "+LIFT_TABLE_NAME+" ("+
	"id INTEGER PRIMARY KEY,date TEXT,protocol TEXT,liftname TEXT,is_complete INTEGER, reps INTEGER, weight INTEGER);";
	private static final String RUN_TABLE_CREATE = "CREATE TABLE "+RUN_TABLE_NAME+" (id INTEGER PRIMARY KEY,date TEXT DEFAULT(date('now')),"+
	"type TEXT DEFAULT('other'),duration_hours INTEGER DEFAULT(0),duration_minutes INTEGER DEFAULT(0),"+
	"duration_seconds INTEGER DEFAULT(0),miles REAL DEFAULT (0.0),resistance REAL DEFAULT(0.0),incline INTEGER DEFAULT(0),is_complete INTEGER DEFAULT(0));";
	private static final String LIFT_VIEW_CREATE = "CREATE VIEW "+LIFT_VIEW_NAME+" AS select id,date,protocol,liftname,"+
	"is_complete,reps,weight,round((weight*0.45359237),2) as Kilos,(reps*weight) as Tonnage,round(((weight*reps)*0.03333+weight),2) as OneRepMax,"+
	"cast(((weight*reps)*0.03333+weight) as INTEGER)/5*5 as BarOneRep from lifts;";
	private static final String SCHEDULE_VIEW_CREATE = "CREATE VIEW "+SCHEDULE_VIEW_NAME+" AS select date as Date,'Lifts' as Name,"+
	"protocol as Type,count(*) as Total from Lifts GROUP BY date UNION ALL select date,'Runs' as Name,"+
	"type as Type,count(*) as Total from Runs GROUP BY date;";
	private static final String MASTER_SCHEDULE_VIEW = "CREATE VIEW "+MASTER_SCHEDULE_VIEW_NAME+" AS select distinct(date) as Date,"+
	"(SELECT COUNT(*) FROM "+LIFT_TABLE_NAME+" where date=v.date) as LiftCount,"+
	"(SELECT protocol from "+LIFT_TABLE_NAME+" where date=v.date LIMIT 1) as Protocol,"+
	"(SELECT type from "+RUN_TABLE_NAME+" where date=v.date LIMIT 1) as RunType," +
	"(SELECT COUNT(*) FROM Runs where date=v.date) as RunsCount from "+SCHEDULE_VIEW_NAME+" v;";
	
	public static final String INSERT_LIFT = "INSERT INTO "+LIFT_TABLE_NAME+" VALUES(NULL,?,?,?,?,?,?,?);";
	public static final String UPDATE_LIFT = "UPDATE "+LIFT_TABLE_NAME+" set reps=?, weight=?, is_complete=? where id=?;";
	private static final String LIFTS_BY_NAME_VIEW = "CREATE VIEW "+LIFTS_BY_NAME_VIEW_NAME+" AS select date,liftname,tonnage,MAX(onerepmax) as baronerep,(SELECT max(onerepmax) from "+LIFT_VIEW_NAME+" where liftname=v.liftname) as alltimeonerep,count(*) as sets,is_complete,MAX(id) as id from "+LIFT_VIEW_NAME+" v group by liftname,date ORDER BY rowid;";
	public static final String DELETE_LIFT = "DELETE FROM "+LIFT_TABLE_NAME+" where id=?";
	public static final String DELETE_LIFT2 = "DELETE FROM "+LIFT_TABLE_NAME+" where date=? and liftname=?";
	public static final String DELETE_LIFT3 = "DELETE FROM "+LIFT_TABLE_NAME+" where date=?";
	
	public static final String RUNS_BY_TYPE_VIEW = "CREATE VIEW "+RUNS_BY_TYPE_VIEW_NAME+" AS select rowid,date,type,duration_hours,duration_minutes,duration_seconds,miles,is_complete,round(((duration_hours*60+duration_minutes)/miles)) as mm,round((60/((duration_hours*60+duration_minutes)/miles)),2) as mph from "+RUN_TABLE_NAME;
	public static final String INSERT_RUN = "insert into "+RUN_TABLE_NAME+" VALUES (NULL,?,?,?,?,?,?,0,0,0);";
	public static final String UPDATE_RUN = "UPDATE "+RUN_TABLE_NAME+" set duration_hours=?,duration_minutes=?,duration_seconds=?,is_complete=?,miles=? where id=?;";
	public static final String DELETE_RUN = "DELETE FROM "+RUN_TABLE_NAME+" where id=?";
	public static final String DELETE_RUN2 = "DELETE FROM "+RUN_TABLE_NAME+" where date=?";
	
	//version1.1 wendler fix
	public static final String WENDLER_ADDITION = "ALTER TABLE "+LIFT_TABLE_NAME+" ADD COLUMN week INTEGER;";
	public static final String WENDLER_UPDATE = "UPDATE "+LIFT_TABLE_NAME+" SET week=0;";
	
	public LiftbookDataOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(LIFT_TABLE_CREATE);
		db.execSQL(RUN_TABLE_CREATE);
		db.execSQL(LIFT_VIEW_CREATE);
		db.execSQL(SCHEDULE_VIEW_CREATE);
		db.execSQL(MASTER_SCHEDULE_VIEW);
		db.execSQL(LIFTS_BY_NAME_VIEW);
		db.execSQL(RUNS_BY_TYPE_VIEW);
		db.execSQL(WENDLER_ADDITION);
		db.execSQL(WENDLER_UPDATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion == 3){
			db.execSQL(WENDLER_ADDITION);
			db.execSQL(WENDLER_UPDATE);
		}else if (newVersion == 4){
			try{
			db.execSQL(WENDLER_ADDITION);
			db.execSQL(WENDLER_UPDATE);
			}catch(Exception e){
				
			}
		}else if(newVersion == 5){
			db.execSQL("DROP VIEW "+LIFTS_BY_NAME_VIEW_NAME);
			db.execSQL(LIFTS_BY_NAME_VIEW);
		}
	}

}
