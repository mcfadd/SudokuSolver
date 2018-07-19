package com.braintobytes.utils;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.braintobytes.entities.Box;
import com.braintobytes.entities.Cell;
import com.braintobytes.userinterface.BoxGUI;
import com.braintobytes.userinterface.UI;

public class Solver {

	private UI ui;
	private Cell currentCell;
	private Box[] boxes;
	private boolean movingForward = true;
	
	/**
	 * @param ui
	 */
	public Solver(UI ui) {
		
		this.ui = ui;
		this.boxes = new Box[9];
	}
	
	/**
	 * @param display
	 * @return
	 */
	public boolean solve(boolean display) {
		
		init();
		
		while(currentCell != null) {
			
			if(currentCell.getIsFinal()) {
				
				if(movingForward)
					currentCell = currentCell.getNext();
				else
					currentCell = currentCell.getPrev();
				
			}else{
				
				tryNextValue();
			}

		}
		
		if(movingForward) {
			
			//solved puzzle
			if(display)
				Util.updateDisplay(ui, boxes);
			return true;
		
		}else {
			
			// Unsolved puzzle
			if(display)
				JOptionPane.showMessageDialog(ui, "Oops, it looks like this puzzle is unsolvable");
			return false;
		}
		
	}
	
	/**
	 * 
	 */
	private void tryNextValue() {
		
		int[] testedValues, valuesInBox, valuesInRow, valuesInCol;
		testedValues = currentCell.getTestedValues();
		valuesInBox = boxes[currentCell.getBoxID()].getValuesInBox();
		valuesInRow = getRow(currentCell.getRow());
		valuesInCol = getColumn(currentCell.getColumn());
		
		for(int i = 0; i < 9; i++) {
			
			if(testedValues[i] == 0 && valuesInBox[i] == 0 && valuesInRow[i] == 0 && valuesInCol[i] == 0) {
				currentCell.setCurrentValue(i + 1);
				movingForward = true;
				currentCell = currentCell.getNext();
				return;
			}
					
		}
		
		currentCell.clearTestedValues();
		currentCell.setCurrentValue(0);
		movingForward = false;
		currentCell = currentCell.getPrev();
		
	}
	
	/**
	 * @param row
	 * @return
	 */
	private int[] getRow(int row) {

		int[] result, first, second, third;
		int BoxID;
		result = new int[9];

		if (row < 3)
			BoxID = 0;
		else if (row < 6)
			BoxID = 3;
		else
			BoxID = 6;

		first = boxes[BoxID++].getValuesInRow(row % 3);
		second = boxes[BoxID++].getValuesInRow(row % 3);
		third = boxes[BoxID].getValuesInRow(row % 3);

		for (int i = 0; i < result.length; i++) {
			if (first[i] == 1 || second[i] == 1 || third[i] == 1)
				result[i] = 1;
		}

		return result;

	}

	/**
	 * @param col
	 * @return
	 */
	private int[] getColumn(int col) {

		int[] result, first, second, third;
		int BoxID;
		result = new int[9];

		if (col < 3)
			BoxID = 0;
		else if (col < 6)
			BoxID = 1;
		else
			BoxID = 2;

		first = boxes[BoxID].getValuesInColumn(col % 3);
		BoxID += 3;
		second = boxes[BoxID].getValuesInColumn(col % 3);
		BoxID += 3;
		third = boxes[BoxID].getValuesInColumn(col % 3);

		for (int i = 0; i < result.length; i++) {
			if (first[i] == 1 || second[i] == 1 || third[i] == 1)
				result[i] = 1;
		}

		return result;

	}
	
	/**
	 * 
	 */
	private void init() {
		
		initBoxes(ui);
		linkCells();
		this.currentCell = boxes[0].getCell(0);
	}
	
	/**
	 * @param ui
	 */
	private void initBoxes(UI ui) {

		for (int i = 0; i < ui.getPanel().getComponentCount(); i++) {
			boxes[i] = initIndividualBox(i, ui.getBox(i));
		}

	}

	/**
	 * @param index
	 * @param gui
	 * @return
	 */
	private Box initIndividualBox(int index, BoxGUI gui) {

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

			result.addCell(new Cell(row, col++, val, index, isFinalValue), j);

			if (col == initialCol + 3) {
				col = initialCol;
				row++;
			}
		}

		return result;
	}

	/**
	 * 
	 */
	private void linkCells() {

		int BoxID = 0;
		int cellIndex, lag;
		cellIndex = lag = 0;
		Cell cur, prev;

		cur = boxes[BoxID].getCell(cellIndex++);
		// set head Cell's prev to null
		cur.setPrev(null);

		while (BoxID < boxes.length) {

			cur = boxes[BoxID].getCell(cellIndex++);
			prev = boxes[BoxID].getCell(lag++);

			prev.setNext(cur);
			cur.setPrev(prev);

			if (cellIndex == 9) {

				if (BoxID == 8)
					break;

				cellIndex = 0;
				boxes[BoxID].getCell(lag).setNext(boxes[BoxID + 1].getCell(cellIndex));
				boxes[BoxID + 1].getCell(cellIndex++).setPrev(boxes[BoxID++].getCell(lag));

				lag = 0;
			}

		}

		// set tail Cell's next to null
		boxes[8].getCell(8).setNext(null);

	}
	
	/**
	 * @param BoxID
	 * @return
	 */
	private int InitialRow(int BoxID) {
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

	/**
	 * @param BoxID
	 * @return
	 */
	private int InitialCol(int BoxID) {

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

}
