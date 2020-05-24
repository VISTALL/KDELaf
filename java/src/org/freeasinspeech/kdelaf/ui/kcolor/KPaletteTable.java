package org.freeasinspeech.kdelaf.ui.kcolor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;

import org.freeasinspeech.kdelaf.ui.KLocale;
import org.freeasinspeech.kdelaf.Logger;

public class KPaletteTable extends JPanel implements ActionListener
{
	protected JComboBox combo;
	protected KColorCells cells;
	protected KColorList list;
	protected KPalette namedColors;
	protected boolean isDisplayingList;
	protected JScrollPane content;
	
	public KPaletteTable() {
		super(new BorderLayout());
		isDisplayingList = false;
		init();
	}
	
	protected void init() {
		buildNamedColors();
		combo = new JComboBox();
		add(combo, BorderLayout.NORTH);
		cells = new KColorCells();
		content = new JScrollPane(cells, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(content, BorderLayout.CENTER);
		list = new KColorList();
		
		KPalette[] diskPaletteList = KPalettes.getPalettes();
		for (int i = 0; i < diskPaletteList.length; i++) {
			combo.addItem(diskPaletteList[i]);
		}
		combo.addItem(namedColors);
		combo.addActionListener(this);
		combo.setSelectedItem(null);
	}
	
	protected void buildNamedColors() {
		Logger.warning("KPaletteTable::buildNamedColors() use KDE for the path");
		namedColors = new KPalette(KLocale.i18n("Named Colors"));
		String[] path = {
			"/usr/X11R6/lib/X11/rgb.txt",
			"/usr/openwin/lib/X11/rgb.txt"
		};
		boolean loaded = false;
		for (int i = 0; (i < path.length) && !loaded; i++)
			loaded = KPalettes.load(new java.io.File(path[i]), namedColors);
		namedColors.formatNamedColors();
	}

	public void actionPerformed(ActionEvent e) {
		KPalette selected = (KPalette)combo.getSelectedItem();
		if (selected != null)
			setPalette(selected);
	}
	
	protected void setPalette(KPalette palette) {
		if (palette == namedColors) {
			list.setPalette(palette);
			if (!isDisplayingList) {
				content.setViewportView(list);
				isDisplayingList = true;
			}
		}
		else {
			cells.setPalette(palette);
			if (isDisplayingList) {
				content.setViewportView(cells);
				isDisplayingList = false;
			}
		}
	}
}

class KColorList extends JList
{
	protected KPalette palette;
	protected KPaletteListModel listModel;
		
	public KColorList() {
		super();
		listModel = new KPaletteListModel();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setModel(listModel);
		setCellRenderer(new KPaletteCellRenderer());
		addListSelectionListener(new KPaletteListListener());
	}
	
	public void setPalette(KPalette palette) {
		this.palette = palette;
		listModel.update(this);
		setSelectedIndex(0);
	}
	
	class KPaletteListListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e) {
			KColor kcolor = (KColor)getSelectedValue();
			if (kcolor != null)
				KColorEventMgr.sendColorEvent(kcolor);
		}
	}
	
	class KPaletteListModel extends AbstractListModel
	{
		public Object getElementAt(int index) {
			return (palette == null) ? null : palette.get(index);
		}
		public int getSize() {
			if (palette == null)
				return 0;
			return palette.size();
		}
		void update(JList l) {
			fireContentsChanged(l, 0, getSize());
		}
	}
	
	class KPaletteCellRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			return super.getListCellRendererComponent(list, (value instanceof KColor) ? ((KColor)value).name : value, index, isSelected, cellHasFocus);
		}
	}
}

class KColorCells extends JTable implements javax.swing.table.TableCellRenderer
{
	protected KColorCellsRenderer cellsRenderer;
	protected KPalette palette;
	protected KColorCellsModel cellModel;
	
	public KColorCells() {
		super();
		cellModel = new KColorCellsModel();
		setModel(cellModel);
		cellsRenderer = new KColorCellsRenderer();
		setDefaultRenderer(Object.class, this);
		setIntercellSpacing(new Dimension(0, 0));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Dimension cellDim = cellsRenderer.getPreferredSize();
		setRowHeight(cellDim.height);
		setPreferredScrollableViewportSize(new Dimension(cellModel.getColumnCount() * cellDim.width, 9 * cellDim.height));
		setTableHeader(null);
		KColorCellsListener l = new KColorCellsListener();
		getSelectionModel().addListSelectionListener(l);
		getColumnModel().addColumnModelListener(l);
	}
	
	public void select(int row, int col) {
		KColor kcolor = (KColor)getValueAt(row, col);
		if (kcolor != null) {
			ListSelectionModel rm = getSelectionModel();
			ListSelectionModel cm = getColumnModel().getSelectionModel();
			rm.clearSelection();
			cm.clearSelection();
			rm.setSelectionInterval(row, row);
			cm.setSelectionInterval(col, col);
			KColorEventMgr.sendColorEvent(kcolor);
			repaint();
		}
	}
	
	public void setPalette(KPalette palette) {
		this.palette = palette;
		cellModel.fireTableDataChanged();
		select(0, 0);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if ( (value != null) && (value instanceof KColor) ) {
			KColor kcolor = (KColor)value;
			cellsRenderer.setColor(kcolor.color);
		}
		else
			cellsRenderer.setColor(null);
		cellsRenderer.drawFocus(isSelected);
		return cellsRenderer;
	}
	
	class KColorCellsModel extends javax.swing.table.AbstractTableModel
	{
		
		public int getColumnCount() {
			return 16;
		}
		
		public int getRowCount() {
			if (palette == null)
				return 0;
			return (int)Math.ceil((double)palette.size() / (double)getColumnCount());
		}
		
		public Object getValueAt(int row, int col) {
			if (palette == null)
				return null;
			return palette.get(col + row * getColumnCount());
		}
	}
	
	class KColorCellsRenderer extends Canvas	{
		protected Color color = null;
		protected boolean mustDrawFocus = false;
		
		public void setColor(Color color) {
			this.color = color;
		}
		public void drawFocus(boolean mustDrawFocus) {
			this.mustDrawFocus = mustDrawFocus;
		}
		
		public void paint(Graphics g) {
			if (color != null) {
				Color saved = g.getColor();
				g.setColor(color);
				g.fillRect(1, 1, getWidth() - 1, getHeight() - 1);
				g.setColor(saved);
			}
			if (mustDrawFocus) {
			// [!] to do focus rect
			}
		}
		
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}
		
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(13, 13);
		}	
		
		public boolean isFocusable() {
			return false;
		}
	}
	
	class KColorCellsListener implements ListSelectionListener, TableColumnModelListener
	{
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
				return ;
			int row = getSelectedRow();
			int col = getSelectedColumn();
			if ( (row > -1) && (col > -1) ) {
				KColor kcolor = (KColor)getValueAt(row, col);
				if (kcolor != null) {
					KColorEventMgr.sendColorEvent(kcolor);
				}
			}
		}
		public void columnAdded(TableColumnModelEvent e) {}
		public void columnRemoved(TableColumnModelEvent e) {}
		public void columnMoved(TableColumnModelEvent e) {}
		public void columnMarginChanged(ChangeEvent e) {}
		public void columnSelectionChanged(ListSelectionEvent e) {
			valueChanged(e);
		}
	}
}
