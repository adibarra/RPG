import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * The FileLoader Class enables easy loading of a file.
 * This can be used to save and retrieve savefiles, settings, and more.
 * @author Alec Ibarra
 */
public class FileLoader{

	private String workingDirectory;
	private String OS = (System.getProperty("os.name")).toUpperCase();
	
	/**
	 * Prepares special variables
	 */
	public void prepare()
	{
		if (OS.contains("WIN"))
		{
			workingDirectory = System.getenv("AppData");
		}
		else
		{
			workingDirectory = System.getProperty("user.home");
			workingDirectory += "/Library/Application Support";
		}
	}
	
	/**
	 * Loads file from outside jar file, parses its content
	 */
	public ArrayList<String> load(String fileName)
	{
		String gameData = "";
		String line = "";
		ArrayList<String> saveGame = new ArrayList<String>();
		
		File jarFile;
		try
		{
			jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			File saveFile = new File(jarFile.getParentFile()+"/"+fileName+".dat");
		
			if(saveFile.exists())
			{
				BufferedReader br = new BufferedReader(new FileReader(saveFile));
			
				while((line = br.readLine()) != null)
				{
					if(!line.contains("#"))
					{
						gameData += line;
					}
				}
				br.close();
				
				for(int k = 0; k < gameData.split(";").length; k++)
				{
					saveGame.add(gameData.split(";")[k]);
				}
			}
			else
			{
				System.out.println("Failed to load file: "+fileName);
			}
					
		} catch (URISyntaxException | IOException e) {}	
		
		return saveGame;
	}
	
	/**
	 * Loads file from jar's internal res folder, parses its content
	 */
	public ArrayList<String> loadRes(String fileName)
	{
		String gameData = "";
		String line = "";
		ArrayList<String> saveGame = new ArrayList<String>();
		
		try
		{
			InputStream saveFile = this.getClass().getResourceAsStream(fileName+".dat");
					
			if(saveFile != null)
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(saveFile,"UTF-8"));
			
				while((line = br.readLine()) != null)
				{
					if(!line.contains("#"))
					{
						gameData += line;
					}
				}
				br.close();
				
				for(int k = 0; k < gameData.split(";").length; k++)
				{
					saveGame.add(gameData.split(";")[k]);
				}
			}
			else
			{
				System.out.println("Failed to load file: "+fileName);
			}
					
		} catch (IOException e) {}	
		
		return saveGame;
	}
	
	/**
	 * Loads file named Save.dat, parses its content to load it as a save file
	 */
	public void firstLoad()
	{
		String gameData = "";
		String line = "";
		Boolean fileExists = false;
		ArrayList<String> saveGame = new ArrayList<String>();
		
		//File jarFile;
		try
		{
			//jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			File saveFile = new File(getSaveDir()+"/"+Logic.PROJECT_NAME+"/Save.dat");
		
			if(saveFile.exists())
			{
				BufferedReader br = new BufferedReader(new FileReader(saveFile));
			
				while((line = br.readLine()) != null)
				{
					//if a line in save file contains # it will be ignored as a comment
					if(!line.contains("#"))
					{
						gameData += line;
					}
				}
				br.close();
			}
			
			for(int k = 0; k < gameData.split(",").length; k++)
			{
				saveGame.add(gameData.split(",")[k]);
			}
		
			//If the file is empty or otherwise incomplete then dont load in the save
			if(saveGame.get(0) != null && saveGame.get(saveGame.size()-1) != null 
					&& (fileExists = saveFile.exists()))
			{	
				//TODO retrieve value from saveGame (loading saves)
				//EXAMPLE: >>> variable = Float.parseFloat(saveGame.get(0));
				//Write game specific ones below vvv
				{
					//variable = Float.parseFloat(saveGame.get(0));
					//variable = Float.parseFloat(saveGame.get(1));
					//variable = Float.parseFloat(saveGame.get(2));
				}
			}
			else if(!fileExists)
			{
				makeNewSave();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Saves to file named Save.dat in save directory
	 */
	public void saveGame()
	{
		ArrayList<String> saveGame = new ArrayList<String>();
		
		//TODO save variables to Save.dat (saving game)
		//EXAMPLE: >>> saveGame.add(Float.toString(variable));
		//Write game specific ones below vvv
		{
			//saveGame.add(Float.toString(variable));
			//saveGame.add(Float.toString(variable));
			//saveGame.add(Float.toString(variable));
		}
		
		try
		{
			File saveFile = new File(getSaveDir()+"/"+Logic.PROJECT_NAME+"/Save.dat");
			BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
			
			int numOfLinesBeforeLineBreak = 10;
			
			for(int k = 0; k < saveGame.size(); k++)
			{
				if(k == saveGame.size()-1)
				{
					bw.write(saveGame.get(k));
				}
				else
				{
					bw.write(saveGame.get(k)+",");
				}
				if(k % numOfLinesBeforeLineBreak == numOfLinesBeforeLineBreak-1)
				{
					bw.write("\n");
				}
			}
			bw.write("\n#For Version "+Logic.GAME_VERSION+" of Game");
			bw.close();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new file named Save.dat, everything written to it are default start variables
	 */
	public void makeNewSave()
	{
		try
		{
			File saveFile = new File(getSaveDir()+"/"+Logic.PROJECT_NAME+"/Save.dat");
			if(!saveFile.exists())
				{
					new File(getSaveDir()+"/RPG/").mkdirs();
					saveFile.createNewFile();
				}
			//System.out.println("SaveFile Created at: "+saveFile.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
			
			//TODO Blank save of game to load when there is no save should look somewhat like saveGame method
			{
				//bw.write("value,value2,value3,value4,value5,value6,value7,value8,value9,value10,\n");
				//bw.write("value,value2,value3,value4,value5,value6,value7,value8,value9,value10,\n");
				//bw.write("value,value2,value3,value4,value5,value6,value7,value8,value9,value10,\n");
			}
			bw.write("\n#For Version "+Logic.GAME_VERSION+" of Game");
			bw.close();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get save directory
	 */
	public String getSaveDir()
	{
		return workingDirectory;
	}

}
