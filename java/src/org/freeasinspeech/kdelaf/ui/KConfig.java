package org.freeasinspeech.kdelaf.ui;

import java.util.*;
import java.io.*;
import java.util.regex.*;

import org.freeasinspeech.kdelaf.*; 

/**
 * 
 */
public class KConfig
{
	protected static String[] confPaths = null;
		
	protected File confFile = null;
	protected Hashtable data;
	protected LinkedList groupsInOrder;
		
	public KConfig(String appName) {
		init(appName);
	}
		
	public KConfig(File confFile) {
		init(confFile);
	}
	
	public void init(String appName) {
		String[] paths = KConfig.getPaths();
		for (int i = 0; (i < paths.length) && (confFile == null); i++) {
			confFile = new File(paths[i], appName + "rc");
			if (!(confFile.exists() && confFile.isFile() && confFile.canRead()))
				confFile = new File(paths[i], appName);
			if (!(confFile.exists() && confFile.isFile() && confFile.canRead()))
				confFile = null;
		}
		init(confFile);
	}
	
	public void init(File confFile) {
		data = new Hashtable();
		groupsInOrder = new LinkedList();
		
		if (confFile != null) {
			KConfigParser parser = new KConfigParser();
			Logger.log("Parsing " + confFile.getAbsolutePath());
			if (!parser.parse(confFile, data, groupsInOrder))
				Logger.log("Parsing error " + parser.getError());
		}
	}
	
	public KConfigGroup getGroup(String group) {
		KConfigGroup gp = (KConfigGroup)data.get(group);
		if (gp == null)
			gp = new KConfigGroup(group);
		return gp;
	}	
	
	public static synchronized String[] getPaths() {
		if (confPaths == null) {
			try {
				confPaths = KdeLAF.getClient().getConfigPaths();
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				confPaths = new String[1];
				confPaths[0] = System.getProperty("user.home") + ".kde/share/config";
			}
		}
		return confPaths;
	}
	
	public static File getFile(String key) {
		File file = null;
		Logger.log("Todo : KConfig::getFile(String)");
		if (key.equals("documentPath"))
			file = new File("/home/blunted");
		else if (key.equals("desktopPath"))
			file = new File("/home/blunted/Desktop");
		return file;
	}
	
	class KConfigParser
	{
		protected String error;
		protected String fileName;
		protected String groupName;
		
		protected void setError(String msg, int line) {
			error = fileName + ":" + line + ": " + msg + ".";
		}
		
		protected void setError(String msg) {
			error = fileName + ": " + msg + ".";
		}
		
		protected String getError() {
			return error;
		}
			
		public boolean parse(File confFile, Hashtable data, LinkedList groupsInOrder) {
			fileName = confFile.getName();
			groupName = "dummy";
			if (confFile.exists() && confFile.isFile() && confFile.canRead()) {
				String line;
				error = null;
				try {
					BufferedReader reader = new BufferedReader(new FileReader(confFile));
					
					while ( (line = reader.readLine()) != null )
						maybeAddEntry(line.trim(), data, groupsInOrder);
					
					reader.close();
				}
				catch (Exception e) {
					setError(e.toString());
				}
			}
			else
				setError("The file is not readable");
			return (error == null);
		}
		
		public void maybeAddEntry(String line, Hashtable data, LinkedList groupsInOrder) {
			int equalPos = line.indexOf('=');
			if (equalPos > 0) {
				String key = line.substring(0, equalPos);
				String value = line.substring(equalPos + 1);
				if (key.length() > 0)
					addEntry(key, value, data);
			}
			else if ( line.startsWith("[") && line.endsWith("]") ) {
				groupName = line.substring(1, line.length() - 1);
				groupsInOrder.add(groupName);
			}
		}
		
		public void addEntry(String key, String value, Hashtable data) {
			KConfigGroup group = (KConfigGroup)data.get(groupName);
			if (group == null) {
				group = new KConfigGroup(groupName);
				data.put(groupName, group);
			}
			group.add(key, value);
		}
	}
}
