package com.latchd.liftbook.data;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

public class OpenLiftStorageFactory implements ILiftStorageFactory {

	public int Save(Context context, ArrayList<ScheduledLift> lifts,boolean useWarmupSets) {
		LiftbookDataHelper helper = new LiftbookDataHelper(context);
		for(int i=0; i < lifts.size();i++){
			for(int x=0;x < lifts.get(i).getSets();x++){
				helper.StoredScheduledLift(context,lifts.get(i),"Open");
			}
		}
		return 0;
	}

}
