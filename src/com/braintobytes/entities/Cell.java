package com.braintobytes.entities;

public class Cell {

	private int row, column, currentValue, BoxID;
	private int[] testedValues = new int[9];	// if 1 at index i, then i has been tested. cal
	private boolean isFinal;
	private Cell next, prev;
	
	/**
	 * @param row
	 * @param col
	 * @param val
	 * @param BoxID
	 * @param isFinal
	 */
	public Cell(int row, int col, int val, int BoxID, boolean isFinal){
		
		this.row = row;
		this.column = col;
		this.BoxID = BoxID;
		this.isFinal = isFinal;
		setCurrentValue(val);
	}
	
	/**
	 * @return
	 */
	public int getCurrentValue() {
		return currentValue;
	}
	
	/**
	 * @param val
	 */
	public void setCurrentValue(int val) {
		
		currentValue = val;
		if(currentValue != 0)
			testedValues[val - 1] = 1;
	}
	
	/**
	 * 
	 */
	public void clearTestedValues() {
		testedValues = new int[9];
	}
	
	/**
	 * @return
	 */
	public int[] getTestedValues() {
		return testedValues;
	}
	
	/**
	 * @return
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * @return
	 */
	public int getBoxID() {
		return BoxID;
	}
	
	/**
	 * @return
	 */
	public Cell getNext() {
		return next;
	}
	
	/**
	 * @param c
	 */
	public void setNext(Cell c) {
		next = c;
	}
	
	/**
	 * @return
	 */
	public Cell getPrev() {
		return prev;
	}
	
	/**
	 * @param c
	 */
	public void setPrev(Cell c) {
		prev = c;
	}
	
	/**
	 * @return
	 */
	public int getNextValueToTest() {
		
		for(int i = 0; i < testedValues.length; i++) {
			
			if(testedValues[i] == 0) {
				return i + 1;
			}
		}
		
		return 0;
	}
	
	/**
	 * @return
	 */
	public boolean getIsFinal() {
		return isFinal;
	}
	
}
