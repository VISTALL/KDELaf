package org.freeasinspeech.kdelaf.ui.kfile;

import java.util.*;
import java.io.*; 
import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.*;
import javax.swing.event.*;

import org.freeasinspeech.kdelaf.Logger;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class MyManager implements KFileBase.AccessoryManager
{
	protected KFileToolBar tools;
	protected KFileFavorites favorites;
	protected KFilePreview preview;
	
	protected static Hashtable eventListeners = new Hashtable();
	
	public MyManager(KFileToolBar tools, KFileFavorites favorites, KFilePreview preview) {
		KFileBase.getKPopupMenu().setAccessoryManager(this);
		this.tools = tools;
		this.favorites = favorites;
		this.preview = preview;
	}
	
	public boolean isNavigationVisible() {
		return favorites.isVisible();
	}
	public boolean isBookmarksVisible() {
		return tools.isBookmarksVisible();
	}
	public boolean isPreviewVisible() {
		return preview.isVisible();
	}
	
	public void toggleNavigation() {
		favorites.setVisible(!favorites.isVisible());
	}
	public void toggleBookmarks() {
		tools.toggleBookmarks();
	}
	public void togglePreview() {
		preview.setVisible(!preview.isVisible());
	}
	
	public static synchronized void sendPropertyChangeEvent(String property) {
		sendEvent(new KFileEvent(KFileEvent.PROPERTY_CHANGED, property));
	}
	
	public static synchronized void sendEvent(KFileEvent event) {
		LinkedList eventTargets = (LinkedList)eventListeners.get(event.name);
		if (eventTargets != null) {
			Iterator it = eventTargets.listIterator();
			while (it.hasNext())
				((KFileEventListener)it.next()).onKFileEvent(event);
		}
	}
	
	public static synchronized void sendEvent(KFileEvent event, KFileEventListener exception) {
		LinkedList eventTargets = (LinkedList)eventListeners.get(event.name);
		if (eventTargets != null) {
			Iterator it = eventTargets.listIterator();
			while (it.hasNext()) {
				KFileEventListener target = (KFileEventListener)it.next();
				if (target != exception)
					target.onKFileEvent(event);
			}
		}
	}
	
	public static synchronized void registerEvent(KFileEvent event, KFileEventListener target) {
		LinkedList eventTargets = (LinkedList)eventListeners.get(event.name);
		if (eventTargets == null) {
			eventTargets = new LinkedList();
			eventListeners.put(event.name, eventTargets);
		}
		if (!eventTargets.contains(target))
			eventTargets.add(target);
	}
	
	public static synchronized void registerEvent(String eventName, KFileEventListener target) {
		LinkedList eventTargets = (LinkedList)eventListeners.get(eventName);
		if (eventTargets == null) {
			eventTargets = new LinkedList();
			eventListeners.put(eventName, eventTargets);
		}
		if (!eventTargets.contains(target))
			eventTargets.add(target);
	}
	
	public static synchronized void init(JFileChooser filechooser) {
		LinkedList eventTargets = (LinkedList)eventListeners.get(KFileEvent.LOCATION_CHANGED);
		if (eventTargets == null) {
			eventTargets = new LinkedList();
			eventListeners.put(KFileEvent.LOCATION_CHANGED, eventTargets);
		}
		FileChooserManager m = new FileChooserManager(filechooser);
		if (!eventTargets.contains(m))
			eventTargets.add(0, m);
		//sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, filechooser.getCurrentDirectory()));
	}
	public static synchronized void clear() {
		eventListeners.clear();
	}
}

class FileChooserManager implements KFileEventListener
{
	protected JFileChooser filechooser;
		
	public FileChooserManager(JFileChooser filechooser) {
		this.filechooser = filechooser;
	}
	public void onKFileEvent(KFileEvent event) {
		if (event.is(KFileEvent.LOCATION_CHANGED))
			filechooser.setCurrentDirectory((File)event.value);
		else
			Logger.log("FileChooserManager>unknown event :" + event);
	}
}
	
class KFileEvent
{
	public String name;
	public Object value;
		
	public static String PROPERTY_CHANGED = "property.changed";
	public static String LOCATION_CHANGED = "location.changed";
	public static String NEW_FOLDER = "new.folder";
	public static String SELECTION_CHANGED = "selection.changed";
		
	public KFileEvent(String name, Object value) {
		this.name = name;
		this.value = value;
	}
		
	public KFileEvent(String name) {
		this.name = name;
		value = null;
	}
	
	public boolean is(String name) {
		return this.name.equals(name);
	}
	
	public String toString() {
		return name + "=" + value;
	}
}

class KFileSelectionEvent extends KFileEvent
{
	protected File[] selected;
	
	public KFileSelectionEvent(File[] selected) {
		super(KFileEvent.SELECTION_CHANGED);
		this.selected = selected;
	}
	
	public KFileSelectionEvent() {
		super(KFileEvent.SELECTION_CHANGED);
		selected = null;
	}
	
	public File[] getSelectedFiles() {
		return selected;
	}
}

interface KFileEventListener
{
	public void onKFileEvent(KFileEvent event);
}
/*

				addItem(fileViewPopup, propertiesMenuItem, "file_properties");
				addItem(toolBarPopup, navigationMenuItem, "show_navigation");
				addItem(toolBarPopup, bookmarksMenuItem, "show_boomarks");
				addItem(toolBarPopup, previewMenuItem, "show_preview");				
				addItem(sortMenu, menuItem, "sort_by_name");
				addItem(sortMenu, menuItem, "sort_by_date");
				addItem(sortMenu, menuItem, "sort_by_size");
				addItem(sortMenu, menuItem, "sort_reverse");
				addItem(sortMenu, menuItem, "sort_folder_first");
				addItem(sortMenu, caseInsensitiveMenuItem, "sort_case_insensitive");
				setUpItem(shortView, "view_short");
				setUpItem(detailedView, "view_detailed");
				setUpItem(showHiddenFiles, "view_hidden");
				setUpItem(separateFolders, "view_separate_folder");
				addItem(subMenu, menuItem, "view_large_icons");
			addItem(subMenu, menuItem, "view_small_icons");
			addItem(subMenu, menuItem, "view_thumbnail");
			addItem(subMenu, menuItem, "view_zoom+");
			addItem(subMenu, menuItem, "view_zoom-");
			else if (cmd.equals("go_back"))
			else if (cmd.equals("go_forward"))
			else if (cmd.equals("go_home"))
			if (cmd.equals("file_new_folder"))
			else if (cmd.equals("file_move_to_trash"))
			else if (cmd.equals("file_properties"))
			if (cmd.equals("show_navigation"))
			else if (cmd.equals("show_boomarks"))
			else if (cmd.equals("show_preview"))
			if (cmd.equals("sort_by_name"))
			else if (cmd.equals("sort_by_date"))
			else if (cmd.equals("sort_by_size"))
			else if (cmd.equals("sort_reverse"))
			else if (cmd.equals("sort_folder_first"))
			else if (cmd.equals("sort_case_insensitive"))
*/