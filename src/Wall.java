import java.awt.geom.Rectangle2D;

//Alec Ibarra
public class Wall {

	private int xpos;
	private int ypos;
	private int rotation;
	private String iconName;
	
	public Wall()
	{
		
	}
	
	public Wall(int xpos, int ypos, int rotation, String iconName)
	{
		setXpos(xpos);
		setYpos(ypos);
		setRotation(rotation);
		setIconName(iconName);
	}
	
	public Rectangle2D.Double getRect()
	{
		return new Rectangle2D.Double(getXpos(),getYpos(),25,25);
	}

	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

}
