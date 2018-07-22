package com.braintobytes.utils;

import java.awt.Color;
import java.awt.Component;
import java.security.SecureRandom;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.braintobytes.entities.Box;
import com.braintobytes.entities.Cell;
import com.braintobytes.userinterface.BoxGUI;
import com.braintobytes.userinterface.UI;

public class Util {

	public static void updateDisplay(UI ui, Box[] boxes) {

		int row, col, index;
		JTextField tmpTextField;

		for (int i = 0; i < boxes.length; i++) {

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

	public static void diaplayLoadedGame(UI ui, Box[] boxes) {

		clearUI(ui);
		
		int row, col, index;
		JTextField tmpTextField;

		for (int i = 0; i < ui.getContentPanel().getComponentCount(); i++) {

			BoxGUI tmpBoxGui = ui.getBox(i);
			Box tmpBox = boxes[i];
			row = col = index = 0;

			while (row < 3) {

				tmpTextField = tmpBoxGui.getTextField(row, col++);

				if (tmpBox.getCell(index).getIsFinal()) {

					tmpTextField.setEditable(false);
					tmpTextField.setForeground(Color.RED);

				}

				if (tmpBox.getCell(index).getCurrentValue() != 0)
					tmpTextField.setText(tmpBox.getCell(index).getCurrentValue() + "");

				index++;
				if (col == 3) {
					row++;
					col = 0;
				}
			}

		}

	}

	protected static void initBoxes(UI ui, Box[] boxes, boolean saveGame) {

		for (int i = 0; i < ui.getContentPanel().getComponentCount(); i++) {
			boxes[i] = initIndividualBox(i, ui.getBox(i), saveGame);
		}

	}

	private static Box initIndividualBox(int index, BoxGUI gui, boolean saveGame) {

		Box result = new Box(index);
		boolean isFinalValue;
		int initialRow = InitialRow(index);
		int initialCol = InitialCol(index);
		int row = initialRow;
		int col = initialCol;
		int val;
		JTextField textTmp;

		for (int j = 0; j < gui.getComponentCount(); j++) {

			textTmp = (JTextField) gui.getComponent(j);

			if ("".equals(textTmp.getText())) {

				val = 0;
				isFinalValue = false;
				
			} else {

				val = Integer.parseInt(textTmp.getText());
				isFinalValue = true;
				
			}

			if (saveGame && !textTmp.getForeground().equals(Color.RED)) {

				isFinalValue = false;

			}
			
			result.addCell(new Cell(row, col++, val, index, isFinalValue), j);

			if (col == initialCol + 3) {
				col = initialCol;
				row++;
			}
		}

		return result;
	}

	public static boolean checkValidPuzzle(UI ui) {
		return checkBoxes(ui) && checkRows(ui) && checkColumns(ui);
	}

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
			// e.printStackTrace();
			System.out.println("Exception in checkColumns");
			return false;
		}

		return true;

	}

	public static void generateRandomPuzzle(UI ui) {

		boolean solveable = false;
		Solver solver = new Solver(ui);

		while (!solveable) {

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

			if (solver.solve(false))
				solveable = true;

		}

		solver = null;

	}

	public static void clearUI(UI ui) {

		JPanel mainPanel = ui.getContentPanel();

		for (Component gui : mainPanel.getComponents()) {

			((BoxGUI) gui).clearBox();

		}

	}

	private static int InitialRow(int BoxID) {
		switch (BoxID) {

		case 0:
		case 1:
		case 2:
			return 0;
		case 3:
		case 4:
		case 5:
			return 3;
		case 6:
		case 7:
		case 8:
			return 6;
		}
		return -1;
	}

	private static int InitialCol(int BoxID) {

		switch (BoxID) {

		case 0:
		case 3:
		case 6:
			return 0;
		case 1:
		case 4:
		case 7:
			return 3;
		case 2:
		case 5:
		case 8:
			return 6;
		}
		return -1;
	}

	// // for testing
	// private static void printRow(Solver solver, int row) {
	//
	// System.out.println("Row " + row + ":");
	//
	// int[] rows = solver.getRow(row);
	// for (int i = 0; i < 9; i++) {
	// System.out.print(i + " ");
	// }
	//
	// System.out.println();
	//
	// for (int v : rows) {
	// System.out.print(v + " ");
	// }
	//
	// System.out.println();
	//
	// }
	//
	// // for testing
	// private static void printColumn(Solver solver, int col) {
	//
	// System.out.println("Coumn " + col + ":");
	//
	// int[] cols = solver.getColumn(col);
	// for (int i = 0; i < 9; i++) {
	// System.out.print(i + " ");
	// }
	//
	// System.out.println();
	//
	// for (int v : cols) {
	// System.out.print(v + " ");
	// }
	//
	// System.out.println();
	//
	// }

}
