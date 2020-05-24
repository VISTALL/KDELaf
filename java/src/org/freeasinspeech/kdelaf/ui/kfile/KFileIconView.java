package org.freeasinspeech.kdelaf.ui.kfile;
 
import java.util.*;
import java.io.*; 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;


/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KFileIconView extends KFileView
{
	JList content;
	KFileModel fileModel;
	JPanel panelView;
	
	public KFileIconView(JFileChooser filechooser) {
		super(filechooser);
		content = new JList();
		//content.setPreferredSize(getPreferredSize());
		content.setVisibleRowCount(-1);
		content.setLayoutOrientation(JList.VERTICAL_WRAP);
		content.setCellRenderer(this);
		fileModel = new KFileModel();
		content.setModel(fileModel);
		content.addMouseListener(new ListMouseListener());
		content.addMouseListener(newPopupListener());
		panelView = new JPanel(new BorderLayout());
		panelView.add(new JScrollPane(content), BorderLayout.CENTER);
		updateView();
	}
	protected JComponent getView() {
		return panelView;
	}
	
	public void registerEvents() {
		super.registerEvents();
		registerEvent(KFileEvent.SELECTION_CHANGED);
	}
	
	public void onKFileEvent(KFileEvent event) {
		super.onKFileEvent(event);
		if (event.is(KFileEvent.SELECTION_CHANGED)) {
			File[] selected = ((KFileSelectionEvent)event).getSelectedFiles();
			if (filechooser.isMultiSelectionEnabled())
				filechooser.setSelectedFiles(selected);
			else
				filechooser.setSelectedFile( ((selected != null) && (selected.length > 0)) ? selected[0] : null );
		}
	}
	
	public void reload() {
		content.setSelectionMode(filechooser.isMultiSelectionEnabled() ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);
		fileModel.clear();
		fileModel.setData(getFilesToDisplay());
	}
	
	public void clearSelection() {
		content.clearSelection();
		MyManager.sendEvent(new KFileSelectionEvent(new File[0]));
	}
	
	public File[] getSelectedFiles() {
		Object[] oSelected = content.getSelectedValues();
		File[] selected = new File[oSelected.length];
		for (int i = 0; i < oSelected.length; i++)
			selected[i] = (File)oSelected[i];
		return selected;
	}
	
	public boolean hasAtLeastOneSelection() {
		return (content.getSelectedIndex() > -1);
	}
		
	class ListMouseListener extends MouseAdapter{	
		protected int locationToIndex(Point p) {
			int index = content.locationToIndex(p);
			if (index > -1) {
				Rectangle r = content.getCellBounds(index, index);
				if ( (r != null) && (!r.contains(p) ) )
					index = -1;
			}
			return index;
		}
		public void mousePressed(MouseEvent e) {
			int index = locationToIndex(e.getPoint());
			if (index > -1) {
				boolean done = false;
				Object[] oSelected = content.getSelectedValues();
				File[] selected = new File[oSelected.length];
				for (int i = 0; i < oSelected.length; i++) {
					selected[i] = (File)oSelected[i];
				}
				if ( (selected.length == 1) && (selected[0].isDirectory()) && (!canSelectDir()) && singleClick) {
					MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, selected[0]));
				}
				else
					MyManager.sendEvent(new KFileSelectionEvent(selected));
			}
			else {
				clearSelection();
			}
		}
		public void mouseClicked(MouseEvent e) {
			int index = locationToIndex(e.getPoint());
			if (index > -1) {
				boolean done = false;
				if (e.getClickCount() == 2) {
					File selectedFile = (File)fileModel.getElementAt(index);
					if (selectedFile.isFile()) {
						if (canSelectFile()) {
							done = true;
						}
					}
					else {
						done = true;
						if (canSelectDir()) {
						}
						else {
							MyManager.sendEvent(new KFileEvent(KFileEvent.LOCATION_CHANGED, selectedFile));
						}
					}
				}/*
				if (!done && !content.getValueIsAdjusting()) {
					System.out.println("count = 1");
					Object[] oSelected = content.getSelectedValues();
					File[] selected = new File[oSelected.length];
					for (int i = 0; i < oSelected.length; i++) {
						selected[i] = (File)oSelected[i];
					}
					if ( (selected.length == 1) && (selected[0].isDirectory()) && (!canSelectDir()) && singleClick) {
						filechooser.setCurrentDirectory(selected[0]);
						fireLocationValueChanged(selected[0]);
					}
					else
						fireSelectionValueChanged(selected);
				}*/
			}
			/*else {
				clearSelection();
			}*/
		}
	}
}
 
						
