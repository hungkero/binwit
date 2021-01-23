package bitwin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class MainFrame extends JFrame {
	private static Point point = new Point();
	
	private TextPanel mainText;
	private DataPanel dataPanel;
	private BitPanel  bitPanel;
	private TittleBarPanel tittleBarPanel;
	
	public MainFrame() {
		super("BinWit");
		
		DataManipulation datadetect = DataManipulation.getInst();

		mainText = new TextPanel();

		dataPanel = new DataPanel();

		bitPanel = new BitPanel(1);
		
		tittleBarPanel = new TittleBarPanel();
		
		tittleBarPanel.setMemHistMenuItemListener(new StringListener() {
			@Override
			public void textDetect(String text) {
				long rawDecData = datadetect.getRawDecData(text.replaceAll("\\s", ""));

				dataPanel.setRawDecData(rawDecData);
				bitPanel.setRawDecData(rawDecData);

				mainText.setText(text);
				tittleBarPanel.setCurrentOperation(text);
				repaint();
			}
		});

		bitPanel.setBitTableModified(new StringListener() {
			@Override
			public void textDetect(String text) {
				dataPanel.setRawDecData(Long.parseLong(text));
				mainText.setText("");
				repaint();
			}
		});
		
		mainText.setTypingDataString(new StringListener() {
			@Override
			public void textDetect(String text) {
				long rawDecData = datadetect.getRawDecData(text.replaceAll("\\s", ""));

				dataPanel.setRawDecData(rawDecData);
				bitPanel.setRawDecData(rawDecData);
				
				tittleBarPanel.setCurrentOperation(text);
				repaint();
			}
		});
		
		DataManipulation.getInst().setStringErrorListener(new StringListener() {
			@Override
			public void textDetect(String errorText) {
				mainText.setErrorText(errorText);
			}
		});

		
		// init
		dataPanel.setRawDecData(DataManipulation.getInst().getRawDecData("0+0"));
		
		layoutConfigure();
	}
	

	public void layoutConfigure() {

        ArrayList<BitPanel> lowerBitPanels = new ArrayList<BitPanel>();

        // misc
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // remove window default title bar
		setUndecorated(true);

		//border of sub-component
		tittleBarPanel.setBorder(BorderFactory.createEmptyBorder(8,8,3,8));
		mainText.setBorder(BorderFactory.createEmptyBorder(0,8,15,8));
		Border outerBorder = BorderFactory.createEmptyBorder(10,8,10,8);
		dataPanel.setBorder(outerBorder);
		bitPanel.setBorder(outerBorder);

		// color
		Color backgroundColor = new Color(224,235,235, 248);
		setBackground(backgroundColor);

		backgroundColor = new Color(224,235,235, 0);
		tittleBarPanel.setBackground(backgroundColor);
		mainText.setBackground(backgroundColor);
		dataPanel.setBackground(backgroundColor);
		bitPanel.setBackground(backgroundColor);
		
		tittleBarPanel.refreshColor();

		//Layout manager
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(tittleBarPanel);
		add(mainText);
		add(dataPanel);
		add(bitPanel);
		

		// size control
		tittleBarPanel.setPreferredSize(new Dimension(this.getWidth(), 20));
		mainText.setPreferredSize(new Dimension(this.getWidth(), 80));
		dataPanel.setPreferredSize(new Dimension(this.getWidth(), 80));
		bitPanel.setPreferredSize(new Dimension(this.getWidth(), 160));

		setSize(750, 120+120+180+lowerBitPanels.size()*180);

		bitPanel.setBitPanelLevelListener(new BitPanelUpdateListener() {
			@Override
			public void lowerBitPanelHandle(BitPanel bitPanel, boolean newAdd) {
				if (newAdd == true) {
					add(bitPanel);
					lowerBitPanels.add(bitPanel);
					bitPanel.setBorder(outerBorder);
					bitPanel.setBackground(new Color(1, 1, 1, 1));
				}
				else {
					int bitPanelLeveltoRemove = bitPanel.getBitPanelLevel();
					for (BitPanel bitPanel_itr: lowerBitPanels) {
						if (bitPanel_itr.getBitPanelLevel() >= bitPanelLeveltoRemove ) {
							remove(bitPanel_itr);
							revalidate();
						}
					}
					lowerBitPanels.removeIf(bitPanel_itr -> (bitPanel_itr.getBitPanelLevel() >= bitPanelLeveltoRemove)	);
				}
				Dimension dm = getSize();
				setSize(new Dimension(dm.width, 120+120+180+lowerBitPanels.size()*180));
				repaint();
			}
		});

		
		//border
        getRootPane().setBorder(BorderFactory.createMatteBorder(0,0,0,0, Color.LIGHT_GRAY));
		
		//set logo
		java.net.URL imageURL = MainFrame.class.getResource("dvtalk.png");
		if (imageURL != null) {
		    ImageIcon icon = new ImageIcon(imageURL);
		    setIconImage(icon.getImage());
		}
		else {
			System.out.println("NULL ICO");
		}
		
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
        setLocationRelativeTo(null); 

		
	}
}

