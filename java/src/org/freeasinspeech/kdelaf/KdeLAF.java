package org.freeasinspeech.kdelaf;

import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import org.freeasinspeech.kdelaf.ui.*;
import org.freeasinspeech.kdelaf.client.*;
import java.awt.event.WindowEvent;
/**
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KdeLAF extends BasicLookAndFeel implements java.io.Serializable, java.awt.event.WindowListener, WorkerErrorListener
{
	protected static KDEClient client;
	protected static Container rootContainer = null;
	protected static KdeLAF instance = null;
	public KdeLAF() {		
		super();
		instance = this;
	}
	public void initialize() {
		Logger.log("KdeLAF::initialize()");
		Logger.log("Java version is : \"" + System.getProperty("java.version") + "\"");
		Logger.log("Java vendor is : \"" + System.getProperty("java.vendor") + "\"");
		Logger.log("OS architecture is : \"" + System.getProperty("os.arch") + "\"");
		Logger.log("OS name is : \"" + System.getProperty("os.name") + "\"");
		try {
			client = new KDEClient();
			if (!client.connect(this))
				_onError(null);
			Logger.log("KDE version is : \"" + client.getKdeVersion() + "\"");
			Logger.log("The current style is : \"" + client.getStyleName() + "\"");
		}
		catch (Exception e) {
			_onError(e);
		}
	}
	
	public static KDEClient getClient() {
		return client;
	}
	
	public void onWorkerError(Exception e)  {
		_onError(e);
	}
	
	public void _onError(Exception exception)  {
		if (exception != null)
			Logger.criticalError(exception);
		else
			Logger.criticalError("Unknown error");
		quitError();
	}
	public static void onError() {
		if (instance != null)
			instance._onError(null);
	}
	
	public static void onError(Exception exception) {
		if (instance != null)
			instance._onError(exception);
	}
	protected synchronized void quit() {
		if (client != null) {
			try {
				client.disconnect();
			}
			catch (Exception e) {
			}
			client = null;
		}
		instance = null;
	}
	
	protected void quitOk() {
		Logger.log("KdeLAF::quitOk()");
		quit();
	}
	
	protected void quitError() {
		Logger.log("KdeLAF::quitError()");
		quit();
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			if (rootContainer != null)
				SwingUtilities.updateComponentTreeUI(rootContainer);
		}
		catch (Exception e) {
		}
		System.out.println("KdeLAF has crashed see \""+Logger.getFilePath()+"\" for more information.");
	}
	
	public void uninitialize() {
		Logger.log("KdeLAF::uninitialize()");
		quitOk();
	}
	
	public void windowClosing(WindowEvent e) {
		uninitialize();
	}
	
	public static void declareRoot(JComponent c) {
		if (rootContainer == null) {
			//Logger.log("KdeLAF::declareRoot()");
			rootContainer = c.getTopLevelAncestor();
			if (rootContainer != null) {
				if (rootContainer instanceof Window)
					declareRootWindow((Window)rootContainer);
				/*else {
					Window rootWindow = null;
					Component comp  = c.getParent();
					while ( (comp != null) && (rootWindow == null) ) {
						comp = c.getParent();
						if (comp instanceof Window)
							rootWindow = (Window)comp;
					}
					if (rootWindow != null)
						declareRootWindow(rootWindow);
				}*/
			}
		}
	}
  
	protected static void declareRootWindow(Window w) {
		Logger.log("KdeLAF::declareRootWindow()");
		w.addWindowListener(instance);
	}
	
	protected void initSystemColorDefaults(UIDefaults table) {
		Logger.log("KdeLAF::initSystemColorDefaults()");
		try {
			if (client != null) {
				Date start = new Date();
				table.putDefaults(client.initSystemColorDefaults());
				Logger.log("KdeLAF::initSystemColorDefaults() in " + ((new Date()).getTime() - start.getTime()) + "Ms");
			}
			else
				super.initSystemColorDefaults(table);
		}
		catch (Exception e) {
			super.initSystemColorDefaults(table);
			_onError(e);
		}
	}
	
	protected void initComponentDefaults(UIDefaults table) {
		Logger.log("KdeLAF::initComponentDefaults()");
		super.initComponentDefaults(table);
		if (client != null) {
			try {
				Date start = new Date();
				table.putDefaults(client.initComponentDefaults());
				Logger.log("KdeLAF::initComponentDefaults() in " + ((new Date()).getTime() - start.getTime()) + "Ms");
			}
			catch (Exception e) {
				_onError(e);
			}
		}
	}
	
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);
		// Initialize Class Defaults
		Object[] uiDefaultsCommon = new Object[] {
			"ButtonUI"  ,  "org.freeasinspeech.kdelaf.ui.KButtonUI",
			"CheckBoxMenuItemUI"  ,  "org.freeasinspeech.kdelaf.ui.QCheckBoxMenuItemUI",
			"CheckBoxUI"  ,  "org.freeasinspeech.kdelaf.ui.QCheckBoxUI",
			"ColorChooserUI"  ,  "org.freeasinspeech.kdelaf.ui.KColorChooserUI",
			"ComboBoxUI"  ,  "org.freeasinspeech.kdelaf.ui.QComboBoxUI",
			"DesktopIconUI"  ,  "org.freeasinspeech.kdelaf.ui.QDesktopIconUI",
			"DesktopPaneUI"  ,  "org.freeasinspeech.kdelaf.ui.QDesktopPaneUI",
			"EditorPaneUI"  ,  "org.freeasinspeech.kdelaf.ui.QEditorPaneUI",
			"FileChooserUI"  ,  "org.freeasinspeech.kdelaf.ui.KDEFileChooserUI",
			"FormattedTextFieldUI"  ,  "org.freeasinspeech.kdelaf.ui.QFormattedTextFieldUI",
			"InternalFrameUI"  ,  "org.freeasinspeech.kdelaf.ui.QInternalFrameUI",
			"LabelUI"  ,  "org.freeasinspeech.kdelaf.ui.QLabelUI",
			"ListUI"  ,  "org.freeasinspeech.kdelaf.ui.QListUI",
			"MenuBarUI"  ,  "org.freeasinspeech.kdelaf.ui.QMenuBarUI",
			"MenuItemUI"  ,  "org.freeasinspeech.kdelaf.ui.QMenuItemUI",
			"MenuUI"  ,  "org.freeasinspeech.kdelaf.ui.QMenuUI",
			"OptionPaneUI"  ,  "org.freeasinspeech.kdelaf.ui.KOptionPaneUI",
			"PanelUI"  ,  "org.freeasinspeech.kdelaf.ui.QPanelUI",
			"RadioButtonMenuItemUI"  ,  "org.freeasinspeech.kdelaf.ui.QRadioButtonMenuItemUI",
			"RadioButtonUI"  ,  "org.freeasinspeech.kdelaf.ui.QRadioButtonUI",
			"RootPaneUI"  ,  "org.freeasinspeech.kdelaf.ui.QRootPaneUI",
			"ScrollPaneUI"  ,  "org.freeasinspeech.kdelaf.ui.QScrollPaneUI",
			"ScrollBarUI"  ,  "org.freeasinspeech.kdelaf.ui.KScrollBarUI",
			"SliderUI"  ,  "org.freeasinspeech.kdelaf.ui.QSliderUI",
			"SpinnerUI"  ,  "org.freeasinspeech.kdelaf.ui.QSpinnerUI",
			"TabbedPaneUI"  ,  "org.freeasinspeech.kdelaf.ui.QTabbedPaneUI",
			"TableUI"  ,  "org.freeasinspeech.kdelaf.ui.QTableUI",
			"TableHeaderUI"  ,  "org.freeasinspeech.kdelaf.ui.QTableHeaderUI",
			"TextAreaUI"  ,  "org.freeasinspeech.kdelaf.ui.QTextAreaUI",
			"TextFieldUI"  ,  "org.freeasinspeech.kdelaf.ui.QTextFieldUI",
			"TextPaneUI"  ,  "org.freeasinspeech.kdelaf.ui.QTextPaneUI",
			"ToolTipUI"  ,  "org.freeasinspeech.kdelaf.ui.QToolTipUI",
			"ToggleButtonUI"  ,  "org.freeasinspeech.kdelaf.ui.KToggleButtonUI",
			"ToolBarSeparatorUI"  ,  "org.freeasinspeech.kdelaf.ui.QToolBarSeparatorUI",
			"ToolBarUI"  ,  "org.freeasinspeech.kdelaf.ui.QToolBarUI",
			"ViewportUI"  ,  "org.freeasinspeech.kdelaf.ui.QViewportUI"
		};
		table.putDefaults(uiDefaultsCommon);
		
		if (Utilities.isClasspath()) {
			Logger.log("KdeLAF::gnu-classpath " + Utilities.getClasspathVersion() + " detected.");
			
			Object[] uiDefaultClasspath_90 = new Object[] {
				"ProgressBarUI"  ,  "org.freeasinspeech.kdelaf.ui.QProgressBarUI",
			};
			table.putDefaults(uiDefaultClasspath_90);
		}
		else {
			Object[] uiDefaultJava1_4 = new Object[] {
				"TreeUI"  ,  "org.freeasinspeech.kdelaf.ui.KTreeUI",
				//"ProgressBarUI"  ,  "org.freeasinspeech.kdelaf.ui.QProgressBarUI",
			};
			table.putDefaults(uiDefaultJava1_4);
		}
		
		Logger.log("KdeLAF::initClassDefaults()");
	}
	
	public boolean isSupportedLookAndFeel() {
		boolean supported = true;
		Logger.log("KdeLAF::isSupportedLookAndFeel() => " + supported);
		return supported;
	}
	
	public boolean isNativeLookAndFeel() {
		boolean isNative = true;
		Logger.log("KdeLAF::isNativeLookAndFeel() => " + true);
		return QtProbe.isKDERunning();
	}
	
	public String getDescription() {
		return "KDE Look & Feel (sekou.diakite@fais.org)";
	}
	
	public String getID() {
		return "KDELookAndFeel";
	}
	
	public String getName() {
		return "KdeLAF";
	}
	
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	

	public static Icon getIcon(String location, String name) {
		URL url = KdeLAF.class.getResource(location);
		if (url == null) System.err.println("WARNING " + location + " not found.");
		return new ImageIcon(url, name);
	}
}
