package org.freeasinspeech.kdelaf;

import java.io.File;
/**
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class Logger
{
	/** Sets this to true if you want the log to appear also on the console. */
	private static final boolean STD_ERR = false;
		
	private static File logFile = null;
		
	public static void log(String message) {
		log("[InfoF]   ", message);
	}
	
	public static void warning(String message) {
		log("[WarningF]", message);
	}
	
	public static void error(String message) {
		log("[ErrorF]  ", message);
	}
	public static void logWorker(String message) {
		log("[InfoW]   ", message);
	}
	
	public static void warningWorker(String message) {
		log("[WarningW]", message);
	}
	
	public static void errorWorker(String message) {
		log("[ErrorW]  ", message);
	}
	
	public static void error(Throwable t) {
		error(t.toString());
		StackTraceElement[] trace = t.getStackTrace();
		for (int i = 0; i < trace.length; i++)
			error(">>" + trace[i].toString());
	}
	
	public static void criticalError(String message) {
		error(message);
		log("Stopping excecution");
	}
	
	public static void criticalError(Throwable t) {
		error(t);
		log("Stopping excecution");
	}
	
	public static String getFilePath() {
		return (logFile != null) ? logFile.getAbsolutePath() : "";
	}
	
	private static synchronized void log(String preffix, String message) {
		init();
		java.util.Calendar now = java.util.Calendar.getInstance();
		
		String date = now.get(java.util.Calendar.YEAR) + "/" + (now.get(java.util.Calendar.MONTH) + 1) + "/" + now.get(java.util.Calendar.DAY_OF_MONTH);
		date += "@" + now.get(java.util.Calendar.HOUR_OF_DAY) + ":" + now.get(java.util.Calendar.MINUTE) + ":" + now.get(java.util.Calendar.SECOND);
		
		String line = preffix + " " + date + "\t" + message;
		
		boolean stdErr = STD_ERR;
		if (logFile != null) {
			try {
				java.io.BufferedWriter out = new java.io.BufferedWriter(new java.io.FileWriter(logFile, true));
				out.write(line, 0, line.length());
				out.newLine();
				out.flush();
				out.close();
			}
			catch (Exception e) {
				stdErr = true;
				System.err.println("Unable to write to \"" + getFilePath() + "\".");
				System.err.flush();
			}
		}
		else
			stdErr = true;
		if (stdErr) {
			System.err.println(line);
			System.err.flush();
		}
	}
	
	private static void init() {
		if (logFile == null) {
			try {
				logFile = File.createTempFile("KdeLAF", ".log");
			}
			catch (Exception e) {
				logFile = new File("/tmp/KdeLaf.log");
				try {
					logFile.createNewFile();
				}
				catch (Exception ee) {}
			}
		}
	}
}
