package com.latchd.templates;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import com.latchd.liftbook.data.ILiftStorageFactory;
import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.liftbook.data.LiftbookSettingsHelper;
import com.latchd.liftbook.data.ScheduledLift;

public class StrongLiftsBasicStorageFactory implements ILiftStorageFactory {
	public static final String LABEL = "StrongLifts 5x5";
	public int Save(Context context, ArrayList<ScheduledLift> lifts,boolean useWarmupSets) {
		
		LiftbookDataHelper helper = new LiftbookDataHelper(context);
		float defaultWeight = LiftbookSettingsHelper.GetDefaultWeight((Activity)context);
		
		if(useWarmupSets){
						
			for(ScheduledLift lift : lifts){
				
				if(!lift.getName().contains("Deadlift")){
					helper.StoredScheduledLift(context, new ScheduledLift(lift.getDate(), lift.getName()+" Warmup",2, 5, defaultWeight), LABEL);
					helper.StoredScheduledLift(context, new ScheduledLift(lift.getDate(), lift.getName()+" Warmup",2, 5, defaultWeight), LABEL);
					helper.StoredScheduledLift(context, new ScheduledLift(lift.getDate(), lift.getName()+" Warmup",1, 3, GetWarmupValue(lift.getWeight(),50)),LABEL);
					helper.StoredScheduledLift(context, new ScheduledLift(lift.getDate(), lift.getName()+" Warmup",1, 3, GetWarmupValue(lift.getWeight(),60)),LABEL);
					helper.StoredScheduledLift(context, new ScheduledLift(lift.getDate(), lift.getName()+" Warmup",1, 2, GetWarmupValue(lift.getWeight(),70)),LABEL);
				
				}else{
					helper.StoredScheduledLift(context, new ScheduledLift(lift.getDate(), lift.getName()+" Warmup",1, 3, GetWarmupValue(lift.getWeight(),70)),LABEL);
					helper.StoredScheduledLift(context, new ScheduledLift(lift.getDate(), lift.getName()+" Warmup",1, 3, GetWarmupValue(lift.getWeight(),80)),LABEL);
					helper.StoredScheduledLift(context, new ScheduledLift(lift.getDate(), lift.getName()+" Warmup",1, 2, GetWarmupValue(lift.getWeight(),90)),LABEL);
				}
				
				for(int x=0;x < lift.getSets();x++){
					helper.StoredScheduledLift(context,lift,LABEL);
				}
			}	
			
		}else{
			for(int i=0; i < lifts.size();i++){
				for(int x=0;x < lifts.get(i).getSets();x++){
					helper.StoredScheduledLift(context,lifts.get(i),"StrongLifts 5x5");
				}
			}
		}
		return 0;
	}
	
	private int GetWarmupValue(float totalWeight, float percentage){
		 double value = totalWeight * ((double)percentage / 100.00);
         int adjustedWeight = (int) ((int)Math.round(value / 5.0)) * 5;
		 return adjustedWeight;
	}

	

}
