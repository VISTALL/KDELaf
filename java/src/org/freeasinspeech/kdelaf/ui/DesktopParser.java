package org.freeasinspeech.kdelaf.ui;

import java.util.*;
import java.io.*;
import java.util.regex.*;

/**
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class DesktopParser
{
	protected Hashtable entries;
	protected String error;
	protected String desktopFile;
	protected int lineNumer;
		
	protected String type;
	protected String version;
	protected String charset;
	protected String currentSection;
		
	
	protected byte lastByte;
	
	DesktopParser(String desktopFile) {
		this.desktopFile = desktopFile;
	}
	
	DesktopParser(File desktopFile) {
		this.desktopFile = desktopFile.getAbsolutePath();
	}
	
	protected void setError(String msg, int line) {
		error = desktopFile + ":" + line + ": " + msg + ".";
	}
	protected void setError(String msg) {
		error = desktopFile + ": " + msg + ".";
	}
	
	protected void addEntry(DesktopEntry entry) {
		if ( (type == null) && entry.key.equals("Type") )
			type = entry.value;
		else if ( (version == null) && entry.key.equals("Version") )
			version = entry.value;
		else if ( (charset == null) && entry.key.equals("Encoding") )
			charset = entry.value;
		else {
			entries.put(entry.key, entry.value);
		}
	}
	
	protected DesktopEntry readEntry(DataInputStream reader) {
		DesktopEntry entry = null;
		try {
			String line = readLine(reader);
			if (line != null) {
				boolean withError = false;
				int equalPos = line.indexOf('=');
				if (equalPos > 0) {
					String key = line.substring(0, equalPos);
					String value = line.substring(equalPos + 1);
					if (key.length() > 0)
						entry = new DesktopEntry(key, value);
					else
						withError = true;
				}
				else
					withError = true;
				if (withError)
					setError("Expecting \"key=value\", found \""+line+"\"", lineNumer);
			}
		}
		catch (Exception e) {
			entry = null;
			setError(e.toString(), lineNumer);
		}
		return entry;
	}
	
	protected String readSingleLine(DataInputStream reader) throws IOException {
		Vector v = new Vector(255, 255);
		if (lastByte > -1) {
			v.add(new Byte(lastByte));
			lastByte = -1;
		}
		try {
			byte b;
			boolean endOfLine = false;
			do {
				b = reader.readByte();
				if (b == (byte)'\n') {
					endOfLine = true;
					b = reader.readByte();
					if (b !=(byte)'\r')
						lastByte = b;
				}
				else if (b != (byte)'\r')
					v.add(new Byte(b));
			}
			while (!endOfLine);
		}
		catch (EOFException e) {
			if (v.size() == 0)
				return null;
		}
		byte[] buff = new byte[v.size()];
		for (int i =0; i < buff.length; i++)
			buff[i] = ((Byte)v.get(i)).byteValue();
		String line = null;
		if (charset == null)
			line = new String(buff);
		else {
			try {
				line = new String(buff, charset);
			}
			catch (UnsupportedEncodingException e) {
				line = new String(buff);
			}
		}
		return line;
	}
	
	protected String readLine(DataInputStream reader) throws IOException {
		String line = readSingleLine(reader);
		if (line != null) {
			lineNumer++;
			line = line.trim();
			if ((line.length() == 0) || line.startsWith("#"))
				line = readLine(reader);
			else if ( line.startsWith("[") && line.endsWith("]") ) {
				currentSection = line;
				if (line.toLowerCase().equals("[desktop entry]")) {
					line = "[Desktop Entry]";
					currentSection = line;
				}
				else
					line = readLine(reader);
			}
		}
		return line;
	}
	
	public boolean parse() {
		entries = new Hashtable();
		error = null;
		type = null;
		version = null;
		charset = null;
		currentSection = null;
		lineNumer = 0;
		lastByte = -1;
		File file = new File(desktopFile);
		if (file.exists() && file.isFile() && file.canRead()) {
			String line;
			try {
				DataInputStream reader = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
				
				// read the header
				line = readLine(reader);
				if ( (line == null) || (!line.equals("[Desktop Entry]")) ) {
					setError("Expecting \"[Desktop Entry]\" found \""+line+"\"", lineNumer);
					return false;
				}
				// read at least one entry
				DesktopEntry entry = readEntry(reader);
				if (entry == null) {
					setError("Expecting \"key=value\"", lineNumer);
					return false;
				}
				addEntry(entry);
				// read the rest of the file
				while ( (entry = readEntry(reader)) != null )
					addEntry(entry);
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
	
	public String getError() {
		return error;
	}
	
	protected boolean valueNotNull(String key) {
		try {
			return (entries.containsKey(key) && (((String)entries.get(key)).length() > 0));
		}
		catch (Exception e) {
			// null values
			return false;
		}
	}
	
	public boolean isMimeItem() {
		boolean isAMimeItem = ((type != null) && type.equals("MimeType") && valueNotNull("MimeType"));
		if (isAMimeItem) {
			// hack for inodes
			String mimeType = (String)entries.get("MimeType");
			if (mimeType.startsWith("inode/") && (!valueNotNull("Patterns"))) {
				entries.put("Patterns", mimeType);
			}
		}
		return (isAMimeItem && valueNotNull("Patterns"));
	}
	
	protected String[] getMimeItemExt() {
		Vector v = new Vector(255, 255);
		String ext =  (String)entries.get("Patterns");
		String[] splited = ext.split(";");
		for (int i = 0; i < splited.length; i++) {
			splited[i] = splited[i].trim();
			if (splited[i].startsWith("*."))
				splited[i] = splited[i].substring(2).trim();
			if (splited[i].length() > 0) {
				splited[i] = splited[i].toLowerCase();
				if (!v.contains(splited[i]))
					v.add(splited[i]);
			}
		}
		String[] val = new String[v.size()];
		for (int i = 0; i < val.length; i++)
			val[i] = (String)v.get(i);
		return val;
	}
	
	public KMimeItem getMimeItem() {
		KMimeItem item = null;
		if (isMimeItem()) {
			item = new KMimeItem(
				getMimeItemExt(),
				(String)entries.get("Comment"),
				(String)entries.get("Comment["+KLocale.getLanguage()+"]"),
				(String)entries.get("MimeType"),
				(String)entries.get("Icon")
			);
		}
		return item;
	}
	
	public String get(String key) {
		return (String)entries.get(key);
	}
	
	class DesktopEntry
	{
		public String key;
		public String value;
			
		DesktopEntry(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}
}
