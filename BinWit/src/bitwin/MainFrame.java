package bitwin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.border.Border;

import com.oracle.truffle.object.ShapeBasic;

public class MainFrame extends JFrame {
	private static Point point = new Point();
	
	private TextPanel mainText;
	private DataPanel dataPanel;
	private BitPanel  bitPanel;
	
	public MainFrame() {
		super("BinWit");
		
		mainText = new TextPanel();

		dataPanel = new DataPanel();

		bitPanel = new BitPanel(1);
		
		bitPanel.setBitTableModified(new StringListener() {
			@Override
			public void textDetect(String text) {
				dataPanel.setRawDecData(Long.parseLong(text));
				mainText.setText("");
			}
		});
		

		mainText.setFinalStringListener(new StringListener() {
			@Override
			public void textDetect(String text) {
			}
		});

		mainText.setTypingDataString(new StringListener() {
			@Override
			public void textDetect(String text) {
				dataPanel.setRawDecData(Long.parseLong(text));
				bitPanel.setRawDecData(Long.parseLong(text));
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

		setUndecorated(true);

		//Layout manager
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		mainText.setPreferredSize(new Dimension(this.getWidth(), 80));
		dataPanel.setPreferredSize(new Dimension(this.getWidth(), 80));
		bitPanel.setPreferredSize(new Dimension(this.getWidth(), 160));

		add(mainText);
		add(dataPanel);
		add(bitPanel);
		
		Border outerBorder = BorderFactory.createEmptyBorder(10,8,10,8);
		mainText.setBorder(BorderFactory.createEmptyBorder(15,8,15,8));
		dataPanel.setBorder(outerBorder);
		bitPanel.setBorder(outerBorder);

		Color backgroundColor = new Color(224,235,235, 248);
		setBackground(backgroundColor);

		backgroundColor = new Color(224,235,235, 0);
		mainText.setBackground(backgroundColor);
		dataPanel.setBackground(backgroundColor);
		bitPanel.setBackground(backgroundColor);

		setSize(700, 120+120+180+lowerBitPanels.size()*180);
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

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
            }
        });
        setLocationRelativeTo(null); 

		
	}
}

