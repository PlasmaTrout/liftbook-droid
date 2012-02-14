package com.latchd.templates;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.latchd.liftbook.data.ILiftStorageFactory;
import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.liftbook.data.ScheduledLift;

public class WendlerBasicStorageFactory implements ILiftStorageFactory {
	public static final String LABEL = "Wendler 531";

	public int Save(Context context, ArrayList<ScheduledLift> lifts,
			boolean useWarmupSets) {
		LiftbookDataHelper helper = new LiftbookDataHelper(context);
		
		double[] weekonepct = { 0.65, 0.75, 0.85 };
        double[] weektwopct = { 0.70, 0.80, 0.90 };
        double[] weekthrpct = { 0.75, 0.85, 0.95 };
        
        double[][] weeks = { weekonepct, weektwopct, weekthrpct };
        
        int[] reps = { 5, 5, 5 };

        for(ScheduledLift lift : lifts){
			
			if(!lift.getName().contains("Assistance")){
				
				if(useWarmupSets){
					
		        	helper.StoredScheduledLift(context,
		        			GetScheduledWarmupLift(lift.getDate(), lift.getName(), lift.getWeight(), 5, 40),
		        			LABEL);
		        	helper.StoredScheduledLift(context,
		        			GetScheduledWarmupLift(lift.getDate(), lift.getName(), lift.getWeight(), 5, 50),
		        			LABEL);
		        	
		        	if(lift.getWeek() == 4){
		        		helper.StoredScheduledLift(context,
			        			GetScheduledWarmupLift(lift.getDate(), lift.getName(), lift.getWeight(), 5, 60),
			        			LABEL);
		        	}else{
		        		helper.StoredScheduledLift(context,
		        			GetScheduledWarmupLift(lift.getDate(), lift.getName(), lift.getWeight(), 3, 60),
		        			LABEL);
		        	}
		        }
				
				Log.d("Wendler","Storing Wendler Week "+Integer.toString(lift.getWeek()));
				
				if(lift.getWeek() < 4 && lift.getWeek() > 0){
					switch (lift.getWeek())
					{
		            case 1:
		                reps = new int[] { 5, 5, 5 };
		                break;
		            case 2:
		                reps = new int[] { 3, 3, 3 };
		                break;
		            case 3:
		                reps = new int[] { 5, 3, 1 };
		                break;
		            default:
		                break;
					}
				
					int week = lift.getWeek();
					int result1 = (int) (Math.ceil(lift.getWeight() * weeks[week - 1][0] / 5.0) * 5.0);
					int result2 = (int) (Math.ceil(lift.getWeight() * weeks[week - 1][1] / 5.0) * 5.0);
					int result3 = (int) (Math.ceil(lift.getWeight() * weeks[week - 1][2] / 5.0) * 5.0);
				
					helper.StoredScheduledLift(context,
						new ScheduledLift(lift.getDate(), lift.getName(), lift.getSets(), reps[0], (int) result1),
						LABEL);
					helper.StoredScheduledLift(context,
							new ScheduledLift(lift.getDate(), lift.getName(), lift.getSets(), reps[1], (int) result2),
						LABEL);
					helper.StoredScheduledLift(context,
						new ScheduledLift(lift.getDate(), lift.getName(), lift.getSets(), reps[2], (int) result3),
						LABEL);
				
				}
			}else{
				for(int i=0;i < lift.getSets();i++){
					ScheduledLift l = new ScheduledLift(lift.getDate(), lift.getName(), 1, lift.getReps(), lift.getWeight());
					helper.StoredScheduledLift(context, l, LABEL);
				}
			}
		}
			
			
		
		
		return 0;
	}
	
	private ScheduledLift GetScheduledWarmupLift(String date, String name, float weight, int reps, int percentage){
		double value = ((double)percentage / 100.00);
        //double adjustedWeight = (double)((int)Math.round(value / 5.0)) * 5;
		int result = (int) (Math.ceil(weight*value/5.0)*5.0);
		ScheduledLift lift = new ScheduledLift(date, name+ " Warmup", 1, reps, result);
		return lift;
	}

}
