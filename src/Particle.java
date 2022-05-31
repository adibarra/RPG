
//Alec Ibarra
public class Particle extends Entity{

	public Particle()
	{
		setIcon(ImageLoader.getImage("noTexture"));
		setType("unknown");
		setHealth(50);
	}
	
	public Particle(float xPos, float yPos, int rotation, String type)
	{
		setXPos(xPos);
		setYPos(yPos);
		setType(type);
		setHealth(50);
		setMaxHealth(50);
		setRotation(rotation);
		setIcon(ImageLoader.getImage(type));
	}
	
	public Particle(float xPos, float yPos, int rotation, String type, int health)
	{
		setXPos(xPos);
		setYPos(yPos);
		setType(type);
		setHealth(health);
		setMaxHealth(health);
		setRotation(rotation);
		setIcon(ImageLoader.getImage(type));
	}
	
}
