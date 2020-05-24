package org.freeasinspeech.kdelaf.client;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.font.*;
import java.util.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
import java.awt.geom.AffineTransform;

import org.freeasinspeech.kdelaf.ui.KIconFactory;
import org.freeasinspeech.kdelaf.ui.KIcon;
import org.freeasinspeech.kdelaf.ui.KLocale;

/**
 * Class KDEClient
 * Client for the KDELafServer.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KDEClient
{
	// [!] pb
	public static boolean USE_CACHE = false;
		
	/** The cache. */
	protected Worker worker;
	protected OutBuffer toServer;
	protected InBuffer fromServer;
	protected long imgsTime;
	protected double imgsBytes;
	protected boolean wReady = false;
	protected boolean lock = false;
		
	/**
	 * Constructor.
	 * @param s The connection to the KDELafServer.
	 */
	public KDEClient() throws IOException {
		worker = null;
		toServer = null;
		fromServer = null;
		imgsTime = 0;
		imgsBytes = 0;
	}
	
	public boolean connect(WorkerErrorListener l) throws IOException {
		boolean ok = false;
		imgsTime = 0;
		imgsBytes = 0;
		wReady = false;
		if (worker == null) {
			worker = new Worker(l, this);
			worker.start();
			// hack
			while (!isWorkerReady());
			if (worker.waitReady()) {
				toServer = worker.getOut();
				fromServer = worker.getIn();
				ok = true;
			}
		}
		return ok;
	}
	public synchronized boolean isWorkerReady() {
			// hack
		return wReady;
	}
	
	public synchronized void workerReady() {
			// hack
		wReady = true;
	}
	
	public synchronized void acquire() {
		while (lock) {
			try {
				Thread.sleep(5);
			}
			catch (Exception e) {}
		}
		lock = true;
	}
	
	public synchronized void release() {
		lock = false;
	}
	
	public void disconnect() throws IOException {
		if (worker != null) {
			acquire();
			// write the operation code
			toServer.writeInt(KDEConstants.OP_KILL);
			// sends the message
			toServer.send();
			release();
			worker.kill();
			worker = null;
			toServer = null;
			fromServer = null;
			reportImgBench();
		}
	}
	
	protected void reportImgBench() {
		double imgsKb = imgsBytes / 1024.0;
		double seconds = (double)imgsTime / 1000.0;
		double kbPerSecond = imgsKb / seconds;
		org.freeasinspeech.kdelaf.Logger.log("We've received " + round(imgsKb, 2) + "Kb images-data in " + round(seconds, 2) + "s");
		org.freeasinspeech.kdelaf.Logger.log("throuput = " + round(kbPerSecond, 2) + "Kb/s.");
	}
	
	protected double round(double val, long numDec) {
		double factor = (double)Math.pow(10, numDec);
		double rounded = Math.round(val * factor) / factor;
		return rounded;
	}
	
	/**
	 * Returns the default configuration all ComponentUI
	 * @return The default configuration all ComponentUI
	 */
	public Object[] initComponentDefaults() throws IOException {
		LinkedList comp = new LinkedList();
		BaseDefaults def;
		// Button
		def = getBaseDefaults(KDEConstants.WT_BUTTON);
		comp.add("Button.background"); comp.add(def.background);
		comp.add("Button.foreground"); comp.add(def.foreground);
		comp.add("Button.font"); comp.add(def.font);
		comp.add("Button.textIconGap"); comp.add( new Integer(4));
		comp.add("Button.border"); comp.add(null);
		comp.add("Button.darkShadow"); comp.add(null);
		comp.add("Button.light"); comp.add(null);
		comp.add("Button.highlight"); comp.add(null);
		// CheckBox
		def = getBaseDefaults(KDEConstants.WT_CHECKBOX);
		comp.add("CheckBox.background"); comp.add(def.background);
		comp.add("CheckBox.foreground"); comp.add(def.foreground);
		comp.add("CheckBox.font"); comp.add(def.font);
		comp.add("CheckBox.border"); comp.add(null);
		comp.add("CheckBox.margin"); comp.add(null);
		comp.add("CheckBox.darkShadow"); comp.add(null);
		comp.add("CheckBox.light"); comp.add(null);
		comp.add("CheckBox.highlight"); comp.add(null);
		comp.add("CheckBox.icon"); comp.add(org.freeasinspeech.kdelaf.ui.QCheckBoxUI.getIcon());
		// CheckBoxMenuItem 
		def = getBaseDefaults(KDEConstants.WT_CHECKBOXMENUITEM);
		comp.add("CheckBoxMenuItem.background"); comp.add(def.background);
		comp.add("CheckBoxMenuItem.foreground"); comp.add(def.foreground);
		comp.add("CheckBoxMenuItem.font"); comp.add(def.font);
		comp.add("CheckBoxMenuItem.border"); comp.add(null);
		comp.add("CheckBoxMenuItem.margin"); comp.add(null);
		comp.add("CheckBoxMenuItem.checkIcon"); comp.add(new org.freeasinspeech.kdelaf.ui.QCheckBoxMenuItemIcon());
		// ComboBox
		getComboBoxDefaults(comp);
		// InternalFrame
		getInternalFrameDefaults(comp);
		// Label
		getLabelDefaults(comp);
		// List
		getListDefaults(comp);
		// MenuBar
		def = getBaseDefaults(KDEConstants.WT_MENUBAR);
		comp.add("MenuBar.background"); comp.add(def.background);
		comp.add("MenuBar.foreground"); comp.add(def.foreground);
		comp.add("MenuBar.font"); comp.add(def.font);
		comp.add("MenuBar.border"); comp.add(null);
		// MenuItem
		def = getBaseDefaults(KDEConstants.WT_MENUITEM);
		comp.add("MenuItem.background"); comp.add(def.background);
		comp.add("MenuItem.foreground"); comp.add(def.foreground);
		comp.add("MenuItem.font"); comp.add(def.font);
		comp.add("MenuItem.acceleratorDelimiter"); comp.add("+");
		// Menu
		def = getBaseDefaults(KDEConstants.WT_MENU);
		comp.add("Menu.background"); comp.add(def.background);
		comp.add("Menu.foreground"); comp.add(def.foreground);
		comp.add("Menu.font"); comp.add(def.font);
		// OptionPane
		comp.add("OptionPane.sameSizeButtons"); comp.add(new Boolean(false));
		comp.add("OptionPane.buttonOrientation"); comp.add(new Integer(SwingConstants.RIGHT));
		comp.add("OptionPane.yesIcon"); comp.add(KIconFactory.getIcon(KIcon.S_22x22, KIcon.C_ACTION, "button_ok").load());
		comp.add("OptionPane.okIcon"); comp.add(KIconFactory.getIcon(KIcon.S_22x22, KIcon.C_ACTION, "button_ok").load());
		comp.add("OptionPane.noIcon"); comp.add(null);//comp.add(KIconFactory.getIcon(KIcon.S_22x22, KIcon.C_ACTION, "button_cancel").load());
		comp.add("OptionPane.cancelIcon"); comp.add(KIconFactory.getIcon(KIcon.S_22x22, KIcon.C_ACTION, "button_cancel").load());
		String dict = KLocale.getSelected();
		comp.add("OptionPane.yesButtonText"); comp.add(KLocale.i18n("&Yes"));
		comp.add("OptionPane.okButtonText"); comp.add(KLocale.i18n("&OK"));
		comp.add("OptionPane.noButtonText"); comp.add(KLocale.i18n("&No"));
		comp.add("OptionPane.cancelButtonText"); comp.add(KLocale.i18n("&Cancel"));
		KLocale.select(dict);
/*

		
		org.freeasinspeech.kdelaf.ui.KLocale.select("kdelibs_colors");
				color.name = org.freeasinspeech.kdelaf.ui.
		org.freeasinspeech.kdelaf.ui.

97 KGuiItem KStdGuiItem::ok()
00098 {
00099   return KGuiItem( i18n( "" ), "button_ok" );
00100 }
00101 
00102 
00103 KGuiItem KStdGuiItem::cancel()
00104 {
00105   return KGuiItem( i18n( "" ), "button_cancel" );
00106 }
00107 
00108 KGuiItem KStdGuiItem::yes()
00109 {
00110   return KGuiItem( i18n( "" ), "button_ok", i18n( "Yes" ) );
00111 }
00112 
00113 KGuiItem KStdGuiItem::no()
00114 {
00115   return KGuiItem( i18n( "" ), "", i18n( "No" ) );
00116 }






yesButtonMnemonic
okButtonMnemonic
noButtonMnemonic
cancelButtonMnemonic
*/
		// PopupMenu
		def = getBaseDefaults(KDEConstants.WT_POPUPMENU);
		comp.add("PopupMenu.background"); comp.add(def.background);
		comp.add("PopupMenu.foreground"); comp.add(def.foreground);
		comp.add("PopupMenu.font"); comp.add(def.font);
		// ProgressBar
		def = getBaseDefaults(KDEConstants.WT_PROGRESSBAR);
		comp.add("ProgressBar.background"); comp.add(def.background);
		comp.add("ProgressBar.foreground"); comp.add(def.foreground);
		comp.add("ProgressBar.font"); comp.add(def.font);
		comp.add("ProgressBar.repaintInterval"); comp.add(new Integer(50));
		comp.add("ProgressBar.cycleTime"); comp.add(new Integer(3000));
		comp.add("ProgressBar.cellSpacing"); comp.add(new Integer(0));
		comp.add("ProgressBar.border"); comp.add(null);
		/*Object[] components = {
			"ProgressBar.selectionBackground", QtUIUtilities.QColorToColor(wrapper.palette().active().highlight()),
			"ProgressBar.selectionForeground", QtUIUtilities.QColorToColor(wrapper.palette().active().highlightedText()),
		};*/
		// RadioButtonMenuItem
		def = getBaseDefaults(KDEConstants.WT_RADIOBUTTONMENUITEM);
		comp.add("RadioButtonMenuItem.background"); comp.add(def.background);
		comp.add("RadioButtonMenuItem.foreground"); comp.add(def.foreground);
		comp.add("RadioButtonMenuItem.font"); comp.add(def.font);
		comp.add("RadioButtonMenuItem.border"); comp.add(null);
		comp.add("RadioButtonMenuItem.margin"); comp.add(null);
		comp.add("RadioButtonMenuItem.checkIcon"); comp.add(new org.freeasinspeech.kdelaf.ui.QRadioButtonMenuItemIcon());
		// RadioButton
		def = getBaseDefaults(KDEConstants.WT_RADIOBUTTON);
		comp.add("RadioButton.background"); comp.add(def.background);
		comp.add("RadioButton.foreground"); comp.add(def.foreground);
		comp.add("RadioButton.font"); comp.add(def.font);
		comp.add("RadioButton.border"); comp.add(null);
		comp.add("RadioButton.margin"); comp.add(null);
		comp.add("RadioButton.darkShadow"); comp.add(null);
		comp.add("RadioButton.light"); comp.add(null);
		comp.add("RadioButton.highlight"); comp.add(null);
		comp.add("RadioButton.icon"); comp.add(org.freeasinspeech.kdelaf.ui.QRadioButtonUI.getIcon());
		// Spinner
		def = getBaseDefaults(KDEConstants.WT_SPINNER);
		comp.add("Spinner.background"); comp.add(def.background);
		comp.add("Spinner.foreground"); comp.add(def.foreground);
		comp.add("Spinner.font"); comp.add(def.font);
		comp.add("Spinner.border"); comp.add(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));		
		comp.add("Spinner.arrowButtonInsets"); comp.add(null);		
		// TextField
		getTextFieldDefaults(comp);
		// TabbedPane
		//[!] bug with some themes
		//int vertical = getPixelMetric(KDEConstants.JPM_TabBarTabVSpace) / 2;
		//int horizontal = getPixelMetric(KDEConstants.JPM_TabBarTabHSpace) / 2;
		int vertical = 4;
		int horizontal = 4;
		def = getBaseDefaults(KDEConstants.WT_TABBEDPANE);
		comp.add("TabbedPane.background"); comp.add(def.background);
		comp.add("TabbedPane.foreground"); comp.add(def.foreground);
		comp.add("TabbedPane.font"); comp.add(def.font);
		comp.add("TabbedPane.tabbedPaneTabPadInsets"); comp.add(new InsetsUIResource(0, 0, 0, horizontal));
		comp.add("TabbedPane.tabInsets"); comp.add(new InsetsUIResource(vertical, horizontal, vertical, horizontal));
		comp.add("TabbedPane.tabbedPaneTabPadInsets"); comp.add(new InsetsUIResource(0, 0, 0, 0));
		// ToggleButton
		def = getBaseDefaults(KDEConstants.WT_TOGGLEBUTTON);
		comp.add("ToggleButton.background"); comp.add(def.background);
		comp.add("ToggleButton.foreground"); comp.add(def.foreground);
		comp.add("ToggleButton.font"); comp.add(def.font);
		comp.add("ToggleButton.border"); comp.add(null);
		comp.add("ToggleButton.margin"); comp.add(null);
		comp.add("ToggleButton.shadow"); comp.add(null);
		comp.add("ToggleButton.darkShadow"); comp.add(null);
		comp.add("ToggleButton.focusInputMap"); comp.add(null);
		comp.add("ToggleButton.light"); comp.add(null);
		comp.add("ToggleButton.highlight"); comp.add(null);
		comp.add("ToggleButton.textIconGap"); comp.add(new Integer(4));
		comp.add("ToggleButton.textShiftOffset"); comp.add(new Integer(0));
		// ToolTip
		getToolTipDefaults(comp);
		// ToolBar
		def = getBaseDefaults(KDEConstants.WT_TOOLBAR);
		comp.add("ToolBar.background"); comp.add(def.background);
		comp.add("ToolBar.foreground"); comp.add(def.foreground);
		comp.add("ToolBar.font"); comp.add(def.font);
		comp.add("ToolBar.border"); comp.add(null);
		// Tree
		getTreeDefaults(comp);
		return comp.toArray();
	}
	
	public String getStyleName()  throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_STYLE_NAME);
		// sends the message
		toServer.send();
		fromServer.receive();
		String name = fromServer.readString();
		release();
		return name;
	}
	
	public String getKdeVersion()  throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_KDE_VERSION);
		// sends the message
		toServer.send();
		fromServer.receive();
		String name = fromServer.readString();
		release();
		return name;
	}	
	
	public void getToolTipDefaults(LinkedList comp) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_TOOLTIPDEFAULTS);
		// sends the message
		toServer.send();
		
		fromServer.receive();
		// receive the background color
		Color bg = fromServer.readColor();
		// receive the foreground color
		Color fg = fromServer.readColor();
		// receive the font
		Font ft = fromServer.readFont();
		// receive the inactive background color
		Color bgi = fromServer.readColor();
		// receive the inactive foreground color
		Color fgi = fromServer.readColor();
		release();
		comp.add("ToolTip.background"); comp.add(bg);
		comp.add("ToolTip.foreground"); comp.add(fg);
		comp.add("ToolTip.font"); comp.add( ft);
		comp.add("ToolTip.backgroundInactive"); comp.add(bgi);
		comp.add("ToolTip.foregroundInactive"); comp.add(fgi);
			// [!] todo : use Qt to retrieve border info
		comp.add("ToolTip.border"); comp.add(BorderFactory.createLineBorder(Color.BLACK, 1));
		comp.add("ToolTip.borderInactive"); comp.add(BorderFactory.createLineBorder(Color.GRAY, 1));
	}
	
	public void getTreeDefaults(LinkedList comp) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_TREEDEFAULTS);
		// sends the message
		toServer.send();
		
		fromServer.receive();
		// receive the background color
		Color bg = fromServer.readColor();
		// receive the foreground color
		Color fg = fromServer.readColor();
		// receive the font
		Font ft = fromServer.readFont();
		// receive the text background color
		Color tbg = fromServer.readColor();
		// receive the text foreground color
		Color tfg = fromServer.readColor();
		// receive the selection background color
		Color sbg = fromServer.readColor();
		// receive the selection foreground color
		Color sfg = fromServer.readColor();
		release();
		comp.add("Tree.background"); comp.add(bg);
		comp.add("Tree.foreground"); comp.add(fg);
		comp.add("Tree.font"); comp.add(ft);
		comp.add("Tree.textBackground"); comp.add(tbg);
		comp.add("Tree.textForeground"); comp.add(tfg);
		comp.add("Tree.selectionBackground"); comp.add(sbg);
		comp.add("Tree.selectionForeground"); comp.add(sfg);
		
		comp.add("Tree.openIcon"); comp.add(KIconFactory.getIcon(KIcon.S_16x16, KIcon.C_FILESYSTEM, "folder_open").load());
		comp.add("Tree.closedIcon"); comp.add(KIconFactory.getIcon(KIcon.S_16x16, KIcon.C_FILESYSTEM, "folder").load());
		comp.add("Tree.leafIcon"); comp.add(KIconFactory.getIcon(KIcon.S_16x16, KIcon.C_MIMETYPE, "empty").load());
	}
	
	public void getListDefaults(LinkedList comp) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_TREEDEFAULTS);
		// sends the message
		toServer.send();
		
		fromServer.receive();
		// receive the background color
		Color bg = fromServer.readColor();
		// receive the foreground color
		Color fg = fromServer.readColor();
		// receive the font
		Font ft = fromServer.readFont();
		// receive the text background color
		Color tbg = fromServer.readColor(); // Compatibility with getTreeDefaults
		// receive the text foreground color
		Color tfg = fromServer.readColor(); // Compatibility with getTreeDefaults
		// receive the selection background color
		Color sbg = fromServer.readColor();
		// receive the selection foreground color
		Color sfg = fromServer.readColor();
		release();
		comp.add("List.background"); comp.add(bg);
		comp.add("List.foreground"); comp.add(fg);
		comp.add("List.font"); comp.add(ft);
		comp.add("List.selectionBackground"); comp.add(sbg);
		comp.add("List.selectionForeground"); comp.add(sfg);
		comp.add("List.border"); comp.add(null);
		comp.add("List.focusCellHighlightBorder"); comp.add(null);
	}
	
	public void getTextFieldDefaults(LinkedList comp) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_TEXTFIELDDEFAULTS);
		// sends the message
		toServer.send();
		
		fromServer.receive();
		// receive the background color
		Color bg = fromServer.readColor();
		// receive the foreground color
		Color fg = fromServer.readColor();
		// receive the font
		Font ft = fromServer.readFont();
		// receive the inactive background color
		Color ibg = fromServer.readColor();
		// receive the inactive foreground color
		Color ifg = fromServer.readColor();
		// receive the selection background color
		Color sbg = fromServer.readColor();
		// receive the selection foreground color
		Color sfg = fromServer.readColor();
		release();
		comp.add("TextField.background"); comp.add(bg);
		comp.add("TextField.foreground"); comp.add(fg);
		comp.add("TextField.font"); comp.add(ft);
		comp.add("TextField.inactiveBackground"); comp.add(ibg);
		comp.add("TextField.inactiveForeground"); comp.add(ifg);
		comp.add("TextField.selectionBackground"); comp.add(sbg);
		comp.add("TextField.selectionForeground"); comp.add(sfg);
	}
	
	public void getLabelDefaults(LinkedList comp) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_LABELDEFAULTS);
		// sends the message
		toServer.send();
		
		fromServer.receive();
		// receive the background color
		Color bg = fromServer.readColor();
		// receive the foreground color
		Color fg = fromServer.readColor();
		// receive the font
		Font ft = fromServer.readFont();
		// receive the inactive foreground color
		Color ifg = fromServer.readColor();
		// receive the inactive shadow color
		Color is = fromServer.readColor();
		release();
		comp.add("Label.background"); comp.add(bg);
		comp.add("Label.foreground"); comp.add(fg);
		comp.add("Label.font"); comp.add(ft);
		comp.add("Label.disabledForeground"); comp.add(ifg);
		comp.add("Label.disabledShadow"); comp.add(is);
	}
	
	public void getComboBoxDefaults(LinkedList comp) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_COMBOBOXDEFAULTS);
		// sends the message
		toServer.send();
		
		fromServer.receive();
		// receive the background color
		Color bg = fromServer.readColor();
		// receive the foreground color
		Color fg = fromServer.readColor();
		// receive the font
		Font ft = fromServer.readFont();
		// receive the selection background color
		Color sbg = fromServer.readColor();
		// receive the selection foreground color
		Color sfg = fromServer.readColor();
		// receive the disabled background color
		Color dbg = fromServer.readColor();
		// receive the disabled background color
		Color dfg = fromServer.readColor();
		release();
		comp.add("ComboBox.background"); comp.add(bg);
		comp.add("ComboBox.foreground"); comp.add(fg);
		comp.add("ComboBox.font"); comp.add(ft);
		comp.add("ComboBox.selectionBackground"); comp.add(sbg);
		comp.add("ComboBox.selectionForeground"); comp.add(sfg);
		comp.add("ComboBox.disabledBackground"); comp.add(dbg);
		comp.add("ComboBox.disabledForeground"); comp.add(dfg);
		comp.add("ComboBox.buttonBackground"); comp.add(null);
		comp.add("ComboBox.buttonShadow"); comp.add(null);
		comp.add("ComboBox.buttonDarkShadow"); comp.add(null);
		comp.add("ComboBox.buttonHighlight"); comp.add(null);
	}
	
	public void getInternalFrameDefaults(LinkedList comp) throws IOException {
		comp.add("InternalFrame.maximizeIcon"); comp.add(new ImageIcon(getTitleButtonImage(KDEConstants.JSC_TitleBarMaxButton, false, false)));
		comp.add("InternalFrame.minimizeIcon"); comp.add(new ImageIcon(getTitleButtonImage(KDEConstants.JSC_TitleBarShadeButton, false, false)));
		comp.add("InternalFrame.closeIcon"); comp.add(new ImageIcon(getTitleButtonImage(KDEConstants.JSC_TitleBarCloseButton, false, false)));
		comp.add("InternalFrame.iconifyIcon"); comp.add(new ImageIcon(getTitleButtonImage(KDEConstants.JSC_TitleBarShadeButton, false, false)));
		/*
		OutBuffer toServer = new OutBuffer(socket);
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_INTERNALFRAMEDEFAULTS);
		// sends the message
		toServer.send();
		
		InBuffer in = new InBuffer(socket);
		// receive the background color
		Color bg = fromServer.readColor();
		// receive the foreground color
		Color fg = fromServer.readColor();
		// receive the font
		Font ft = fromServer.readFont();
		
		// receive the selection background color
		Color l = fromServer.readColor();
		// receive the selection foreground color
		Color hl = fromServer.readColor();
		// receive the disabled background color
		Color dbg = fromServer.readColor();
		// receive the disabled background color
		Color dfg = fromServer.readColor();
		comp.add("InternalFrame.background"); comp.add(bg);
		comp.add("InternalFrame.foreground"); comp.add(fg);
		comp.add("InternalFrame.font"); comp.add(ft);
		comp.add("InternalFrame.light"); comp.add(l);
		comp.add("InternalFrame.highlight"); comp.add(hl);
		//comp.add("ComboBox.border"); comp.add(null);
		//comp.add("ComboBox.margin"); comp.add(null);
		comp.add("InternalFrame.textIconGap"); comp.add(new Integer(4));
		comp.add("InternalFrame.darkShadow"); comp.add(null);
    title.setFont(UIManager.getFont("InternalFrame.titleFont"));
    selectedTextColor = UIManager.getColor("InternalFrame.activeTitleForeground");
    selectedTitleColor = UIManager.getColor("InternalFrame.activeTitleBackground");
    notSelectedTextColor = UIManager.getColor("InternalFrame.inactiveTitleForeground");
    notSelectedTitleColor = UIManager.getColor("InternalFrame.inactiveTitleBackground");*/
	}
	
	
	
	
	/**
	 * Returns the default color for the system.
	 * @return The default color for the system.
	 */
	public Object[] initSystemColorDefaults() throws IOException {
		LinkedList colors = new LinkedList();
		colors.add("desktop"); colors.add(getColorByKey(KDEConstants.CR_DESKTOP));
		colors.add("activeCaption"); colors.add(getColorByKey(KDEConstants.CR_ACTIVECAPTION));
		colors.add("activeCaptionText"); colors.add(getColorByKey(KDEConstants.CR_ACTIVECAPTIONTEXT));
		colors.add("activeCaptionBorder"); colors.add(getColorByKey(KDEConstants.CR_ACTIVECAPTIONBORDER));
		colors.add("inactiveCaption"); colors.add(getColorByKey(KDEConstants.CR_INACTIVECAPTION));
		colors.add("inactiveCaptionText"); colors.add(getColorByKey(KDEConstants.CR_INACTIVECAPTIONTEXT));
		colors.add("inactiveCaptionBorder"); colors.add(getColorByKey(KDEConstants.CR_INACTIVECAPTIONBORDER));
		colors.add("window"); colors.add(getColorByKey(KDEConstants.CR_WINDOW));
		colors.add("windowBorder"); colors.add(getColorByKey(KDEConstants.CR_WINDOWBORDER));
		colors.add("windowText"); colors.add(getColorByKey(KDEConstants.CR_WINDOWTEXT));
		colors.add("menu"); colors.add(getColorByKey(KDEConstants.CR_MENU));
		colors.add("menuText"); colors.add(getColorByKey(KDEConstants.CR_MENUTEXT));
		colors.add("text"); colors.add(getColorByKey(KDEConstants.CR_TEXT));
		colors.add("textText"); colors.add(getColorByKey(KDEConstants.CR_TEXTTEXT));
		colors.add("textHighlight"); colors.add(getColorByKey(KDEConstants.CR_TEXTHIGHLIGHT));
		colors.add("textHighlightText"); colors.add(getColorByKey(KDEConstants.CR_TEXTHIGHLIGHTTEXT));
		colors.add("textInactiveText"); colors.add(getColorByKey(KDEConstants.CR_TEXTINACTIVETEXT));
		colors.add("control"); colors.add(getColorByKey(KDEConstants.CR_CONTROL));
		colors.add("controlText"); colors.add(getColorByKey(KDEConstants.CR_CONTROLTEXT));
		colors.add("controlHighlight"); colors.add(getColorByKey(KDEConstants.CR_CONTROLHIGHLIGHT));
		colors.add("controlLtHighlight"); colors.add(getColorByKey(KDEConstants.CR_CONTROLLTHIGHLIGHT));
		colors.add("controlShadow"); colors.add(getColorByKey(KDEConstants.CR_CONTROLSHADOW));
		colors.add("controlDkShadow"); colors.add(getColorByKey(KDEConstants.CR_CONTROLDKSHADOW));
		colors.add("scrollbar"); colors.add(getColorByKey(KDEConstants.CR_SCROLLBAR));
		colors.add("info"); colors.add(getColorByKey(KDEConstants.CR_INFO));
		colors.add("infoText"); colors.add(getColorByKey(KDEConstants.CR_INFOTEXT));
		return colors.toArray();
	}
	
	/**
	 * Returns the color associed to the given key.
	 * @param key The color key.
	 * @return the color associed to the given key.
	 */
	protected Color getColorByKey(int key) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_COLOR_BY_KEY);
		// write the key
		toServer.writeInt(key);
		// sends the message
		toServer.send();
		// receive the color
		fromServer.receive();
		Color col = fromServer.readColor();
		release();
		return col; 
	}
	
	/**
	 * Returns the pixel metric associed to the given key.
	 * @param key The pixel metric key.
	 * @return the pixel metric associed to the given key.
	 */
	public int getPixelMetric(int key) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_PIXEL_METRIC);
		// write the key
		toServer.writeInt(key);
		// sends the message
		toServer.send();
		// receive the pixel metric
		fromServer.receive();
		int val = fromServer.readInt();
		release();
		return val;
	}
	
	public Dimension getCheckBoxDimension() throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_EXCLUSIVE_INDICATOR_DIM);
		// sends the message
		toServer.send();
		// receive the pixel metric
		fromServer.receive();
		Dimension val = fromServer.readDimension();
		release();
		return val;
	}
	
	public Dimension getRadioButtonDimension() throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_INDICATOR_DIM);
		// sends the message
		toServer.send();
		// receive the pixel metric
		fromServer.receive();
		Dimension val = fromServer.readDimension();
		release();
		return val;
	}
	
	/**
	 * Returns an array of kde icon directories.
	 * @return An array of kde icon directories.
	 */
	public String[] getIconPaths() throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_ICONPATHS);
		// sends the message
		toServer.send();
		// receive the kde icon directories
		fromServer.receive();
		String[] val = fromServer.readStrings();
		release();
		return val;
	}
	
	/**
	 * Returns an array of kde locale directories.
	 * @return An array of kde locale directories.
	 */
	public String[] getLocalePaths() throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_LOCALEPATHS);
		// sends the message
		toServer.send();
		// receive the kde locale directories
		fromServer.receive();
		String[] val = fromServer.readStrings();
		release();
		return val;
	}
	
	/**
	 * Returns an array of kde mime directories.
	 * @return An array of kde mime directories.
	 */
	public String[] getMimePaths() throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_MIMEPATHS);
		// sends the message
		toServer.send();
		// receive the kde mime directories
		fromServer.receive();
		String[] val = fromServer.readStrings();
		release();
		return val;
	}
	
	/**
	 * Returns an array of kde config directories.
	 * @return An array of kde config directories.
	 */
	public String[] getConfigPaths() throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_CONFIGPATHS);
		// sends the message
		toServer.send();
		// receive the kde config directories
		fromServer.receive();
		String[] val = fromServer.readStrings();
		release();
		return val;
	}
		
	public Dimension getSimplePreferredSize(int key) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_SIMPLEPREFERREDSIZE);
		// write the key
		toServer.writeInt(key);
		// sends the message
		toServer.send();
		// receive the dimension
		fromServer.receive();
		int width = fromServer.readInt();
		int height = fromServer.readInt();
		release();
		return new Dimension(width, height); 
	}
	
	/**
	* Returns the size of the display area for the combo box. This size will be 
	* the size of the combo box, not including the arrowButton.
	* @param editable Is the combobox editable.
	* @param text The text for the current value.
	* @return The size of the display area for the combo box.
	*/
	public Dimension getComboDisplaySize(boolean editable, String text) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_COMBOBOX_DISPLAY_SIZE);
		// write the combobox infos
		toServer.writeBool(editable); toServer.writeChars(text);
		// sends the message
		toServer.send();
		
		// receive the display size
		fromServer.receive();
		Dimension val = fromServer.readDimension(); 
		release();
		return val;
	}
	
	/**
	* Returns the bounds in which comboBox's selected item will be
	* displayed.
	* @param width The width of the combobox.
	* @param height The height of the combobox.
	* @param editable Is the combobox editable.
	* @param text The text for the current value.
	* @return rectangle bounds in which comboBox's selected Item will be displayed.
	*/
	public Rectangle getComboRectangleForCurrentValue(int width, int height, boolean editable, String text) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_COMBOBOX_VALUE_RECT);
		// write the combobox infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(editable); toServer.writeChars(text);
		// sends the message
		toServer.send();
		
		// receive the rectangle for current value
		fromServer.receive();
		Rectangle val = fromServer.readRectangle();
		release();
		return val;
	}
	
	public Rectangle[] getScrollBarMetrics(int width, int height, boolean horizontal, int min, int max, int val, int pageStep) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_SCROLLBAR_METRIS);
		// writes the ScrollBar infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(horizontal);
		toServer.writeInt(min); toServer.writeInt(max); toServer.writeInt(val); toServer.writeInt(pageStep);
		// sends the message
		toServer.send();
		
		Rectangle[] results = new Rectangle[8];
		// receive the rectangle for the ScrollBar
		fromServer.receive();
		results[0] = fromServer.readRectangle(); 
		results[1] = fromServer.readRectangle(); 
		results[2] = fromServer.readRectangle(); 
		results[3] = fromServer.readRectangle(); 
		results[4] = fromServer.readRectangle(); 
		results[5] = fromServer.readRectangle(); 
		results[6] = fromServer.readRectangle(); 
		results[7] = fromServer.readRectangle(); 
		release();
		return results; 
	}
	
	/**
	 * Gets the base default for a given widget type.
	 * @param widgetType The widget type.
	 * @return The base default for a given widget type.
	 */
	protected BaseDefaults getBaseDefaults(int widgetType) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_BASE_DEFAULTS);
		// write the widget type
		toServer.writeInt(widgetType);
		// sends the message
		toServer.send();
		
		fromServer.receive();
		// receive the background color
		Color bg = fromServer.readColor();
		// receive the foreground color
		Color fg = fromServer.readColor();
		// receive the font
		Font ft = fromServer.readFont();
		release();
		
		return new BaseDefaults(bg, fg, ft);
	}
	
	public BufferedImage getButtonImage(int width, int height, boolean enabled, boolean pressed, boolean rollover, boolean focus, boolean contentAreaFilled, boolean borderPainted) throws IOException {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_BUTTON_IMAGE);
		// writes the button infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(enabled); toServer.writeBool(pressed); toServer.writeBool(rollover); toServer.writeBool(focus); toServer.writeBool(contentAreaFilled); toServer.writeBool(borderPainted);
		return receiveImage();
	}
	
	public BufferedImage getToolbarButtonImage(int width, int height, int tbHorW, boolean enabled, boolean pressed, boolean rollover, boolean focus, boolean contentAreaFilled, boolean borderPainted, boolean horizontal) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_TOGGLEBUTTON_IMAGE);
		// writes the ToggleButton infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeInt(tbHorW); toServer.writeBool(enabled); toServer.writeBool(pressed); toServer.writeBool(rollover); toServer.writeBool(focus); toServer.writeBool(contentAreaFilled); toServer.writeBool(borderPainted); toServer.writeBool(horizontal);
		return receiveImage();
	}
	
	public Dimension getButtonPreferredSize(String text, int iconWidth, int iconHeight) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_BUTTON_PREF_SZ);
		// writes the button infos
		toServer.writeChars(text);
		toServer.writeInt(iconWidth);
		toServer.writeInt(iconHeight);
		// sends the message
		toServer.send();
		// receive the dimension
		fromServer.receive();
		int width = fromServer.readInt();
		int height = fromServer.readInt();
		release();
		return new Dimension(width, height); 
	}
	public Dimension getToolbarButtonPreferredSize(String text, int iconWidth, int iconHeight) throws IOException {
		acquire();
		// write the operation code
		toServer.writeInt(KDEConstants.OP_GET_TOOLBUTTON_PREF_SZ);
		// writes the button infos
		toServer.writeChars(text);
		toServer.writeInt(iconWidth);
		toServer.writeInt(iconHeight);
		// sends the message
		toServer.send();
		// receive the dimension
		fromServer.receive();
		int width = fromServer.readInt();
		int height = fromServer.readInt();
		release();
		return new Dimension(width, height); 
	}
		
	public BufferedImage getTabImage(int tabPlacement, int tabIndex, int w, int h, boolean isEnabled, boolean isSelected) throws IOException {
		int w2, h2;
		if ( (tabPlacement == JTabbedPane.LEFT) || (tabPlacement == JTabbedPane.LEFT) ) {
			w2 = h;
			h2 = w;
		}
		else {
			w2 = w;
			h2 = h;
		}
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_TAB_IMAGE);
		// writes the tab infos
		toServer.writeInt(w2); toServer.writeInt(h2); toServer.writeInt(tabIndex); toServer.writeBool(isEnabled);  toServer.writeBool(isSelected);
		BufferedImage img = receiveImage();
		switch (tabPlacement) {
			case JTabbedPane.TOP :
				break;
			case JTabbedPane.BOTTOM :
				img = org.freeasinspeech.kdelaf.ui.ImageUtilities.rotate(img, org.freeasinspeech.kdelaf.ui.ImageUtilities.ANGLE_180);
				break;
			case JTabbedPane.LEFT :
				img = org.freeasinspeech.kdelaf.ui.ImageUtilities.rotate(img, org.freeasinspeech.kdelaf.ui.ImageUtilities.ANGLE_270);
				break;
			default : //JTabbedPane.RIGHT
				img = org.freeasinspeech.kdelaf.ui.ImageUtilities.rotate(img, org.freeasinspeech.kdelaf.ui.ImageUtilities.ANGLE_90);
		}
		return img;
	}
	
	public BufferedImage getTableHeaderImage(int width, int height, boolean isPressed, boolean isSelected, boolean hasFocus) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_TABLEHEADER_IMAGE);
		// writes the MenuItem infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(isPressed); toServer.writeBool(isSelected); toServer.writeBool(hasFocus);
		return receiveImage();
	}
	
	public BufferedImage getMenuItemImage(JMenuItem menuItem) throws IOException  {
		int width = menuItem.getWidth();
		int height = menuItem.getHeight();
		boolean enabled = menuItem.isEnabled();
		boolean rollover = (menuItem.isSelected() || menuItem.getModel().isRollover());
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_MENUITEM_IMAGE);
		// writes the MenuItem infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(enabled); toServer.writeBool(rollover);
		return receiveImage();
	}
	
	public BufferedImage getCheckBoxMenuItemImage(JMenuItem menuItem, boolean rollover) throws IOException  {
		BufferedImage img = null;
		if (menuItem instanceof JCheckBoxMenuItem) {
			int width = menuItem.getWidth();
			int height = menuItem.getHeight();
			boolean enabled = false;
			boolean checked = ((JCheckBoxMenuItem)menuItem).isSelected();
			ButtonModel model = menuItem.getModel();
			enabled = menuItem.isEnabled();
			acquire();
			// writes the operation code
			toServer.writeInt(KDEConstants.OP_GET_CHECKBOXMENUITEM_IMAGE);
			// writes the MenuItem infos
			toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(enabled); toServer.writeBool(rollover); toServer.writeBool(checked);
			img = receiveImage();
		}
		else
			img = getMenuItemImage(menuItem);
		return img;
	}
	
	public BufferedImage getRadioButtonMenuItemImage(JMenuItem menuItem, boolean rollover) throws IOException  {
		BufferedImage img = null;
		if (menuItem instanceof JRadioButtonMenuItem) {
			int width = menuItem.getWidth();
			int height = menuItem.getHeight();
			boolean enabled = false;
			boolean checked = ((JRadioButtonMenuItem)menuItem).isSelected();
			ButtonModel model = menuItem.getModel();
			enabled = menuItem.isEnabled();
			acquire();
			// writes the operation code
			toServer.writeInt(KDEConstants.OP_GET_RADIOBUTTONMENUITEM_IMAGE);
			// writes the MenuItem infos
			toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(enabled); toServer.writeBool(rollover); toServer.writeBool(checked);
			img = receiveImage();
		}
		else
			img = getMenuItemImage(menuItem);
		return img;
	}
	
	public BufferedImage getMenuImage(int width, int height, boolean topLevel, boolean enabled, boolean selected) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_MENU_IMAGE);
		// writes the Menu infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(topLevel); toServer.writeBool(enabled); toServer.writeBool(selected);
		return receiveImage();
	}
	
	public BufferedImage getMenuBarImage(int width, int height, boolean horizontal) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_MENUBAR_IMAGE);
		// writes the MenuBar infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(horizontal);
		return receiveImage();
	}
	
	public BufferedImage getToolBarSeparatorImage(int width, int height, boolean horizontal) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_TOOLBARSEPARATOR_IMAGE);
		// writes the ToolBarSeparator infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(horizontal);
		return receiveImage();
	}
	
	public BufferedImage getToolbarHandleImage(int width, int height, boolean horizontal) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_TOOLBARHANDLE_IMAGE);
		// writes the ToolBarSeparator infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(horizontal);
		return receiveImage();
	}
	
	public BufferedImage getToolbarBackgroundImage(int width, int height, boolean horizontal) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_TOOLBARBACKGROUND_IMAGE);
		// writes the ToolBarSeparator infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(horizontal);
		return receiveImage();
	}
	
	public BufferedImage getComboBoxImage(int width, int height, boolean editable, String text, boolean enabled, boolean hasFocus, boolean mouseOver) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_COMBOBOX_IMAGE);
		// writes the ComboBox infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(editable); toServer.writeChars(text); toServer.writeBool(enabled); toServer.writeBool(hasFocus); toServer.writeBool(mouseOver);
		return receiveImage();
	}
	
	public BufferedImage getRadioButtonImage(boolean enabled, boolean selected, boolean rollover) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_RADIOBUTTON_IMAGE);
		// writes the RadioButton infos
		toServer.writeBool(enabled); toServer.writeBool(selected); toServer.writeBool(rollover);
		return receiveImage();
	}
	
	public BufferedImage getCheckBoxImage(boolean enabled, boolean selected, boolean rollover) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_CHECKBOX_IMAGE);
		// writes the CheckBox infos
		toServer.writeBool(enabled); toServer.writeBool(selected); toServer.writeBool(rollover);
		return receiveImage();
	}
	
	public BufferedImage getMenuButtonIndicatorImage(int width, int height) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_MENUBUTTONINDICATOR_IMAGE);
		// writes the RadioButton infos
		toServer.writeInt(width); toServer.writeInt(height);
		return receiveImage();
	}
	
	public BufferedImage getScrollBarThumbImage(int width, int height, boolean horizontal, boolean highlighted) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_SCROLLBARTHUMB_IMAGE);
		// writes the ScrollBar Thumb infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(horizontal); toServer.writeBool(highlighted);
		return receiveImage();
	}
	
	public BufferedImage getScrollBarTrackImage(int width, int height, boolean horizontal) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_SCROLLBARTRACK_IMAGE);
		// writes the ScrollBar Thumb infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(horizontal);
		return receiveImage();
	}
	
	public BufferedImage getScrollBarNextImage(boolean horizontal, int index) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_SCROLLBARNEXT_IMAGE);
		// writes the ScrollBar button infos
		toServer.writeBool(horizontal); toServer.writeInt(index);
		return receiveImage();
	}
	
	public BufferedImage getScrollBarPrevImage(boolean horizontal, int index) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_SCROLLBARPREV_IMAGE);
		// writes the ScrollBar button infos
		toServer.writeBool(horizontal); toServer.writeInt(index); toServer.writeBool(true); 
		return receiveImage();
	}
	
	public BufferedImage getScrollBarImage(
			int width, int height, boolean horizontal, boolean enabled, int min, int max, int val, int pageStep,
			boolean subPressed, boolean addPressed, boolean sliderPressed
	) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_SCROLLBAR_IMAGE);
		// writes the ScrollBar infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(horizontal); toServer.writeBool(enabled);
		toServer.writeInt(min); toServer.writeInt(max); toServer.writeInt(val); toServer.writeInt(pageStep);
		toServer.writeBool(subPressed); toServer.writeBool(addPressed); toServer.writeBool(sliderPressed);
		return receiveImage();
	}
	
	public BufferedImage getSpinnerImage(int index, boolean isNext) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_SPINNER_IMAGE);
		// writes the Spinner infos
		toServer.writeInt(index); toServer.writeBool(isNext); 
		return receiveImage();
	}
	
	public BufferedImage getTitleButtonImage(int JSC, boolean clicked, boolean rollover) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_TITLE_BUTTON);
		// writes the TitleButton infos
		toServer.writeInt(JSC); toServer.writeBool(clicked);  toServer.writeBool(rollover); 
		return receiveImage();
	}
	
	public BufferedImage getProgressBarImage(int width, int height, boolean isEnabled, boolean horizonal, boolean hasFocus, int value, int length) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_PROGRESSBAR_IMAGE);
		// writes the ProgressBar infos
		toServer.writeInt(width); toServer.writeInt(height); toServer.writeBool(isEnabled); toServer.writeBool(horizonal); toServer.writeBool(hasFocus); toServer.writeInt(value); toServer.writeInt(length);
		return receiveImage();
	}
	
	public BufferedImage getSliderHandleImage(boolean isEnabled, boolean isHorizonal, boolean isSelected, boolean hasFocus) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_SLIDERHANDLE_IMAGE);
		// writes the ProgressBar infos
		toServer.writeBool(isEnabled); toServer.writeBool(isHorizonal); toServer.writeBool(isSelected); toServer.writeBool(hasFocus);
		return receiveImage();
	}
	
	public BufferedImage getSliderGrooveImage(boolean isEnabled, boolean isHorizonal, boolean hasFocus) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_SLIDERGROOVE_IMAGE);
		// writes the ProgressBar infos
		toServer.writeBool(isEnabled); toServer.writeBool(isHorizonal); toServer.writeBool(hasFocus);
		return receiveImage();
	}
	
	public int getThumbTrackOffset(boolean isHorizonal) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_THUMBTRACKOFFSET);
		// writes the ProgressBar infos
		toServer.writeBool(isHorizonal);
		// sends the message
		toServer.send();
		
		// receive the offset
		fromServer.receive();
		int val = fromServer.readInt();
		release();
		return val;
	}
		
	public BufferedImage getCommonImage(int _key) throws IOException  {
		acquire();
		// writes the operation code
		toServer.writeInt(KDEConstants.OP_GET_COMMON_IMAGE);
		// writes the common image info
		toServer.writeInt(_key);
		return receiveImage();
	}
	
	protected BufferedImage receiveImage() throws IOException {
		BufferedImage result = null;
		// sends the message
		toServer.send();
		
		// receive the image
		fromServer.receive();
		result = fromServer.readImage();
		if (fromServer.getLastImageSize() > 3500) {
			imgsTime += fromServer.getLastImageTime() ;
			imgsBytes += (double)fromServer.getLastImageSize();
		}
		release();
		return result;
	}
	
	protected BufferedImage rotate(BufferedImage img, int angle) {
		AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle), img.getWidth() / 2, img.getHeight() / 2);
		AffineTransformOp atOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return atOp.filter(img, atOp.createCompatibleDestImage(img, null) );
	}
	
	/**
	 * Class BaseDefaults
	 * Represents the base component defaults
	 * 
	 * @author Sekou DIAKITE (sekou.diakite@freeasinspeech.org)
	 */
	class BaseDefaults
	{
		public Color background;
		public Color foreground;
		public Font font;
		BaseDefaults(Color background, Color foreground, Font font) {
			this.background = background;
			this.foreground = foreground;
			this.font = font;
		}
		public String toString() {
			return "bg=" + background.toString() + ", fg=" + foreground.toString() + " and font=" + font.toString();
		}
	}
	
}


