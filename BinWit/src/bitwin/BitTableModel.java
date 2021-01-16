package bitwin;

import java.util.List;

import javax.swing.table.AbstractTableModel;


public class BitTableModel extends AbstractTableModel {
	private List<String> bitList;
	
	public BitTableModel(List<String> bitList) {
		this.bitList = bitList;
	}

	@Override
	public int getColumnCount() {
		return 40; //32 + 8 spaces
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	public void setBitList(List<String> bitList) {
		this.bitList = bitList;
	}

	@Override
	public Object getValueAt(int row, int col) {
		int listidx = row*32 +col;
		return bitList.get(listidx);
	}

}
