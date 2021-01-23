package bitwin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;

import javax.swing.JMenuItem;

public class MemHistItem extends JMenuItem implements Comparator<MemHistItem>{
	private String str_operation;
	private long rawDecResult;
	
	private StringListener itemClickedStringListener;

	public void setItemClickedStringListener(StringListener itemClickedStringListener) {
		this.itemClickedStringListener = itemClickedStringListener;
	}

	public MemHistItem(String str_operation) {
		this.str_operation = str_operation;
		this.rawDecResult = DataManipulation.getInst().getRawDecData(str_operation);
		setText(this.toString());
		
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				itemClickedStringListener.textDetect(str_operation);
				System.out.println(str_operation);
			}
		});
	}
	

	public String getStr_operation() {
		return str_operation;
	}

	public void setStr_operation(String str_operation) {
		this.str_operation = str_operation;
		this.rawDecResult = DataManipulation.getInst().getRawDecData(str_operation);
	}

	public long getRawDecResult() {
		return rawDecResult;
	}
	
	public String getStrHexResult() {
		return Long.toHexString(rawDecResult);
	}

	public String getStrDecResult() {
		return Long.toString(rawDecResult);
	}

	public String getStrBinResult() {
		return Long.toBinaryString(rawDecResult);
	}

	@Override
	public String toString() {
		return str_operation + " -->  'd" + rawDecResult + "  'h" + getStrHexResult();
	}

	@Override
	public int compare(MemHistItem item0, MemHistItem item1) {
		if (item0.rawDecResult != item1.rawDecResult) {
			return -1; //different
		}
		if (item0.str_operation.length() != item1.str_operation.length()) {
			return -1; //different
		}
		return 0; // equal
	}
}
