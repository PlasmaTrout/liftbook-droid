package com.latchd.templates;

import java.util.ArrayList;

import android.content.Context;

import com.latchd.liftbook.data.ILiftStorageFactory;
import com.latchd.liftbook.data.LiftbookDataHelper;
import com.latchd.liftbook.data.ScheduledLift;

public class StartingStrengthStorageFactory implements ILiftStorageFactory {

	private static final String LABEL = "Starting Strength";
	
	public int Save(Context context, ArrayList<ScheduledLift> lifts,
			boolean useWarmupSets) {
		int i=0;
		
		LiftbookDataHelper helper = new LiftbookDataHelper(context);
		
		if(useWarmupSets){
			for(ScheduledLift lift : lifts){
				if(lift.getName().contains("Deadlift")){
					
				}else{
					helper.StoreScheduledSet(context, lift.getDate(), lift.getName(),
							lift.getReps(), lift.getWeight(), LABEL);
				}
			}
		}
		
		return i;
	}

}
