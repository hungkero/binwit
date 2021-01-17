package bitwin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;

public class TextPanel extends JPanel {
	private JTextArea textArea;
	private JTextPane prevTextArea;

	private StringListener finalDataString;
	private StringListener typingDataString;

	private DataManipulation datadetect;


	public TextPanel() {
		textArea = new JTextArea(this.getWidth(), this.getHeight());
		prevTextArea = new JTextPane();

		setLayout(new BorderLayout());
		add(new JScrollPane(textArea), BorderLayout.CENTER);
		add(new JScrollPane(prevTextArea), BorderLayout.SOUTH);
		
		prevTextArea.setEditable(false);
		
		Border innerBorder = BorderFactory.createTitledBorder("");
		Border outerBorder = BorderFactory.createEmptyBorder(7,7,7,11);
//		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setBorder(BorderFactory.createCompoundBorder(outerBorder, BorderFactory.createEtchedBorder()));
		
		setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 20));
		
		textArea.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {

			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String textstr = textArea.getText().toLowerCase();

				if (datadetect != null) {
					long rawDecData = datadetect.getRawDecData(textstr.replace(" ", ""));

					if (typingDataString != null) {
						typingDataString.textDetect(Long.toString(rawDecData));
						prevTextArea.setText("");
					}
					
					// check if character is allow based on the char before --> need to add later
					if (datadetect.hasUnAllowChar(textstr)) {
						if (textstr.length() > 0) {
							textArea.setText(textstr.substring(0,textstr.length() - 1));
						}
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}


	public void setDatadetect(DataManipulation mainText) {
		this.datadetect = mainText;
	}

	public void setFinalStringListener(StringListener stringListener2) {
		this.finalDataString = stringListener2;
	}
	
	public void setTypingDataString(StringListener typingDataString) {
		this.typingDataString = typingDataString;
	}

	public String getText() {
		return textArea.getText();
	}
	
	public void setText(String str){
		String textData = textArea.getText();
		if (textData.length()>0) {
			prevTextArea.setText("previous input : "+ textData);
		}
		textArea.setText(str);
	}
	
	public void appendText(String text) {
		textArea.append(text);
	}
}
