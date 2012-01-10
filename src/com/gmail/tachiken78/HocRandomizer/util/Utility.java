package com.gmail.tachiken78.HocRandomizer.util;

import java.util.Calendar;

public class Utility {

	public static String getDate(){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		return(String.format("%04d/%02d/%02d %02d:%02d:%02d",
				year, (month + 1), day, hour, minute, second));
	}
}
