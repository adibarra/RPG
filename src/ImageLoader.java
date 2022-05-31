import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//Alec Ibarra
public class ImageLoader {
	
	private static ArrayList<GameIcon> icons = new ArrayList<GameIcon>();
	
	public ImageLoader()
	{		
		icons.add(new GameIcon("Background"));
		icons.add(new GameIcon("BackgroundBlack"));
		icons.add(new GameIcon("BackgroundGrass1"));
		icons.add(new GameIcon("BackgroundGrass2"));
		icons.add(new GameIcon("BackgroundGrass3"));
		icons.add(new GameIcon("BackgroundGreen"));
		icons.add(new GameIcon("BackgroundRed"));
		icons.add(new GameIcon("BackgroundSpace1"));
		icons.add(new GameIcon("BackgroundSpace2"));
		icons.add(new GameIcon("BackgroundSpace3"));
		icons.add(new GameIcon("BackgroundWhiteTile"));
		icons.add(new GameIcon("Barrel"));
		icons.add(new GameIcon("Basket"));
		icons.add(new GameIcon("Bed"));
		icons.add(new GameIcon("Brick1"));
		icons.add(new GameIcon("Brick2"));
		icons.add(new GameIcon("Bullet"));
		icons.add(new GameIcon("CardboardBox"));
		icons.add(new GameIcon("Chair"));
		icons.add(new GameIcon("Cobblestone"));
		icons.add(new GameIcon("EffectFire"));
		icons.add(new GameIcon("EffectSmoke"));
		icons.add(new GameIcon("GUIBack"));
		icons.add(new GameIcon("GUICheckbox"));
		icons.add(new GameIcon("GUICheckedCheckbox"));
		icons.add(new GameIcon("GUIPause"));
		icons.add(new GameIcon("Icon0128x0128"));
		icons.add(new GameIcon("Icon0256x0256"));
		icons.add(new GameIcon("Icon0512x0512"));
		icons.add(new GameIcon("Icon1024x1024"));
		icons.add(new GameIcon("Icon2048x2048"));
		icons.add(new GameIcon("Key"));
		icons.add(new GameIcon("Lamp"));
		icons.add(new GameIcon("NoTexture"));
		icons.add(new GameIcon("NPC1[1]"));
		icons.add(new GameIcon("NPC1[2]"));
		icons.add(new GameIcon("NPC2[1]"));
		icons.add(new GameIcon("NPC2[2]"));
		icons.add(new GameIcon("NPC3[1]"));
		icons.add(new GameIcon("NPC3[2]"));
		icons.add(new GameIcon("NPC4[1]"));
		icons.add(new GameIcon("NPC4[2]"));
		icons.add(new GameIcon("Plate"));
		icons.add(new GameIcon("Player1"));
		icons.add(new GameIcon("Player2"));
		icons.add(new GameIcon("Player3"));
		icons.add(new GameIcon("PowerupAmmo"));
		icons.add(new GameIcon("PowerupBoost"));
		icons.add(new GameIcon("PowerupHealth"));
		icons.add(new GameIcon("ShirtBlue"));
		icons.add(new GameIcon("ShirtGreen"));
		icons.add(new GameIcon("ShirtRed"));
		icons.add(new GameIcon("Spawn"));
		icons.add(new GameIcon("SplashScreen"));
		icons.add(new GameIcon("Stone1"));
		icons.add(new GameIcon("Stone2"));
		icons.add(new GameIcon("Table"));
		icons.add(new GameIcon("Tank"));
		icons.add(new GameIcon("Shingle1"));
		icons.add(new GameIcon("Shingle2"));
		icons.add(new GameIcon("Shingle3"));
		icons.add(new GameIcon("Shingle4"));
		icons.add(new GameIcon("Washer"));
		icons.add(new GameIcon("WoodenBox1"));
		icons.add(new GameIcon("WoodenBox2"));
		icons.add(new GameIcon("WoodenFence"));
		icons.add(new GameIcon("WoodenFenceCorner"));
		icons.add(new GameIcon("WoodenFenceConnector"));
		icons.add(new GameIcon("WoodenGate"));
		icons.add(new GameIcon("WoodPlanks1"));
		icons.add(new GameIcon("WoodPlanks2"));
		icons.add(new GameIcon("WindowWoodPlanks1"));
		icons.add(new GameIcon("WindowWoodPlanks2"));
		icons.add(new GameIcon("Wave[1]"));
		icons.add(new GameIcon("Wave[2]"));
		icons.add(new GameIcon("Wave[3]"));
		icons.add(new GameIcon("Wave[4]"));
		icons.add(new GameIcon("Wave[5]"));
		icons.add(new GameIcon("Fish"));
		icons.add(new GameIcon("Shingle4-WoodPlanks2"));
	}
	
	public static int[] getPixelColorAt(int x, int y)
	{
		Color color = new Color(((BufferedImage) Logic.offscreen).getRGB(x,y));
		return new int[]{color.getRed(),color.getGreen(),color.getBlue()};
	}
	
	public static BufferedImage getRandomImage(String imageName)
	{
		if(imageName.toLowerCase().contains("backgroundspace"))
		{
			int rand = Tracker.r.nextInt(2)+1;//choose random space pic
			if (rand == 1)
				return getImage("backgroundSpace1");
			else if (rand == 2)
				return getImage("backgroundSpace2");
			else if (rand == 3)
				return getImage("backgroundSpace3");
		}
		
		return getImage("noTexture");
	}
	
	public static BufferedImage[] getImageSeries(String imageName)
	{
		if(imageName.toLowerCase().contains("player"))
			return new BufferedImage[]{getImage("player1"),getImage("player2"),getImage("player3")};
		
		if(imageName.toLowerCase().contains("npc"))
			return new BufferedImage[]{getImage(imageName+"[1]"),getImage(imageName+"[2]")};
		
		if(imageName.toLowerCase().contains("wave1"))
			return new BufferedImage[]{getImage("wave[1]"),getImage("wave[2]"),getImage("wave[3]"),getImage("wave[2]")};
		
		if(imageName.toLowerCase().contains("wave2"))
			return new BufferedImage[]{getImage("wave[5]"),getImage("wave[4]"),getImage("wave[3]"),getImage("wave[4]")};
		
		return new BufferedImage[]{getImage("notexture")};
	}
	
	public static BufferedImage getImage(String imageName)
	{
		if(imageName.equalsIgnoreCase("none"))
		{
			return null;
		}
		
		for(int k = 0; k < icons.size(); k++)
		{
			if(icons.get(k).getIconName().equalsIgnoreCase(imageName))
			{
				return icons.get(k).getIcon();
			}
		}
		
		return getImage("noTexture");
	}

}

class GameIcon
{
	private BufferedImage icon;
	private String iconName;
	
	public GameIcon(String iconName)
	{
		try
		{
			setIconName(iconName);
			setIcon(ImageIO.read(getClass().getClassLoader().getResource(iconName+".png")));
			
		} catch (IOException | IllegalArgumentException e)
		{
			System.out.println("Failed to load: "+iconName+".png");
		}
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
}
