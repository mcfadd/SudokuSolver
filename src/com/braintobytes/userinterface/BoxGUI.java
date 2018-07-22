package com.braintobytes.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.braintobytes.utils.Util;


@SuppressWarnings("serial")
public class BoxGUI extends JPanel {
	
	private JTextField[][] textfields = new JTextField[3][3];
	private Font font = new Font("TimesRoman", Font.PLAIN, 50);
	private Color panelColor = new Color(245, 245, 245);
	private Color selectedColor = new Color(225, 225, 225);
	
	private UI parent;
		
	public BoxGUI(UI parent) {
		this.parent = parent;
		init();	
	}
	
	private void init() {
		
		this.setLayout(new GridLayout(3,3));
		setTextFields();
		this.setVisible(true);
	}
	
	private void setTextFields() {
		
		int row = 0;
		int col = 0;
		int i = 0;
		while(row < 3) {
			
			textfields[row][col] = new JTextField("", 1);
			textfields[row][col].setHorizontalAlignment(JTextField.CENTER);
			textfields[row][col].setFont(font);
			textfields[row][col].setDocument( new JTextFieldLimit(1));
			textfields[row][col].setBackground(panelColor);
			textfields[row][col].addFocusListener(new FocusListener() {
				
				@Override
				public void focusGained(FocusEvent e) { 
					((JTextField) e.getSource()).setBackground(selectedColor);
				}

				@Override
				public void focusLost(FocusEvent e) {
					
					JTextField field = (JTextField) e.getSource();
					if(!(field).getText().equals("") && !Util.checkValidPuzzle(parent)) {
			
						field.setText("");
					}
					
					field.setBackground(panelColor);
					
				}
			});
			this.add(textfields[row][col], i++);
			col++;
			if(col == 3) {
				row++;
				col = 0;
			}
			
			
		}
	
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 200);	
	}
	
	public JTextField getTextField(int row, int col) {
		return textfields[row][col];
	}
	
	public void clearBox() {
		
		for(JTextField[] fields : textfields) {
			
			for(JTextField j : fields) {
				j.setEditable(true);
				j.setText("");
				j.setForeground(Color.BLACK);
				
			}
			
		}
	}
	
	/**
	 * {@link https://stackoverflow.com/questions/3519151/how-to-limit-the-number-of-characters-in-jtextfield}
	 * 
	 */
	private class JTextFieldLimit extends PlainDocument {
	  private int limit;

	  JTextFieldLimit(int limit) {
	   this.limit = limit;
	   }

	  public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
	    if (str == null) return;

	    if ((getLength() + str.length()) <= limit) {
	    	if(str.equals("1") || str.equals("2") || str.equals("3") || str.equals("4") ||
	    			str.equals("5") || str.equals("6") || str.equals("7") ||
	    			str.equals("8") || str.equals("9"))
	    		
	    		super.insertString(offset, str, attr);
	    }
	  }
	}
	
}
