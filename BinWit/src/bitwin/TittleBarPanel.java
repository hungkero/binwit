package bitwin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class TittleBarPanel extends JPanel {
	private JButton closeBtn;
	private JMenuBar memHistMenuBar;
	private JMenu   memHistMenu;
	private JMenu   addToMemBtn;
	
	class ButtonPanel extends JPanel {
		public ButtonPanel() {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		}
		public void addComponent(JComponent component) {
			add(component);
		}
	}
	
	private ButtonPanel westPanel;
	private ButtonPanel eastPanel;

	public TittleBarPanel() {
		westPanel = new ButtonPanel();
		eastPanel = new ButtonPanel();

		closeBtn = new JButton("×");
		memHistMenuBar = new JMenuBar();
		addToMemBtn = new JMenu(" M+ ");
		memHistMenu = new JMenu(" M↓ ");
		
		memHistMenuBar.add(addToMemBtn);
		memHistMenuBar.add(memHistMenu);

		closeBtn.addMouseListener(new MouseListener() {
			Color color;
			int red,blue,green,alpha;
			
			public void mouseReleased(MouseEvent arg0) {
				color = eastPanel.getBackground();
				red = color.getRed();
				blue = color.getBlue();
				green = color.getGreen();
				alpha = color.getAlpha();
				closeBtn.setBackground(new Color(red, green, blue, alpha ));
			}
			
			public void mousePressed(MouseEvent arg0) {
				color = eastPanel.getBackground();
				red = color.getRed();
				blue = color.getBlue();
				green = color.getGreen();
				alpha = color.getAlpha();
				closeBtn.setBackground(new Color(red - 10, green - 10, blue -10, 200 ));
			}
			
			public void mouseExited(MouseEvent arg0) {
				color = eastPanel.getBackground();
				red = color.getRed();
				blue = color.getBlue();
				green = color.getGreen();
				alpha = color.getAlpha();
				closeBtn.setBackground(new Color(red, green, blue, alpha ));
			}
			
			public void mouseEntered(MouseEvent arg0) {
				color = eastPanel.getBackground();
				red = color.getRed();
				blue = color.getBlue();
				green = color.getGreen();
				alpha = color.getAlpha();
				closeBtn.setBackground(new Color(red - 10, green - 10, blue -10, 200 ));
			}
			
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
		
		layoutConfigure();
	}

	private void layoutConfigure() {
		setLayout(new BorderLayout(5,5));
		
		add(westPanel, BorderLayout.WEST);
		add(eastPanel, BorderLayout.EAST);
		westPanel.addComponent(memHistMenuBar);
		eastPanel.addComponent(closeBtn);
		
		addToMemBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
		memHistMenu.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
		closeBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		
		refreshColor();
		
	}
	
	public void refreshColor() {
		Color color = this.getBackground();
		
		int red = color.getRed();
		int blue = color.getBlue();
		int green = color.getGreen();
		int alpha = color.getAlpha();
		
		westPanel.setBackground(new Color(red, green, blue, alpha));
		eastPanel.setBackground(new Color(red, green, blue, alpha));
		
		closeBtn.setBackground(new Color(red, green, blue, alpha));
		addToMemBtn.setBackground(new Color(red, green, blue, alpha));
		memHistMenu.setBackground(new Color(red, green, blue, alpha));
		memHistMenuBar.setBackground(new Color(red, green, blue, alpha));
		
		closeBtn.setForeground(new Color(red -150, green-150, blue -150, 255));
		addToMemBtn.setForeground(new Color(red -150, green-150, blue -150, 255));
		memHistMenu.setForeground(new Color(red -150, green-150, blue -150, 255));
		memHistMenuBar.setForeground(new Color(red -150, green-150, blue -150, 255));

		closeBtn.setBorder(BorderFactory.createLineBorder(closeBtn.getBackground(), 1, true));
		addToMemBtn.setBorder(BorderFactory.createLineBorder(addToMemBtn.getBackground(), 1, true));
		memHistMenu.setBorder(BorderFactory.createLineBorder(addToMemBtn.getBackground(), 1, true));
		memHistMenuBar.setBorder(BorderFactory.createLineBorder(addToMemBtn.getBackground(), 1, true));
		
	}
}