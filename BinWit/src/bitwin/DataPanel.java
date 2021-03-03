package bitwin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ColorModel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import com.formdev.flatlaf.util.ColorFunctions;

public class DataPanel extends JPanel {
	private JTextPane decData;
	private JTextPane hexData;
	private JTextPane binData;
	
	private long rawDecData;
	
	private Color dataTextPaneBackgroundColor;
	
	private boolean binDataDisable=false;
	private boolean horizontalLayoutEnable=false;
	
	public void setBinDataDisable(boolean binDataDisable) {
		this.binDataDisable = binDataDisable;
		binData.setVisible(false);
	}

	public void setRawDecData(long rawDecData) {
		this.rawDecData = (long) rawDecData;
		updateDataLabel();
	}

	private void updateDataLabel() {
		decData.setText("DEC    " + Long.toString(rawDecData));
		hexData.setText("HEX  'h" + addUnderscore(Long.toHexString(rawDecData)));
		binData.setText("BIN  'b" + addUnderscore(Long.toBinaryString(rawDecData)));

		decData.updateUI();
		hexData.updateUI();
		binData.updateUI();
	}
	
	public void setHorizontalLayoutEnable(boolean horizontalLayoutEnable) {
		this.horizontalLayoutEnable = horizontalLayoutEnable;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		updateUI();
	}

	private String addUnderscore(String str) {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i=(str.length()-1); i >= 0; i--) {
			stringBuilder.append(str.charAt(i));
			if ((str.length()-i)%4==0 & (i!= (str.length()-1)) ) {
				stringBuilder.append("_");
			}
		}
		if (stringBuilder.lastIndexOf("_") == (stringBuilder.length()-1)) {
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
		}
		return stringBuilder.reverse().toString();

	}

	public DataPanel() {
		constructPanel();
	}
	
	
	public DataPanel(boolean horizontalLayoutEnable) {
		this.horizontalLayoutEnable = horizontalLayoutEnable;
		constructPanel();
	}

	
	private void constructPanel() {
		//data JTextPane setting
		decData = new JTextPane();
		hexData = new JTextPane();
		binData = new JTextPane();
		
		decData.setContentType("text/plain");
		hexData.setContentType("text/plain");
		binData.setContentType("text/plain");
		
		Color color = getBackground();
		int blue    = color.getBlue() ;
		int red     = color.getRed() ;
		int green   = color.getGreen();
		
		decData.setBackground(new Color(red, green, blue, 255));
		hexData.setBackground(new Color(red, green, blue, 255));
		binData.setBackground(new Color(red, green, blue, 255));
		
		decData.setEditable(false);
		hexData.setEditable(false);
		binData.setEditable(false);
		
		decData.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		hexData.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		binData.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));

		decData.setText("DEC");
		hexData.setText("HEX");
		binData.setText("BIN");
		
		setDataPanelLayout();
	}

	private void setDataPanelLayout() {
		if (horizontalLayoutEnable) {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		}
		else {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}
		add(decData);
		add(hexData);
		add(binData);
		
	}

	public Color getDataTextPaneBackgroundColor() {
		dataTextPaneBackgroundColor = decData.getBackground();
		return dataTextPaneBackgroundColor;
	}
	
	public void setDataFont(Font font) {
		decData.setFont(font);
		hexData.setFont(font);
		binData.setFont(font);
		updateUI();
	}

}
