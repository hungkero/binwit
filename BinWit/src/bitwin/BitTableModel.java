package bitwin;

import java.util.List;

import javax.swing.table.AbstractTableModel;


public class BitTableModel extends AbstractTableModel {
	private List<String> bitList;
	private int bitsPerRow;
	
	public BitTableModel(List<String> bitList, int bitsPerRow) {
		this.bitList = bitList;
		this.bitsPerRow = bitsPerRow;
	}

	@Override
	public int getColumnCount() {
		return bitsPerRow + bitsPerRow/4; //32 + 8 spaces
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
		int listidx = row*bitsPerRow +col;
		return bitList.get(listidx);
	}

}
