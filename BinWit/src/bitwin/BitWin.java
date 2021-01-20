package bitwin;
import javax.swing.JFrame;
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

//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice gd = ge.getDefaultScreenDevice();

        JFrame.setDefaultLookAndFeelDecorated(true);

		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
			}
			
		});
		
	}

}
