package userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.SaveGame;
import utils.Solver;
import utils.Util;


@SuppressWarnings("serial")
public class UI extends JFrame {

	// puzzle menu
	private JPanel contentPanel;
	private GridLayout contentPanelLayout;
	private JPanel contentButtonPnl;
	private JButton startButton, clearButton, genPuzzleButton, saveButton, loadButton, cancelButton, removeButton, loadPuzzleButton;
	private JSlider slider;
	private JLabel genCount;
	private int genCnt = 15;
	
	// loading menu
	private JPanel loadButtonPnl;
	private JPanel loadPanel;
	private JList<String> loadlist;
	private JScrollPane loadMenuScrollPane;
	
	private JPanel mainPanel;
	private UI thisUI = this;
	private Solver solver;

	private Font btnFont = new Font("TimesRoman", Font.BOLD, 20);

	public UI() {

		super("Sudoku");
		init();
		this.setVisible(true);

	}

	private void init() {

		solver = new Solver(this);

		startButton = new JButton("Solve");
		clearButton = new JButton("Clear");
		genPuzzleButton = new JButton("Generate Random Puzzle");
		saveButton = new JButton("Save");
		loadButton = new JButton("Load");

		genCount = new JLabel("15");
		slider = new JSlider(JSlider.HORIZONTAL, 5, 30, 15);

		loadPanel = new JPanel();
		loadMenuScrollPane = new JScrollPane();
		contentPanel = new JPanel();
		contentPanelLayout = new GridLayout(3, 3);
		loadButtonPnl = new JPanel();
		mainPanel = new JPanel();

		setUp();

		JPanel buttonPnl1 = new JPanel(new FlowLayout());
		JPanel buttonPnl2 = new JPanel(new FlowLayout());
		contentButtonPnl = new JPanel();

		contentButtonPnl.setLayout(new BoxLayout(contentButtonPnl, BoxLayout.Y_AXIS));

		buttonPnl2.add(startButton);
		buttonPnl2.add(Box.createHorizontalStrut(8));
		buttonPnl2.add(clearButton);
		buttonPnl2.add(Box.createHorizontalStrut(8));
		buttonPnl2.add(loadButton);
		buttonPnl2.add(Box.createHorizontalStrut(8));
		buttonPnl2.add(saveButton);

		buttonPnl1.add(genPuzzleButton);
		buttonPnl1.add(Box.createHorizontalStrut(8));
		buttonPnl1.add(genCount);
		buttonPnl1.add(Box.createHorizontalStrut(8));
		buttonPnl1.add(slider);

		contentButtonPnl.add(buttonPnl1);
		contentButtonPnl.add(buttonPnl2);

		mainPanel.add(contentButtonPnl, BorderLayout.SOUTH);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		this.add(mainPanel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(500, 500));
		this.setResizable(false);

		try {
			URL resource = getClass().getResource(".." + System.getProperty("file.separator") + "Sudoku(icon).png");
			BufferedImage image = ImageIO.read(resource);
			setIconImage(image);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setUp() {

		mainPanel.setLayout(new BorderLayout());

		initLoadPanel();

		genCount.setFont(btnFont);

		startButton.setFont(btnFont);
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				solver.solve(true);
			}

		});

		clearButton.setFont(btnFont);
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Util.clearUI(thisUI);
			}

		});

		genPuzzleButton.setFont(btnFont);
		genPuzzleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Util.generateRandomPuzzle(thisUI);
			}

		});

		saveButton.setFont(btnFont);
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String result = JOptionPane.showInputDialog(thisUI, "Name your saved game", "Save Game",
						JOptionPane.PLAIN_MESSAGE);

				if (result != null) {
					SaveGame.saveGame(thisUI, result);
					updateLoadlist();
				}
			}

		});

		loadButton.setFont(btnFont);
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (loadPanel == null)
					initLoadPanel();

				mainPanel.removeAll();
				mainPanel.add(loadPanel);
				mainPanel.add(loadButtonPnl, BorderLayout.SOUTH);
				thisUI.pack();

			}

		});

		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				genCnt = slider.getValue();
				genCount.setText(Integer.toString(slider.getValue()));
			}

		});

		contentPanelLayout.setHgap(5);
		contentPanelLayout.setVgap(5);
		contentPanel.setLayout(contentPanelLayout);
		contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
		contentPanel.setBackground(Color.DARK_GRAY);

		for (int i = 0; i < 9; i++) {
			contentPanel.add(new BoxGUI(this));
		}

	}

	public BoxGUI getBox(int index) {
		return (BoxGUI) contentPanel.getComponent(index);
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}

	public int getGenCnt() {
		return genCnt;
	}

	private void initLoadPanel() {

		cancelButton = new JButton("Cancel");
		loadPuzzleButton = new JButton("Load");
		removeButton = new JButton("Remove");

		JPanel pnl = new JPanel(new FlowLayout());
		
		loadMenuScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		cancelButton.setFont(btnFont);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				mainPanel.removeAll();
				mainPanel.add(contentPanel, BorderLayout.CENTER);
				mainPanel.add(contentButtonPnl, BorderLayout.SOUTH);
				thisUI.pack();

			}

		});

		loadPuzzleButton.setFont(btnFont);
		loadPuzzleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int i = loadlist.getSelectedIndex();

				if (i == -1)
					return;

				mainPanel.removeAll();
				mainPanel.add(contentPanel, BorderLayout.CENTER);
				mainPanel.add(contentButtonPnl, BorderLayout.SOUTH);
				
				Util.diaplayLoadedGame(thisUI,
						SaveGame.getSavedGames().get((String) loadlist.getModel().getElementAt(i)).getBoxes());
				
				thisUI.pack();

			}

		});

		removeButton.setFont(btnFont);
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int i = loadlist.getSelectedIndex();

				if (i == -1)
					return;

				SaveGame.removeGame((String) loadlist.getModel().getElementAt(i));
				updateLoadlist();

			}

		});

		pnl.add(loadPuzzleButton);
		pnl.add(Box.createHorizontalStrut(8));
		pnl.add(removeButton);
		pnl.add(Box.createHorizontalStrut(8));
		pnl.add(cancelButton);

		updateLoadlist();

		loadPanel.add(loadMenuScrollPane);
		loadButtonPnl.add(pnl);

	}

	private void updateLoadlist() {

		DefaultListModel<String> l1 = new DefaultListModel<>();

		for (String s : SaveGame.getSavedGames().keySet()) {
			l1.addElement(s);
		}

		loadlist = new JList<String>(l1);
		loadlist.setFont(new Font("TimesRoman", Font.BOLD, 30));
		loadlist.setFixedCellWidth(400);
		loadMenuScrollPane.setViewportView(loadlist);
		loadPanel.repaint();

	}

//	public void keyPressed(KeyEvent e) {
//	    int keyCode = e.getKeyCode();
//	    switch( keyCode ) { 
//	        case KeyEvent.VK_UP:
//	            // handle up 
//	            break;
//	        case KeyEvent.VK_DOWN:
//	            // handle down 
//	            break;
//	        case KeyEvent.VK_LEFT:
//	            // handle left
//	            break;
//	        case KeyEvent.VK_RIGHT :
//	            // handle right
//	            break;
//	     }
//	} 

//	public Cell getCell(int row, int col) {
//		
//		
//		
//	}

}
