package org.freeasinspeech.kdelaf.ui.kfile;
 
import java.util.*;
import java.io.*; 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import org.freeasinspeech.kdelaf.Cache;
import org.freeasinspeech.kdelaf.ui.*;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public abstract class KFileView extends KFileBase implements KFileBase.FileViewer,  ListCellRenderer, ListSelectionListener
{
	protected boolean singleClick = true;
		/*
Set speedbar defaults=false
View Style=Detail
	*/
		
	protected JComponent mainContainer = null;
	protected JList folderView = null;
	protected KFileModel folderModel = null;
	JLabel listCellRendererComponent;
		
	protected boolean folderNotLoaded = true;
	private FileList childs;
		
	protected int iconSize = KIcon.S_16x16;
	public static final int BY_NAME = 0;
	public static final int BY_DATE = 1;
	public static final int BY_SIZE = 2;
		
	/** The cache. */
	protected Cache cache;
	
	KFileView(JFileChooser filechooser) {
		super(filechooser);
		cache = new Cache();
		KFileBase.getKPopupMenu().setFileViewer(this);
		childs = new FileList();
		setLayout(new BorderLayout());
		listCellRendererComponent = new JLabel();
		listCellRendererComponent.setOpaque(true);

		folderView = new JList();
		folderModel = new KFileModel();
		folderView.setModel(folderModel);
		folderView.setCellRenderer(this);
		folderView.setVisibleRowCount(-1);
		folderView.setLayoutOrientation(JList.VERTICAL_WRAP);
		folderView.addListSelectionListener(this);
	}
	
	public void registerEvents() {
		super.registerEvents();
		registerEvent(KFileEvent.LOCATION_CHANGED);
		registerEvent(KFileEvent.NEW_FOLDER);
		registerEvent(KFileEvent.PROPERTY_CHANGED);
	}
	
	public void onKFileEvent(KFileEvent event) {
		super.onKFileEvent(event);
		if (event.is(KFileEvent.LOCATION_CHANGED)) {
			folderNotLoaded = true;
			reload();
		}
		else if (event.is(KFileEvent.NEW_FOLDER)) {
			newFolder();
		}
		else if (event.is(KFileEvent.PROPERTY_CHANGED)) {
			String n = (String)event.value;
			if (n.equals(JFileChooser.MULTI_SELECTION_ENABLED_CHANGED_PROPERTY)) {
				folderNotLoaded = true;
				reload();
			}
			else if (n.equals(JFileChooser.FILE_HIDING_CHANGED_PROPERTY)) {
				folderNotLoaded = true;
				reload();
			}
		}
	}
	protected abstract JComponent getView();
	protected void updateView() {
		JComponent view = getView();
		setVisible(false);
		if (mainContainer != null)
			remove(mainContainer);
		if (KFileConfig.getSeparateDirectories()) {
			mainContainer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(folderView), view);
			((JSplitPane)mainContainer).setDividerLocation(100);
			add(mainContainer, BorderLayout.CENTER);
		}
		else {
			mainContainer = view;
			add(mainContainer, BorderLayout.CENTER);
		}
		setVisible(true);
		folderNotLoaded = true;
		reload();
	}
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
			File file = (File)value;
			
			listCellRendererComponent.setText(file.getName());
			listCellRendererComponent.setIcon(getIcon(file));
			listCellRendererComponent.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
			listCellRendererComponent.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
		}
		return listCellRendererComponent;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		File selectedFolder = (File)folderView.getSelectedValue();
		if (selectedFolder != null) {
			MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, selectedFolder));
		}
	}
	
	public PopupListener newPopupListener() {
		return new PopupListener();
	}
	
	protected String suggestNewFolderName() {
		String baseFolderName = KLocale.i18n("New Folder");
		String newFolderName = baseFolderName;
		File newFolder = new File(filechooser.getCurrentDirectory(), newFolderName);
		int i = 0;
		while (newFolder.exists()) {
			newFolderName = baseFolderName + "_" + (++i);
			newFolder = new File(filechooser.getCurrentDirectory(), newFolderName);
		}
		return newFolderName;
	}
	
	public void newFolder() {
		Object[] tmp = new Object[0];
		String message = KLocale.i18n("Create new folder in:\n%1", filechooser.getCurrentDirectory().getAbsolutePath());
		String folderName = (String)JOptionPane.showInputDialog(this, message, KLocale.i18n("New Folder"), JOptionPane.PLAIN_MESSAGE, null, null, suggestNewFolderName());
		if (folderName != null) {
			File newFolder = new File(filechooser.getCurrentDirectory(), folderName);
			if (newFolder.exists()) {
				message = KLocale.i18n("A file or folder named %1 already exists.", newFolder.getAbsolutePath());
				JOptionPane.showMessageDialog(this, message, KLocale.i18n("Sorry"), JOptionPane.ERROR_MESSAGE);
			}
			else {
				try {
					if (newFolder.mkdir()) {
						MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, newFolder));
					}
					else {
						message = KLocale.i18n("You do not have permission to create that folder.");
						JOptionPane.showMessageDialog(this, message, KLocale.i18n("Sorry"), JOptionPane.ERROR_MESSAGE);
					}
				}
				catch (SecurityException e) {
					message = KLocale.i18n("You do not have permission to create that folder.");
					JOptionPane.showMessageDialog(this, message, KLocale.i18n("Sorry"), JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	public FileList getFilesToDisplay() {
		FileComparator comparator = new FileComparator(KFileConfig.getSortBy(), KFileConfig.getSortReversed(), KFileConfig.getSortCaseInsensitively());
		File childsTmp[] = null;
		if (folderNotLoaded) {
			childsTmp = filechooser.getCurrentDirectory().listFiles();
			folderNotLoaded = false;
		}
		else
			childsTmp = childs.toArray();
			
		childs.clear();
		if (KFileConfig.getSeparateDirectories()) {
				FileList childsFolder = new FileList();
				for (int i = 0; i < childsTmp.length; i++)
					if (childsTmp[i].isDirectory() && filechooser.accept(childsTmp[i]) && (!filechooser.isFileHidingEnabled() || (!childsTmp[i].isHidden())))
						childsFolder.add(childsTmp[i], comparator);
				folderModel.clear();
				folderModel.setData(childsFolder);
					
			for (int i = 0; i < childsTmp.length; i++)
				if (childsTmp[i].isFile())
					if (filechooser.accept(childsTmp[i]) && (!filechooser.isFileHidingEnabled() || (!childsTmp[i].isHidden())))
						childs.add(childsTmp[i], comparator);
		}
		else {
			if (KFileConfig.getSortDirectoriesFirst()) {
				for (int i = 0; i < childsTmp.length; i++)
					if (childsTmp[i].isDirectory() && filechooser.accept(childsTmp[i]) && (!filechooser.isFileHidingEnabled() || (!childsTmp[i].isHidden())))
						childs.add(childsTmp[i], comparator);
				FileList childs2 = new FileList();
				for (int i = 0; i < childsTmp.length; i++)
					if (childsTmp[i].isFile() && filechooser.accept(childsTmp[i]) && (!filechooser.isFileHidingEnabled() || (!childsTmp[i].isHidden())))
						childs2.add(childsTmp[i], comparator);
				childs = childs.merge(childs2);
			}
			else {
				for (int i = 0; i < childsTmp.length; i++)
					if (filechooser.accept(childsTmp[i]) && (!filechooser.isFileHidingEnabled() || (!childsTmp[i].isHidden())))
						childs.add(childsTmp[i], comparator);
			}
		}
		
		return childs;
	}
		
	public abstract void reload();
	public abstract void clearSelection();
	public abstract File[] getSelectedFiles();
	public abstract boolean hasAtLeastOneSelection();
		
		
	public boolean canMoveToTrash() {
		return hasAtLeastOneSelection();
	}
	public boolean canShowProperties() {
		return false;
	}
	public boolean canCreateFolder() {
		return true;
	}
	public boolean canUseCase() {
		return isSortingByName();
	}
	public boolean isSortingByName() {
		return (KFileConfig.getSortBy() == BY_NAME);
	}
	public boolean isSortingByDate() {
		return (KFileConfig.getSortBy() == BY_DATE);
	}
	public boolean isSortingBySize() {
		return (KFileConfig.getSortBy() == BY_SIZE);
	}
	public boolean isReverseOrdered() {
		return KFileConfig.getSortReversed();
	}
	public boolean isCaseInsensitive() {
		return (!canUseCase() || KFileConfig.getSortCaseInsensitively());
	}
	public boolean areFolderFirst() {
		return KFileConfig.getSortDirectoriesFirst();
	}
	public boolean areFolderSeparated() {
		return KFileConfig.getSeparateDirectories();
	}
	public boolean areHiddenFilesShown() {
		return !filechooser.isFileHidingEnabled();
	}
	
	public void toggleHiddenFilesShown() {
		KFileConfig.setShowHiddenFiles(!areHiddenFilesShown());
		filechooser.setFileHidingEnabled(!filechooser.isFileHidingEnabled());
	}
	public void toggleFolderSeparated() {
		KFileConfig.setSeparateDirectories(!KFileConfig.getSeparateDirectories());
		updateView();
	}
	
	public void moveToTrash() {
		File[] toTrash = getSelectedFiles();
		if (toTrash.length > 0) {
			if (KfmClient.moveToTrash(toTrash)) {
				folderNotLoaded = true;
				clearSelection();
				reload();
			}
			else {
				JOptionPane.showMessageDialog(this, KfmClient.lastError(), KLocale.i18n("Sorry"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	public void showProperties() {
		System.out.println("showProperties");
	}
	
	public void sortByName() {
		KFileConfig.setSortBy(BY_NAME);
		reload();
	}
	
	public void sortByDate() {
		KFileConfig.setSortBy(BY_DATE);
		reload();
	}
	
	public void sortBySize() {
		KFileConfig.setSortBy(BY_SIZE);
		reload();
	}
	
	public void setReverseOrder(boolean reverseOrder) {
		KFileConfig.setSortReversed(reverseOrder);
		reload();
	}
	
	public void setCaseInsensitive(boolean caseInsensitive) {
		KFileConfig.setSortCaseInsensitively(caseInsensitive);
		reload();
	}
	public void setFolderFirst(boolean folderFirst) {
		KFileConfig.setSortDirectoriesFirst(folderFirst);
		reload();
	}
	
	
	protected Icon getIcon(File file) {
		return KIconFactory.getIcon(file, iconSize).load(cache);
	}
	
	public Dimension getPreferredSize() {
	return new Dimension(400, 300);
	}
	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3)
				maybeShowPopup(e);
		}
		
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3)
				maybeShowPopup(e);
		}
		
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger())
				KFileBase.getKPopupMenu().getFileViewPopup().show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	class KFileModel extends DefaultListModel
	{
		public void setData(FileList childs) {
			for (int i = 0; i < childs.size(); i++)
				addElement(childs.get(i));
		}
	}
	
	class FileList
	{
		protected ArrayList files;
		
		FileList() {
			files = new ArrayList();
		}
		FileList(FileList toCopy) {
			files = new ArrayList();
			for (int i = 0; i < toCopy.size(); i++)
				files.add(toCopy.get(i));
		}
		
		public void add(File file, FileComparator comparator) {
			int i = 0;
			while ( (i < size()) && (comparator.compare(file, get(i)) > 0) )
				i++;
			files.add(i, file);
		}
		public int size() {
			return files.size();
		}
		public File get(int index) {
			return (File)files.get(index);
		}
		public FileList merge(FileList other) {
			FileList newList = new FileList(this);
			for (int i = 0; i < other.size(); i++)
				newList.files.add(other.get(i));
			return newList;
		}
		public void clear() {
			files.clear();
		}
		public File[] toArray() {
			File[] fileArray = new File[size()];
			for (int i = 0; i < size(); i++)
				fileArray[i] = get(i);
			return fileArray;
		}
	}
	
	class FileComparator
	{
		protected int sortMethod;
		protected boolean reverseOrder;
		protected boolean caseInsensitive;
		FileComparator(int sortMethod, boolean reverseOrder, boolean caseInsensitive) {
			this.sortMethod = sortMethod;
			this.reverseOrder = reverseOrder;
		}
		
		protected int compareStrings(String v1, String v2) {
			return compareStrings(v1, v2, caseInsensitive);
		}
		protected int compareStrings(String v1, String v2, boolean _caseInsensitive) {
			int val = 0;
			java.text.Collator collator = java.text.Collator.getInstance();
			if (_caseInsensitive)
				val = collator.compare(v1.toLowerCase(), v2.toLowerCase());
			else
				val = collator.compare(v1, v2);
			return val;
		}
		
		protected int compareLongs(long v1, long v2) {
			return (v1 == v2) ? 0 : ((v1 > v2) ? 1 : -1);
		}
		
		public int compare(File f1, File f2) {
			int val = 0;
			switch (sortMethod) {
				case KFileView.BY_NAME :
					val = compareStrings(f1.getName(), f2.getName());
					break;
				case KFileView.BY_DATE :
					val = compareLongs(f1.lastModified(), f2.lastModified());
					break;
				default : //KFileView.BY_SIZE
					if (f1.isDirectory() && f2.isDirectory())
						val = compareStrings(f1.getName(), f2.getName(), true);
					else
						val = compareLongs(f1.length(), f2.length());
			}
			if (val < 0)
				val = reverseOrder ? 1 : -1;
			else if (val > 0)
				val = reverseOrder ? -1 : 1;
			return val;
		}
	}
}

