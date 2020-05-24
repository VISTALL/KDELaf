import java.awt.*;
import java.awt.image.*;
import javax.swing.plaf.*;
import javax.swing.border.*;
import javax.swing.*;

import org.freeasinspeech.kdelaf.KdeLAF;
public class Demo extends JFrame {
  
	
	public Demo() {
		super("Demo");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Icon icon = getIcon();
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel  buttonPanel = new JPanel();
		JPanel  progressPanel = new JPanel();
		JPanel  tabPanel = new JPanel(new GridLayout(2,2));
		JSpinner spinner = new JSpinner();
		JButton classicButton = new JButton("Classic button");
		JButton classicIconButton = new JButton("Classic icon button", icon);
		JButton disabledButton = new JButton("Disabled button");
		disabledButton.setEnabled(false);
		JButton disabledIconButton = new JButton("Disabled icon button", icon);
		disabledIconButton.setEnabled(false);
		buttonPanel.setBorder(new TitledBorder("Buttons"));
		buttonPanel.add(disabledButton);
		buttonPanel.add(classicButton);
		buttonPanel.add(disabledIconButton);
		buttonPanel.add(classicIconButton);
		buttonPanel.add(spinner);
		
		JRadioButton radioButton = new JRadioButton("Radio button");
		JCheckBox jCheckBox = new JCheckBox("Checkbox");
		jCheckBox.setSelected(true);
		buttonPanel.add(jCheckBox);
		buttonPanel.add(radioButton);
		classicIconButton.setSelected(true);
		
		String[] comboStrs = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
		JComboBox comboBox = new JComboBox(comboStrs);
		buttonPanel.add(comboBox);
		String[] comboEdStrs = { "Editable Item 1", "Editable Item 2", "Editable Item 3", "Editable Item 4", "Editable Item 5" };
		JComboBox comboEdBox = new JComboBox(comboStrs);
		comboEdBox.setEditable(true);
		buttonPanel.add(comboEdBox);
		
		JTabbedPane tabbedPaneTOP = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneTOP.addTab("1", new JLabel("TOP"));
		tabbedPaneTOP.addTab("2", new JLabel("TOP"));
		tabbedPaneTOP.addTab("3", new JLabel("TOP"));
		tabPanel.add(tabbedPaneTOP);
		
		JTabbedPane tabbedPaneBOTTOM = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPaneBOTTOM.addTab("1", new JLabel("BOTTOM"));
		tabbedPaneBOTTOM.addTab("2", new JLabel("BOTTOM"));
		tabbedPaneBOTTOM.addTab("3", new JLabel("BOTTOM"));
		tabPanel.add(tabbedPaneBOTTOM);
		
		JTabbedPane tabbedPaneLEFT = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPaneLEFT.addTab("1", new JLabel("LEFT"));
		tabbedPaneLEFT.addTab("2", new JLabel("LEFT"));
		tabbedPaneLEFT.addTab("3", new JLabel("LEFT"));
		tabPanel.add(tabbedPaneLEFT);
		
		JTabbedPane tabbedPaneRIGHT = new JTabbedPane(JTabbedPane.RIGHT);
		tabbedPaneRIGHT.addTab("1", new JLabel("RIGHT"));
		tabbedPaneRIGHT.addTab("2", new JLabel("RIGHT"));
		tabbedPaneRIGHT.addTab("3", new JLabel("RIGHT"));
		tabPanel.add(tabbedPaneRIGHT);
		
		tabbedPane.addTab("Buttons", icon, buttonPanel);
		tabbedPane.addTab("Progress bars", progressPanel);
		tabbedPane.addTab("Tabs", tabPanel);
		
		setContentPane(tabbedPane);
		//setPreferredSize(new Dimension(800, 600));
		pack();
		setVisible(true); 
	}
  public static void main(String [] args) {
			try {
				UIManager.setLookAndFeel(new KdeLAF());
			} catch (Exception e) {
				System.out.println(e);
			}
    new Demo();
  }
	public ImageIcon getIcon() {
		int black = Color.BLACK.getRGB();
		BufferedImage icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 16; j++)
				icon.setRGB(i, j, 0);
		icon.setRGB(3, 3, black); icon.setRGB(4, 3, black); icon.setRGB(3, 4, black); icon.setRGB(4, 4, black);
		icon.setRGB(11, 3, black); icon.setRGB(12, 3, black); icon.setRGB(11, 4, black); icon.setRGB(12, 4, black);
		icon.setRGB(7, 5, black); icon.setRGB(7, 6, black); icon.setRGB(7, 7, black); icon.setRGB(7, 8, black); icon.setRGB(8, 5, black); icon.setRGB(8, 6, black); icon.setRGB(8, 7, black); icon.setRGB(8, 8, black);
		for (int i = 5; i < 11; i++)
			for (int j = 10; j < 12; j++)
				icon.setRGB(i, j, black);
		return new ImageIcon(icon);
	}
}