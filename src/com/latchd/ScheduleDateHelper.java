package com.latchd;

import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ScheduleDateHelper {

	public static String GetReadableScheduleDate(String dbDate){
				
		Date d = Date.valueOf(dbDate);
		Format formatter = new SimpleDateFormat("EEE MMM dd yyyy",Locale.US);
		String date = formatter.format(d);
		return date;
	}
}
