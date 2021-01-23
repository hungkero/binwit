package bitwin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.plaf.MenuItemUI;

public class TittleBarPanel extends JPanel {
	private JButton closeBtn;
	private JMenuBar memHistMenuBar;
	private JMenu    memHistMenu;
	private JMenu    addToMemBtn;
	
	private String currentOperation;
	
	private StringListener memHistMenuItemListener;
	
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

	private ArrayList<MemHistItem> operationHistory;

	public TittleBarPanel() {
		operationHistory = new ArrayList<MemHistItem>();

		westPanel = new ButtonPanel();
		eastPanel = new ButtonPanel();

		closeBtn = new JButton("×");
		memHistMenuBar = new JMenuBar();
		addToMemBtn = new JMenu(" M+ ");
		memHistMenu = new JMenu(" M↓ ");
		
		memHistMenuBar.add(addToMemBtn);
		memHistMenuBar.add(memHistMenu);

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
				System.exit(0);
			}
		});
		
		addToMemBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
				releasedColored(addToMemBtn);
			}
			public void mousePressed(MouseEvent arg0) {
				selectedColored(addToMemBtn);
			}
			public void mouseExited(MouseEvent arg0) {
				releasedColored(addToMemBtn);
			}
			public void mouseEntered(MouseEvent arg0) {
				selectedColored(addToMemBtn);
			}
			public void mouseClicked(MouseEvent arg0) {
				selectedColored(addToMemBtn);
				if (currentOperation != null) {
					MemHistItem newmemHistItem = new MemHistItem(currentOperation);
					for (MemHistItem item_itr: operationHistory) {
						if (item_itr.compare(item_itr, newmemHistItem) == 0) {
							return; //duplicate item
						}
					}
					if (operationHistory.size() > 10) {
						operationHistory.remove(0);
					}
					operationHistory.add(newmemHistItem);
				}
				updateMemHist();
			}
		});

		memHistMenu.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				releasedColored(memHistMenu);
			}
			public void mousePressed(MouseEvent e) {
				selectedColored(memHistMenu);
			}
			public void mouseExited(MouseEvent e) {
				releasedColored(memHistMenu);
			}
			public void mouseEntered(MouseEvent e) {
				selectedColored(memHistMenu);
			}
			public void mouseClicked(MouseEvent e) {
				selectedColored(memHistMenu);
			}
		});

		layoutConfigure();
	}
	
	private void updateMemHist() {
		memHistMenu.removeAll();
		int i = 1;
		for (MemHistItem item_itr: operationHistory) {
			memHistMenu.add(item_itr);
			item_itr.setItemClickedStringListener(memHistMenuItemListener);
			item_itr.setText("$"+i+ ":  " + item_itr.getText());
		}
		memHistMenu.updateUI();
	}

	private void layoutConfigure() {
		setLayout(new BorderLayout(5,5));
		
		add(westPanel, BorderLayout.WEST);
		add(eastPanel, BorderLayout.EAST);
		westPanel.addComponent(memHistMenuBar);
		eastPanel.addComponent(closeBtn);
		
		addToMemBtn.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
		memHistMenu.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
		closeBtn.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		
		refreshColor();
		
	}
	
	public void refreshColor() {
		Color color = this.getBackground();
		
		int red = color.getRed();
		int blue = color.getBlue();
		int green = color.getGreen();
		int alpha = color.getAlpha();
		
		addToMemBtn.setOpaque(true);
		memHistMenu.setOpaque(true);
		closeBtn.setOpaque(true);
		
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
	
	public void selectedColored(JComponent component) {
		Color color = eastPanel.getBackground();
		int red = color.getRed();
		int blue = color.getBlue();
		int green = color.getGreen();
		int alpha = color.getAlpha();
		component.setBackground(new Color(red - 10, green - 10, blue -10, 200 ));
	}
	
	public void releasedColored(JComponent component) {
		Color color = eastPanel.getBackground();
		int red = color.getRed();
		int blue = color.getBlue();
		int green = color.getGreen();
		int alpha = color.getAlpha();
		component.setBackground(new Color(red, green, blue, alpha ));
	}
	

	public void setCurrentOperation(String currentOperation) {
		this.currentOperation = currentOperation;
	}

	public void setMemHistMenuItemListener(StringListener memHistMenuItemListener) {
		this.memHistMenuItemListener = memHistMenuItemListener;
	}
}