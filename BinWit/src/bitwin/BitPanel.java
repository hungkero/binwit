package bitwin;

import java.awt.BorderLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

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
	
	private ArrayList<String> bitList31to0;
	private ArrayList<String> bitList63to32;
	private ArrayList<String> bitListblank31to0;
	private ArrayList<String> bitListblank63to32;
	
	private StringListener bitTableModifiedListener;

	public BitPanel(int bitPanelLevel) {
		this.bitPanelLevel = bitPanelLevel;

//		GridBagConstraints gc = new GridBagConstraints();
		addLowerBitPanelbtn = new JButton("+");
		addLowerBitPanelbtn.setPreferredSize(new Dimension(10, 15));
		addLowerBitPanelbtn.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

		bitList31to0  = new ArrayList<String>(32);
		bitList63to32 = new ArrayList<String>(32);
		
		bitList31to0  = initBitList(bitList31to0);
		bitList63to32 = initBitList(bitList63to32);

		selectedDataPanel = new DataPanel();

		// table for 63-32 bit
		tableModel63to32 = new BitTableModel(parsingBitList(bitList63to32));
		table63to32 = new JTable(tableModel63to32);	

		table63to32.setCellSelectionEnabled(true);
		table63to32.setBackground(this.getBackground());
		table63to32.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		table63to32.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
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
				
			}
		});

		// table for 31 to 0 bit
		tableModel31to0 = new BitTableModel(parsingBitList(bitList31to0));
		table31to0 = new JTable(tableModel31to0);	

		table31to0.setCellSelectionEnabled(true);
		table31to0.setBackground(this.getBackground());
		table31to0.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		table31to0.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				getSelectedTableValue();
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
						System.out.println("Bit Modification is disable");
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
					
					//notify upper data panel
					if (bitTableModifiedListener != null) {
						bitTableModifiedListener.textDetect(Integer.toString(Integer.parseUnsignedInt(String.join("", bitList31to0),2)));
					}

		        }
			}
		});
		
		// create bit position bars
		bitListblank31to0 = new ArrayList<String>();
		for (int i = 0; i< 32; i++) {
			if ( (i %4)	== 0) {
				bitListblank31to0.add(Integer.toString(31-i));
			}
			else {
				bitListblank31to0.add(" ");
			}
		}
		tableModelblank31to0 = new BitTableModel(parsingBitList(bitListblank31to0));
		tableblank31to0 = new JTable(tableModelblank31to0);	
		tableblank31to0.setCellSelectionEnabled(false);
		tableblank31to0.setBackground(this.getBackground());
		tableblank31to0.setFont(new Font(Font.SERIF, Font.PLAIN, 9));
		
		
		bitListblank63to32 = new ArrayList<String>();
		for (int i = 32; i< 64; i++) {
			if ( (i %4)	== 0) {
				bitListblank63to32.add(Integer.toString(63-(i-32)));
			}
			else {
				bitListblank63to32.add(" ");
			}
		}
		tableModelblank63to32 = new BitTableModel(parsingBitList(bitListblank63to32));
		tableblank63to32 = new JTable(tableModelblank63to32);	
		tableblank63to32.setCellSelectionEnabled(false);
		tableblank63to32.setBackground(this.getBackground());
		tableblank63to32.setFont(new Font(Font.SERIF, Font.PLAIN, 9));
		
		
		addLowerBitPanelbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lowerBitPanelisEnable == false ) {
					lowerBitPanel = new BitPanel(bitPanelLevel+1);
					add(lowerBitPanel, BorderLayout.SOUTH);
					lowerBitPanel.setRawDecData(selectedDataforLowerBitPanel);

					addLowerBitPanelbtn.setText("-");
					lowerBitPanelisEnable = true;
					repaint();
				} else {
					try {
						remove(lowerBitPanel);
						revalidate();
						repaint();
						lowerBitPanel.finalize();

						addLowerBitPanelbtn.setText("+");
						lowerBitPanelisEnable = false;
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		layoutConfigure();
	}

	private void layoutConfigure() {
		//Layout

		setBorder(BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder(0,7,7,11)	, BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		setLayout(new BorderLayout());

		add(selectedDataPanel, BorderLayout.CENTER);
		add(addLowerBitPanelbtn, BorderLayout.EAST);
		addLowerBitPanelbtn.setPreferredSize(new Dimension(40, 5));
		
		class TablePanel extends JPanel {
			public TablePanel() {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				add(table63to32);
				add(tableblank63to32);
				add(table31to0);
				add(tableblank31to0);
			}
		}
		add(new TablePanel(), BorderLayout.NORTH);
		
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
			setBitList(Long.toBinaryString(rawDecData).substring(rawDecDataStr.length() - 33, rawDecDataStr.length() - 1 ), bitList31to0);
			setBitList(Long.toBinaryString(rawDecData).substring(0, rawDecDataStr.length() - 32), bitList63to32);
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
		if (table31to0.getSelectedColumn() != -1) { 
			int[] selectedCols = table31to0.getSelectedColumns();
			int   selectedRow =  table31to0.getSelectedRow();
			
			StringBuilder selectedBits = new StringBuilder();

			for (int i: selectedCols) {
				selectedBits.append((String) table31to0.getValueAt(selectedRow, i));
			}
			
			String selectBitsStr = selectedBits.toString().replace(" " , "");
			
			if (selectBitsStr.length() > 0 ) {
				selectedDataPanel.setRawDecData(Long.parseLong(selectBitsStr, 2));
				selectedDataforLowerBitPanel = Long.parseLong(selectBitsStr, 2);
			}
		}	
	}

	public void setBitTableModified(StringListener bitTableModified) {
		this.bitTableModifiedListener = bitTableModified;
	}
}
