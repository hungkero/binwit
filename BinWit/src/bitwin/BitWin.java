package bitwin;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatLightLaf;


public class BitWin {

	public static void main(String[] args) {
		
		
		
		FlatLightLaf.install();
//		try {
//			UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialOceanicTheme()));
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
