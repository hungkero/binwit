package bitwin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class TextPanel extends JPanel {
	private JTextArea textArea;
	private JTextPane prevTextArea;

	private StringListener finalDataString;
	private StringListener typingDataString;


	public TextPanel() {
		textArea = new JTextArea(this.getWidth(), this.getHeight());
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		prevTextArea = new JTextPane();
		prevTextArea.setEditable(false);
		
		textArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
		prevTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		
		layoutConfiure();
		textArea.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {

			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String textstr = textArea.getText().toLowerCase();
				DataManipulation datadetect = DataManipulation.getInst();

				if (datadetect != null) {
					long rawDecData = datadetect.getRawDecData(textstr.replaceAll("\\s", ""));

					if (typingDataString != null) {
						typingDataString.textDetect(Long.toString(rawDecData));
					}
					
					// check if character is allow based on the char before --> need to add later
					if (textstr.length() > 0) {
						String lastCharacter = textstr.substring(textstr.length()-1);
						if ((datadetect.hasUnAllowChar(lastCharacter)) 
								& (!lastCharacter.matches("\\s"))) //whitespace
						{
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


	private void layoutConfiure() {
		setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 20));
		setLayout(new BorderLayout());
		add(textArea, BorderLayout.CENTER);
		add(prevTextArea, BorderLayout.SOUTH);
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
			prevTextArea.setForeground(Color.BLACK);
			prevTextArea.setText("previous input : "+ textData);
		}
		textArea.setText(str);
	}
	
	public void setErrorText(String str) {
		prevTextArea.setForeground(Color.RED);
		prevTextArea.setText(str);
	}
	
	public void appendText(String text) {
		textArea.append(text);
	}
}
