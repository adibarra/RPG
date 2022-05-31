import java.awt.Point;
import java.util.ArrayList;


//Alec Ibarra
public class NPC extends Entity{
	
	private String name = "null";
	private boolean met = false;
	private ArrayList<Point> points = new ArrayList<Point>();
	
	public NPC()
	{
		
	}
	
	public NPC(float XPos, float YPos, String name)
	{
		setName(name);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setSolid(true);
		setIcon(ImageLoader.getImage("player"));
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),1f));
		updateHitBox();
	}
	
	public NPC(float XPos, float YPos, float rotation, String name)
	{
		setName(name);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setRotation(rotation);
		setSolid(true);
		setIcon(ImageLoader.getImage("player"));
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),1f));
		updateHitBox();
	}
	
	public NPC(float XPos, float YPos, float rotation, String name, String iconName)
	{
		setName(name);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setRotation(rotation);
		setSolid(true);
		setIcon(ImageLoader.getImage(iconName));
		setAnimator(new Animator(ImageLoader.getImageSeries(iconName),1.5f));
		setNormalIcon(getIcon());
		updateHitBox();
	}
	
	public NPC(float XPos, float YPos, float rotation, String name, String iconName, float delay)
	{
		setName(name);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setRotation(rotation);
		setSolid(true);
		setIcon(ImageLoader.getImage(iconName));
		setAnimator(new Animator(ImageLoader.getImageSeries(iconName),delay));
		setNormalIcon(getIcon());
		updateHitBox();
	}
	
	public NPC(float XPos, float YPos, float rotation, String name, String iconName, float delay, boolean startAtZeroFrame)
	{
		setName(name);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setRotation(rotation);
		setSolid(true);
		setIcon(ImageLoader.getImage(iconName));
		setAnimator(new Animator(ImageLoader.getImageSeries(iconName),delay,startAtZeroFrame));
		setNormalIcon(getIcon());
		updateHitBox();
	}
	
	public void runScript(String fileName)
	{
		ArrayList<String> tempArray = Logic.fl.loadRes(fileName);

		for(int k = 0; k < tempArray.size(); k++)
		{
			String[] args = tempArray.get(k).split(",");
			points.add(new Point(Integer.parseInt(args[0]),Integer.parseInt(args[1])));
		}

		while(points.size() > 0)
		{
			if(points.get(0).getY() == -1)
			{
				Logic.delay(points.get(0).getX());
				points.remove(0);
			}
			else if(points.get(0).getX() == -1)
			{
				setRotation((float)points.get(0).getY());
				points.remove(0);
			}
			else if(moveTo((float)points.get(0).getX(),(float)points.get(0).getY(),1.8f))
			{
				points.remove(0);
			}
			Logic.delay(Logic.TARGET_FRAME_TIME);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMet() {
		return met;
	}

	public void setMet(boolean met) {
		this.met = met;
	}
}
