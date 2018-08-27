package utils;

import javax.swing.JOptionPane;

import entities.Box;
import entities.Cell;
import userinterface.UI;

public class Solver {

	private UI ui;
	private Cell currentCell;
	private Box[] boxes;
	private boolean movingForward = true;
	
	public Solver(UI ui) {
		
		this.ui = ui;
		this.boxes = new Box[9];
	}
	
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
	
	private void init() {
		
		Util.initBoxes(ui, boxes, false);
		linkCells();
		this.currentCell = boxes[0].getCell(0);
	}

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
	
	

}
