package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import entities.Box;
import userinterface.UI;

public class SaveGame implements Serializable {

	private static final long serialVersionUID = 1L;

	private static HashMap<String, SaveGame> savedGames = loadGamesfromFile();
	private static File saveFile;
	
	private String name;
	private Box[] boxes;
	
	private SaveGame(String name) {
		this.name = name;
		this.boxes = new Box[9];
	}

	public String getName() {
		return name;
	}

	public boolean setName(String name) {

		if (name == null)
			return false;

		savedGames.remove(this.name);
		this.name = name;
		savedGames.put(name, this);

		return true;
	}
	
	public Box[] getBoxes() {
		return boxes;
	}
	
	public static HashMap<String, SaveGame> getSavedGames() {
		return savedGames;
	}
	
	public static void removeGame(String name) {
		savedGames.remove(name);
		writeGamestoFile();
	}

	public static void saveGame(UI ui, String name) {

		if (ui == null || name == null)
			return;

		SaveGame savedGame = new SaveGame(name);
		Util.initBoxes(ui, savedGame.getBoxes(), true);
		savedGames.put(name, savedGame);
		writeGamestoFile();

	}
	
	public static void writeGamestoFile() {

		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		
		try {
			
			fout = new FileOutputStream(saveFile, false);
			oos = new ObjectOutputStream(fout);

			oos.writeObject(savedGames);
			oos.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	private static HashMap<String, SaveGame> loadGamesfromFile() {
		
		ObjectInputStream objectinputstream = null;
		HashMap<String, SaveGame> readCase = null;
		
		try {
			saveFile =  new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "savedGames");
			
//			System.out.println(System.getProperty("user.dir") + System.getProperty("file.separator") + "savedGames");
			if(!saveFile.exists())
				saveFile.createNewFile();
			
		    FileInputStream streamIn = new FileInputStream(saveFile);
		    objectinputstream = new ObjectInputStream(streamIn);
		    readCase = (HashMap<String, SaveGame>) objectinputstream.readObject();
		    objectinputstream .close();    
		    
		} catch (Exception e) {
//		    e.printStackTrace();
			return new HashMap<String, SaveGame>();
		}
		
		if(readCase == null) 
	    	return new HashMap<String, SaveGame>();
		
		return readCase;
		
	}
	
	

}
