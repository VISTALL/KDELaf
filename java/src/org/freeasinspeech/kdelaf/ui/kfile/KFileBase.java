package org.freeasinspeech.kdelaf.ui.kfile;
 
import java.util.*;
import java.io.*; 
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

import org.freeasinspeech.kdelaf.ui.*;
import org.freeasinspeech.kdelaf.Logger;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public abstract class KFileBase extends JPanel implements KFileEventListener
{
	protected JFileChooser filechooser;
	protected Vector fileSelectionListeners = new Vector();
	protected static KPopupMenu kPopupMenu = null;
		
	KFileBase(JFileChooser filechooser) {
		this.filechooser = filechooser;
		registerEvents();
	}
	
	protected void registerEvents() {
	}
	protected void registerEvent(KFileEvent event) {
		MyManager.registerEvent(event, this);
	}
	protected void registerEvent(String eventName) {
		MyManager.registerEvent(eventName, this);
	}
	public void onKFileEvent(KFileEvent event) {
	}
		
	public boolean canSelectFile() {
		return ( (filechooser.getFileSelectionMode() == JFileChooser.FILES_ONLY) || (filechooser.getFileSelectionMode() == JFileChooser.FILES_AND_DIRECTORIES) );
	}
	public boolean canSelectDir() {
		return ( (filechooser.getFileSelectionMode() == JFileChooser.DIRECTORIES_ONLY) || (filechooser.getFileSelectionMode() == JFileChooser.FILES_AND_DIRECTORIES) );
	}
		
	interface Navigator
	{
		public boolean canGoUp();
		public boolean canGoPrev();
		public boolean canGoNext();
			
		public void goUp();
		public void goPrev();
		public void goNext();
		public void goHome();
	}
	
	interface FileViewer
	{
		public boolean canMoveToTrash();
		public boolean canShowProperties();
		public boolean canCreateFolder();
		public boolean canUseCase();
		public boolean isSortingByName();
		public boolean isSortingByDate();
		public boolean isSortingBySize();
		public boolean isReverseOrdered();
		public boolean areFolderFirst();
		public boolean isCaseInsensitive();
		public boolean areFolderSeparated();
		public boolean areHiddenFilesShown();
			
		public void moveToTrash();
		public void showProperties();
		public void newFolder();
		public void sortByName();
		public void sortByDate();
		public void sortBySize();
		public void setReverseOrder(boolean reverseOrder);
		public void setFolderFirst(boolean folderFirst);
		public void setCaseInsensitive(boolean caseInsensitive);
		public void toggleFolderSeparated();
		public void toggleHiddenFilesShown();
	}
	
	interface AccessoryManager
	{
		public boolean isNavigationVisible();
		public boolean isBookmarksVisible();
		public boolean isPreviewVisible();
			
		public void toggleNavigation();
		public void toggleBookmarks();
		public void togglePreview();
	}
		
	public static KPopupMenu getKPopupMenu() {
		if (kPopupMenu == null)
			kPopupMenu = new KPopupMenu();
		return kPopupMenu;
	}
	public static void freeKPopupMenu() {
		kPopupMenu = null;
	}
	
}
	
	class KPopupMenu implements ActionListener
	{
		JMenu sortMenu = null;
		JMenuItem shortView = null;
		JMenuItem detailedView = null;
		JMenuItem showHiddenFiles = null;
		JMenuItem separateFolders = null;
		JPopupMenu fileViewPopup = null;
		JPopupMenu toolBarPopup = null;
		JMenuItem parentMenuItem = null;
		JMenuItem backMenuItem = null;
		JMenuItem forwardMenuItem = null;
		JMenuItem newFolderMenuItem = null;
		JMenuItem moveToTrashMenuItem = null;
		JMenuItem propertiesMenuItem = null;
		JMenuItem navigationMenuItem = null;
		JMenuItem bookmarksMenuItem = null;
		JMenuItem previewMenuItem = null;
		JMenuItem caseInsensitiveMenuItem = null;
		KFileBase.Navigator navigator = null;
		KFileBase.FileViewer fileViewer = null;
		KFileBase.AccessoryManager accessoryManager = null;
		KPopupMenu() {
		}
		
		private void addItem(JMenu menu, JMenuItem item, String actionCommand) {
			menu.add(item);
			setUpItem(item, actionCommand);
		}
		private void addItem(JPopupMenu menu, JMenuItem item, String actionCommand) {
			menu.add(item);
			setUpItem(item, actionCommand);
		}
		private void setUpItem(JMenuItem item, String actionCommand) {
			item.addActionListener(this);
			item.setActionCommand(actionCommand);
		}
		
		public JPopupMenu getFileViewPopup() {
			buildShared();
			if (fileViewPopup == null) {
				fileViewPopup = new JPopupMenu();
				JMenuItem menuItem;
				
				parentMenuItem = new JMenuItem(KLocale.i18n("Parent Folder"), getActionIcon("up"));
				addItem(fileViewPopup, parentMenuItem, "go_up");
				backMenuItem = new JMenuItem(KLocale.i18n("Back"), getActionIcon("back"));
				addItem(fileViewPopup, backMenuItem, "go_back");
				forwardMenuItem = new JMenuItem(KLocale.i18n("Forward"), getActionIcon("forward"));
				addItem(fileViewPopup, forwardMenuItem, "go_forward");
				menuItem = new JMenuItem(KLocale.i18n("Home Folder"), getActionIcon("gohome"));
				addItem(fileViewPopup, menuItem, "go_home");
				fileViewPopup.addSeparator();
				newFolderMenuItem = new JMenuItem(KLocale.i18n("New Folder..."), getActionIcon("folder_new"));
				addItem(fileViewPopup, newFolderMenuItem, "file_new_folder");
				moveToTrashMenuItem = new JMenuItem(KLocale.i18n("Move to Trash"), getActionIcon("edittrash"));
				addItem(fileViewPopup, moveToTrashMenuItem, "file_move_to_trash");
				fileViewPopup.addSeparator();
				
				fileViewPopup.add(sortMenu);
				fileViewPopup.addSeparator();
				fileViewPopup.add(buildViewMenu());
				fileViewPopup.addSeparator();
				propertiesMenuItem = new JMenuItem(KLocale.i18n("Properties"));
				addItem(fileViewPopup, propertiesMenuItem, "file_properties");
				//[!] todo
				propertiesMenuItem.setEnabled(false);
			}
			updateShared();
			return fileViewPopup;
		}
		public void setNavigator(KFileBase.Navigator navigator) {
			this.navigator = navigator;
		}
		public void setFileViewer(KFileBase.FileViewer fileViewer) {
			this.fileViewer = fileViewer;
		}
		public void setAccessoryManager(KFileBase.AccessoryManager accessoryManager) {
			this.accessoryManager = accessoryManager;
		}
		
		public JPopupMenu getToolBarPopup() {
			buildShared();
			if (toolBarPopup == null) {
				toolBarPopup = new JPopupMenu();
				
				toolBarPopup.add(sortMenu);
				toolBarPopup.addSeparator();
				toolBarPopup.add(shortView);
				toolBarPopup.add(detailedView);
				toolBarPopup.addSeparator();
				toolBarPopup.add(showHiddenFiles);
				navigationMenuItem = new TwoStatesMenuItem(KLocale.i18n("Show Quick Access Navigation Panel"), KLocale.i18n("Hide Quick Access Navigation Panel"));
				addItem(toolBarPopup, navigationMenuItem, "show_navigation");
				bookmarksMenuItem = new TwoStatesMenuItem(KLocale.i18n("Show Bookmarks"), KLocale.i18n("Hide Bookmarks"));
				addItem(toolBarPopup, bookmarksMenuItem, "show_boomarks");
				previewMenuItem = new TwoStatesMenuItem(KLocale.i18n("Show Preview"), KLocale.i18n("Hide Preview"), getActionIcon("thumbnail"));
				addItem(toolBarPopup, previewMenuItem, "show_preview");				
				
				toolBarPopup.add(separateFolders);
			}
			updateShared();
			return toolBarPopup;
		}
		/*i18n(), Key_F9, coll,"toggleSpeedbar"
		*/
		protected void buildShared() {
			if (sortMenu == null) {
				sortMenu = new JMenu(KLocale.i18n("Sorting"));
				ButtonGroup group = new ButtonGroup();
				JMenuItem menuItem = new JRadioButtonMenuItem(KLocale.i18n("By Name"));
				if ( (fileViewer != null) && (fileViewer.isSortingByName()) )
					menuItem.setSelected(true);
				addItem(sortMenu, menuItem, "sort_by_name");
				group.add(menuItem);
				menuItem = new JRadioButtonMenuItem(KLocale.i18n("By Date"));
				if ( (fileViewer != null) && (fileViewer.isSortingByDate()) )
					menuItem.setSelected(true);
				addItem(sortMenu, menuItem, "sort_by_date");
				group.add(menuItem);
				menuItem = new JRadioButtonMenuItem(KLocale.i18n("By Size"));
				if ( (fileViewer != null) && (fileViewer.isSortingBySize()) )
					menuItem.setSelected(true);
				addItem(sortMenu, menuItem, "sort_by_size");
				group.add(menuItem);
				sortMenu.addSeparator();
				menuItem = new JCheckBoxMenuItem(KLocale.i18n("Reverse"));
				if ( (fileViewer != null) && (fileViewer.isReverseOrdered()) )
					menuItem.setSelected(true);
				addItem(sortMenu, menuItem, "sort_reverse");
				menuItem = new JCheckBoxMenuItem(KLocale.i18n("Folder First"));
				if ( (fileViewer != null) && (fileViewer.areFolderFirst()) )
					menuItem.setSelected(true);
				addItem(sortMenu, menuItem, "sort_folder_first");
				caseInsensitiveMenuItem = new JCheckBoxMenuItem(KLocale.i18n("Case Insensitive"));
				addItem(sortMenu, caseInsensitiveMenuItem, "sort_case_insensitive");
				
				ButtonGroup group2 = new ButtonGroup();
				shortView = new JRadioButtonMenuItem(KLocale.i18n("Short View"), getActionIcon("view_multicolumn"));
				setUpItem(shortView, "view_short");
				sortMenu.add(shortView);
				group2.add(shortView);
				detailedView = new JRadioButtonMenuItem(KLocale.i18n("Detailed View"), getActionIcon("view_detailed"));
				setUpItem(detailedView, "view_detailed");
				sortMenu.add(detailedView);
				group2.add(detailedView);
				//[!] todo
				shortView.setSelected(true);
				detailedView.setEnabled(false);
				showHiddenFiles = new JCheckBoxMenuItem(KLocale.i18n("Show Hidden Files"));
				setUpItem(showHiddenFiles, "view_hidden");
				separateFolders = new JCheckBoxMenuItem(KLocale.i18n("Separate Folders"));
				setUpItem(separateFolders, "view_separate_folder");
			}
		}
		protected void updateShared() {
			if (navigator != null) {
				if (parentMenuItem != null)
					parentMenuItem.setEnabled(navigator.canGoUp());
				if (backMenuItem != null)
					backMenuItem.setEnabled(navigator.canGoPrev());
				if (forwardMenuItem != null)
					forwardMenuItem.setEnabled(navigator.canGoNext());
			}
			if (fileViewer != null) {
				if (newFolderMenuItem != null)
					newFolderMenuItem.setEnabled(fileViewer.canCreateFolder());
				if (moveToTrashMenuItem != null)
					moveToTrashMenuItem.setEnabled(fileViewer.canMoveToTrash());
				if (propertiesMenuItem != null)
					propertiesMenuItem.setEnabled(fileViewer.canShowProperties());
				if (caseInsensitiveMenuItem != null) {
					caseInsensitiveMenuItem.setSelected(fileViewer.isCaseInsensitive());
					caseInsensitiveMenuItem.setEnabled(fileViewer.canUseCase());
				}
				if (showHiddenFiles != null)
					showHiddenFiles.setSelected(fileViewer.areHiddenFilesShown());				
				if (separateFolders != null)
					separateFolders.setSelected(fileViewer.areFolderSeparated());
			}
			if (accessoryManager != null) {
				if (navigationMenuItem != null)
					navigationMenuItem.setSelected(accessoryManager.isNavigationVisible());
				if (bookmarksMenuItem != null)
					bookmarksMenuItem.setSelected(accessoryManager.isBookmarksVisible());
				if (previewMenuItem != null)
					previewMenuItem.setSelected(accessoryManager.isPreviewVisible());
			}
		}
						
		public JMenu buildViewMenu() {
			JMenu subMenu = new JMenu(KLocale.i18n("&View"));
			subMenu.add(shortView);
			subMenu.add(detailedView);
			subMenu.addSeparator();
			subMenu.add(showHiddenFiles);
			subMenu.add(separateFolders);
			subMenu.addSeparator();
			JMenuItem menuItem = new JRadioButtonMenuItem(KLocale.i18n("Large Icons"));
			addItem(subMenu, menuItem, "view_large_icons");
			//[!] todo
			menuItem.setEnabled(false);
			menuItem = new JRadioButtonMenuItem(KLocale.i18n("Small Icons"));
			addItem(subMenu, menuItem, "view_small_icons");
			//[!] todo
			menuItem.setEnabled(false);
			subMenu.addSeparator();
			menuItem = new JCheckBoxMenuItem(KLocale.i18n("Thumbnail Previews"));
			addItem(subMenu, menuItem, "view_thumbnail");
			//[!] todo
			menuItem.setEnabled(false);
			menuItem = new JRadioButtonMenuItem(KLocale.i18n("Zoom &In"), getActionIcon("viewmag+"));
			addItem(subMenu, menuItem, "view_zoom+");
			//[!] todo
			menuItem.setEnabled(false);
			menuItem = new JRadioButtonMenuItem(KLocale.i18n("Zoom &Out"), getActionIcon("viewmag-"));
			addItem(subMenu, menuItem, "view_zoom-");
			//[!] todo
			menuItem.setEnabled(false);
			return subMenu;
		}
		public Icon getActionIcon(String iconName) {
			return KIconFactory.getIcon(KIcon.S_16x16, KIcon.C_ACTION, iconName).load();
		}
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd.startsWith("go_")) {
				if (navigator != null) {
					if (cmd.equals("go_up"))
						navigator.goUp();
					else if (cmd.equals("go_back"))
						navigator.goPrev();
					else if (cmd.equals("go_forward"))
						navigator.goNext();
					else if (cmd.equals("go_home"))
						navigator.goHome();
					else
						Logger.log("KFileBase::actionPerformed Unknow cmd : " + cmd);
				}
			}
			else if (cmd.startsWith("file_")) {
				if (fileViewer != null) {
					if (cmd.equals("file_new_folder"))
						MyManager.sendEvent(new KFileEvent(KFileEvent.NEW_FOLDER));
					else if (cmd.equals("file_move_to_trash"))
						fileViewer.moveToTrash();
					else if (cmd.equals("file_properties"))
						fileViewer.showProperties();
					else
						Logger.log("KFileBase::actionPerformed Unknow cmd : " + cmd);
				}
			}
			else if (cmd.startsWith("show_")) {
				if (accessoryManager != null) {
					if (cmd.equals("show_navigation"))
						accessoryManager.toggleNavigation();
					else if (cmd.equals("show_boomarks"))
						accessoryManager.toggleBookmarks();
					else if (cmd.equals("show_preview"))
						accessoryManager.togglePreview();
					else
						Logger.log("KFileBase::actionPerformed Unknow cmd : " + cmd);
				}
			}
			else if (cmd.startsWith("sort_")) {
				if (fileViewer != null) {
					if (cmd.equals("sort_by_name"))
						fileViewer.sortByName();
					else if (cmd.equals("sort_by_date"))
						fileViewer.sortByDate();
					else if (cmd.equals("sort_by_size"))
						fileViewer.sortBySize();
					else if (cmd.equals("sort_reverse"))
						fileViewer.setReverseOrder(((JCheckBoxMenuItem)e.getSource()).isSelected());
					else if (cmd.equals("sort_folder_first"))
						fileViewer.setFolderFirst(((JCheckBoxMenuItem)e.getSource()).isSelected());
					else if (cmd.equals("sort_case_insensitive"))
						fileViewer.setCaseInsensitive(((JCheckBoxMenuItem)e.getSource()).isSelected());
					else
						Logger.log("KFileBase::actionPerformed Unknow cmd : " + cmd);
				}
			}
			else if (cmd.startsWith("view_")) {
				if (fileViewer != null) {
					if (cmd.equals("view_separate_folder"))
						fileViewer.toggleFolderSeparated();
					else if (cmd.equals("view_hidden"))
						fileViewer.toggleHiddenFilesShown();
					else
						Logger.log("KFileBase::actionPerformed Unknow cmd : " + cmd);
				}
			}
			else
				Logger.log("KFileBase::actionPerformed Unknow cmd : " + cmd);
		}
	}
	
	class TwoStatesMenuItem extends JMenuItem
	{
		protected String unselectedText;
		protected String selectedText;
		TwoStatesMenuItem(String unselectedText, String selectedText) {
			super(unselectedText);
			this.unselectedText = unselectedText;
			this.selectedText = selectedText;
		}
		TwoStatesMenuItem(String unselectedText, String selectedText, Icon icon) {
			super(unselectedText, icon);
			this.unselectedText = unselectedText;
			this.selectedText = selectedText;
		}
		public void setSelected(boolean selected) {
			setText(selected ? selectedText : unselectedText);
		}
	}
