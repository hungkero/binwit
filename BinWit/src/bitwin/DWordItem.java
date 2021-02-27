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
	private DataPanel  selectedDataPanel;
	private JTextField dWDataInput;
	private int        itemIndex;

	private StringArrayListener dWItemListListener;
	
	private JTable table15to0;
	private JTable tableblank15to0;
	private JTable table31to16;
	private JTable tableblank31to16;

	private BitTableModel tableModel15to0;
	private BitTableModel tableModelblank15to0;
	private BitTableModel tableModel31to16;
	private BitTableModel tableModelblank31to16;
	
	private ArrayList<String> bitList15to0;
	private ArrayList<String> bitList31to16;
	private ArrayList<String> bitListblank15to0;
	private ArrayList<String> bitListblank31to16;
	
	public DWordItem(int itemIndex) {
		this.itemIndex = itemIndex;

		bitList15to0  = new ArrayList<String>(16);
		bitList31to16 = new ArrayList<String>(16);
		
		bitList15to0  = initBitList(bitList15to0);
		bitList31to16 = initBitList(bitList31to16);
		
		selectedDataPanel = new DataPanel();
		selectedDataPanel.setBinDataDisable(false);
		
		dWDataInput = new JTextField();
		
		// table for 31-16 bit
		tableModel31to16 = new BitTableModel(parsingBitList(bitList31to16),16);
		table31to16 = new JTable(tableModel31to16);	

		table31to16.setCellSelectionEnabled(true);
		table31to16.setBackground(this.getBackground());
		table31to16.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		table31to16.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				getSelectedTableValue();
				updateBitRangetoBlankTable();
			}
			
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		tableModel15to0 = new BitTableModel(parsingBitList(bitList15to0),16);
		table15to0 = new JTable(tableModel15to0);	

		table15to0.setCellSelectionEnabled(true);
		table15to0.setBackground(this.getBackground());
		table15to0.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		table15to0.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				getSelectedTableValue();
				updateBitRangetoBlankTable();
			}
			
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
			}

		});
		
		// create bit position bars
		bitListblank15to0 = new ArrayList<String>();
		initBlankList15to0();
		tableModelblank15to0 = new BitTableModel(parsingBitList(bitListblank15to0),16);
		tableblank15to0 = new JTable(tableModelblank15to0);	
		tableblank15to0.setCellSelectionEnabled(false);
		tableblank15to0.setBackground(this.getBackground());
		tableblank15to0.setFont(new Font(Font.SERIF, Font.PLAIN, 8));
		
		
		bitListblank31to16 = new ArrayList<String>();
		initBlankList31to16();
		tableModelblank31to16 = new BitTableModel(parsingBitList(bitListblank31to16),16);
		tableblank31to16 = new JTable(tableModelblank31to16);	
		tableblank31to16.setCellSelectionEnabled(false);
		tableblank31to16.setBackground(this.getBackground());
		tableblank31to16.setFont(new Font(Font.SERIF, Font.PLAIN, 8));
		
		
		dWDataInput.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				String textstr = dWDataInput.getText();
				DataManipulation datadetect = DataManipulation.getInst();
				
				textstr = textstr.replaceAll("\\s", " ");
				String[] items = textstr.split(" ");
				ArrayList<String> itemList = new ArrayList<String>(Arrays.asList(items));

				if (itemList.size() == 1) {
					long rawDecData = datadetect.getRawDecData(textstr.replaceAll("\\s", ""));
					setRawDecData(rawDecData);
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
		String rawDecDataStr = Long.toBinaryString(rawDecData);

		if (rawDecDataStr.length() > 16) {
			setBitList(rawDecDataStr.substring(rawDecDataStr.length() - 16, rawDecDataStr.length()), bitList15to0);
			setBitList(rawDecDataStr.substring(0, rawDecDataStr.length() - 16), bitList31to16);
		}
		else {
			setBitList(Long.toBinaryString(rawDecData), bitList15to0);
			setBitList("0", bitList31to16);
		}
		
		tableModel15to0.setBitList(parsingBitList(bitList15to0));
		table15to0.updateUI();
		
		tableModel31to16.setBitList(parsingBitList(bitList31to16));
		table31to16.updateUI();
		
		getSelectedTableValue();
	}
	
	private void setBitList(String bitStr, ArrayList<String> list) {
		list = initBitList(list);
		for (int i=0; i< bitStr.length(); i++) {
			list.set((15-i),String.valueOf(bitStr.charAt((bitStr.length()-1)-i)));
		}
	}

	private ArrayList<String> initBitList(ArrayList<String> list) {
		list.clear();
		for (int i = 0; i< 16; i++) {
			list.add("0");
		}
		return list;
	}	

	private ArrayList<String> parsingBitList(ArrayList<String> bitList) {
		ArrayList<String> sparsebitList = new ArrayList<String>();
		for (int i=0; i<16; i++) {
			sparsebitList.add(bitList.get(i));
			if ((i%4)==3) {
				sparsebitList.add(" ");
			}
		}
		return sparsebitList;
	}

	public void getSelectedTableValue() {
		StringBuilder selectedBits = new StringBuilder();

		if (table31to16.getSelectedColumn() != -1) { 
			int[] selectedCols = table31to16.getSelectedColumns();
			int   selectedRow =  table31to16.getSelectedRow();

			for (int i: selectedCols) {
				selectedBits.append((String) table31to16.getValueAt(selectedRow, i));
			}
		}	

		if (table15to0.getSelectedColumn() != -1) { 
			int[] selectedCols = table15to0.getSelectedColumns();
			int   selectedRow =  table15to0.getSelectedRow();

			for (int i: selectedCols) {
				selectedBits.append((String) table15to0.getValueAt(selectedRow, i));
			}
		}	
		String selectBitsStr = selectedBits.toString().replace(" " , "");
		if (selectBitsStr.length() > 0 ) {
			selectedDataPanel.setRawDecData(Long.parseLong(selectBitsStr, 2));
		}
	}

	protected void updateBitRangetoBlankTable() {
		if (table31to16.getSelectedColumn() != -1) { 
			initBlankList31to16();
			int[] selectedCols = table31to16.getSelectedColumns();
			
			int upperSelectedBit = selectedCols[0];
			int lowerSelectedBit = selectedCols[selectedCols.length -1];
			upperSelectedBit = upperSelectedBit - upperSelectedBit/5;
			lowerSelectedBit = lowerSelectedBit - lowerSelectedBit/5;
			int upperBit = 31 - upperSelectedBit;
			int lowerBit = 31 - lowerSelectedBit;
			
			if (upperSelectedBit < 16) bitListblank31to16.set(upperSelectedBit, Integer.toString(upperBit));
			if (lowerSelectedBit < 16) bitListblank31to16.set(lowerSelectedBit, Integer.toString(lowerBit) );
			
			tableModelblank31to16.setBitList(parsingBitList(bitListblank31to16));
			tableblank31to16.updateUI();
		}	

		if (table15to0.getSelectedColumn() != -1) { 
			initBlankList15to0();
			int[] selectedCols = table15to0.getSelectedColumns();
			
			int upperSelectedBit = selectedCols[0];
			int lowerSelectedBit = selectedCols[selectedCols.length -1];
			upperSelectedBit = upperSelectedBit - upperSelectedBit/5;
			lowerSelectedBit = lowerSelectedBit - lowerSelectedBit/5;
			int upperBit = 15 - upperSelectedBit;
			int lowerBit = 15 - lowerSelectedBit;
			
			if (upperSelectedBit < 16) bitListblank15to0.set(upperSelectedBit, Integer.toString(upperBit));
			if (lowerSelectedBit < 16) bitListblank15to0.set(lowerSelectedBit, Integer.toString(lowerBit) );
			
			tableModelblank15to0.setBitList(parsingBitList(bitListblank15to0));
			tableblank15to0.updateUI();
		}	
	}
	
	private void initBlankList31to16() {
		bitListblank31to16.clear();
		for (int i = 16; i< 32; i++) {
			if ( (i %4)	== 0) {
				bitListblank31to16.add(Integer.toString(31-(i-16)));
			}
			else {
				bitListblank31to16.add(" ");
			}
		}
	}

	private void initBlankList15to0() {
		bitListblank15to0.clear();
		for (int i = 0; i< 16; i++) {
			if ( (i %4)	== 0) {
				bitListblank15to0.add(Integer.toString(15-i));
			}
			else {
				bitListblank15to0.add(" ");
			}
		}
		
	}
	
	private void layoutConfigure() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		class TablePanel extends JPanel {
			public TablePanel() {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				add(table31to16);
				add(tableblank31to16);
				add(table15to0);
				add(tableblank15to0);
				table15to0.setBackground(Color.WHITE);
				table31to16.setBackground(Color.WHITE);
				tableblank15to0.setBackground(Color.WHITE);
				tableblank31to16.setBackground(Color.WHITE);
				setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				setPreferredSize(new Dimension(this.getWidth()/2, this.getHeight()));
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
				add(selectedDataPanel);
				selectedDataPanel.setDataFont(new Font(Font.MONOSPACED, Font.PLAIN, 13) );
				selectedDataPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
				setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				setPreferredSize(new Dimension(this.getWidth()/2, this.getHeight()));
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
