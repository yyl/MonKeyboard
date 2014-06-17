package com.yyl.inputmethod.latin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import android.R;
import android.content.Context;
import android.os.Environment;

public class Logger {
	private static final String LOG_PATH = "/keyboard_monitoring";
	private File logFile;
	private static File directory = new File(
			Environment.getExternalStorageDirectory(), LOG_PATH);
	private Calendar calendar;
	private HashMap<String, String> hm;
	private Context context;
	private String fname;

	public Logger(String filename) {
		this.fname = filename;
		hm = new HashMap<String, String>();
		try {
			mappingKey();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
	}

	public void appendWithTime(String text) {
		calendar = Calendar.getInstance();
		long millis = calendar.getTimeInMillis();
		String time = parseTime(millis);
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(time + "," + text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void appendWithTimeAndKey(String text) {
		calendar = Calendar.getInstance();
		String time = parseTime(calendar.getTimeInMillis());
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(time + "@ " + calendar.getTimeInMillis() + " " + text
					+ " " + hm.get(text));
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void mappingKey() throws IOException {
		InputStream raw = context.getAssets().open("keymap.txt");
		InputStreamReader inputreader = new InputStreamReader(raw);
		BufferedReader buffreader = new BufferedReader(inputreader);
		String line;

		try {
			while ((line = buffreader.readLine()) != null) {
				String item[] = line.split(" ");
				System.out.println("code: " + item[0] + " label: " + item[1]);
				hm.put(item[0], item[1]);
			}
			inputreader.close();
			buffreader.close();
		} catch (IOException e) {
			e.printStackTrace();
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
		String format = "HH::mm::ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		String gmtTime = sdf.format(t);
		return gmtTime;
	}
}