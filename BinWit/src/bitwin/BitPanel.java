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
	private JTable table;
	private JTable tableblank;
	private BitTableModel tableModel;
	private BitTableModel tableModelblank;
	private DataPanel selectedDataPanel;
	
	// for creating lower bitpanel with new selected bits
	private BitPanel lowerBitPanel;
	private JButton  addLowerBitPanelbtn;
	private int      selectedBitsforLowerBitPanel;
	private boolean  lowerBitPanelisEnable;
	private int      bitPanelLevel;
	
	private ArrayList<String> bitList;
	private ArrayList<String> bitListblank;
	
	private StringListener bitTableModifiedListener;

	public BitPanel(int bitPanelLevel) {
		this.bitPanelLevel = bitPanelLevel;

//		GridBagConstraints gc = new GridBagConstraints();
		addLowerBitPanelbtn = new JButton("+");
		addLowerBitPanelbtn.setPreferredSize(new Dimension(10, 15));
		addLowerBitPanelbtn.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

		bitList = new ArrayList<String>(32);
		
		initBitList();

		selectedDataPanel = new DataPanel();

		tableModel = new BitTableModel(parsingBitList(bitList));
		table = new JTable(tableModel);	

		table.setCellSelectionEnabled(true);
		table.setBackground(this.getBackground());
		table.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				getSelectedTableValue();
				if (lowerBitPanelisEnable) {
					lowerBitPanel.setRawDecData(selectedBitsforLowerBitPanel);
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
					int selectedCol = table.getSelectedColumn();
					int selectedRow = table.getSelectedRow();
					String selectedCellVal = (String) (table.getValueAt(selectedRow, selectedCol));

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
					bitList.set(selectedCellLocation , selectedCellVal);

					//update table
					tableModel.setBitList(parsingBitList(bitList));
					table.updateUI();
					
					//notify upper data panel
					if (bitTableModifiedListener != null) {
						bitTableModifiedListener.textDetect(Integer.toString(Integer.parseUnsignedInt(String.join("", bitList),2)));
					}

		        }
			}
		});
		
		bitListblank = new ArrayList<String>();
		for (int i = 0; i< 32; i++) {
			if ( (i %4)	== 0) {
				bitListblank.add(Integer.toString(31-i));
			}
			else {
				bitListblank.add(" ");
			}
		}
		tableModelblank = new BitTableModel(parsingBitList(bitListblank));
		tableblank = new JTable(tableModelblank);	
		tableblank.setCellSelectionEnabled(false);
		tableblank.setBackground(this.getBackground());
		tableblank.setFont(new Font(Font.SERIF, Font.PLAIN, 9));
		
		addLowerBitPanelbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lowerBitPanelisEnable == false ) {
					lowerBitPanel = new BitPanel(bitPanelLevel+1);
					add(lowerBitPanel, BorderLayout.SOUTH);
					lowerBitPanel.setRawDecData(selectedBitsforLowerBitPanel);

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
				add(table);
				add(tableblank);
			}
		}
		add(new TablePanel(), BorderLayout.NORTH);
		
	}

	private void initBitList() {
		for (int i = 0; i< 32; i++) {
			bitList.add("0");
		}
	}

	public void setRawDecData(long rawDecData) {
		this.rawDecData = rawDecData;
		setBitList(Long.toBinaryString(rawDecData));
		getSelectedTableValue();
		if (lowerBitPanelisEnable) {
			lowerBitPanel.setRawDecData(selectedBitsforLowerBitPanel);
		}
	}

	private void setBitList(String bitStr) {
		bitList.clear();
		initBitList();
		for (int i=0; i< bitStr.length(); i++) {
			bitList.set((31-i),String.valueOf(bitStr.charAt((bitStr.length()-1)-i)));
		}

		tableModel.setBitList(parsingBitList(bitList));
		table.updateUI();
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
		if (table.getSelectedColumn() != -1) { 
			int[] selectedCols = table.getSelectedColumns();
			int   selectedRow = table.getSelectedRow();
			
			StringBuilder selectedBits = new StringBuilder();

			for (int i: selectedCols) {
				selectedBits.append((String) table.getValueAt(selectedRow, i));
			}
			
			String selectBitsStr = selectedBits.toString().replace(" " , "");
			
			if (selectBitsStr.length() > 0 ) {
				selectedDataPanel.setRawDecData(Integer.parseInt(selectBitsStr, 2));
				selectedBitsforLowerBitPanel = Integer.parseInt(selectBitsStr, 2);
			}
		}	
	}

	public void setBitTableModified(StringListener bitTableModified) {
		this.bitTableModifiedListener = bitTableModified;
	}
}
