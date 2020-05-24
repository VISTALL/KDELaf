package org.freeasinspeech.kdelaf.ui;

import java.util.*;
import java.io.*;
import java.util.regex.*;

import org.freeasinspeech.kdelaf.*; 

/**
 * 
 */
public class KLocale
{
	//protected static final String[] DICTIONARIES_TO_LOAD = {"kio", "kdelibs"};
	protected static KLocaleDictionary all = null;
	protected static KLocaleDictionary current = null;
	protected static Hashtable dictionaries = new Hashtable();
	protected static Locale userLocal = Locale.getDefault();
	protected static boolean isLoaded = false;
	protected static String[] paths = null;
	
	
	
	public static String i18n(String text) {
		load();
		return current.i18n(text, all);
	}	
	public static String i18n(String text, String[] replace) {
		load();
		return current.i18n(text, replace, all);
	}
	public static String i18n(String text, String replace1) {
		String[] replace = {replace1};
		return i18n(text, replace);
	}
	public static String _i18n(String prefix, String text) {
		load();
		return current._i18n(prefix, text, all);
	}	

	
	public static String getLanguage() {
		return userLocal.getLanguage();
	}
	
	public static String getLocaleCode() {
		return userLocal.toString();
	}
	
	public static boolean select(String name) {
		load();
		KLocaleDictionary dictionary = (KLocaleDictionary)dictionaries.get(name);
		if (dictionary == null) {
			if (loadDictionary(name))
				dictionary = (KLocaleDictionary)dictionaries.get(name);
		}
		if (dictionary != null)
			current = dictionary;
		return (dictionary != null);
	}
	
	public static String getSelected() {
		load();
		return current.name;
	}
	
	protected static boolean loadDictionary(String name) {
		boolean found = true;
		if (dictionaries.get(name) == null) {
			found = false;
			for (int i = 0; i < paths.length; i++) {
				found = found || explore(new File(paths[i] + File.separator + getLocaleCode()), name) || explore(new File(paths[i] + File.separator + getLanguage()), name);
			}
		}
		return found;
	}
	
	protected static void append(String locationOfMO) {
		MOParser parser = new MOParser(locationOfMO);
		String name = (new File(locationOfMO)).getName();
		name = name.substring(0, name.length() - 3); // remove ".mo"
		KLocaleDictionary dictionary = (KLocaleDictionary)dictionaries.get(name);
		if (dictionary == null) {
			dictionary = new KLocaleDictionary(name);
			dictionaries.put(name, dictionary);
		}
		parser.parse(dictionary, all);
	}
		
	protected static boolean maybeAppend(String locationOfMO, String name) {
		boolean ok = false;
		String fileName = (new File(locationOfMO)).getName();
		String wanted = name + ".mo";
		if (wanted.equals(fileName)) {
			ok = true;
			append(locationOfMO);
		}
		return ok;
	}
	
	protected static boolean explore(File path, String name) {
		boolean found = false;
		if (!path.isHidden()) {
			if (path.isDirectory()) {
				File[] childs = path.listFiles();
				for (int i = 0; i < childs.length; i++)
					found = found || explore(childs[i], name);
			}
			else if (path.isFile() && path.canRead()) {
				found = maybeAppend(path.getAbsolutePath(), name);
			}
		}
		return found;
	}
	
