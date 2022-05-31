import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * The Entity Class keeps track of various useful variables for an object.
 * @author Alec Ibarra
 */
public class Entity {

	private float XPos = 100;
	private float YPos = 100;
	private float XPosTarget = 100;
	private float YPosTarget = 100;
	private float XSpawn = 100;
	private float YSpawn = 100;
	private float XVelocity = 0;
	private float YVelocity = 0;
	private float minVelocity = 0.2f;
	private float maxVelocity = 2.5f;
	private float friction = 0.4f;
	private float speed = 2.5f;
	private float maxSpeed = 1.5f;
	private float rotation = 0;
	private int points = 0;
	private int ID = 0;
	private int ownerID = 0;
	private int teamID = 0;
	private int health = 100;
	private int maxHealth = 100;
	private int damage = 100;
	private int money = 0;
	private int currentCooldown = 100;
	private int maxCooldown = 100;
	private String owner = "unknown";
	private String type = "unknown";
	private String lore = "";
	private boolean dynamic = false;
	private boolean solid = true;
	private boolean neutral = false;
	private boolean friendly = false;
	private boolean hostile = false;
	private float opacity = 1f;
	private int iconNum = 0;
	private String[] icons;
	private BufferedImage normalIcon = ImageLoader.getImage("noTexture");
	private BufferedImage icon = ImageLoader.getImage("noTexture");
	private int iconWidth = icon.getWidth();
	private int iconHeight = icon.getHeight();
	private Rectangle2D hitBox = new Rectangle2D.Double(XPos,YPos,iconWidth,iconHeight);
	private Point lastGoodPos = new Point(0,0);
	private Animator animator = null;
	private float movementMultiplier = 1f;
	
	private Point2D.Double north = new Point2D.Double(hitBox.getX()+hitBox.getWidth()/2,hitBox.getY());
	private Point2D.Double east = new Point2D.Double(hitBox.getX()+hitBox.getWidth(),hitBox.getY()+hitBox.getHeight()/2);
	private Point2D.Double south = new Point2D.Double(hitBox.getX()+hitBox.getWidth()/2,hitBox.getY()+hitBox.getHeight());
	private Point2D.Double west = new Point2D.Double(hitBox.getX(),hitBox.getY()+hitBox.getHeight()/2);
	
	public Entity()
	{
		setIcon(ImageLoader.getImage("noTexture"));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos)
	{
		setXPos(XPos);
		setYPos(YPos);
		setIcon(ImageLoader.getImage("noTexture"));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, boolean isSolid)
	{
		setXPos(XPos);
		setYPos(YPos);
		setSolid(isSolid);
		setIcon(ImageLoader.getImage("noTexture"));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, boolean isSolid, String iconName)
	{
		setXPos(XPos);
		setYPos(YPos);
		setSolid(isSolid);
		setIcon(ImageLoader.getImage(iconName));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, int rotation, boolean isSolid, int id, String iconName)
	{
		setXPos(XPos);
		setYPos(YPos);
		setSolid(isSolid);
		setID(id);
		setRotation(rotation);
		setIcon(ImageLoader.getImage(iconName));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, float rotation, int id, String iconName)
	{
		setXPos(XPos);
		setYPos(YPos);
		setRotation(rotation);
		setIcon(ImageLoader.getImage(iconName));
		setID(id);
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, float rotation, String seriesName, float delay)
	{
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setRotation(rotation);
		setSolid(true);
		setIcon(ImageLoader.getImageSeries(seriesName)[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries(seriesName),delay));
		setNormalIcon(getIcon());
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, float rotation, String seriesName, float delay, boolean startAtZeroFrame)
	{
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setRotation(rotation);
		setSolid(true);
		setIcon(ImageLoader.getImageSeries(seriesName)[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries(seriesName),delay,startAtZeroFrame));
		setNormalIcon(getIcon());
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, float rotation, boolean isSolid, String iconName)
	{
		setXPos(XPos);
		setYPos(YPos);
		setSolid(isSolid);
		setRotation(rotation);
		setIcon(ImageLoader.getImage(iconName));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, float rotation, boolean isSolid, float opacity, String iconName)
	{
		setXPos(XPos);
		setYPos(YPos);
		setSolid(isSolid);
		setRotation(rotation);
		setOpacity(opacity);
		setIcon(ImageLoader.getImage(iconName));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, String iconName, int ID)
	{
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setIcon(ImageLoader.getImage(iconName));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, String iconName, int rotation, int ID, String lore)
	{
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setRotation(rotation);
		setIcon(ImageLoader.getImage(iconName));
		setLore(lore);
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, String iconName, int rotation, boolean isSolid, int ID)
	{
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setRotation(rotation);
		setSolid(isSolid);
		setIcon(ImageLoader.getImage(iconName));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, String[] icons, int rotation, boolean isSolid, int ID, String lore)
	{
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setRotation(rotation);
		setIcons(icons);
		setSolid(isSolid);
		setLore(lore);
		updateHitBox();
	}

