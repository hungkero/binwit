package bitwin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class DWordItem extends JPanel {
	private BitPanel   bitPanel;
	private JTextField dWDataInput;
	private int        itemIndex;

	private StringArrayListener dWItemListListener;
	
	public DWordItem(int itemIndex) {
		this.itemIndex = itemIndex;

		bitPanel = new BitPanel(2);
		bitPanel.setMultipleDWords(true);
		
		dWDataInput = new JTextField();
		dWDataInput.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		dWDataInput.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				String textstr = dWDataInput.getText();
				DataManipulation datadetect = DataManipulation.getInst();
				
				textstr = textstr.replaceAll("\\s", " ");
				String[] items = textstr.split(" ");
				ArrayList<String> itemList = new ArrayList<String>(Arrays.asList(items));

				if (itemList.size() == 1) {
					long rawDecData = datadetect.getRawDecData(textstr.replaceAll("\\s", ""));
					bitPanel.setRawDecData(rawDecData);
				}
				else {
					dWItemListListener.textDetect(itemList);
				}


			}
			public void keyTyped(KeyEvent e) { }
			public void keyPressed(KeyEvent e) { }
		});
		
		layoutConfigure();

	}
	
	private void setRawDecData(long rawDecData) {
		bitPanel.setRawDecData(rawDecData);
	}
	
	
	private void layoutConfigure() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		class TablePanel extends JPanel {
			public TablePanel() {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				add(bitPanel);
				setBorder(BorderFactory.createEmptyBorder(2, 13, 2, 2));
			}
		}
		
		class DwDataPanel extends JPanel {
			public DwDataPanel() {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				class LableDataInput extends JPanel {
					public LableDataInput() {
						setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
						JLabel jLabel = new JLabel(Integer.toString(itemIndex)+" ");
						add(jLabel);
						add(dWDataInput);
					}
				} 
				add(new LableDataInput());
				setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			}
		}
		add(new DwDataPanel());
		add(new TablePanel());

	}


	public void setdWItemListListener(StringArrayListener dWItemListListener) {
		this.dWItemListListener = dWItemListListener;
	}
	
	public void setdWData(long rawDecData) {
		dWDataInput.setText("0x"+Long.toHexString(rawDecData));
		setRawDecData(rawDecData);
	};
}
