package bitwin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class BitPanel extends JPanel {
	private long rawDecData;
	private JTable table31to0;
	private JTable tableblank31to0;
	private JTable table63to32;
	private JTable tableblank63to32;

	private BitTableModel tableModel31to0;
	private BitTableModel tableModelblank31to0;
	private BitTableModel tableModel63to32;
	private BitTableModel tableModelblank63to32;
	private DataPanel selectedDataPanel;
	
	// for creating lower bitpanel with new selected bits
	private BitPanel lowerBitPanel;
	private JButton  addLowerBitPanelbtn;
	private long     selectedDataforLowerBitPanel;
	private boolean  lowerBitPanelisEnable;
	private int      bitPanelLevel;
	
	// register button
	private JButton  registerDataBtn;
	private JLabel subNo;
	
	// for multiple dwords
	private boolean multipleDWords;
	
	private ArrayList<String> bitList31to0;
	private ArrayList<String> bitList63to32;
	private ArrayList<String> bitListblank31to0;
	private ArrayList<String> bitListblank63to32;
	
	private StringListener bitTableModifiedListener;
	private BitPanelUpdateListener bitPanelLevelListener;

	public BitPanel(int bitPanelLevel) {
		this.bitPanelLevel = bitPanelLevel;

		addLowerBitPanelbtn = new JButton("+");
		
		registerDataBtn = new JButton("R");

		bitList31to0  = new ArrayList<String>(32);
		bitList63to32 = new ArrayList<String>(32);
		
		bitList31to0  = initBitList(bitList31to0);
		bitList63to32 = initBitList(bitList63to32);

		selectedDataPanel = new DataPanel();

		// table for 63-32 bit
		tableModel63to32 = new BitTableModel(parsingBitList(bitList63to32),32);
		table63to32 = new JTable(tableModel63to32);	

		table63to32.setCellSelectionEnabled(true);
		table63to32.setBackground(this.getBackground());
		table63to32.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		table63to32.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				getSelectedTableValue();
				updateBitRangetoBlankTable();
				if (lowerBitPanelisEnable) {
					lowerBitPanel.setRawDecData(selectedDataforLowerBitPanel);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getClickCount()==2){
					int selectedCol = table63to32.getSelectedColumn();
					int selectedRow = table63to32.getSelectedRow();
					String selectedCellVal = (String) (table63to32.getValueAt(selectedRow, selectedCol));

					if (bitPanelLevel > 1) {
						setErrorText("Bit Modification is disable for sub Bit Select Table");
						return;
					};

					if (selectedCellVal == " ") return; 

					if (Integer.parseInt(selectedCellVal) == 0) {
						selectedCellVal = "1";
					}
					else if (Integer.parseInt(selectedCellVal) == 1) {
						selectedCellVal = "0";
					}
					int selectedCellLocation = selectedCol + 32*selectedRow;
					selectedCellLocation = selectedCellLocation - (selectedCellLocation)/5;
					bitList63to32.set(selectedCellLocation , selectedCellVal);

					//update table
					tableModel63to32.setBitList(parsingBitList(bitList63to32));
					table63to32.updateUI();
					
					notifyTableModifiedListener();
		        }
				
			}

		});

		// table for 31 to 0 bit
		tableModel31to0 = new BitTableModel(parsingBitList(bitList31to0),32);
		table31to0 = new JTable(tableModel31to0);	

		table31to0.setCellSelectionEnabled(true);
		table31to0.setBackground(this.getBackground());
		table31to0.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		table31to0.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				getSelectedTableValue();
				updateBitRangetoBlankTable();
				if (lowerBitPanelisEnable) {
					lowerBitPanel.setRawDecData(selectedDataforLowerBitPanel);
				}
		
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					int selectedCol = table31to0.getSelectedColumn();
					int selectedRow = table31to0.getSelectedRow();
					String selectedCellVal = (String) (table31to0.getValueAt(selectedRow, selectedCol));

					if (bitPanelLevel > 1) {
						setErrorText("Bit Modification is disable for sub Bit Select Table");
						return;
					};

					if (selectedCellVal == " ") return; 

					if (Integer.parseInt(selectedCellVal) == 0) {
						selectedCellVal = "1";
					}
					else if (Integer.parseInt(selectedCellVal) == 1) {
						selectedCellVal = "0";
					}
					int selectedCellLocation = selectedCol + 32*selectedRow;
					selectedCellLocation = selectedCellLocation - (selectedCellLocation)/5;
					bitList31to0.set(selectedCellLocation , selectedCellVal);

					//update table
					tableModel31to0.setBitList(parsingBitList(bitList31to0));
					table31to0.updateUI();
					
					notifyTableModifiedListener();

		        }
			}

		});
		
		// create bit position bars
		bitListblank31to0 = new ArrayList<String>();
		initBlankList31to0();
		tableModelblank31to0 = new BitTableModel(parsingBitList(bitListblank31to0),32);
		tableblank31to0 = new JTable(tableModelblank31to0);	
		tableblank31to0.setCellSelectionEnabled(false);
		tableblank31to0.setBackground(this.getBackground());
		tableblank31to0.setFont(new Font(Font.SERIF, Font.PLAIN, 9));
		
		
		bitListblank63to32 = new ArrayList<String>();
		initBlankList63to32();
		tableModelblank63to32 = new BitTableModel(parsingBitList(bitListblank63to32),32);
		tableblank63to32 = new JTable(tableModelblank63to32);	
		tableblank63to32.setCellSelectionEnabled(false);
		tableblank63to32.setBackground(this.getBackground());
		tableblank63to32.setFont(new Font(Font.SERIF, Font.PLAIN, 9));
		

		// button for create LowerBitPanel
		addLowerBitPanelbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lowerBitPanelisEnable == false ) {
					lowerBitPanel = new BitPanel(bitPanelLevel+1);
					lowerBitPanel.setRawDecData(selectedDataforLowerBitPanel);
					lowerBitPanel.setBitPanelLevelListener(bitPanelLevelListener);

					addLowerBitPanelbtn.setFont(new Font(Font.SANS_SERIF, Font.CENTER_BASELINE, 18));
					addLowerBitPanelbtn.setText("×");
					lowerBitPanelisEnable = true;
					bitPanelLevelListener.lowerBitPanelHandle(lowerBitPanel, lowerBitPanelisEnable);
					repaint();
				} else {
					try {
						addLowerBitPanelbtn.setFont(new Font(Font.SANS_SERIF, Font.CENTER_BASELINE, 15));
						addLowerBitPanelbtn.setText("+");
						lowerBitPanelisEnable = false;
						bitPanelLevelListener.lowerBitPanelHandle(lowerBitPanel, lowerBitPanelisEnable);
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		layoutConfigure();
	}

	private void initBlankList63to32() {
		bitListblank63to32.clear();
		for (int i = 32; i< 64; i++) {
			if ( (i %4)	== 0) {
				bitListblank63to32.add(Integer.toString(63-(i-32)));
			}
			else {
				bitListblank63to32.add(" ");
			}
		}
		
	}

	private void initBlankList31to0() {
		bitListblank31to0.clear();
		for (int i = 0; i< 32; i++) {
			if ( (i %4)	== 0) {
				bitListblank31to0.add(Integer.toString(31-i));
			}
			else {
				bitListblank31to0.add(" ");
			}
		}
		
	}


	private void notifyTableModifiedListener() {
		if (bitTableModifiedListener != null) {
			ArrayList<String> bitList63to0 = new ArrayList<String>(64);
			bitList63to0.addAll(bitList63to32);
			bitList63to0.addAll(bitList31to0);
			bitTableModifiedListener.textDetect(Long.toString(Long.parseUnsignedLong(String.join("", bitList63to0),2)));
		}
	}
	
	private void setErrorText(String string) {
		DataManipulation.getInst().setErrorText("Bit Modification is disable for sub Bit Select Table");
		Thread t1 = new Thread(new Runnable() { 
			@Override
			public void run() {
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				DataManipulation.getInst().setErrorText("");
			}
		});
		t1.start();
	}

	private ArrayList<String> initBitList(ArrayList<String> list) {
		list.clear();
		for (int i = 0; i< 32; i++) {
			list.add("0");
		}
		return list;
	}

	public void setRawDecData(long rawDecData) {
		this.rawDecData = rawDecData;
		
		String rawDecDataStr = Long.toBinaryString(rawDecData);

		if (rawDecDataStr.length() > 32) {
			setBitList(rawDecDataStr.substring(rawDecDataStr.length() - 32, rawDecDataStr.length()), bitList31to0);
			setBitList(rawDecDataStr.substring(0, rawDecDataStr.length() - 32), bitList63to32);
		}
		else {
			setBitList(Long.toBinaryString(rawDecData), bitList31to0);
			setBitList("0", bitList63to32);
		}
		
		
		tableModel31to0.setBitList(parsingBitList(bitList31to0));
		table31to0.updateUI();
		
		tableModel63to32.setBitList(parsingBitList(bitList63to32));
		table63to32.updateUI();
		
		getSelectedTableValue();
		if (lowerBitPanelisEnable) {
			lowerBitPanel.setRawDecData(selectedDataforLowerBitPanel);
		}
	}

	private void setBitList(String bitStr, ArrayList<String> list) {
		list = initBitList(list);
		for (int i=0; i< bitStr.length(); i++) {
			list.set((31-i),String.valueOf(bitStr.charAt((bitStr.length()-1)-i)));
		}
	}

	private ArrayList<String> parsingBitList(ArrayList<String> bitList) {
		ArrayList<String> sparsebitList = new ArrayList<String>();;
		for (int i=0; i<32; i++) {
			sparsebitList.add(bitList.get(i));
			if ((i%4)==3) {
				sparsebitList.add(" ");
			}
		}
		return sparsebitList;
	}
	
	public void getSelectedTableValue() {
		StringBuilder selectedBits = new StringBuilder();

		if (table63to32.getSelectedColumn() != -1) { 
			int[] selectedCols = table63to32.getSelectedColumns();
			int   selectedRow =  table63to32.getSelectedRow();

			for (int i: selectedCols) {
				selectedBits.append((String) table63to32.getValueAt(selectedRow, i));
			}
		}	 

		if (table31to0.getSelectedColumn() != -1) { 
			int[] selectedCols = table31to0.getSelectedColumns();
			int   selectedRow =  table31to0.getSelectedRow();

			for (int i: selectedCols) {
				selectedBits.append((String) table31to0.getValueAt(selectedRow, i));
			}
		}	
		String selectBitsStr = selectedBits.toString().replace(" " , "");
		if (selectBitsStr.length() > 0 ) {
			selectedDataPanel.setRawDecData(Long.parseLong(selectBitsStr, 2));
			selectedDataforLowerBitPanel = Long.parseLong(selectBitsStr, 2);
		}
	}
	
	protected void updateBitRangetoBlankTable() {
		if (table63to32.getSelectedColumn() != -1) { 
			initBlankList63to32();
			int[] selectedCols = table63to32.getSelectedColumns();
			
			int upperSelectedBit = selectedCols[0];
			int lowerSelectedBit = selectedCols[selectedCols.length -1];
			upperSelectedBit = upperSelectedBit - upperSelectedBit/5;
			lowerSelectedBit = lowerSelectedBit - lowerSelectedBit/5;
			int upperBit = 63 - upperSelectedBit;
			int lowerBit = 63 - lowerSelectedBit;
			
			if (upperSelectedBit < 32) bitListblank63to32.set(upperSelectedBit, Integer.toString(upperBit));
			if (lowerSelectedBit < 32) bitListblank63to32.set(lowerSelectedBit, Integer.toString(lowerBit) );
			
			tableModelblank63to32.setBitList(parsingBitList(bitListblank63to32));
			tableblank63to32.updateUI();

		}	

		if (table31to0.getSelectedColumn() != -1) { 
			initBlankList31to0();
			int[] selectedCols = table31to0.getSelectedColumns();
			
			int upperSelectedBit = selectedCols[0];
			int lowerSelectedBit = selectedCols[selectedCols.length -1];
			upperSelectedBit = upperSelectedBit - upperSelectedBit/5;
			lowerSelectedBit = lowerSelectedBit - lowerSelectedBit/5;
			int upperBit = 31 - upperSelectedBit;
			int lowerBit = 31 - lowerSelectedBit;
			
			if (upperSelectedBit < 32) bitListblank31to0.set(upperSelectedBit, Integer.toString(upperBit));
			if (lowerSelectedBit < 32) bitListblank31to0.set(lowerSelectedBit, Integer.toString(lowerBit) );
			
			tableModelblank31to0.setBitList(parsingBitList(bitListblank31to0));
			tableblank31to0.updateUI();

		}	
		
	}
	

	public void setBitTableModified(StringListener bitTableModified) {
		this.bitTableModifiedListener = bitTableModified;
	}
	
	
	private void layoutConfigure() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		class TablePanel extends JPanel {
			public TablePanel() {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				add(table63to32);
				add(tableblank63to32);
				table63to32.setBackground(Color.WHITE);
				tableblank63to32.setBackground(Color.WHITE);
				add(table31to0);
				add(tableblank31to0);
				table31to0.setBackground(Color.WHITE);
				tableblank31to0.setBackground(Color.WHITE);
				setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			}
		}
		
		class DataPanelandExpandButton extends JPanel{
			public DataPanelandExpandButton() {
				Color datapanelColor = selectedDataPanel.getDataTextPaneBackgroundColor();

				setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
				add(selectedDataPanel);
				
				class LowerBitPanelButton extends JPanel {
					public LowerBitPanelButton() {
						setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//						add(registerDataBtn);
//						registerDataBtn.setPreferredSize(new Dimension(30, 10));
//						registerDataBtn.setBackground(Color.LIGHT_GRAY);
//						registerDataBtn.setForeground(Color.WHITE);
//						registerDataBtn.setFont(new Font(Font.SANS_SERIF, Font.CENTER_BASELINE, 15));
//						registerDataBtn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,1,1, datapanelColor.brighter()), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
//						
						if (bitPanelLevel > 1 ) {
							subNo = new JLabel("Sub " + Integer.toString(bitPanelLevel-1));
							subNo.setFont(new Font(Font.SANS_SERIF, Font.CENTER_BASELINE, 11));
							add(subNo);
						}
						
						add(addLowerBitPanelbtn);
						addLowerBitPanelbtn.setPreferredSize(new Dimension(30, 10));
						addLowerBitPanelbtn.setBackground(Color.LIGHT_GRAY);
						addLowerBitPanelbtn.setForeground(Color.WHITE);
						addLowerBitPanelbtn.setFont(new Font(Font.SANS_SERIF, Font.CENTER_BASELINE, 15));
						addLowerBitPanelbtn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,1,2, datapanelColor), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
						setBackground(datapanelColor);
					}
				}

				add(new LowerBitPanelButton());
				setBackground(datapanelColor);
			}
		}

		add(new TablePanel());
		add(new DataPanelandExpandButton());
		
	}

	public void setBitPanelLevelListener(BitPanelUpdateListener bitPanelLevelListener) {
		this.bitPanelLevelListener = bitPanelLevelListener;
	}

	public int getBitPanelLevel() {
		return bitPanelLevel;
	}

	public void setMultipleDWords(boolean multipleDWords) {
		this.multipleDWords = multipleDWords;

		if (multipleDWords)	{
			table63to32.setVisible(false);
			tableblank63to32.setVisible(false);
			addLowerBitPanelbtn.setVisible(false);
			if (subNo != null) {
				subNo.setVisible(false);
			}

			selectedDataPanel.setBinDataDisable(true);
			selectedDataPanel.setHorizontalLayoutEnable(true);
			
			table31to0.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
			tableblank31to0.setFont(new Font(Font.SERIF, Font.PLAIN, 9));
			selectedDataPanel.setDataFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
			updateUI();
		}
	}
}
