package org.freeasinspeech.kdelaf.ui;

import java.util.*;
import java.io.*;
import java.net.*;

import org.freeasinspeech.kdelaf.*; 

/**
 * 
 */
public class KConfigGroup
{
	protected String name;
	protected Hashtable data;
	protected LinkedList keysInOrder;
		
	
	public KConfigGroup(String name) {
		this.name = name;
		data = new Hashtable();
		keysInOrder = new LinkedList();
	}
	
	public KConfigGroup(KConfigGroup other) {
		this.name = other.name;
		Logger.log("todo : KConfigGroup :: true copy of other.keysInOrder + KConfig::save (ex name.tmp => mv name.tmp name");
		this.keysInOrder = other.keysInOrder;
		data = new Hashtable();
		for (Enumeration e = other.data.keys() ; e.hasMoreElements() ;) {
			String key = (String)e.nextElement();
			String value = other.get(key);
			add(key, value);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isValid() {
		return (data.size() > 0);
	}
	
	protected String expandEnvironement(String aValue) {
		// [!] todo seriously : find a way to get the env values
		// maybe the env cmd is protable
		return aValue.replaceAll("\\$HOME" , System.getProperty("user.home"));
	}
	
	public String get(String key) {
		String val = (String)data.get(key);
		if (val == null)
			val = "";
		return val;
	}
	
	public void set(String key, String value) {
		data.put(key, value);
	}
	
	protected KURL parseURL(String value) {
		KURL url = null;
		try {
			url = new KURL(expandEnvironement(value));
		}
		catch (KURL.KURLSyntaxException e) {
			Logger.error(e);
			url = null;
		}
		return url;
	}
	
	public int getInt(String key) {
		String strValue = get(key);
		int value;
		try {
			value = Integer.parseInt(strValue);
		}
		catch (Exception e) {
			value = 0;
		}
		return value;
	}
	
	public boolean getBoolean(String key) {
		String strValue = get(key).toLowerCase();
		if (strValue.equals("1"))
			strValue = "true";
		if (strValue.equals("yes"))
			strValue = "true";
		boolean value;
		try {
			value = Boolean.valueOf(strValue).booleanValue();
		}
		catch (Exception e) {
			value = false;
		}
		return value;
	}
	public void setBoolean(String key, boolean value) {
		set(key, value ? "true" : "false");
	}
	
	public KURL getURL(String key) {
		String strValue = get(key);
		return parseURL(strValue);
	}
	
	public KURL[] getURLs(String key) {
		String strValue = get(key);
		ArrayList tmp = new ArrayList();
		String[] strUrls = strValue.split(",");
		for (int i = 0; i < strUrls.length; i++) {
			KURL url = parseURL(strUrls[i]);
			if (url != null)
				tmp.add(url);
		}
		KURL[] urls = new KURL[tmp.size()];
		for (int i = 0; i < tmp.size(); i++) {
			urls[i] = (KURL)tmp.get(i);
		}
		return urls;
	}
	
	public void add(String key, String value) {
		data.put(key, value);
		keysInOrder.add(key);
	}
	
}