package com.yyl.inputmethod.latin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.os.Environment;

public class Logger {
	private static final String LOG_PATH = "/monkeyboard";
	private File logFile;
	private static File directory = new File(
			Environment.getExternalStorageDirectory(), LOG_PATH);
	private Calendar calendar;
	private HashMap<Integer, String> hm;
	private Context context;
	private String fname;

	public Logger(String filename) {
		this.fname = filename;
		if (!directory.exists()) {
			directory.mkdirs();
		}
		logFile = new File(directory, filename);
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		hm = new HashMap<Integer, String>();
		hm.put(-1, "shift");
		hm.put(-2, "switch");
		hm.put(-4, "delete");
		hm.put(-7, "next");
		hm.put(-8, "prev");
		hm.put(-5, "config");
		hm.put(10, "enter");
		hm.put(32, "space");
	}

	public void append(String text) {
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public void appendWithTime(int code, String text) {
		calendar = Calendar.getInstance();
		long millis = calendar.getTimeInMillis();
		String time = parseTime(millis);
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(time + "," + mappingKey(code) + "," + text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String mappingKey(int code){
		if (hm.get(code) != null) {
			return hm.get(code);
		} else if (97 <= code && code <= 122) {
			return "lowercase";
		} else if (65 <= code && code <= 90) {
			return "uppercase";
		} else if (48 <= code && code <= 57) { 
			return "digit";
		} else if (code <= 126) {
			return "symbol";
		} else {
			return "special";
		}
	}

	public void renameFile() {
		// get current time
		calendar = Calendar.getInstance();
		String format = "yyyy-MM-dd-HH-mm-ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		String gmtTime = sdf.format(calendar.getTimeInMillis());
		// change file name according to the time
		File from = new File(directory, fname);
		File to = new File(directory, "Trial_" + gmtTime + ".txt");
		from.renameTo(to);
	}

	public String parseTime(long t) {
		String format = "yyyy-MM-dd-HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		sdf.setTimeZone(TimeZone.getDefault());
		String gmtTime = sdf.format(t);
		return gmtTime;
	}
}