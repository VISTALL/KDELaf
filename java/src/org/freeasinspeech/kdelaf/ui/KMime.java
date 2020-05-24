package org.freeasinspeech.kdelaf.ui;

import java.util.*;
import java.io.*;
import java.util.regex.*;

import org.freeasinspeech.kdelaf.*; 
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KMime
{
	protected static Hashtable dictionary = new Hashtable();
	protected static boolean isLoaded = false;
	
	protected static KMimeItem get(String key) {
		KMimeItem item = (KMimeItem)dictionary.get(key);
		return (item == null) ? null : new KMimeItem(item);
	}
	
	protected static KMimeItem getMimeByExtension(String extension) {
		load();
		KMimeItem item = get(extension.toLowerCase());
		if (item == null)
			item = get("???");
		return item;
	}
	protected static KMimeItem getMimeForFile(File file) {
		/*
			2 cases :
				- file with a given mime icon
				- file with no given mime icon
		*/
		// file with a given mime icon and file with no given mime icon
		return getMimeByExtension(Utilities.getExtension(file.getName()));
	}
	protected static KMimeItem getMimeForDirectory(File dir) {
		/*
			3 cases :
				- locked directory
				- directory with .directory
				- directory with no .directory
		*/
		load();
		KMimeItem item = null;
		if (!dir.canRead()) {
			// locked directory
			item = get("inode/directory-locked");
		}
		else {
			item = get("inode/directory");
			// search for a directory icon.
			File directoryIcon = new File(dir, ".directory");
			if (directoryIcon.exists() && directoryIcon.isFile() && directoryIcon.canRead()) {
				// directory with .directory
				DesktopParser parser = new DesktopParser(directoryIcon);
				if (parser.parse())
					item.setIconName(parser.get("Icon"));
			}
			else {
				// directory with no .directory (do nothing)
			}
		}
		// nothing worked
		if (item == null)
			item = get("???");
		return item;
	}
	
	public static KMimeItem getMime(File fileOrDir) {
		/*
			3 cases :
				- directory
				- file
				- the file or directory does not exists
		*/
		load();
		KMimeItem item = null;
		if (fileOrDir.exists()) {
			if (fileOrDir.isDirectory())
				// directory
				item = getMimeForDirectory(fileOrDir);
			else 
				// file
				item = getMimeForFile(fileOrDir);
		}
		else
			// the file or directory does not exists
			item = getMimeByExtension(Utilities.getExtension(fileOrDir.getName()));
		return item;
	}
	
	/**
	 * equivalent to getMime(new File(file)).
	 */
	public static KMimeItem getMime(String file) {
		return getMime(new File(file));
	}
	
	protected static void append(String locationOfDesktop) {
		DesktopParser parser = new DesktopParser(locationOfDesktop);
		if (parser.parse()) {
			KMimeItem item = parser.getMimeItem();
			if (item != null) {
				String[] ext = item.getExtensions();
				for (int i = 0; i < ext.length; i++)
					dictionary.put(ext[i], item);
			}
			else {
				//System.out.println("todo check : " + locationOfDesktop);
			}
		}
		/*else {
			System.out.println(parser.getError());
			System.exit(0);
		}*/
	}
	
	protected static void maybeAppend(String locationOfDesktop) {
		if (Utilities.getExtension(locationOfDesktop).equals("desktop")) {
			append(locationOfDesktop);
		}
	}
	
	protected static void explore(File path) {
		if (!path.isHidden()) {
			if (path.isDirectory()) {
				File[] childs = path.listFiles();
				for (int i = 0; i < childs.length; i++)
					explore(childs[i]);
			}
			else if (path.isFile() && path.canRead()) {
				maybeAppend(path.getAbsolutePath());
			}
		}
	}
	
	public synchronized static void load() {
		if (!isLoaded) {
			long time = (new java.util.Date()).getTime();
			String[] paths = null;
			try {
				paths = KdeLAF.getClient().getMimePaths();
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				paths = new String[1];
				paths[0] = "/usr/share/mimelnk";
			}
			for (int i = 0; i < paths.length; i++) {
				explore(new File(paths[i]));
			}
			// adds the unknown item if necessary
			if (dictionary.get("???") == null)			
				dictionary.put("???", KMimeItem.unknownItem());
			isLoaded = true;
			Logger.log("KMime::load() in " + ((new Date()).getTime() - time) + "Ms");
		}
	}
}