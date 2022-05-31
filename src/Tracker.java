import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

//Alec Ibarra
@SuppressWarnings("rawtypes")
public class Tracker implements Serializable{
	
	private static final long serialVersionUID = 1L;
	static ArrayList<ArrayList> everything = new ArrayList<ArrayList>();
	static ArrayList<Player> players = new ArrayList<Player>();
	static ArrayList<NPC> npcs = new ArrayList<NPC>();
	static ArrayList<Entity> objects = new ArrayList<Entity>();
	static ArrayList<Entity> roofs = new ArrayList<Entity>();
	static ArrayList<Particle> particles = new ArrayList<Particle>();
	static ArrayList<Wall> walls = new ArrayList<Wall>();
	static ArrayList<Entity> gates = new ArrayList<Entity>();
	
	static ArrayList<ArrayList> titleStuff = new ArrayList<ArrayList>();
	static ArrayList<ArrayList> controlsStuff = new ArrayList<ArrayList>();
	static ArrayList<ArrayList> settingsStuff = new ArrayList<ArrayList>();
	static ArrayList<ArrayList> pauseStuff = new ArrayList<ArrayList>();
	static ArrayList<ArrayList> hudStuff = new ArrayList<ArrayList>();
	
	static ArrayList<Button> titleButtons = new ArrayList<Button>();
	static ArrayList<Button> controlsButtons = new ArrayList<Button>();
	static ArrayList<Button> settingsButtons = new ArrayList<Button>();
	static ArrayList<Button> pauseButtons = new ArrayList<Button>();
	static ArrayList<Button> hudButtons = new ArrayList<Button>();
	
	static ArrayList<Slider> titleSliders = new ArrayList<Slider>();
	static ArrayList<Slider> controlsSliders = new ArrayList<Slider>();
	static ArrayList<Slider> settingsSliders = new ArrayList<Slider>();
	static ArrayList<Slider> pauseSliders = new ArrayList<Slider>();
	static ArrayList<Slider> hudSliders = new ArrayList<Slider>();
	
	static ArrayList<ProgressBar> titleProgressBars = new ArrayList<ProgressBar>();
	static ArrayList<ProgressBar> controlsProgressBars = new ArrayList<ProgressBar>();
	static ArrayList<ProgressBar> settingsProgressBars = new ArrayList<ProgressBar>();
	static ArrayList<ProgressBar> pauseProgressBars = new ArrayList<ProgressBar>();
	static ArrayList<ProgressBar> hudProgressBars = new ArrayList<ProgressBar>();
	
	static int[][] world = new int[Logic.screenWidth/25][Logic.screenHeight/25];
	static Rectangle2D tempRec = new Rectangle();
	static Area top = new Area(new Rectangle(-100,-125,Logic.screenWidth+200,100));
	static Area bottom = new Area(new Rectangle(-100,Logic.screenHeight+25,Logic.screenWidth+200,100));
	static Area right = new Area(new Rectangle(Logic.screenWidth+25,-100,100,Logic.screenHeight+200));
	static Area left = new Area(new Rectangle(-125,-100,100,Logic.screenHeight+200));
	static Area objectZone = new Area();
	static Area wallZone = new Area();
	static Area tempArea = new Area();
	static Random r = new Random();
	
	static Graphics g2;
	static BufferedImage backgroundPic = null;	
	
	public static void prepare()
	{
		everything.clear();
		everything.add(objects);
		everything.add(roofs);
		everything.add(npcs);
		everything.add(players);
		everything.add(gates);
		
		titleStuff.clear();
		titleStuff.add(titleButtons);
		titleStuff.add(titleSliders);
		titleStuff.add(titleProgressBars);
		
		controlsStuff.clear();
		controlsStuff.add(controlsButtons);
		controlsStuff.add(controlsSliders);
		controlsStuff.add(controlsProgressBars);
		
		settingsStuff.clear();
		settingsStuff.add(settingsButtons);
		settingsStuff.add(settingsSliders);
		settingsStuff.add(settingsProgressBars);
		
		pauseStuff.clear();
		pauseStuff.add(pauseButtons);
		pauseStuff.add(pauseSliders);
		pauseStuff.add(pauseProgressBars);
		
		hudStuff.clear();
		hudStuff.add(hudButtons);
		hudStuff.add(hudSliders);
		hudStuff.add(hudProgressBars);
	}
	
