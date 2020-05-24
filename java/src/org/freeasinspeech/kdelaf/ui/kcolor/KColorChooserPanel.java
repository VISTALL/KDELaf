package org.freeasinspeech.kdelaf.ui.kcolor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import org.freeasinspeech.kdelaf.ui.KLocale;
import org.freeasinspeech.kdelaf.ui.KIconFactory;
import org.freeasinspeech.kdelaf.ui.KIcon;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KColorChooserPanel extends AbstractColorChooserPanel
{
	protected JColorChooser chooser;
		
	public KColorChooserPanel(JColorChooser chooser) {
		super();
		this.chooser = chooser;
		setLayout(new BorderLayout());
		buildChooser();
	}
		
	protected void buildChooser() {
		JButton addCustomColors = new JButton(KLocale.i18n("&Add to Custom Colors"));
		JButton colorPicker = new JButton(KIconFactory.getIconBySize(KIcon.S_22x22, "colorpicker").load());
		addCustomColors.setEnabled(false);
		colorPicker.setEnabled(false);
		KHSSelector hsSelector = new KHSSelector();
		KValueSelector vSelector = new KValueSelector(SwingConstants.VERTICAL);
		KPaletteTable paletteTable = new KPaletteTable();
		HsvRgbPanel hsvRgbPanel = new HsvRgbPanel();
		KHtmlAndName htmlAndName = new KHtmlAndName();
		KColorPatch colorPatch = new KColorPatch();
		vSelector.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		hsvRgbPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		colorPicker.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		hsSelector.setPreferredSize(new Dimension(140, 140));
		
		JPanel hsvPanel = new JPanel(new BorderLayout());
		hsvPanel.add(hsSelector, BorderLayout.CENTER);
		hsvPanel.add(vSelector, BorderLayout.EAST);
		
		JPanel hsvSelSpinPanel = new JPanel(new BorderLayout());
		hsvSelSpinPanel.add(hsvPanel, BorderLayout.NORTH);
		hsvSelSpinPanel.add(hsvRgbPanel, BorderLayout.CENTER);
		
		JPanel colorButtonsPanel = new JPanel(new BorderLayout());
		colorButtonsPanel.add(addCustomColors, BorderLayout.CENTER);
		colorButtonsPanel.add(colorPicker, BorderLayout.EAST);
		colorButtonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		
		JPanel patchHtmlAndName = new JPanel(new BorderLayout());
		patchHtmlAndName.add(htmlAndName, BorderLayout.CENTER);
		patchHtmlAndName.add(colorPatch, BorderLayout.WEST);
		JPanel palettePatchHtmlAndNamePanel = new JPanel(new BorderLayout());
		palettePatchHtmlAndNamePanel.add(paletteTable, BorderLayout.NORTH);
		palettePatchHtmlAndNamePanel.add(colorButtonsPanel, BorderLayout.CENTER);
		palettePatchHtmlAndNamePanel.add(patchHtmlAndName, BorderLayout.SOUTH);
		palettePatchHtmlAndNamePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
		
		add(hsvSelSpinPanel, BorderLayout.WEST);
		add(palettePatchHtmlAndNamePanel, BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
	}	
	
	public Icon getSmallDisplayIcon() {
		return null;
	}
	public Icon getLargeDisplayIcon() {
		return null;
	}
	public void updateChooser() {
	}
	public String getDisplayName() {
		return "kde";
	}
}

// [!] todo drag & drop
class KColorPatch extends JPanel implements KColorEventMgr.KColorEventListener
{
	protected Color current;
		
	public KColorPatch() {
		super();
		current = Color.BLACK;
		KColorEventMgr.addColorEventListener(this);
	}
	
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(48, 48);
	}
	
	public void onColorSelection(KColor c) {
		current = c.color;
		repaint();
	}
	
	public void paint(Graphics g) {
		Color saved = g.getColor();
		g.setColor(current);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(saved);
	}
}

class KHtmlAndName extends JPanel implements KColorEventMgr.KColorEventListener
{
	protected JTextField htmlName;
	protected JLabel colorName;
	protected String unnamedName; 
	
	public KHtmlAndName() {
		super();
		unnamedName = KLocale.i18n("-unnamed-");
		htmlName = new JTextField(7);
		colorName = new JLabel();
		setLayout(new GridLayout(2, 2));
		add(new JLabel(KLocale.i18n("Name:")));
		add(colorName);
		add(new JLabel(KLocale.i18n("HTML:")));
		add(htmlName);
		onColorSelection(KColor.BLACK);
		KColorEventMgr.addColorEventListener(this);
	}
	
	public void onColorSelection(KColor c) {
		String html = "#";
		html += formatHTMLComponent(Integer.toHexString(c.color.getRed()));
		html += formatHTMLComponent(Integer.toHexString(c.color.getGreen()));
		html += formatHTMLComponent(Integer.toHexString(c.color.getBlue()));
		htmlName.setText(html);
		String name = KPalettes.getName(c.color);
		colorName.setText((name == null) ? unnamedName : KLocale.i18n(name));
	}
	
	protected String formatHTMLComponent(String hex) {
		String formated = hex;
		while (formated.length() < 2)
			formated = "0" + formated;
		return formated.toUpperCase();
	}
}
