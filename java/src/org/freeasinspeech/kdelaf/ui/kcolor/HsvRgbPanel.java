package org.freeasinspeech.kdelaf.ui.kcolor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*; 

import org.freeasinspeech.kdelaf.ui.KLocale;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class HsvRgbPanel extends JPanel implements LayoutManager, ChangeListener, KColorEventMgr.KColorEventListener
{
	JSpinner hSpinner;
	JSpinner sSpinner;
	JSpinner vSpinner;
	JSpinner rSpinner;
	JSpinner gSpinner;
	JSpinner bSpinner;
	JLabel hLabel;
	JLabel sLabel;
	JLabel vLabel;
	JLabel rLabel;
	JLabel gLabel;
	JLabel bLabel;
	Component[][] comp;
	
	public HsvRgbPanel() {
		/*setLayout(new GridLayout(3, 2));
		JPanel tmp = new JPanel(new GridLayout(1, 2));
		tmp.add(new JLabel(KLocale.i18n("H:")));
		tmp.add(hSpinner = createJSpinner(359));
		add(tmp);
		tmp = new JPanel(new GridLayout(1, 2));
		tmp.add(new JLabel(KLocale.i18n("R:")));
		tmp.add(rSpinner = createJSpinner(255));
		add(tmp);
		tmp = new JPanel(new GridLayout(1, 2));
		tmp.add(new JLabel(KLocale.i18n("S:")));
		tmp.add(sSpinner = createJSpinner(255));
		add(tmp);
		tmp = new JPanel(new GridLayout(1, 2));
		tmp.add(new JLabel(KLocale.i18n("G:")));
		tmp.add(gSpinner = createJSpinner(255));
		add(tmp);
		tmp = new JPanel(new GridLayout(1, 2));
		tmp.add(new JLabel(KLocale.i18n("V:")));
		tmp.add(vSpinner = createJSpinner(255));
		add(tmp);
		tmp = new JPanel(new GridLayout(1, 2));
		tmp.add(new JLabel(KLocale.i18n("B:")));
		tmp.add(bSpinner = createJSpinner(255));
		add(tmp);*/
		
		setLayout(this);
		add(hLabel = new JLabel(KLocale.i18n("H:")));
		add(hSpinner = createJSpinner(359));
		add(rLabel = new JLabel(KLocale.i18n("R:")));
		add(rSpinner = createJSpinner(255));
		add(sLabel = new JLabel(KLocale.i18n("S:")));
		add(sSpinner = createJSpinner(255));
		add(gLabel = new JLabel(KLocale.i18n("G:")));
		add(gSpinner = createJSpinner(255));
		add(vLabel = new JLabel(KLocale.i18n("V:")));
		add(vSpinner = createJSpinner(255));
		add(bLabel = new JLabel(KLocale.i18n("B:")));
		add(bSpinner = createJSpinner(255));
		comp = new Component[4][3];
		comp[0][0] = hLabel;	comp[1][0] = hSpinner;	comp[2][0] = rLabel;	comp[3][0] = rSpinner; 
		comp[0][1] = sLabel;	comp[1][1] = sSpinner;	comp[2][1] = gLabel;	comp[3][1] = gSpinner; 
		comp[0][2] = vLabel;	comp[1][2] = vSpinner;	comp[2][2] = bLabel;	comp[3][2] = bSpinner; 
		KColorEventMgr.addColorEventListener(this);
	}
	
	protected JSpinner createJSpinner(int max) {
		SpinnerNumberModel spinM = new SpinnerNumberModel(0, 0, max, 1);
		spinM.addChangeListener(this);
		return new JSpinner(spinM);
	}
	
	protected void removeCListener(JSpinner spin) {
		spin.getModel().removeChangeListener(this);
	}
	protected void addCListener(JSpinner spin) {
		spin.getModel().addChangeListener(this);
	}
	
	protected void setRGB(Color c) {
		removeCListener(rSpinner);
		removeCListener(gSpinner);
		removeCListener(bSpinner);
		rSpinner.setValue(new Integer(c.getRed()));
		gSpinner.setValue(new Integer(c.getGreen()));
		bSpinner.setValue(new Integer(c.getBlue()));
		addCListener(rSpinner);
		addCListener(gSpinner);
		addCListener(bSpinner);
	}
	protected void setHSV(HSV hsv) {
		removeCListener(hSpinner);
		removeCListener(sSpinner);
		removeCListener(vSpinner);
		hSpinner.setValue(new Integer(hsv.h));
		sSpinner.setValue(new Integer(hsv.s));
		vSpinner.setValue(new Integer(hsv.v));
		addCListener(hSpinner);
		addCListener(sSpinner);
		addCListener(vSpinner);
	}
	
	public void setColor(KColor c) {
		setRGB(c.color);
		setHSV(c.hsv);
	}
	
	public void onColorSelection(KColor c) {
		setColor(c);
	}
	
	public void stateChanged(ChangeEvent e) {
		KColor c = null;
		if (	(e.getSource() == rSpinner.getModel()) ||
				(e.getSource() == gSpinner.getModel()) ||
				(e.getSource() == bSpinner.getModel()) ) {
			c = new KColor(new Color(
				((SpinnerNumberModel)rSpinner.getModel()).getNumber().intValue(),
				((SpinnerNumberModel)gSpinner.getModel()).getNumber().intValue(),
				((SpinnerNumberModel)bSpinner.getModel()).getNumber().intValue()
			));
			setHSV(c.hsv);
		}
		else {
			c = new KColor(new HSV(
				((SpinnerNumberModel)hSpinner.getModel()).getNumber().intValue(),
				((SpinnerNumberModel)sSpinner.getModel()).getNumber().intValue(),
				((SpinnerNumberModel)vSpinner.getModel()).getNumber().intValue()
			));
			setRGB(c.color);
		}
		KColorEventMgr.sendColorEvent(c, this);
	}
	
	public void addLayoutComponent(String name, Component comp) {
	}
	public void removeLayoutComponent(Component comp) {
	}
	public Dimension preferredLayoutSize(Container parent) {
		// [!] its false
		int wSeparator = 5;
		int hSeparator = 5;
		
		int h = 0;
		for (int i = 0; i < 4; i++) {
			int _h = 0;
			for (int j = 0; j < 3; j++)
				_h = Math.max(_h, comp[i][j].getPreferredSize().height);
			h += _h;
		}
		h += hSeparator * 2;
		
		int w = 0;
		for (int j = 0; j < 3; j++) {
			int _w = 0;
			_w = Math.max(_w, comp[0][j].getPreferredSize().width);
			_w = Math.max(_w, comp[2][j].getPreferredSize().width);
			w += _w * 2;
			_w = 0;
			_w = Math.max(_w, comp[1][j].getPreferredSize().width);
			_w = Math.max(_w, comp[3][j].getPreferredSize().width);
			w += _w * 2;
		}
		w += wSeparator * 3;
		
		int widthRGBSpinners = 0;
		for (int j = 0; j < 3; j++)
			widthRGBSpinners = Math.max(widthRGBSpinners, comp[3][j].getPreferredSize().width);
		int widthRGBLabels = 0;
		for (int j = 0; j < 3; j++)
			widthRGBLabels = Math.max(widthRGBLabels, comp[2][j].getPreferredSize().width);
		
		int heightOfALine = 0;
		for (int i = 0; i < 4; i++)
			heightOfALine = Math.max(heightOfALine, comp[i][0].getPreferredSize().height);
		
		return new Dimension((widthRGBSpinners * 2) + (widthRGBLabels * 2) + (3 * wSeparator), (heightOfALine * 3) + (hSeparator * 2));
	}
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}
	public void layoutContainer(Container parent) {
		int wSeparator = 5;
		int hSeparator = 5;
		int widthRGBSpinners = 0;
		int heightOfALine = 0;
		for (int j = 0; j < 3; j++)
			widthRGBSpinners = Math.max(widthRGBSpinners, comp[3][j].getPreferredSize().width);
		for (int i = 0; i < 4; i++)
			heightOfALine = Math.max(heightOfALine, comp[i][0].getPreferredSize().height);
			
		
		Rectangle[][] bounds = new Rectangle[4][3];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 3; j++)
				bounds[i][j] = new Rectangle(0, 0, 0, 0);
		for (int i = 0; i < 4; i++) {
			int _h = 0;
			for (int j = 0; j < 3; j++)
				_h = Math.max(_h, comp[i][j].getPreferredSize().height);
			for (int j = 0; j < 3; j++)
				bounds[i][j].height = _h;
		}
		for (int j = 0; j < 3; j++) {
			int _w = 0;
			_w = Math.max(_w, comp[0][j].getPreferredSize().width);
			_w = Math.max(_w, comp[2][j].getPreferredSize().width);
			bounds[0][j].width = _w;
			bounds[2][j].width = _w;
			_w = 0;
			_w = Math.max(_w, comp[1][j].getPreferredSize().width);
			_w = Math.max(_w, comp[3][j].getPreferredSize().width);
			bounds[1][j].width = _w;
			bounds[3][j].width = _w;
		}
		int width = getWidth();
		int height = getHeight();
		int x = width - widthRGBSpinners;
		int y = height - (heightOfALine * 3) - (hSeparator * 2);
			
		for (int j = 0; j < 3; j++)
			bounds[3][j].x = x;
		for (int i = 0; i < 4; i++)
			bounds[i][0].y = y;
		for (int i = 2; i >= 0; i--)
			for (int j = 0; j < 3; j++)
				bounds[i][j].x = bounds[i + 1][j].x - bounds[i][j].width - wSeparator;
		for (int j = 1; j < 3; j++)
			for (int i = 0; i < 4; i++)
				bounds[i][j].y = bounds[i][j - 1].y + bounds[i][j - 1].height + hSeparator;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 3; j++)
				comp[i][j].setBounds(bounds[i][j]);
	}
}
