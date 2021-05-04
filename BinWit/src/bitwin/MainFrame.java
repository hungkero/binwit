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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	private boolean multipleDWordEn = false;
	
	//scroll pane
	JScrollPane scrollPane;

	public MainFrame() {
		super("BinWit");
		
		
		DataManipulation datadetect = DataManipulation.getInst();

		tittleBarPanel = new TittleBarPanel();

		tittleBarPanel.setMemHistMenuItemListener(new StringListener() {
			@Override
			public void textDetect(String text) {
				long rawDecData = datadetect.getRawDecData(text.replaceAll("\s", ""));

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
					multipleDWordEn = true;
					updateLayout();
				}
				else {
					multipleDWordEn = false;
					updateLayout();
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
		for (int i = 0; i<11; i++) {
			dWordItemList.add(new DWordItem(i));
		}
		

		// init
		dataPanel.setRawDecData(DataManipulation.getInst().getRawDecData("0+0"));
		
		layoutConfigure();
	}

	private void updateLayout() {
		if (this.multipleDWordEn == true) {
			mainText.setVisible(false);
			dataPanel.setVisible(false);
			bitPanel.setVisible(false);
			lowerBitPanels.forEach(bitpanel -> bitpanel.setVisible(false));
			 
			scrollPane.setVisible(true);
			dWordItemList.forEach(dWItem -> dWItem.setVisible(true));
		}
		else {
			mainText.setVisible(true);
			dataPanel.setVisible(true);
			bitPanel.setVisible(true);
			if (lowerBitPanels.size() > 0) {
				scrollPane.setVisible(true);
				lowerBitPanels.forEach(bitpanel -> bitpanel.setVisible(true));
			}
			else {
				scrollPane.setVisible(false);
			}
			
			dWordItemList.forEach(dWItem -> dWItem.setVisible(false));
		}

		configureSize();

//		repaint();

	}

	public void layoutConfigure() {
        // remove window default title bar
		setUndecorated(true);

		//border of sub-component
		tittleBarPanel.setBorder(BorderFactory.createEmptyBorder(8,8,3,8));
		mainText.setBorder(BorderFactory.createEmptyBorder(0,8,15,8));
		Border outerBorder = BorderFactory.createEmptyBorder(10,8,10,8);
		dataPanel.setBorder(outerBorder);
		bitPanel.setBorder(outerBorder);
		dWordItemList.forEach(dWItem -> dWItem.setBorder(outerBorder));


		// color
		Color backgroundColor = new Color(224,235,235, 245);
		setBackground(backgroundColor);

		backgroundColor = new Color(224,235,235, 0);
		tittleBarPanel.setBackground(backgroundColor);
		mainText.setBackground(backgroundColor);
		dataPanel.setBackground(backgroundColor);
		bitPanel.setBackground(backgroundColor);
		dWordItemList.forEach(dWItem -> dWItem.setBackground(new Color(224,235,235, 0)));

		tittleBarPanel.refreshColor();


		//Layout manager 
		class ScrollPanel extends JPanel {
			public ScrollPanel() {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				dWordItemList.forEach(dWItem -> add(dWItem));
				class DWordItemListListener implements DWordItemListener {
					@Override
					public void textDetect(ArrayList<String> arrayList, int itemIndex) {
						if (arrayList.size() > (dWordItemList.size()-itemIndex)) {
							for (int i=dWordItemList.size(); i < (arrayList.size()+itemIndex); i++) {
								dWordItemList.add(new DWordItem(i));
								add(dWordItemList.get(i));
								dWordItemList.get(i).setBackground(new Color(224,235,235, 0));
								dWordItemList.get(i).setBorder(outerBorder);
								dWordItemList.get(i).setdWItemListListener(new DWordItemListListener());
							}
						}

						for (int i=0; i<arrayList.size(); i++) {
							DWordItem item = dWordItemList.get(i+itemIndex);
							long rawDecData = DataManipulation.getInst().getRawDecData(arrayList.get(i));
							item.setdWData(rawDecData);
						}

						updateLayout(); 
						updateUI();
						repaint();					
					}
				}
				dWordItemList.forEach(dWItem -> dWItem.setdWItemListListener(new DWordItemListListener()));
				
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
						
						updateLayout();
						updateUI();
						repaint();
					}
				});

				setBackground(new Color(224,235,235, 0));
				

			}
		}
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		scrollPane = new JScrollPane(new ScrollPanel());
		add(tittleBarPanel);
		add(mainText);
		add(dataPanel);
		add(bitPanel);
		add(scrollPane);
		
		//initial disable dWordItem
		dWordItemList.forEach(dWItem -> dWItem.setVisible(false));
		scrollPane.setVisible(false);
		
		 
		// size control
		configureSize();

		
        // misc
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
        // make our frame dragable.
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

	private void configureSize() {

		tittleBarPanel.setPreferredSize(new Dimension(this.getWidth(), 40));
		mainText.setPreferredSize(new Dimension(this.getWidth(), 90));
		dataPanel.setPreferredSize(new Dimension(this.getWidth(), 110));
		bitPanel.setPreferredSize(new Dimension(this.getWidth(), 180));
 
		//lowerBitPanels/dWordItem & scrollPane
		if (multipleDWordEn == true) {
			dWordItemList.forEach(dWItem -> dWItem.setPreferredSize(new Dimension(this.getWidth()-17, 130)));
			scrollPane.setPreferredSize(new Dimension(this.getWidth(), 5*135));
			setSize(800, 5*135 + 40);
		}
		else if (lowerBitPanels.size() > 0) {
			lowerBitPanels.forEach(bitPanelItr -> bitPanelItr.setPreferredSize(new Dimension(this.getWidth()-17, 180))); 
			scrollPane.setPreferredSize(new Dimension(this.getWidth(), 200));
			if (lowerBitPanels.size() > 1)	{
				scrollPane.setPreferredSize(new Dimension(this.getWidth(), 200*2));
				setSize( 800, 40+90+110+180+190*2);
			}
			else {
				setSize( 800, 40+90+110+180+190);
			}
		}
		else {
			scrollPane.setPreferredSize(new Dimension(this.getWidth(), 200));
			setSize(800, 40+90+110+180);
		}
	}
}