	/**
	 * Updates the current image for the Entity if using the Animator Class
	 */
	public void updateImage()
	{
		if(getAnimator() != null)
		{
			setIcon(getAnimator().getCurrentImage());
		}
	}
	
	/**
	 * Updates the current image for the Entity if using the Animator Class
	 */
	public void updateRandomImage()
	{
		if(getAnimator() != null)
		{
			setIcon(getAnimator().getRandomImage());
		}
	}
	
	/**
	 * Moves the Entity according to its type
	 */
	public void moveAI(double timeElapsed)
	{	
		if(dynamic && neutral)
		{
			
		}
		else if(dynamic && friendly)
		{
			
		}
		else if(dynamic && hostile)
		{			
			
		}
	}
	
	/**
	 * Moves the Entity closer to the given position every call
	 * @return True if the target position has been reached, otherwise False
	 */
	public boolean moveTo(float targetX, float targetY, float speed)
	{
		if(targetX == -1)
		{
			targetX = XPosTarget;
		}
		else
		{
			XPosTarget = targetX;
		}
		
		if(targetY == -1)
		{
			targetY = YPosTarget;
		}
		else
		{
			YPosTarget = targetY;
		}
		
		if(speed == -1)
		{
			speed = this.speed;
		}
		
		getLastGoodPos().setLocation((int)getXPos(),(int)getYPos());
		
		float xDistance = XPosTarget - XPos;
		float yDistance = YPosTarget - YPos;
		double direction = Math.atan2(yDistance,xDistance);
		setRotation((float)Math.toDegrees(direction)+90);
		
		XPos += speed*Math.cos(direction)*getMovementMultiplier();
		YPos += speed*Math.sin(direction)*getMovementMultiplier();
		
		if(collisionDetect())
		{
			setXPos((int) getLastGoodPos().getX());
			setYPos((int) getLastGoodPos().getY());
			
			/*
			//Find alternate route around obstacle in path
			XPos += speed*Math.cos(direction-90);
			YPos += speed*Math.sin(direction-90);
			
			if(collisionDetect())
			{
				setXPos((int) getLastGoodPos().getX());
				setYPos((int) getLastGoodPos().getY());
				
				XPos += speed*Math.cos(direction-180);
				YPos += speed*Math.sin(direction-180);
				
				if(collisionDetect())
				{
					setXPos((int) getLastGoodPos().getX());
					setYPos((int) getLastGoodPos().getY());
					
					XPos += speed*Math.cos(direction-270);
					YPos += speed*Math.sin(direction-270);
				}
			}
			*/
		}
		
		//If the target has been reached then retun true else false
		if(Logic.getDistance(XPosTarget,YPosTarget,XPos,YPos) <= speed*getMovementMultiplier())
		{
			XPos = targetX;
			YPos = targetY;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the Entity has collided
	 * @return True if a collision has occured, otherwise False
	 */
	public boolean collisionDetect()
	{	
		for(int k = 0; k < Tracker.everything.size(); k++)
		{
			for(int j = 0; j < Tracker.everything.get(k).size(); j++)
			{
				if(getHitBox().intersects(((Entity)Tracker.everything.get(k).get(j)).getHitBox())
						&& (((Entity)Tracker.everything.get(k).get(j)).getID() != getID()) 
						&& (((Entity)Tracker.everything.get(k).get(j)).isSolid()))
				{
					return true;//if current Entity collides with any object (excluding itself) then return true
				}
				
				
				if(this.equals(Logic.player))
				{
					Area temp;
					//collison with top of screen
					temp = new Area(getHitBox());
					temp.intersect(Tracker.top);
					if(!temp.isEmpty())
					{
						Logic.player.setYPos(Logic.screenHeight);
						Logic.currentWorld.setLocation((int)Logic.currentWorld.getX(),(int)Logic.currentWorld.getY()+1);
					}
				
					//collison with bottom of screen
					temp = new Area(getHitBox());
					temp.intersect(Tracker.bottom);
					if(!temp.isEmpty())
					{
						Logic.player.setYPos(-25);
						Logic.currentWorld.setLocation((int)Logic.currentWorld.getX(),(int)Logic.currentWorld.getY()-1);
					}
				
					//collison with right of screen
					temp = new Area(getHitBox());
					temp.intersect(Tracker.right);
					if(!temp.isEmpty())
					{
						Logic.player.setXPos(-25);
						Logic.currentWorld.setLocation((int)Logic.currentWorld.getX()+1,(int)Logic.currentWorld.getY());
					}
				
					//collison with left of screen
					temp = new Area(getHitBox());
					temp.intersect(Tracker.left);
					if(!temp.isEmpty())
					{
						Logic.player.setXPos(Logic.screenWidth);
						Logic.currentWorld.setLocation((int)Logic.currentWorld.getX()-1,(int)Logic.currentWorld.getY());
					}
				}
				
				//collision with any other wall
				Area hitbox = new Area(getHitBox());
				hitbox.intersect(Tracker.wallZone);
				if(!hitbox.isEmpty())     
				{
					return true;
				}
			}	
		}
		return false;
	}
	
	/**
	 * Checks if the Entity has collided (Unfinished)
	 * @return True if a collision has occured, otherwise False
	 */
	public boolean collisionHandler()
	{
		boolean collided = false;
		for(int k = 0; k < Tracker.everything.size(); k++)
		{
			for(int j = 0; j < Tracker.everything.get(k).size(); j++)
			{
				Area hitbox = new Area(getHitBox());
				hitbox.intersect(Tracker.wallZone);
				
				if((getHitBox().intersects(((Entity)Tracker.everything.get(k).get(j)).getHitBox())
						&& (((Entity)Tracker.everything.get(k).get(j)).getID() != getID()) 
						&& (((Entity)Tracker.everything.get(k).get(j)).isSolid())) || !hitbox.isEmpty())
				{
					//Check north and south points
					if(((Entity)Tracker.everything.get(k).get(j)).getHitBox().contains(getNorth())||
							((Entity)Tracker.everything.get(k).get(j)).getHitBox().contains(getSouth()))
					{
						setYVelocity(-getYVelocity());
					}
					//Check east and west points
					if(((Entity)Tracker.everything.get(k).get(j)).getHitBox().contains(getEast())||
							((Entity)Tracker.everything.get(k).get(j)).getHitBox().contains(getWest()))
					{
						setXVelocity(-getXVelocity());
					}
					collided = true;
				}
			}	
		}
		return collided;
	}
	
	/**
	 * Calculate angle in degrees to target location
	 * @return Angle in degrees to target
	 */
	public float getAngleToTarget(double targetx, double targety) {
	    float angle = (float) Math.toDegrees(Math.atan2(targety - getYPos(), targetx - getXPos()));

	    if(angle < 0){
	        angle += 360;
	    }

	    return angle;
	}
	
	public void updateHitBox(){
		this.iconWidth = icon.getWidth();
		this.iconHeight = icon.getHeight();
		this.hitBox = new Rectangle2D.Double(XPos,YPos,iconWidth-1,iconHeight-1);
	}
	
	public void addPoints(int points){
		this.points += points;
	}
	
	public void addMoney(int money){
		this.money += money;
	}

	public float getXPos() {
		return XPos;
	}

	public void setXPos(float xPos) {
		XPos = xPos;
	}

	public float getYPos() {
		return YPos;
	}

	public void setYPos(float yPos) {
		YPos = yPos;
	}

	public float getXSpawn() {
		return XSpawn;
	}

	public void setXSpawn(float xSpawn) {
		XSpawn = xSpawn;
	}

	public float getYSpawn() {
		return YSpawn;
	}

	public void setYSpawn(float ySpawn) {
		YSpawn = ySpawn;
	}

	public float getXVelocity() {
		return XVelocity;
	}

	public void setXVelocity(float xVelocity) {
		XVelocity = xVelocity;
	}
	
	public void addXVelocity(float addXVelocity) {
		XVelocity += addXVelocity;
	}

	public float getYVelocity() {
		return YVelocity;
	}

	public void setYVelocity(float yVelocity) {
		YVelocity = yVelocity;
	}
	
	public void addYVelocity(float addYVelocity) {
		YVelocity += addYVelocity;
	}

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getCurrentCooldown() {
		return currentCooldown;
	}

	public void setCurrentCooldown(int currentCooldown) {
		this.currentCooldown = currentCooldown;
	}

	public int getMaxCooldown() {
		return maxCooldown;
	}

	public void setMaxCooldown(int maxCooldown) {
		this.maxCooldown = maxCooldown;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getLore() {
		return lore;
	}

	public void setLore(String lore) {
		this.lore = lore;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}
	
	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isNeutral() {
		return neutral;
	}

	public void setNeutral(boolean neutral) {
		this.neutral = neutral;
	}

	public boolean isFriendly() {
		return friendly;
	}

	public void setFriendly(boolean friendly) {
		this.friendly = friendly;
	}

	public boolean isHostile() {
		return hostile;
	}

	public void setHostile(boolean hostile) {
		this.hostile = hostile;
	}

	public int getIconNum() {
		return iconNum;
	}

	public void setIconNum(int iconNum) {
		this.iconNum = iconNum;
	}

	public int getIconsLength() {
		return icons.length;
	}
	
	public String[] getIcons() {
		return icons;
	}

	public void setIcons(String[] icons) {
		this.icons = icons;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
		updateHitBox();
	}

	public int getIconWidth() {
		return iconWidth;
	}

	public void setIconWidth(int iconWidth) {
		this.iconWidth = iconWidth;
	}

	public int getIconHeight() {
		return iconHeight;
	}

	public void setIconHeight(int iconHeight) {
		this.iconHeight = iconHeight;
	}

	public Rectangle2D getHitBox() {
		updateHitBox();
		return hitBox;
	}

	public void setHitBox(Rectangle2D hitBox) {
		this.hitBox = hitBox;
	}
	
	public Point getLastGoodPos() {
		return lastGoodPos;
	}

	public void setLastGoodPos(Point lastGoodPos) {
		this.lastGoodPos = lastGoodPos;
	}
	
	public boolean isDead(){
		if(this.health < 0)
			return true;
		return false;
	}

	public Animator getAnimator() {
		return animator;
	}

	public void setAnimator(Animator animator) {
		this.animator = animator;
	}

	public Point2D.Double getNorth() {
		return north;
	}

	public void setNorth(Point2D.Double north) {
		this.north = north;
	}

	public Point2D.Double getEast() {
		return east;
	}

	public void setEast(Point2D.Double east) {
		this.east = east;
	}

	public Point2D.Double getSouth() {
		return south;
	}

	public void setSouth(Point2D.Double south) {
		this.south = south;
	}

	public Point2D.Double getWest() {
		return west;
	}

	public void setWest(Point2D.Double west) {
		this.west = west;
	}

	public float getMinVelocity() {
		return minVelocity;
	}

	public void setMinVelocity(float minVelocity) {
		this.minVelocity = minVelocity;
	}
	
	public float getMaxVelocity() {
		return maxVelocity;
	}

	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	public float getXPosTarget() {
		return XPosTarget;
	}

	public void setXPosTarget(float xPosTarget) {
		XPosTarget = xPosTarget;
	}

	public float getYPosTarget() {
		return YPosTarget;
	}

	public void setYPosTarget(float yPosTarget) {
		YPosTarget = yPosTarget;
	}

	public BufferedImage getNormalIcon() {
		return normalIcon;
	}

	public void setNormalIcon(BufferedImage normalIcon) {
		this.normalIcon = normalIcon;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public float getMovementMultiplier() 
	{
		return movementMultiplier;
	}

	public void setMovementMultiplier(float timeMultiplier) 
	{
		this.movementMultiplier = timeMultiplier;
	}
}
