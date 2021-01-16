package bitwin;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JTextPane;

public class DataPanel extends JPanel {
	private JTextPane decData;
	private JTextPane hexData;
	private JTextPane binData;
	
//	private JTextField decData;
//	private JTextField hexData;
//	private JTextField binData;

//	private JLabel decData;
//	private JLabel hexData;
//	private JLabel binData;


	private JPanel decPanel;
	private JPanel hexPanel;
	private JPanel binPanel;
	
	private long rawDecData;
	
	public void setRawDecData(long rawDecData) {
		this.rawDecData = (long) rawDecData;
		updateDataLabel();
	}

	private void updateDataLabel() {
		decData.setText("DEC   " +Long.toString(rawDecData));
		hexData.setText("HEX   " +Long.toHexString(rawDecData));
		binData.setText("BIN   " +Long.toBinaryString(rawDecData));

		decData.updateUI();
		hexData.updateUI();
		binData.updateUI();
	}

	public DataPanel() {
		
		//data JTextPane setting
		decData = new JTextPane();
		hexData = new JTextPane();
		binData = new JTextPane();
		
		decData.setContentType("text/plain");
		hexData.setContentType("text/plain");
		binData.setContentType("text/plain");
		
//		decData = new JTextField();
//		hexData = new JTextField();
//		binData = new JTextField();
		
//		decData = new JLabel();
//		hexData = new JLabel();
//		binData = new JLabel();
		
		decData.setBackground(this.getBackground());
		hexData.setBackground(this.getBackground());
		binData.setBackground(this.getBackground());
		
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
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		// row 1
		gc.gridx = 0;
		gc.gridy = 0;
		
		gc.weighty = 0.1;
		gc.weightx = 1;
		
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.NORTHWEST ;
		gc.insets = new Insets(0,5,0,30);
		add(decData, gc);
		
		gc.gridy ++;
		add(hexData, gc);

		gc.gridy ++;
		add(binData, gc);

		
	}

}
