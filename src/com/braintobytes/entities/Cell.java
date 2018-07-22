package com.braintobytes.entities;

import java.io.Serializable;

public class Cell implements Serializable {

	private int row, column, currentValue, BoxID;
	private int[] testedValues = new int[9];	// if 1 at index i, then i has been tested. cal
	private boolean isFinal;
	private Cell next, prev;
	
	public Cell(int row, int col, int val, int BoxID, boolean isFinal) {
		
		this.row = row;
		this.column = col;
		this.BoxID = BoxID;
		this.isFinal = isFinal;
		setCurrentValue(val);
	}
	
	public int getCurrentValue() {
		return currentValue;
	}
	
	public void setCurrentValue(int val) {
		
		currentValue = val;
		if(currentValue != 0)
			testedValues[val - 1] = 1;
	}
	
	public void clearTestedValues() {
		testedValues = new int[9];
	}
	
	public int[] getTestedValues() {
		return testedValues;
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	public int getBoxID() {
		return BoxID;
	}
	
	public Cell getNext() {
		return next;
	}
	
	public void setNext(Cell c) {
		next = c;
	}
	
	public Cell getPrev() {
		return prev;
	}
	
	public void setPrev(Cell c) {
		prev = c;
	}
	
	public int getNextValueToTest() {
		
		for(int i = 0; i < testedValues.length; i++) {
			
			if(testedValues[i] == 0) {
				return i + 1;
			}
		}
		
		return 0;
	}
	
	public boolean getIsFinal() {
		return isFinal;
	}
	
}
