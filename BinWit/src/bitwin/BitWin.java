package bitwin;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;


public class BitWin {

	public static void main(String[] args) {
		
		
		
		FlatIntelliJLaf.install();
//		try {
//			UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialLiteTheme()));
//		} catch (UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new MainFrame();

			}
			
		});
		
	}

}
