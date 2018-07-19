package com.braintobytes.userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.braintobytes.utils.Solver;
import com.braintobytes.utils.Util;

@SuppressWarnings("serial")
public class UI extends JFrame {
	
	private JPanel mainPanel;
	private GridLayout mainPanelLayout;
	private JButton start, clear, random;
	private UI thisUI = this;
	private int genCnt = 15;
	private JSlider slider;
	private JLabel cnt;
	private Solver solver;
	
	/**
	 * 
	 */
	public UI() {
		super("Sudoku");
		init();
		this.setVisible(true);
	}
	
	/**
	 * 
	 */
	private void init() {
		
		solver = new Solver(this);
		
		start = new JButton("Solve");
		clear = new JButton("Clear");
		random = new JButton("Generate Random Puzzle");
		
		cnt = new JLabel("15");
		slider = new JSlider(JSlider.HORIZONTAL, 5, 30, 15);
		
		mainPanel = new JPanel();
		mainPanelLayout = new GridLayout(3,3);
		
		setUp();
		
		JPanel buttonPnl1 = new JPanel(new FlowLayout());
		JPanel buttonPnl = new JPanel();
		buttonPnl.setLayout(new BoxLayout(buttonPnl, BoxLayout.Y_AXIS));
		
		buttonPnl1.add(start);
		buttonPnl1.add(Box.createHorizontalStrut(8));
		buttonPnl1.add(clear);
		buttonPnl1.add(Box.createHorizontalStrut(8));
		buttonPnl1.add(random);
		buttonPnl1.add(Box.createHorizontalStrut(8));
		buttonPnl1.add(cnt);
		buttonPnl1.add(Box.createHorizontalStrut(8));
		buttonPnl1.add(slider);
		
		buttonPnl.add(buttonPnl1);
		
		this.add(buttonPnl, BorderLayout.SOUTH);
		this.add(mainPanel, BorderLayout.CENTER);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
	}
	
	/**
	 * 
	 */
	private void setUp() {
		
		cnt.setFont(new Font("TimesRoman", Font.BOLD, 20));
		
		start.setFont(new Font("TimesRoman", Font.BOLD, 20));
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				solver.solve(true);
			}
			
		});
		
		clear.setFont(new Font("TimesRoman", Font.BOLD, 20));
		clear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Util.clearUI(thisUI);
			}
			
		});
		
		random.setFont(new Font("TimesRoman", Font.BOLD, 20));
		random.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
					Util.generateRandomPuzzle(thisUI);
			}
			
		});
		
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				
				genCnt = slider.getValue();
				cnt.setText(Integer.toString(slider.getValue()));
			}	
			
		});
		
		mainPanelLayout.setHgap(5);
		mainPanelLayout.setVgap(5);
		mainPanel.setLayout(mainPanelLayout);
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
		mainPanel.setBackground(Color.DARK_GRAY);
		
		for(int i = 0; i < 9; i++) {
			mainPanel.add(new BoxGUI(this));
		}
		
	}
	
	/**
	 * @param index
	 * @return
	 */
	public BoxGUI getBox(int index) {
		return (BoxGUI) mainPanel.getComponent(index);
	}
	
	/**
	 * @return
	 */
	public JPanel getPanel() {
		return mainPanel;
	}
	
	/**
	 * @return
	 */
	public int getGenCnt() {
		return genCnt;
	}
}