class Worker extends Thread
{
	protected Process worker;
	protected OutBuffer toServer;
	protected InBuffer fromServer;
	protected WorkerErrorListener listener;
	protected ServerListener echoListener;
	protected KDEClient readyListener;
		
	protected boolean runing;
	protected boolean ready;
	
	public Worker(WorkerErrorListener listener, KDEClient readyListener) {
		worker = null;
		toServer = null;
		fromServer = null;
		runing = false;
		ready = false;
		this.listener = listener;
		this.readyListener = readyListener;
	}
	
	public void run() {
		setRuning(true);
		setReady(false);
		try {
			worker = Runtime.getRuntime().exec("kdelafworker");
		}
		catch (SecurityException se) {
			worker = null;
			listener.onWorkerError(se);
		}
		catch (IOException ioe) {
			worker = null;
			listener.onWorkerError(ioe);
		}
		catch (NullPointerException npe) {
			worker = null;
			listener.onWorkerError(npe);
		}
		catch (IllegalArgumentException iae) {
			worker = null;
			listener.onWorkerError(iae);
		}
		if (worker != null) {
			try {
				(echoListener = new ServerListener(worker)).start();
				toServer = new OutBuffer(worker);
				fromServer = new InBuffer(worker);
				setReady(true);
				readyListener.workerReady();
				try {
					int returnCode = worker.waitFor();
					/*if (returnCode != 0) {
						if (listener != null)
							listener.onWorkerError(new Exception("The worker has crashed"));
					}*/
				}
				catch (InterruptedException e) {
					if (listener != null)
						listener.onWorkerError(e);
				}
			}
			catch (IOException ioe2) {
				if (listener != null)
					listener.onWorkerError(ioe2);
			}
		}
		else
			readyListener.workerReady();
		listener = null;
		worker = null;
		toServer = null;
		fromServer = null;
		if (echoListener != null)
			echoListener.kill();
		echoListener = null;
		setReady(false);
		setRuning(false);
	}
	
	public void kill() {
		if (echoListener != null)
			echoListener.kill();
		if (worker != null)
			worker.destroy();
		listener = null;
		worker = null;
		toServer = null;
		fromServer = null;
		echoListener = null;
		setReady(false);
		setRuning(false);
	}
	
	public synchronized boolean waitReady() {
		boolean ok = false;
		if (isRuning()) {
			while (ready == false) {
				try {
					Thread.sleep(20);
				}
				catch (Exception e) {}			
			}
			ok = true;
		}
		return ok;
	}
	
	public synchronized boolean isRuning() {
		return runing;
	}
	
	protected synchronized void setRuning(boolean runing) {
		this.runing = runing;
	}
	
	protected synchronized void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public OutBuffer getOut() {
		return toServer;
	}
	
	public InBuffer getIn() {
		return fromServer;
	}
}
