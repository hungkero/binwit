package bitwin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import bitwin.InfoFrame.TittleBarPanel;

public class InfoFrame extends JFrame {
	private static Point point = new Point();

	class TittleBarPanel extends JPanel{
		public TittleBarPanel() {
			setLayout(new BorderLayout());
		}
		public void addComponent(JComponent component, String position ) {
			add(component, position);
		}
	}
	class ButtonPanel extends JPanel {
		public ButtonPanel() {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		}
		public void addComponent(JComponent component) {
			add(component);
		}
	}
	
	private JButton closeBtn;
	private ButtonPanel buttonPanel;
	private TittleBarPanel tittleBarPanel;
	private TittleBarPanel infoPanel;
	private JTextPane infoText;

	public InfoFrame() {
		closeBtn = new JButton("×");
		buttonPanel = new ButtonPanel();
		tittleBarPanel = new TittleBarPanel();
		infoPanel = new TittleBarPanel();
		infoText = new JTextPane();
		
		infoText.setText("  HELOO"
				+ "\n  IAM DVTALK"
				+ "\n  YOU CAN REACH ME AT DVTALK.ME");
		
		closeBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
				releasedColored(closeBtn);
			}
			public void mousePressed(MouseEvent arg0) {
				selectedColored(closeBtn);
			}
			public void mouseExited(MouseEvent arg0) {
				releasedColored(closeBtn);
			}
			public void mouseEntered(MouseEvent arg0) {
				selectedColored(closeBtn);
			}
			public void mouseClicked(MouseEvent arg0) {
				selectedColored(closeBtn);
				dispose();
			}
		});
		
		layoutConfigure();
	}
	
	private void layoutConfigure() {
        // remove window default title bar
		setUndecorated(true);
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(tittleBarPanel);
		add(infoPanel);

		tittleBarPanel.addComponent(buttonPanel, BorderLayout.EAST);
		buttonPanel.addComponent(closeBtn);
		infoPanel.add(infoText);

		
		setSize(550, 120);
		tittleBarPanel.setPreferredSize(new Dimension(this.getWidth(), 10));
		
		// The mouse listener and mouse motion listener we add here is to simply
		// make our frame draggable.
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                point.x = e.getX();
                point.y = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
                repaint();
            }
        });
        
		//border
        getRootPane().setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.LIGHT_GRAY));
        

		closeBtn.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));

	}
	
	public void setBackgroundColor(Color backgroundColor) {
		Color color = backgroundColor;
		int red = color.getRed();
		int blue = color.getBlue();
		int green = color.getGreen();
		int alpha = color.getAlpha();

		System.out.println(backgroundColor.toString());

		setBackground(new Color(red - 30, green - 30, blue - 30, 245 ));
		tittleBarPanel.setBackground(this.getBackground());
		buttonPanel.setBackground(this.getBackground());
		infoPanel.setBackground(this.getBackground());

		closeBtn.setBackground(this.getBackground());
		closeBtn.setForeground(new Color(red - 150, green - 150, blue - 150, 255));
		closeBtn.setBorder(BorderFactory.createLineBorder(this.getBackground(), 1, true));
		
		infoText.setBackground(this.getBackground());
		infoText.setForeground(new Color(red - 150, green - 150, blue - 150, 255));

		repaint();

	}
	
	public void selectedColored(JComponent component) {
		Color color = this.getBackground();
		int red = color.getRed();
		int blue = color.getBlue();
		int green = color.getGreen();
		int alpha = color.getAlpha();
		component.setBackground(new Color(red - 10, green - 10, blue -10, 200 ));
	}
	
	public void releasedColored(JComponent component) {
		Color color = this.getBackground();
		int red = color.getRed();
		int blue = color.getBlue();
		int green = color.getGreen();
		int alpha = color.getAlpha();
		component.setBackground(new Color(red, green, blue, alpha ));
	}
	
}
