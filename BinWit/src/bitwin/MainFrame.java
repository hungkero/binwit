package bitwin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ItemListener;
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
	
	private TittleBarPanel tittleBarPanel;

	//bit calculator window
	private TextInputPanel mainText;
	private DataPanel dataPanel;
	private BitPanel  bitPanel;
	private ArrayList<BitPanel> lowerBitPanels = new ArrayList<BitPanel>();
	
	//multiple dword parsing window
	private ArrayList<DWordItem> dWordItemList = new ArrayList<DWordItem>();
	
	public MainFrame() {
		super("BinWit");
		
		DataManipulation datadetect = DataManipulation.getInst();

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
		
		tittleBarPanel.setMultipleDwordEnable(new StringListener() {
			@Override
			public void textDetect(String text) {
				if ( Integer.parseInt(text) == 1) {
					updateLayout(true);
				}
				else {
					updateLayout(false);
				}
			}
		});


		// Bit calculator window
		mainText = new TextInputPanel();

		dataPanel = new DataPanel();

		bitPanel = new BitPanel(1);
		
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


		// multiple dword parsing window
		for (int i = 0; i<8; i++) {
			dWordItemList.add(new DWordItem(i));
		}
		
		dWordItemList.forEach(dWItem -> dWItem.setdWItemListListener(new StringArrayListener() {
			@Override
			public void textDetect(ArrayList<String> arrayList) {
				dWordItemList.forEach(i -> i.setdWData(0));
				for (int i=0; i<arrayList.size(); i++) {
					DWordItem item = dWordItemList.get(i);
					long rawDecData = DataManipulation.getInst().getRawDecData(arrayList.get(i));
					item.setdWData(rawDecData);
				}
			}
		}));
		

		// init
		dataPanel.setRawDecData(DataManipulation.getInst().getRawDecData("0+0"));
		
		layoutConfigure();
	}

	
	private void updateLayout(boolean multipleDwordEn) {
		if (multipleDwordEn == true) {
			mainText.setVisible(false);
			dataPanel.setVisible(false);
			bitPanel.setVisible(false);
			lowerBitPanels.forEach(bitpanel -> bitpanel.setVisible(false));
			 
			dWordItemList.forEach(dWItem -> dWItem.setVisible(true));

			setSize(800, dWordItemList.size()*110 );
		}
		else {
			mainText.setVisible(true);
			dataPanel.setVisible(true);
			bitPanel.setVisible(true);
			lowerBitPanels.forEach(bitpanel -> bitpanel.setVisible(true));

			dWordItemList.forEach(dWItem -> dWItem.setVisible(false));

			setSize(800, 40+140+140+180+lowerBitPanels.size()*180);
		}

	}

	public void layoutConfigure() {
        // remove window default title bar
		setUndecorated(true);
		
		//initial disable dWordItem
		dWordItemList.forEach(dWItem -> dWItem.setVisible(false));

		//border of sub-component
		tittleBarPanel.setBorder(BorderFactory.createEmptyBorder(8,8,3,8));
		mainText.setBorder(BorderFactory.createEmptyBorder(0,8,15,8));
		Border outerBorder = BorderFactory.createEmptyBorder(10,8,10,8);
		dataPanel.setBorder(outerBorder);
		bitPanel.setBorder(outerBorder);

		dWordItemList.forEach(dWItem -> dWItem.setBorder(outerBorder));

		// color
		Color backgroundColor = new Color(224,235,235, 248);
		setBackground(backgroundColor);

		backgroundColor = new Color(224,235,235, 0);
		tittleBarPanel.setBackground(backgroundColor);
		mainText.setBackground(backgroundColor);
		dataPanel.setBackground(backgroundColor);
		bitPanel.setBackground(backgroundColor);
		dWordItemList.forEach(dWItem -> dWItem.setBackground(new Color(224,235,235, 0)));

		tittleBarPanel.refreshColor();


		//Layout manager
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(tittleBarPanel);
		add(mainText);
		add(dataPanel);
		add(bitPanel);
		dWordItemList.forEach(dWItem -> add(dWItem));
		
		
		// size control
		tittleBarPanel.setPreferredSize(new Dimension(this.getWidth(), 20));
		mainText.setPreferredSize(new Dimension(this.getWidth(), 80));
		dataPanel.setPreferredSize(new Dimension(this.getWidth(), 100));
		bitPanel.setPreferredSize(new Dimension(this.getWidth(), 180));

		setSize(800, 40+140+140+180+lowerBitPanels.size()*180);

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
				setSize(new Dimension(dm.width, 40+140+140+180+lowerBitPanels.size()*180));
				repaint();
			}
		});

		// size control for dWordItem
		dWordItemList.forEach(dWItem -> dWItem.setPreferredSize(new Dimension(this.getWidth(), 80)));
		
		
        // misc
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//
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

