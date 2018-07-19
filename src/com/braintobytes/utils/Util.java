package com.braintobytes.utils;

import java.awt.Color;
import java.awt.Component;
import java.security.SecureRandom;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.braintobytes.entities.Box;
import com.braintobytes.userinterface.BoxGUI;
import com.braintobytes.userinterface.UI;

public class Util {

	private static Solver solver;
	
	/**
	 * 
	 * @param ui
	 * @param boxes
	 */
	public static void updateDisplay(UI ui, Box[] boxes) {

		int row, col, index;
		JTextField tmpTextField;

		for (int i = 0; i < ui.getPanel().getComponentCount(); i++) {

			BoxGUI tmpBoxGui = ui.getBox(i);
			Box tmpBox = boxes[i];
			row = col = index = 0;

			while (row < 3) {

				tmpTextField = tmpBoxGui.getTextField(row, col++);
				tmpTextField.setEditable(false);

				if (!tmpBox.getCell(index).getIsFinal()) {

					tmpTextField.setText(tmpBox.getCell(index).getCurrentValue() + "");
					tmpTextField.setForeground(Color.RED);

				}
				index++;
				if (col == 3) {
					row++;
					col = 0;
				}
			}

		}

	}

	public static boolean checkValidPuzzle(UI ui) {
		return checkBoxes(ui) && checkRows(ui) && checkColumns(ui);
	}
	
	
	/**Checking matrix
	 * 
	 * @param ui
	 * @return
	 */
	private static boolean checkBoxes(UI ui) {

		int[] valuesInBox = new int[9];
		int boxIndex, value;
		boxIndex = 0;
		String val;
		BoxGUI box;

		try {

			while (boxIndex < 9) {

				box = ui.getBox(boxIndex++);

				Component[] temp = box.getComponents();

				for (int i = 0; i < temp.length; i++) {

					val = ((JTextField) temp[i]).getText();

					if (!val.trim().equals("")) {
						value = Integer.parseInt(val);

						if (valuesInBox[value - 1] == 1)
							return false;
						else
							valuesInBox[value - 1] = 1;
					}

				}

				valuesInBox = new int[9];
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in checkBoxes");
			return false;
		}

		return true;
	}
	
	/**Check rows
	 * 
	 * @param ui
	 * @return
	 */
	private static boolean checkRows(UI ui) {

		int[] valuesInRow = new int[9];

		int boxIndex, row, cnt, col, value;
		boxIndex = row = col = cnt = 0;
		String val;
		BoxGUI box;

		try {

			while (cnt < 9) {

				box = ui.getBox(boxIndex++);

				while (col < 3) {

					val = box.getTextField(row, col++).getText();

					if (!val.equals("")) {
						value = Integer.parseInt(val);

						if (valuesInRow[value - 1] == 1)
							return false;
						else
							valuesInRow[value - 1] = 1;
					}
				}

				col = 0;

				if (boxIndex == 3 || boxIndex == 6 || boxIndex == 9) {
					boxIndex = cnt;
					row++;
					valuesInRow = new int[9];
				}

				if (row == 3) {
					row = 0;
					boxIndex = cnt + 3;
					cnt = cnt + 3;
				}

			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Exception in checkRows");
			return false;
		}

		return true;
	}

	private static boolean checkColumns(UI ui) {

		int[] valuesInCol = new int[9];

		int boxIndex, row, cnt, col, value;
		boxIndex = row = col = cnt = 0;
		String val;
		BoxGUI box;

		try {

			while (cnt < 3) {

				box = ui.getBox(boxIndex);
				boxIndex = boxIndex + 3;

				while (row < 3) {
					val = box.getTextField(row++, col).getText();

					if (!val.trim().equals("")) {
						value = Integer.parseInt(val);

						if (valuesInCol[value - 1] == 1)
							return false;
						else {
							valuesInCol[value - 1] = 1;
							;
						}
					}

				}

				row = 0;

				if (boxIndex == 9 || boxIndex == 10 || boxIndex == 11) {
					boxIndex = cnt;
					col++;
					valuesInCol = new int[9];
				}

				if (col == 3) {
					col = 0;
					boxIndex = cnt + 1;
					cnt = cnt + 1;
				}

			}

		} catch (Exception e) {
			System.out.println("Exception in checkColumns");
			return false;
		}

		return true;
	}
	
	/**Generates the puzzle
	 * 
	 * @param ui
	 */

	public static void generateRandomPuzzle(UI ui) {
		
			clearUI(ui);

			SecureRandom random = new SecureRandom();

			int filled = ui.getGenCnt();
			int num = 0;
			int Box_Xindex, Box_Yindex;
			int n;

			while (num < filled) {

				random = new SecureRandom();
				BoxGUI boxGui = ui.getBox(random.nextInt(9));
				Box_Xindex = random.nextInt(3);
				Box_Yindex = random.nextInt(3);
				JTextField tf = boxGui.getTextField(Box_Xindex, Box_Yindex);

				if (tf.getText().equals("")) {

					tf.setText(Integer.toString(random.nextInt(9) + 1));

					if (checkValidPuzzle(ui)) {

						tf.setEditable(false);
						tf.setForeground(Color.RED);
						num++;

					} else {

						tf.setText("");

					}
				}

			}
	}
	
	
	/**Clear UI
	 * 
	 * @param ui
	 */
	public static void clearUI(UI ui) {

		JPanel mainPanel = ui.getPanel();

		for (Component gui : mainPanel.getComponents()) {

			((BoxGUI) gui).clearBox();

		}

	}
}