package org.freeasinspeech.kdelaf.ui.kfile;
 
import java.util.*;
import java.io.*; 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import org.freeasinspeech.kdelaf.KdeLAF;
import org.freeasinspeech.kdelaf.ui.KLocale;
import org.freeasinspeech.kdelaf.ui.KConfig;
import org.freeasinspeech.kdelaf.ui.KIcon;
import org.freeasinspeech.kdelaf.ui.KIconFactory;
import org.freeasinspeech.kdelaf.ui.KToolButton;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KFileToolBar extends KFileBase implements KFileBase.Navigator, ActionListener
{
	protected JComboBox locationComboBox;
	protected Vector history = new Vector();
	protected int historyIndex = -1;
	protected int numberOfCoreValues = 0;
	protected Hashtable comboData = new Hashtable();
	JToggleButton upButton;
	JToggleButton prevButton;
	JToggleButton nextButton;
	JToggleButton reloadButton;
	JToggleButton newFolderButton;
	MyMenuButton bookmarkButton;
	MyMenuButton confButton;
		
	public KFileToolBar(JFileChooser filechooser) {
		super(filechooser);
		setLayout(new BorderLayout());
		JToolBar tools = new JToolBar("navigation tools");
		tools.setFloatable(false);
		tools.add(upButton = new JToggleButton(new KToolAction(UP, this)));
		tools.add(prevButton = new JToggleButton(new KToolAction(PREV, this)));
		tools.add(nextButton = new JToggleButton(new KToolAction(NEXT, this)));
		tools.add(reloadButton = new JToggleButton(new KToolAction(RELOAD, this)));
		tools.add(newFolderButton = new JToggleButton(new KToolAction(NEW_FOLDER, this)));
		tools.add(bookmarkButton = new MyMenuButton(new KToolAction(BOOKMARK, this)));
		tools.add(confButton = new ConfMenuButton(new KToolAction(CONF, this)));
		KFileBase.getKPopupMenu().setNavigator(this);
		add(tools, BorderLayout.WEST);
		initLocationComboBox();
		add(locationComboBox, BorderLayout.CENTER);
		setCurrentDirectory(filechooser.getCurrentDirectory());
		bookmarkButton.setVisible(KFileConfig.getShowBookmarks());
	}
	
	public boolean isBookmarksVisible() {
		return bookmarkButton.isVisible();
	}
	public void toggleBookmarks() {
		bookmarkButton.setVisible(!bookmarkButton.isVisible());
		KFileConfig.setShowBookmarks(isBookmarksVisible());
	}
	
	public void registerEvents() {
		super.registerEvents();
		registerEvent(KFileEvent.LOCATION_CHANGED);
	}
	public void onKFileEvent(KFileEvent event) {
		super.onKFileEvent(event);
		if (event.name.equals(KFileEvent.LOCATION_CHANGED)) {
			setCurrentDirectory((File)event.value);
		}
	}
		
	protected void removeItem(Object item) {
		locationComboBox.removeItem(item);
	}
	protected void removeItem(ComboItem item) {
		comboData.remove(item.url);
		locationComboBox.removeItem(item);
	}
	protected void addItem(ComboItem item) {
		if (!comboData.containsKey(item.url)) {
			comboData.put(item.url, new Integer(locationComboBox.getItemCount()));
			locationComboBox.addItem(item);
		}
		else {
			locationComboBox.setSelectedIndex( ((Integer)comboData.get(item.url)).intValue() );
		}
	}
	protected void addItem(File file) {
		addItem(new ComboItem( file.getAbsolutePath(), getIcon(file)));
	}
	protected void addItem(String label, File file) {
		addItem(new ComboItem(label,  file.getAbsolutePath(), getIcon(file)));
	}
	protected void initLocationComboBox() {
		locationComboBox = new JComboBox();
		locationComboBox.setEditable(true);
		File[] roots = File.listRoots();
		for (int i = 0; i < roots.length; i++)
			addItem(KLocale.i18n("Root Folder: %1", roots[i].getAbsolutePath()),  roots[i]);
		numberOfCoreValues += roots.length;
		
		File home = new File(System.getProperty("user.home"));
		addItem(KLocale.i18n("Home Folder: %1", home.getAbsolutePath()),  home);
		numberOfCoreValues++;
		
		File document = KConfig.getFile("documentPath");
		if ( (document != null) && (document.getAbsolutePath() != home.getAbsolutePath()) ) 
			addItem(KLocale.i18n("Documents: %1", document.getAbsolutePath()),  document);
		numberOfCoreValues++;
		
		File desktop = KConfig.getFile("desktopPath");
		addItem(KLocale.i18n("Desktop: %1", desktop.getAbsolutePath()),  desktop);
		numberOfCoreValues++;
		
		File[] recents = KFileConfig.getRecentURLs();
		for (int i = 0; i < recents.length; i++)
			addItem(recents[i]);
		numberOfCoreValues += recents.length;
		
		locationComboBox.setSelectedItem(new ComboItem(""));
		locationComboBox.addActionListener(this);
		locationComboBox.getEditor().addActionListener(this);
		//locationComboBox.addKeyListener(this);
	}
	
	public void onSelect() {
		Object tmp = locationComboBox.getSelectedItem();
		String url = null;
		if (tmp != null) {
			if (tmp instanceof ComboItem)
				url = ((ComboItem)tmp).url;
			else
				url = tmp.toString();
		}
		if ( (url == null) || (url.length() == 0) ) {
			File[] roots = File.listRoots();
			url = roots[0].getAbsolutePath();
		}
		File wantedDir = new File(url);
		while (!wantedDir.exists())
			wantedDir = wantedDir.getParentFile();
		
		MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, wantedDir));
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof org.freeasinspeech.kdelaf.ui.KIconTextField) {
			org.freeasinspeech.kdelaf.ui.KIconTextField src = (org.freeasinspeech.kdelaf.ui.KIconTextField)e.getSource();
			locationComboBox.setSelectedItem(src.getText());
		}
		else
			onSelect();
	}
	
	protected void setCurrentDirectory(File currentDirectory) {
		setCurrentDirectory(currentDirectory, true);
	}
	protected Icon getIcon(File file) {
		return KIconFactory.getIcon(file, KIcon.S_16x16).load();
	}
	
	protected void setCurrentDirectory(File currentDirectory, boolean addToHistory) {
		Object oldItem = locationComboBox.getSelectedItem();
		ComboItem newItem = new ComboItem(currentDirectory.getAbsolutePath(), getIcon(currentDirectory));
		if ( (locationComboBox.getSelectedIndex() >= numberOfCoreValues) && (locationComboBox.getSelectedIndex() == (locationComboBox.getItemCount() - 1)) )
			removeItem(oldItem);
		addItem(newItem);
		locationComboBox.setSelectedItem(newItem);
		if (addToHistory) {
			history.add(currentDirectory);
			historyIndex = history.size() - 1;
		}
		updateButtonStatus();
	}
	
	public File goBack() {
		File hist = null;
		if (canGoPrev())
			hist = (File)history.get(--historyIndex);
		return hist;
	}
	
	public File goForward() {
		File hist = null;
		if (canGoNext())
			hist = (File)history.get(++historyIndex);
		return hist;
	}
	
	protected void updateButtonStatus() {
		prevButton.setEnabled(canGoPrev());
		nextButton.setEnabled(canGoNext());
		upButton.setEnabled(canGoUp());
	}
	
	public boolean canGoUp() {
		return filechooser.getCurrentDirectory().getParentFile() != null;
	}
	public boolean canGoPrev() {
		return historyIndex > 0;
	}
	public boolean canGoNext() {
		return historyIndex < (history.size() - 1);
	}		
	public void goUp() {
		File newDir = filechooser.getCurrentDirectory().getParentFile();
		if (newDir != null) {
			MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, newDir));
		}
	}
	public void goPrev() {
		File newDir = goBack();
		if (newDir != null) {
			MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, newDir));
		}
	}
	public void goNext() {
		File newDir = goForward();
		if (newDir != null) {
			MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, newDir));
		}
	}
	public void goHome() {
		File newDir = new File(System.getProperty("user.home"));
		if (newDir != null) {
			MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, newDir));
		}
	}
	class ConfMenuButton extends MyMenuButton
	{
		ConfMenuButton(Action a) {
			super(a);
		}
		protected void showPopup() {
			setPopupMenu(KFileBase.getKPopupMenu().getToolBarPopup());
			super.showPopup();
		}
	}
	class MyMenuButton extends KToolButton implements MouseListener
	{
		protected JPopupMenu popupMenu;
		MyMenuButton(Action a) {
			super(a);
			addMouseListener(this);
		}
		protected void paintChildren(Graphics g) {
			super.paintChildren(g);
			try {
				BufferedImage img = KdeLAF.getClient().getMenuButtonIndicatorImage(getWidth() / 2, getHeight() / 2);
				g.drawImage(img, getWidth() - img.getWidth(), getHeight() - img.getHeight(), this);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
			}
		}
		public void setPopupMenu(JPopupMenu popupMenu) {
			this.popupMenu = popupMenu;
		}
		public JPopupMenu getPopupMenu() {
			return popupMenu;
		}
		public boolean hasPopupMenu() {
			return (popupMenu != null);
		}
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1)
				showPopup();
		}
		
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1)
				showPopup();
		}
		
		protected void showPopup() {
			if (popupMenu == null)
				popupMenu = new JPopupMenu();
			popupMenu.show(this, 0, getHeight());
		}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	
	class ComboItem implements org.freeasinspeech.kdelaf.ui.QComboBoxUI.WithIcon
	{
		public String label = null;
		public String url = null;
		public Icon icon = null;
		ComboItem(String label, String url, Icon icon) {
			this.label = label;
			this.url = url;
			this.icon = icon;
		}
		ComboItem(String url , Icon icon) {
			this.url = url;
			this.icon = icon;
		}
		ComboItem(Icon icon) {
			this.icon = icon;
		}
		ComboItem(String label, String url ) {
			this.label = label;
			this.url = url;
		}
		ComboItem(String url ) {
			this.url = url;
		}
		ComboItem() {
		}
		public String toString() {
			String str = "";
			if (label != null)
				str = label;
			else if (url != null)
				str = url;
			return str;
		}
		public Icon getIcon() {
			return icon;
		}
		public String getPrettyText() {
			return toString();
		}
		public String getText() {
			return url;
		}
		public int getIconTextGap() {
			return 4;
		}
	}
		
	public static final int UP = 0;
	public static final int PREV = 1;
	public static final int NEXT = 2;
	public static final int RELOAD = 3;
	public static final int NEW_FOLDER = 4;
	public static final int BOOKMARK = 5;
	public static final int CONF = 6;
	class KToolAction extends AbstractAction
	{
		int type;
		KFileToolBar parent;
		KToolAction(int type, KFileToolBar parent) {
			super();
			this.type = type;
			this.parent = parent;
			// i18n & icons http://developer.kde.org/documentation/library/3.5-api/kdelibs-apidocs/kio/kfile/html/kdiroperator_8cpp-source.html#l01244
			// i18n & icons http://developer.kde.org/documentation/library/3.5-api/kdelibs-apidocs/kio/kfile/html/kfiledialog_8cpp-source.html#l00808
			// i18n & icons http://developer.kde.org/documentation/library/3.5-api/kdelibs-apidocs/kio/kfile/html/kfiledialog_8cpp-source.html#l02240
			// i18n & icons http://developer.kde.org/documentation/library/3.1-api/kdeui/html/kstdaction_8cpp-source.html
			// i18n & icons /usr/share/icons/crystalsvg/index.theme
			
			String iconName = "";
			String desc = "";
			String shortDesc = "";
			switch (type) {
				case KFileToolBar.UP:
					iconName = "up";
					desc = KLocale.i18n("<qt>Click this button to enter the parent folder.<p>For instance, if the current location is file:/home/%1 clicking this button will take you to file:/home.</qt>"/*, loginName*/);
					shortDesc = KLocale.i18n("Parent Folder");
					break;
				case KFileToolBar.PREV:
					iconName = "back";
					desc = KLocale.i18n("Click this button to move backwards one step in the browsing history.");
					shortDesc = KLocale.i18n("Back");
					break;
				case KFileToolBar.NEXT:
					iconName = "forward";
					desc = KLocale.i18n("Click this button to move forward one step in the browsing history.");
					shortDesc = KLocale.i18n("Forward");
					break;
				case KFileToolBar.RELOAD:
					iconName = "reload";
					desc = KLocale.i18n("Click this button to reload the contents of the current location.");
					shortDesc = KLocale.i18n("Reload");
					break;
				case KFileToolBar.NEW_FOLDER:
					iconName = "folder_new";
					desc = KLocale.i18n("Click this button to create a new folder.");
					shortDesc = KLocale.i18n("New Folder...");
					break;
				case KFileToolBar.BOOKMARK:
					iconName = "bookmark";
					desc = KLocale.i18n("<qt>This button allows you to bookmark specific locations. Click on this button to open the bookmark menu where you may add, edit or select a bookmark.<p>These bookmarks are specific to the file dialog, but otherwise operate like bookmarks elsewhere in KDE.</qt>");
					shortDesc = KLocale.i18n("Bookmarks");
					break;
				case KFileToolBar.CONF:
					iconName = "configure";
					desc = KLocale.i18n("<qt>This is the configuration menu for the file dialog. Various options can be accessed from this menu including: <ul><li>how files are sorted in the list</li><li>types of view, including icon and list</li><li>showing of hidden files</li><li>the Quick Access navigation panel</li><li>file previews</li><li>separating folders from files</li></ul></qt>");
					shortDesc = KLocale.i18n("Configure");
					break;
			}
			Icon buttonIcon = KIconFactory.getIcon(KIcon.S_22x22, KIcon.C_ACTION, iconName).load();
			putValue(Action.SMALL_ICON, buttonIcon) ;
			putValue(Action.LONG_DESCRIPTION, desc) ;
			putValue(Action.SHORT_DESCRIPTION , shortDesc) ;
			//putValue(Action.NAME , iconName) ;
			//desc);
		}
		public void actionPerformed(ActionEvent e) {
			boolean addToHistory = true;
			boolean popupAction = false;
			File newDir = null;
			switch (type) {
				case KFileToolBar.UP:
					newDir = filechooser.getCurrentDirectory().getParentFile();
					break;
				case KFileToolBar.PREV:
					newDir = goBack();
					addToHistory = false;
					break;
				case KFileToolBar.NEXT:
					newDir = goForward();
					addToHistory = false;
					break;
				case KFileToolBar.RELOAD:
					newDir = filechooser.getCurrentDirectory();
					addToHistory = false;
					break;
				case KFileToolBar.NEW_FOLDER:
					MyManager.sendEvent(new KFileEvent(KFileEvent.NEW_FOLDER));
					break;
				case KFileToolBar.BOOKMARK:
					popupAction = true;
					break;
				case KFileToolBar.CONF:
					popupAction = true;
					break;
			}
			if (!popupAction) {
				((AbstractButton)e.getSource()).setSelected(false);
				MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, newDir), parent);
				setCurrentDirectory(newDir, addToHistory);
			}
		}
	}
	
}
