import java.awt.image.BufferedImage;

/**
 * The Animator class manages the appearence of animated entities. Whenever the amount of
 * time represented by switchTime has elapsed the next icon in the Arraylist is used.
 * This creates the illusion of a moving entity much like a flipbook does. Whenever an entity
 * using the Animator class calls its updateImage method its Animator instance will update the
 * image it has saved.
 * 
 * @author Alec Ibarra
 */
public class Animator {
	
	private float lastTime = System.nanoTime()/1000000000;
	private float switchTime = 0.2f;
	private BufferedImage[] icons;
	private int currentImageNum = 0;

	public Animator()
	{
		
	}
	
	public Animator(BufferedImage[] icons)
	{
		setIcons(icons);
		setCurrentImageNum((int)(Math.random()*(icons.length-1)));
	}
	
	public Animator(BufferedImage[] icons, float switchTime)
	{
		setIcons(icons);
		setSwitchTime(switchTime);
		setCurrentImageNum((int)(Math.random()*(icons.length-1)));
	}
	
	public Animator(BufferedImage[] icons, float switchTime, boolean startAtZero)
	{
		setIcons(icons);
		setSwitchTime(switchTime);
		if(!startAtZero)
			setCurrentImageNum((int)(Math.random()*(icons.length-1)));
	}
	
	/**
	 * @return The current BufferedImage of the entity
	 */
	public BufferedImage getCurrentImage()
	{
		//activate every switchTime seconds
		if(System.nanoTime() - getLastTime() > getSwitchTime()*1000000000)
		{
			if(getCurrentImageNum()+1 > getIcons().length-1)
			{
				setCurrentImageNum(-1);
			}
			setCurrentImageNum(getCurrentImageNum()+1);
			setLastTime(System.nanoTime());
		}
		return getIcons()[getCurrentImageNum()];
	}
	
	/**
	 * @return A random BufferedImage of the entity
	 */
	public BufferedImage getRandomImage()
	{	
		//activate every switchTime seconds
		if(System.nanoTime() - getLastTime() > getSwitchTime()*1000000000)
		{
			setCurrentImageNum(Tracker.r.nextInt(getIcons().length-1));
			setLastTime(System.nanoTime());
		}
		return getIcons()[getCurrentImageNum()];
	}

	public BufferedImage[] getIcons() {
		return icons;
	}

	public void setIcons(BufferedImage[] icons) {
		this.icons = icons;
	}
	
	public int getCurrentImageNum() {
		return currentImageNum;
	}

	public void setCurrentImageNum(int currentImageNum) {
		this.currentImageNum = currentImageNum;
	}

	public float getLastTime() {
		return lastTime;
	}

	public void setLastTime(float lastTime) {
		this.lastTime = lastTime;
	}

	public float getSwitchTime() {
		return switchTime;
	}

	public void setSwitchTime(float switchTime) {
		this.switchTime = switchTime;
	}
	
}
