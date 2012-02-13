package com.latchd.liftbook.data;

import com.latchd.templates.StrongLiftsBasicStorageFactory;
import com.latchd.templates.WendlerBasicStorageFactory;

public class LiftStorageFactory {

	public static final int OPEN_PROTOCOL = 0;
	public static final int SLFIVE_PROTOCOL = 1;
	public static final int WENDLER_PROTOCOL = 2;
	
	public static ILiftStorageFactory getFactory(int protocol){
		switch(protocol){
			case OPEN_PROTOCOL:
				return new OpenLiftStorageFactory();
			case SLFIVE_PROTOCOL:
				return new StrongLiftsBasicStorageFactory();
			case WENDLER_PROTOCOL:
				return new WendlerBasicStorageFactory();
			default:
				return new OpenLiftStorageFactory();
				
		}
	}
	
}
