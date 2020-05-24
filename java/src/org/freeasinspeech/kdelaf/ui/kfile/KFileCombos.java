package org.freeasinspeech.kdelaf.ui.kfile;
 
import java.util.*;
import java.io.*; 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

import org.freeasinspeech.kdelaf.ui.*;
import org.freeasinspeech.kdelaf.Logger;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KFileCombos extends KFileBase implements ActionListener, ListCellRenderer
{
	protected JComboBox selectedComboBox;
	protected JComboBox filtersComboBox;
	protected JButton buttonOk;
	protected JButton buttonCancel;
	JLabel listCellRendererComponent;
		
	public KFileCombos(JFileChooser filechooser) {
		super(filechooser);
		
		listCellRendererComponent = new JLabel();
		listCellRendererComponent.setOpaque(true);
		
		selectedComboBox = new JComboBox();
		selectedComboBox.setEditable(true);
		String tmp = "";
		selectedComboBox.addItem(tmp);
		selectedComboBox.setSelectedItem(tmp);
		filtersComboBox = new JComboBox();
		filtersComboBox.setEditable(true);
		filtersComboBox.setRenderer(this);
		filtersComboBox.setEditor(new FilterEditor());
		buildFileFilters();
		/*
 String 	getApproveButtonToolTipText() 
		buttonOk = new JButton(filechooser.getApproveButtonText());*/
		buttonOk = new JButton("OK");
		buttonOk.addActionListener(this);
		buttonOk.setEnabled(false);
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(this);
		
		JPanel panelLabels = new JPanel(new GridLayout(2, 1));
		JPanel panelCombos = new JPanel(new GridLayout(2, 1));
		JPanel panelButtons = new JPanel(new GridLayout(2, 1));
		
		panelLabels.add(new JLabel("Emplacement :"));
		panelLabels.add(new JLabel("Filtres :"));
		
		panelCombos.add(selectedComboBox);
		panelCombos.add(filtersComboBox);
		
		panelButtons.add(buttonOk);
		panelButtons.add(buttonCancel);
		
		
		setLayout(new BorderLayout());
		add(panelLabels, BorderLayout.WEST);
		add(panelCombos, BorderLayout.CENTER);
		add(panelButtons, BorderLayout.EAST);
	}
	
	public void registerEvents() {
		super.registerEvents();
		registerEvent(KFileEvent.SELECTION_CHANGED);
	}
	
	public void onKFileEvent(KFileEvent event) {
		super.onKFileEvent(event);
		if (event.is(KFileEvent.SELECTION_CHANGED)) {
			File[] selected = ((KFileSelectionEvent)event).getSelectedFiles();
			buttonOk.setEnabled(selected.length > 0);
			
			String selectedText = "";
			for (int i = 0; i < selected.length; i++)
				selectedText += "\"" + selected[i].getName() + "\" ";
			selectedComboBox.setSelectedItem(selectedText);
		}
	}
	
	protected void buildFileFilters() {
		javax.swing.filechooser.FileFilter[] filters = filechooser.getChoosableFileFilters();
		for (int i = 0; i < filters.length; i++) {
			filtersComboBox.addItem(filters[i]);
		}
		filtersComboBox.setSelectedItem(filechooser.getFileFilter());
	}
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		javax.swing.filechooser.FileFilter filter = (javax.swing.filechooser.FileFilter)value;
		
		listCellRendererComponent.setText(filter.getDescription());
		listCellRendererComponent.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		listCellRendererComponent.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
		return listCellRendererComponent;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonOk) {
			String selectedTxt = (selectedComboBox.getSelectedItem() != null) ? selectedComboBox.getSelectedItem().toString() : "";
			File[] selected = null;
			try {
				Vector vSelected = new Vector();
				Pattern p = Pattern.compile("\"([^\"]+)\"");
				Matcher m = p.matcher(selectedTxt);
				while (m.find()) {
					vSelected.add(new File(filechooser.getCurrentDirectory(), m.group(1)));
					selectedTxt = selectedTxt.substring(Math.min(m.end(1) + 1, selectedTxt.length()));
					m = p.matcher(selectedTxt);
				}
				selected = new File[vSelected.size()];
				int i = 0;
				for (Enumeration enumeration = vSelected.elements() ; enumeration.hasMoreElements() ;) {
					selected[i++] = (File)enumeration.nextElement();
				}
			}
			catch (PatternSyntaxException pse) {
				Logger.error(pse);
			}
			if (selected == null) {
				selectedTxt = (selectedComboBox.getSelectedItem() != null) ? selectedComboBox.getSelectedItem().toString() : "";
				selectedTxt = selectedTxt.replaceAll("\"", "").trim();
				if (selectedTxt.length() > 0) {
					selected = new File[1];
					selected[0] = new File(filechooser.getCurrentDirectory(), selectedTxt);
				}
			}
			if (filechooser.isMultiSelectionEnabled())
				filechooser.setSelectedFiles(selected);
			else
				filechooser.setSelectedFile( ((selected != null) && (selected.length > 0)) ? selected[0] : null );
			filechooser.approveSelection();
		}
		else if (e.getSource() == buttonCancel) {
			filechooser.cancelSelection();
		}
	}
	
	class FilterEditor extends QComboBoxEditor
	{
		protected javax.swing.filechooser.FileFilter filter;
		public void setItem(Object item) {
			filter = (javax.swing.filechooser.FileFilter)item;
			if (filter == null)
				editor.setText("");
			else
				editor.setText(filter.getDescription());
		}
		
		public Object getItem() {
			return filter;
		}
	}
}
