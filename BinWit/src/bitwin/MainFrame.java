package bitwin;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	
	public enum DataType {DEC, HEX, BIN, OPERATION, ERROR};

	private TextPanel mainText;
	private DataPanel dataPanel;
	private BitPanel  bitPanel;
	
	public MainFrame() {
		super("BinWit");
		
		mainText = new TextPanel();

		dataPanel = new DataPanel();

		bitPanel = new BitPanel(1);
		
		bitPanel.setBitTableModified(new StringListener() {
			@Override
			public void textDetect(String text) {
				dataPanel.setRawDecData(Long.parseLong(text));
				mainText.setText("");
			}
		});
		

		mainText.setFinalStringListener(new StringListener() {
			@Override
			public void textDetect(String text) {
			}
		});

		mainText.setTypingDataString(new StringListener() {
			@Override
			public void textDetect(String text) {
				dataPanel.setRawDecData(Long.parseLong(text));
				bitPanel.setRawDecData(Long.parseLong(text));
			}
		});
		
		DataManipulation.getInst().setStringErrorListener(new StringListener() {
			@Override
			public void textDetect(String errorText) {
				mainText.setErrorText(errorText);
			}
		});

		
		layoutConfigure();
	}
	

	private void layoutConfigure() {

		//Layout manager
		setSize(700,450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		mainText.setPreferredSize(new Dimension(this.getWidth(), 80));
		add(mainText);
		add(dataPanel);
		add(bitPanel);
		
		java.net.URL imageURL = MainFrame.class.getResource("dvtalk.png");
		if (imageURL != null) {
		    ImageIcon icon = new ImageIcon(imageURL);
		    setIconImage(icon.getImage());
		}
		else {
			System.out.println("NULL ICO");
		}

//		ImageIcon img = new ImageIcon("image/dvtalk.png");
		
	}
}