	public static void parseLevelData(String string)
	{
		String[] args = string.split(",");
		
		if(args[0].equals("b"))
		{
			genBackground(args[1]);
		}
		else if(args[0].equalsIgnoreCase("n"))
		{
			//x,y,rotation,name,iconseries,delay,startAtZeroFrame
			Tracker.npcs.add(new NPC(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),args[4],args[5],Float.parseFloat(args[6]),Boolean.parseBoolean(args[7])));
		}
		else if(args[0].equalsIgnoreCase("i"))
		{
			//x/25,y/25,width/25,height/25,rotation,iconname
			imageRect(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),Integer.parseInt(args[5]),args[6]);
		}
		else if(args[0].equalsIgnoreCase("i2"))
		{
			//x/25,y/25,width/25,height/25,rotation,iconname,iconname2
			imageRect(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),Integer.parseInt(args[5]),args[6],args[7]);
		}
		else if(args[0].equalsIgnoreCase("w"))
		{
			//x/25,y/25,width/25,height/25,rotation,iconname
			wallRect(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),Integer.parseInt(args[5]),args[6]);
		}
		else if(args[0].equalsIgnoreCase("w2"))
		{
			//x/25,y/25,width/25,height/25,rotation,iconname,iconname2
			wallRect(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),Integer.parseInt(args[5]),args[6],args[7]);
		}
		else if(args[0].equalsIgnoreCase("wa"))
		{
			//x/25,y/25,width/25,height/25,rotation,iconseries,delay,startAtZeroFrame
			wallRectAnimate(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),Integer.parseInt(args[5]),args[6],Float.parseFloat(args[7]),Boolean.parseBoolean(args[8]));
		}
		else if(args[0].equalsIgnoreCase("wa2"))
		{
			//x/25,y/25,width/25,height/25,rotation,iconseries,iconseries2,delay,startAtZeroFrame
			wallRectAnimate(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),Integer.parseInt(args[5]),args[6],args[7],Float.parseFloat(args[8]),Boolean.parseBoolean(args[9]));
		}
		else if(args[0].equalsIgnoreCase("r"))
		{
			//x/25,y/25,width/25,height/25,rotation,iconname
			roofRect(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),Integer.parseInt(args[5]),Integer.parseInt(args[6]),args[7]);
		}
		else if(args[0].equalsIgnoreCase("r2"))
		{
			//x/25,y/25,width/25,height/25,rotation,iconname,iconname2
			roofRect(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),Integer.parseInt(args[5]),Integer.parseInt(args[6]),args[7],args[8]);
		}
		else if(args[0].equalsIgnoreCase("g"))
		{
			//x/25,y/25,width/25,height/25,rotation,id,iconname
			gateRect(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),Integer.parseInt(args[5]),Integer.parseInt(args[6]),args[7]);
		}
	}
	
	public static void genWorldFromSave(Logic logic, Point world)//pass logic so that jarfile location can be found
	{
		backgroundPic = new BufferedImage(Logic.screenWidth,Logic.screenHeight,BufferedImage.TYPE_INT_ARGB);
		g2 = backgroundPic.getGraphics();
		
		objects.clear();
		walls.clear();
		roofs.clear();
		npcs.clear();
		gates.clear();
		
		ArrayList<String> level = Logic.fl.loadRes(""+(int)world.getX()+(int)world.getY());
		for(int k = 0; k < level.size(); k++)
			parseLevelData(level.get(k));
        
        drawWalls(g2);
        buildWallZone();
	}
	
	public static void genBackground(String background)
	{
		backgroundPic = new BufferedImage(Logic.screenWidth,Logic.screenHeight,BufferedImage.TYPE_INT_ARGB);
		g2 = backgroundPic.getGraphics();
		
		Logic.backgroundType = background;
		if(background.equalsIgnoreCase("space"))
		{
			//Prep background
			for(int j = 0; j < Logic.screenWidth/25; j++)
			{
				for(int k = 0; k < Logic.screenHeight/25; k++)
				{
					world[j][k] = r.nextInt(4)*90;
					if(world[j][k] == 360)
						world[j][k] = 0;
				
					AffineTransform at = new AffineTransform();
						at.translate(j*25+12.5,k*25+12.5);//position image and use midpoint of image as rotation point
						at.rotate(Math.toRadians(world[j][k]));
						at.translate(-12.5,-12.5);//revert image location
					
					((Graphics2D) g2).drawImage(ImageLoader.getRandomImage("backgroundSpace"),at,null);
				}
			}
		}
		else if(background.equalsIgnoreCase("grass"))
		{
			//Prep background
			for(int j = 0; j < Logic.screenWidth/25; j++)
			{
				for(int k = 0; k < Logic.screenHeight/25; k++)
				{
					//world[j][k] = r.nextInt(4)*90;
					//if(world[j][k] == 360)
					//	world[j][k] = 0;
				
					AffineTransform at = new AffineTransform();
						at.translate(j*25+12.5,k*25+12.5);//position image and use midpoint of image as rotation point
						//at.rotate(Math.toRadians(world[j][k]));
						at.translate(-12.5,-12.5);//revert image location
					
					((Graphics2D) g2).drawImage(ImageLoader.getImage("backgroundGrass2"),at,null);
				}
			}
		}
		else if(background.equalsIgnoreCase("sand"))
		{
			//Prep background
			for(int j = 0; j < Logic.screenWidth/25; j++)
			{
				for(int k = 0; k < Logic.screenHeight/25; k++)
				{
					world[j][k] = r.nextInt(4)*90;
					if(world[j][k] == 360)
						world[j][k] = 0;
				
					AffineTransform at = new AffineTransform();
						at.translate(j*25+12.5,k*25+12.5);//position image and use midpoint of image as rotation point
						at.rotate(Math.toRadians(world[j][k]));
						at.translate(-12.5,-12.5);//revert image location
					
					((Graphics2D) g2).drawImage(ImageLoader.getImage("backgroundSand"),at,null);
				}
			}
		}
		else if(background.equalsIgnoreCase("sandRed"))
		{
			//Prep background
			for(int j = 0; j < Logic.screenWidth/25; j++)
			{
				for(int k = 0; k < Logic.screenHeight/25; k++)
				{
					world[j][k] = r.nextInt(4)*90;
					if(world[j][k] == 360)
						world[j][k] = 0;
				
					AffineTransform at = new AffineTransform();
						at.translate(j*25+12.5,k*25+12.5);//position image and use midpoint of image as rotation point
						at.rotate(Math.toRadians(world[j][k]));
						at.translate(-12.5,-12.5);//revert image location
					
					((Graphics2D) g2).drawImage(ImageLoader.getImage("backgroundRed"),at,null);
				}
			}
		}
		else if(background.equalsIgnoreCase("sandGreen"))
		{
			//Prep background
			for(int j = 0; j < Logic.screenWidth/25; j++)
			{
				for(int k = 0; k < Logic.screenHeight/25; k++)
				{
					world[j][k] = r.nextInt(4)*90;
					if(world[j][k] == 360)
						world[j][k] = 0;
				
					AffineTransform at = new AffineTransform();
						at.translate(j*25+12.5,k*25+12.5);//position image and use midpoint of image as rotation point
						at.rotate(Math.toRadians(world[j][k]));
						at.translate(-12.5,-12.5);//revert image location
					
					((Graphics2D) g2).drawImage(ImageLoader.getImage("backgroundGreen"),at,null);
				}
			}
		}
		else if(background.equalsIgnoreCase("tile"))
		{
			//Prep background
			for(int j = 0; j < Logic.screenWidth/25; j++)
			{
				for(int k = 0; k < Logic.screenHeight/25; k++)
				{
					((Graphics2D) g2).drawImage(ImageLoader.getImage("backgroundWhiteTile"),j*25,k*25,null);
				}
			}
		}
		else if(background.contains("custom"))
		{
			background = background.substring(6);
			Logic.backgroundType = background;
			//Prep background
			for(int j = 0; j < Logic.screenWidth/ImageLoader.getImage(background).getWidth(); j++)
			{
				for(int k = 0; k < Logic.screenHeight/ImageLoader.getImage(background).getHeight(); k++)
				{
					((Graphics2D) g2).drawImage(ImageLoader.getImage(background),
							j*ImageLoader.getImage(background).getWidth(),
							k*ImageLoader.getImage(background).getHeight(),null);
				}
			}
		}
		else
		{
			Logic.backgroundType = "default";
			//default
			g2.setColor(Color.WHITE);
			g2.fillRect(0,0,Logic.screenWidth,Logic.screenHeight);
		}
	}

	public static void buildWallZone()//call whenever a wall is placed
	{
		wallZone = new Area();
		
		//adds walls to wallzone
		for (int k = 0; k < walls.size(); k++)
        {
			tempRec = walls.get(k).getRect();
			tempArea = new Area(tempRec);
			wallZone.add(tempArea);
        }
		
		objectZone = new Area();
		
		//adds objects to objectzone
		for (int k = 0; k < objects.size(); k++)
        {
			tempRec = objects.get(k).getHitBox();
			tempArea = new Area(tempRec);
			objectZone.add(tempArea);
        }
	}
	
	public static void drawWalls(Graphics g2)
	{
		for(int k = 0; k < walls.size(); k++)
    	{
    		drawImage(walls.get(k).getXpos(),walls.get(k).getYpos(),walls.get(k).getRotation(),walls.get(k).getIconName());
    	}
	}
	
	public static void drawImage(int x, int y, int rotation, String iconName)
	{		
		BufferedImage image = ImageLoader.getImage(iconName);
		//for each image draw with correct orientation and location
		AffineTransform at = new AffineTransform();
		at.translate(x+image.getWidth()/2,y+image.getHeight()/2);//position image
	
		at.rotate(Math.toRadians(rotation));
		at.translate(-image.getWidth()/2, -image.getHeight()/2);//translate image to use center as point of rotation
	
		// draw the image
		((Graphics2D) g2).drawImage(image, at, null);
		at.rotate(Math.toRadians(-rotation));
	}
	
	public static void imageRect(int x, int y, int width, int height, int rotation, String iconName)
	{
		for(int k = x; k < x+width; k++)//main path
        {
        	for(int j = y; j < y+height; j++)
            {
        		drawImage(25*k,25*j,rotation,iconName);
            }
        }
	}
	
	public static void imageRect(int x, int y, int width, int height, int rotation, String iconName1, String iconName2)
	{
		for(int k = x; k < x+width; k++)
        {
        	for(int j = y; j < y+height; j++)
            {
        		if(j % 2 == 0)
        		{
        			drawImage(25*k,25*j,0,iconName1);
        		}
        		else
        		{
        			drawImage(25*k,25*j,0,iconName2);
        		}
            }
        }
	}
	
	public static void wallRect(int x, int y, int width, int height, int rotation, String iconName)
	{
		for(int k = x; k < x+width; k++)
        {
        	for(int j = y; j < y+height; j++)
            {
        		walls.add(new Wall(25*k,25*j,rotation,iconName));
            }
        }
	}
	
	public static void wallRect(int x, int y, int width, int height, int rotation, String iconName1, String iconName2)
	{
		for(int k = x; k < x+width; k++)
        {
        	for(int j = y; j < y+height; j++)
            {
        		if(j % 2 == 0)
        		{
        			walls.add(new Wall(25*k,25*j,0,iconName1));
        		}
        		else
        		{
        			walls.add(new Wall(25*k,25*j,0,iconName2));
        		}
            }
        }
	}
	
	public static void wallRectAnimate(int x, int y, int width, int height, int rotation, String seriesName, float delay, boolean startAtZeroFrame)
	{
		for(int k = x; k < x+width; k++)
        {
        	for(int j = y; j < y+height; j++)
            {
        		objects.add(new Entity(25*k,25*j,rotation,seriesName,delay,startAtZeroFrame));
            }
        }
	}
	
	public static void wallRectAnimate(int x, int y, int width, int height, int rotation, String seriesName, String seriesName2, float delay, boolean startAtZeroFrame)
	{
		for(int k = x; k < x+width; k++)
        {
        	for(int j = y; j < y+height; j++)
            {
        		if(j % 2 == 0)
        		{
        			objects.add(new Entity(25*k,25*j,rotation,seriesName,delay,startAtZeroFrame));
        		}
        		else
        		{
        			objects.add(new Entity(25*k,25*j,rotation,seriesName2,delay,startAtZeroFrame));
        		}
            }
        }
	}
	
	public static void roofRect(int x, int y, int width, int height, int rotation, int id, String iconName1)
	{
		for(int k = x; k < x+width; k++)
        {
        	for(int j = y; j < y+height; j++)
            {
        		roofs.add(new Entity(25*k,25*j,rotation,false,id,iconName1));
            }
        }
	}
	
	public static void roofRect(int x, int y, int width, int height, int rotation, int id, String iconName1, String iconName2)
	{
		for(int k = x; k < x+width; k++)
        {
        	for(int j = y; j < y+height; j++)
            {
        		if(j % 2 == 0)
        		{
        			roofs.add(new Entity(25*k,25*j,rotation,false,id,iconName1));
        		}
        		else
        		{
        			roofs.add(new Entity(25*k,25*j,rotation,false,id,iconName2));
        		}
            }
        }
	}
	
	public static void gateRect(int x, int y, int width, int height, int rotation, int id, String iconName1)
	{
		for(int k = x; k < x+width; k++)
        {
        	for(int j = y; j < y+height; j++)
            {
        		//check to see if gate is open before adding
        		boolean open = false;
    			for(int z = 0; z < Quests.openGateIds.size(); z++)
    			{
    				if(id == Quests.openGateIds.get(z))
    				{
    					open = true;
    				}
    			}
    			
    			if(!open)
    			{
    				gates.add(new Entity(25*k,25*j,0,id,iconName1));
    			}
            }
        }
	}
	
	public static NPC getNPC(String name)
	{
		for(int k = 0; k < npcs.size(); k++)
		{
			if(npcs.get(k).getName().equals(name))
			{
				return npcs.get(k);
			}
		}
		
		return null;
	}

}
