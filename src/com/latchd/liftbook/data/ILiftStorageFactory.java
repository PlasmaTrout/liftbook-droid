package com.latchd.liftbook.data;

import java.util.ArrayList;

import android.content.Context;

public interface ILiftStorageFactory {
	int Save(Context context,ArrayList<ScheduledLift> lifts,boolean useWarmupSets);
}
