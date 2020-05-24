package org.freeasinspeech.kdelaf.ui.kfile;
 
import java.util.*;
import java.io.*; 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import org.freeasinspeech.kdelaf.Cache;
import org.freeasinspeech.kdelaf.ui.*;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KFileConfig
{
	static int ICON_SIZE = KIcon.S_32x32;
	static KConfig confKFile = null;
	
	protected static synchronized void init() {
		if (confKFile == null) {
			confKFile = new KConfig("kdeglobals");
		}
	}
	
	public static KConfigGroup getGroup(String name) {
		init();
		return confKFile.getGroup(name);
	}
	
	public static boolean getSeparateDirectories() {
		return getGroup("KFileDialog Settings").getBoolean("Separate Directories");
	}
	public static void setSeparateDirectories(boolean val) {
		getGroup("KFileDialog Settings").setBoolean("Separate Directories", val);
	}
	public static boolean getSortReversed() {
		return getGroup("KFileDialog Settings").getBoolean("Sort reversed");
	}
	public static void setSortReversed(boolean val) {
		getGroup("KFileDialog Settings").setBoolean("Sort reversed", val);
	}
	public static boolean getSortCaseInsensitively() {
		return getGroup("KFileDialog Settings").getBoolean("Sort case insensitively");
	}
	public static void setSortCaseInsensitively(boolean val) {
		getGroup("KFileDialog Settings").setBoolean("Sort case insensitively", val);
	}
	public static boolean getSortDirectoriesFirst() {
		return getGroup("KFileDialog Settings").getBoolean("Sort directories first");
	}
	public static void setSortDirectoriesFirst(boolean val) {
		getGroup("KFileDialog Settings").setBoolean("Sort directories first", val);
	}
	public static boolean getShowSpeedbar() {
		return getGroup("KFileDialog Settings").getBoolean("Show Speedbar");
	}
	public static void setShowSpeedbar(boolean val) {
		getGroup("KFileDialog Settings").setBoolean("Show Speedbar", val);
	}
	public static boolean getShowPreview() {
		return getGroup("KFileDialog Settings").getBoolean("Show Preview");
	}
	public static void setShowPreview(boolean val) {
		getGroup("KFileDialog Settings").setBoolean("Show Preview", val);
	}
	public static boolean getShowHiddenFiles() {
		return getGroup("KFileDialog Settings").getBoolean("Show hidden files");
	}
	public static void setShowHiddenFiles(boolean val) {
		getGroup("KFileDialog Settings").setBoolean("Show hidden files", val);
	}
	public static boolean getShowBookmarks() {
		return getGroup("KFileDialog Settings").getBoolean("Show Bookmarks");
	}
	public static void setShowBookmarks(boolean val) {
		getGroup("KFileDialog Settings").setBoolean("Show Bookmarks", val);
	}
	public static int getSortBy() {
		String strVal = getGroup("KFileDialog Settings").get("Sort by");
		int val = KFileView.BY_NAME;
		if (strVal.toLowerCase().equals("size"))
			val = KFileView.BY_SIZE;
		else if (strVal.toLowerCase().equals("date"))
			val = KFileView.BY_DATE;
		return val;
	}
	public static void setSortBy(int val) {
		String strVal = "";
		switch (val) {
			case KFileView.BY_DATE :
				strVal = "Date";
				break;
			case KFileView.BY_SIZE :
				strVal = "Size";
				break;
			default : //KFileView.BY_NAME
				strVal = "Name";
		}
		getGroup("KFileDialog Settings").set("Sort by", strVal);
	}
	
	public static File[] getRecentURLs() {
		init();
		KURL[] recentURL = confKFile.getGroup("KFileDialog Settings").getURLs("Recent URLs");
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < recentURL.length; i++) {
			if (recentURL[i].getProtocol().equals("file")) {
				File tmpFile = recentURL[i].getFile();
				if (tmpFile.isDirectory() && tmpFile.canRead())
					tmp.add(tmpFile);
			}
		}
		File[] recentFile = new File[tmp.size()];
		for (int i = 0; i < tmp.size(); i++)
			recentFile[i] = (File)tmp.get(i);
			
		return recentFile;
	}
	
	public static SpeedBarEntry[] getSpeedBarEntries() {
		init();
		KConfigGroup gp = confKFile.getGroup("KFileDialog Speedbar (Global)");
		int numberOfEntries = gp.getInt("Number of Entries");
		ArrayList listEntries = new ArrayList();
		for (int i = 0; i < numberOfEntries; i++) {
			String description = gp.get("Description_" + i);
			String iconGroup = gp.get("IconGroup_" + i);
			String iconName = gp.get("Icon_" + i);
			KURL url = gp.getURL("URL_" + i);
			SpeedBarEntry tmp = new SpeedBarEntry(description, iconGroup, iconName, url);
			if (tmp.isValid())
				listEntries.add(tmp);
		}
		SpeedBarEntry[] result = new SpeedBarEntry[listEntries.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = (SpeedBarEntry)listEntries.get(i);
		return result;
	}
		
	static class SpeedBarEntry implements ActionListener
	{
		public String description;
		public int iconContext;
		public String iconName;
		public KURL url;
		
		SpeedBarEntry(String description, String iconGroup, String iconName, KURL url) {
			this.description = description;
			/*try {
				iconContext = Integer.parseInt(iconGroup);
			}
			catch (Exception e) {*/
				iconContext = KIcon.C_ANY;
			//}
			this.iconName = iconName;
			this.url = url;
		}
		
		public boolean isValid() {
			return (url != null);
		}
		
		public boolean isEnabled() {
			return (isValid() && url.getProtocol().equals("file"));
		}
		
		public String getDescription() {
			String desc = description;
			if ( (desc.length() == 0) && isValid() ) {
				desc = url.getPath();
				if (desc.endsWith("/")) {
					desc = desc.substring(0, desc.length() - 1);
				}
				int slashIndex = desc.lastIndexOf("/");
				if (slashIndex > -1)
					desc = desc.substring(slashIndex + 1);
			}
			return desc;
		}
		
		public String toString() {
			return getDescription() + "$" + iconContext + "$" + iconName + "$" + url.toString();
		}
		
		public JButton getButton() {
			JButton button = new  JButton(getDescription());
			Icon icon = ( (iconName != null) && (iconName.length() > 0) ) ? KIconFactory.getIcon(ICON_SIZE, iconContext, iconName).load() : null;
			if (icon == null)
				icon = KIconFactory.getIcon(url.getFile(), ICON_SIZE).load();
			if (icon != null)
				button.setIcon(icon);
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setToolTipText(isValid() ? url.toString() : "");
			button.addActionListener(this);
			button.setEnabled(isEnabled());
			button.setCursor(new Cursor(Cursor.HAND_CURSOR));
			return button;
		}
		
		public void actionPerformed(ActionEvent e) {
			MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, url.getFile()));
		}
	}
	
}