	protected synchronized static void load() {
		if (!isLoaded) {
			all = new KLocaleDictionary("all");
			dictionaries.put("all", all);
			current = all;
			try {
				paths = KdeLAF.getClient().getLocalePaths();
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				paths = new String[1];
				paths[0] = "/usr/share/locale";
			}
			loadDictionary("kdelibs");
			//[!] kfile* should ask for this dictionary
			loadDictionary("kio");
			isLoaded = true;
		}
	}

}

	
	class KLocaleDictionary
	{
		protected Hashtable dictionary = new Hashtable();
		public String name;
			
		public KLocaleDictionary(String name) {
			this.name = name;
		}
		public String i18n(String text, KLocaleDictionary fallBack) {
			return (know(text)) ? i18n(text) : fallBack.i18n(text);
		}	
		public String i18n(String text, String[] replace, KLocaleDictionary fallBack) {
			return (know(text)) ? i18n(text, replace) : fallBack.i18n(text, replace);
		}
		public String _i18n(String prefix, String text, KLocaleDictionary fallBack) {
			String key = "_: "+prefix+"\n" + text;
			if (know(text))
				return i18n(text);
			if (know(key))
				return i18n(key);
			return fallBack._i18n(prefix, text);
		}	
		public String i18n(String text) {
			String translation = (String)dictionary.get(text);
			if (translation == null)
				translation = text;
			return translation;
		}	
		public String i18n(String text, String[] replace) {
			String translation = i18n(text);
			for (int i = 0; i < replace.length; i++)
				translation = translation.replaceAll("%" + (i+1), replace[i]);
			return translation;
		}
		public String _i18n(String prefix, String text) {
			String key = "_: "+prefix+"\n" + text;
			if (know(text))
				return i18n(text);
			if (know(key))
				return i18n(key);
			return text;
		}
		public void put(String key, String value) {
			dictionary.put(key, value);
		}
		public String toString() {
			return "KLocaleDictionary["+name+"]";
		}
		public boolean know(String key) {
			return dictionary.containsKey(key);
		}
	}
	
	class MOParser
	{
		protected static final int[] multArray = {1, 256, 65536, 16777216};
		protected static final int BE_MAGIC = 0x950412de;
		protected static final int LE_MAGIC = 0xde120495;
		protected int magic = BE_MAGIC;
		protected int formatRevision;
		protected int nbrOfStr;
		protected int origOffset;
		protected int transOffset;
		protected int size;
		protected int offset;
		protected int pos = 0;
		protected LinkedList origInfos = new LinkedList();
		protected LinkedList transInfos = new LinkedList();
		
		protected String locationOfMO;
		
		protected String charsetName = null;
		
		MOParser(String locationOfMO) {
			this.locationOfMO = locationOfMO;			
		}
		
		
		public void parse(KLocaleDictionary dict1, KLocaleDictionary dict2) {
			try {
				RandomAccessFile is = new RandomAccessFile(new File(locationOfMO), "r");
				magic = readInt(is);
				formatRevision = readInt(is);
				nbrOfStr = readInt(is);
				origOffset = readInt(is);
				transOffset = readInt(is);
				size = readInt(is);
				offset = readInt(is);
				
				for (int i = 0; i < nbrOfStr; i++)
					addEntry(dict1, dict2, is, i);
				is.close();
			}
			catch (Exception e) {
				Logger.error(e);
			}
		}
		
		protected void extractCharsetFromHeader(String header) {
			try {
				Pattern p = Pattern.compile("charset=([^\\s]+)");
				Matcher m = p.matcher(header);
				if (m.find())
					charsetName = m.group(1);
			}
			catch (Exception e) {
				Logger.error(e);
			}
		}
		
		protected void addEntry(KLocaleDictionary dict1, KLocaleDictionary dict2, RandomAccessFile is, int entryIndex) throws IOException {
			int origStrLen, origStrOffset, tranStrLen, tranStrOffset;
			
			is.seek(origOffset + entryIndex * 8);
			origStrLen = readInt(is);
			origStrOffset = readInt(is);
			is.seek(transOffset + entryIndex * 8);
			tranStrLen = readInt(is);
			tranStrOffset = readInt(is);
			
			String origStr = readString(origStrOffset, origStrLen, is);
			String transStr = readString(tranStrOffset, tranStrLen, is);
			if (origStr.length() == 0) {
				// header
				extractCharsetFromHeader(transStr);
			}
			else {
				/*if (origStr.startsWith("Open"))
					System.out.println(origStr + "=>" + transStr);
				if (origStr.startsWith("Save As"))
					System.out.println(origStr + "=>" + transStr);*/
				/*if (origStr.startsWith("Click this button to create a new folder.")) {
					System.out.println(origStr + "=>" + transStr);
					System.exit(0);
				}*//*
				if ( (origStr.indexOf("zoom") > -1) && (transStr.indexOf("oom arri") > -1) && (transStr.length() < 16))
					System.out.println(origStr + "=>" + transStr);*/
					
				dict1.put(origStr, transStr);
				dict2.put(origStr, transStr);
			}
		}
		
		protected String readString(int offset, int len, RandomAccessFile is) throws IOException {
			byte[] bytes = new byte[len];
			is.seek(offset);
			is.readFully(bytes);
			if (charsetName != null)
				return new String(bytes, charsetName);
			else
				return new String(bytes);
		}
		
		protected byte[] reverse(byte[] data) {
			byte []val = new byte[data.length];
			for (int i = 0; i < data.length; i++) {
				val[i] = data[data.length - i - 1];
			}
			return val;
		}
		
		protected int readInt(RandomAccessFile is) throws IOException {
			int val = 0;
			if (magic == LE_MAGIC) {
				byte bVal[] = new byte[4];
				is.readFully(bVal);
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(reverse(bVal)));
				val = dis.readInt();
			}
			else
				val = is.readInt();
			pos += 4;
			return val;
		}
		
		class ListEntry
		{
			int offset;
			int length;
			String str = null;
			ListEntry(int offset, int length) {
				this.offset = offset;
				this.length = length;
			}
		}
	}
/*
        byte
             +------------------------------------------+
          0  | magic number = 0x950412de                |
             |                                          |
          4  | file format revision = 0                 |
             |                                          |
          8  | number of strings                        |  == N
             |                                          |
         12  | offset of table with original strings    |  == O
             |                                          |
         16  | offset of table with translation strings |  == T
             |                                          |
         20  | size of hashing table                    |  == S
             |                                          |
         24  | offset of hashing table                  |  == H
             |                                          |
             .                                          .
             .    (possibly more entries later)         .
             .                                          .
             |                                          |
          O  | length & offset 0th string  ----------------.
      O + 8  | length & offset 1st string  ------------------.
              ...                                    ...   | |
O + ((N-1)*8)| length & offset (N-1)th string           |  | |
             |                                          |  | |
          T  | length & offset 0th translation  ---------------.
      T + 8  | length & offset 1st translation  -----------------.
              ...                                    ...   | | | |
T + ((N-1)*8)| length & offset (N-1)th translation      |  | | | |
             |                                          |  | | | |
          H  | start hash table                         |  | | | |
              ...                                    ...   | | | |
  H + S * 4  | end hash table                           |  | | | |
             |                                          |  | | | |
             | NUL terminated 0th string  <----------------' | | |
             |                                          |    | | |
             | NUL terminated 1st string  <------------------' | |
             |                                          |      | |
              ...                                    ...       | |
             |                                          |      | |
             | NUL terminated 0th translation  <---------------' |
             |                                          |        |
             | NUL terminated 1st translation  <-----------------'
             |                                          |
              ...                                    ...
             |                                          |
             +------------------------------------------+

*/