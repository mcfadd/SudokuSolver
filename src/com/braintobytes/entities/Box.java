package com.braintobytes.entities;

import java.io.Serializable;


public class Box implements Serializable {

	private Cell[] cells;
	private int ID;
	
	public Box(int id) {
		
		this.ID = id;
		cells = new Cell[9];
	}
	
	public void addCell(Cell cell, int index) {
		cells[index] = cell;
	}
	
	public Cell getCell(int index) {
		return cells[index];
	}
	
	// returns an array of integers where a 1 indicates that index value is in the Box
	public int[] getValuesInBox() {
		
		int[] result = new int[9];
		
		for(Cell c: cells) {
			
			if(c.getCurrentValue() != 0)
				result[c.getCurrentValue() - 1] = 1;
				
		}
		
		return result;
	}
	
	// only accepts values in range [0 ,3)
	public int[] getValuesInRow(int row) {
		
		int[] result = new int[9];
		int index, initialIndex, valOfCell;
		if(row == 0)
			index = initialIndex = 0;
		else if(row == 1)
			index = initialIndex = 3;
		else
			index = initialIndex = 6;
		
		while(index < initialIndex + 3) {
			
			valOfCell = cells[index++].getCurrentValue();
			
			if(valOfCell != 0)
				result[valOfCell -1] = 1;
		}

		return result;
	}
	
	// only accepts values in range [0 ,3)
	public int[] getValuesInColumn(int col) {
		
		int[] result = new int[9];
		int valOfCell;
		
		while(col < 9) {
			
			valOfCell = cells[col].getCurrentValue();
			
			if(valOfCell != 0)
				result[valOfCell -1] = 1;
			
			col = col + 3;
		}

		return result;
	}
	
	// for testing
	public void printBox() {
		
		System.out.println("Box: " + ID);
		int spaces = 0;
		for(Cell c: cells) {
			System.out.print(c.getCurrentValue() + ", ");
			spaces++;
			if(spaces == 3) {
				spaces = 0;
				System.out.println();
			}
		}
		
		System.out.println();
	}
	
	// for testing
	public void printArrayOfValuesInBox() {
		
		System.out.println("Available Values: ");
		
		for(int i = 0; i < 9; i++)
			System.out.print(i + " ");
		
		System.out.println();
		
		for(int val: getValuesInBox()) 
			System.out.print(val + " ");
		
		System.out.println("\n");
	}
	
}
