
//Alec Ibarra
public class Player extends Entity{
	
	public Player()
	{
		setDynamic(true);
		setIcon(ImageLoader.getImageSeries("player")[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),0.5f));
		updateHitBox();
	}
	
	public Player(boolean isSolid)
	{
		setDynamic(true);
		setSolid(isSolid);
		setIcon(ImageLoader.getImageSeries("player")[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),0.5f));
		updateHitBox();
	}
	
	public Player(float XPos, float YPos)
	{
		setDynamic(true);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setIcon(ImageLoader.getImageSeries("player")[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),0.5f));
		updateHitBox();
	}
	
	public Player(float XPos, float YPos, boolean isSolid)
	{
		setDynamic(true);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setSolid(isSolid);
		setIcon(ImageLoader.getImageSeries("player")[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),0.5f));
		updateHitBox();
	}
	
	public Player(float XPos, float YPos, int ID)
	{
		setDynamic(true);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setID(ID);
		setIcon(ImageLoader.getImageSeries("player")[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),0.5f));
		updateHitBox();
	}
	
	public Player(float XPos, float YPos, int ID, int teamID)
	{
		setDynamic(true);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setID(ID);
		setTeamID(teamID);
		setIcon(ImageLoader.getImageSeries("player")[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),0.2f));
		setNormalIcon(getIcon());
		updateHitBox();
	}
	
	public Player(float XPos, float YPos, int ID, int teamID, float speed)
	{
		setDynamic(true);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setID(ID);
		setSpeed(speed);
		setTeamID(teamID);
		setIcon(ImageLoader.getImageSeries("player")[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),0.2f));
		setNormalIcon(getIcon());
		updateHitBox();
	}
	
	public Player(float XPos, float YPos, int ID, int teamID, float rotation, float speed)
	{
		setDynamic(true);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setID(ID);
		setSpeed(speed);
		setTeamID(teamID);
		setRotation(rotation);
		setIcon(ImageLoader.getImageSeries("player")[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),0.2f));
		setNormalIcon(getIcon());
		updateHitBox();
	}
	
	public Player(float XPos, float YPos, int ID, int teamID, boolean AIHostile)
	{
		setDynamic(true);
		setXPos(XPos);
		setYPos(YPos);
		setXSpawn(XPos);
		setYSpawn(YPos);
		setID(ID);
		setTeamID(teamID);
		setIcon(ImageLoader.getImageSeries("player")[0]);
		setAnimator(new Animator(ImageLoader.getImageSeries("player"),0.5f));
		setHostile(AIHostile);
		updateHitBox();
	}
	
	public void updateImage()
	{
		if(Keybinds.getActionValue("up") || Keybinds.getActionValue("down") || Keybinds.getActionValue("left") || Keybinds.getActionValue("right"))//if moving
			setIcon(getAnimator().getCurrentImage());
		else
			setIcon(getNormalIcon());//else no motion icon
	}
	
	public void updateRandomImage()
	{
		setIcon(getAnimator().getRandomImage());
	}
	
	public void move(double movementMultiplier)
	{	
		//Something ELSE has moved INTO the player, remove player collision
		if(collisionDetect())
		{
			this.setSolid(false);
		}
		else
		{
			this.setSolid(true);
		}
			
		//For using mouse player spin control scheme
		if(Logic.ControlsSpinMouse)
		{
			setRotation(getAngleToTarget(Logic.mousex-13,Logic.mousey-11)+90);
		}
		
		//Fixes rotation value
		if (getRotation() > 360)
		{
			setRotation(getRotation() - 360);
		}
		else if (getRotation() < 0)
		{
			setRotation(getRotation() + 360);
		}
		
		//Rotation in rads for furture use
		double radsRotation = Math.toRadians(getRotation()-90);
		
		//If player is not colliding with something
		if(!collisionDetect())
		{
			//save position as good position
			getLastGoodPos().setLocation((int)getXPos(),(int)getYPos());
			
			if (Keybinds.getActionValue("spinReset"))
			{
				if(Logic.ControlsSpinKey1)
				{
					if(getRotation() == 0)
					{
						setRotation(90);
					}
					else if(getRotation() == 90)
					{
						setRotation(180);
					}
					else if(getRotation() == 180)
					{
						setRotation(270);
					}
					else
					{
						setRotation(0);
					}
					Keybinds.setActionValue("spinReset",false);
				}
			}
			
			if (Keybinds.getActionValue("up"))
			{
				if(Logic.ControlsMove1)
				{
					setYPos((float)(getYPos() - getSpeed() * movementMultiplier));
				}
				else if(Logic.ControlsMove2)
				{
					setXPos((float)(getXPos() + getSpeed() * movementMultiplier * Math.cos(radsRotation)));
					setYPos((float)(getYPos() + getSpeed() * movementMultiplier * Math.sin(radsRotation)));
				}
				else if(Logic.ControlsMove3)
				{
					setXPos((float)(getXPos() + getSpeed() * movementMultiplier * Math.cos(radsRotation)));
					setYPos((float)(getYPos() + getSpeed() * movementMultiplier * Math.sin(radsRotation)));
				}
				else if(Logic.ControlsMove4)
				{
					setYPos((float)(getYPos() - getSpeed() * movementMultiplier));
					setRotation(0);
				}
			}
			
			if (Keybinds.getActionValue("down"))
			{
				if(Logic.ControlsMove1)
				{
					setYPos((float)(getYPos() + getSpeed() * movementMultiplier));
				}
				else if(Logic.ControlsMove2)
				{
					setXPos((float)(getXPos() - getSpeed() * movementMultiplier * Math.cos(radsRotation)));
			    	setYPos((float)(getYPos() - getSpeed() * movementMultiplier * Math.sin(radsRotation)));
				}
				else if(Logic.ControlsMove3)
				{
					setXPos((float)(getXPos() - getSpeed() * movementMultiplier * Math.cos(radsRotation)));
					setYPos((float)(getYPos() - getSpeed() * movementMultiplier * Math.sin(radsRotation)));
				}
				else if(Logic.ControlsMove4)
				{
					setYPos((float)(getYPos() + getSpeed() * movementMultiplier));
					setRotation(180);
				}
			}
			
			if (Keybinds.getActionValue("right"))
			{
				if(Logic.ControlsMove1)
				{
					setXPos((float)(getXPos() + getSpeed() * movementMultiplier));
				}
				else if(Logic.ControlsMove2)
				{
					setXPos((float)(getXPos() + getSpeed() * movementMultiplier * Math.cos(Math.toRadians(getRotation()-0))));
					setYPos((float)(getYPos() + getSpeed() * movementMultiplier * Math.sin(Math.toRadians(getRotation()-0))));
				}
				else if(Logic.ControlsSpinKey3)
				{
					if(getRotation() == 0)
					{
						setRotation(90);
					}
					else if(getRotation() == 90)
					{
						setRotation(180);
					}
					else if(getRotation() == 180)
					{
						setRotation(270);
					}
					else
					{
						setRotation(0);
					}
					Keybinds.setActionValue("right",false);
				}
				else if(Logic.ControlsMove4)
				{
					setXPos((float)(getXPos() + getSpeed() * movementMultiplier));
					setRotation(90);
				}
			}
			
			if (Keybinds.getActionValue("left"))
			{
				if(Logic.ControlsMove1)
				{
					setXPos((float)(getXPos() - getSpeed() * movementMultiplier));
				}
				else if(Logic.ControlsMove2)
				{
					setXPos((float)(getXPos() + getSpeed() * movementMultiplier * Math.cos(Math.toRadians(getRotation()-180))));
					setYPos((float)(getYPos() + getSpeed() * movementMultiplier * Math.sin(Math.toRadians(getRotation()-180))));
				}
				else if(Logic.ControlsSpinKey3)
				{
					if(getRotation() == 0)
					{
						setRotation(270);
					}
					else if(getRotation() == 270)
					{
						setRotation(180);
					}
					else if(getRotation() == 180)
					{
						setRotation(90);
					}
					else
					{
						setRotation(0);
					}
					Keybinds.setActionValue("left",false);
				}
				else if(Logic.ControlsMove4)
				{
					setXPos((float)(getXPos() - getSpeed() * movementMultiplier));
					setRotation(270);
				}
			}
			
			if (Keybinds.getActionValue("spinLeft"))
			{
				if(Logic.ControlsSpinKey1)
				{
					setRotation(getRotation()-4);
				}
				else if(Logic.ControlsSpinKey2)
				{
					if(getRotation() == 0)
					{
						setRotation(270);
					}
					else if(getRotation() == 270)
					{
						setRotation(180);
					}
					else if(getRotation() == 180)
					{
						setRotation(90);
					}
					else
					{
						setRotation(0);
					}
					Keybinds.setActionValue("spinLeft",false);
				}
			}
			
			if (Keybinds.getActionValue("spinRight"))
			{
				if(Logic.ControlsSpinKey1)
				{
					setRotation(getRotation()+4);
				}
				else if(Logic.ControlsSpinKey2)
				{
					if(getRotation() == 0)
					{
						setRotation(90);
					}
					else if(getRotation() == 90)
					{
						setRotation(180);
					}
					else if(getRotation() == 180)
					{
						setRotation(270);
					}
					else
					{
						setRotation(0);
					}
					Keybinds.setActionValue("spinRight",false);
				}
			}
			
			if(Logic.ControlsMove4)
			{
				if(Keybinds.getActionValue("up") && Keybinds.getActionValue("right"))
				{
					setXPos((int) getLastGoodPos().getX());
					setYPos((int) getLastGoodPos().getY());
					
					setXPos((float) (getXPos() + getSpeed() * movementMultiplier));
					setYPos((float) (getYPos() - getSpeed() * movementMultiplier));
					setRotation(45);
				}
				else if(Keybinds.getActionValue("up") && Keybinds.getActionValue("left"))
				{
					setXPos((int) getLastGoodPos().getX());
					setYPos((int) getLastGoodPos().getY());
					
					setXPos((float) (getXPos() - getSpeed() * movementMultiplier));
					setYPos((float) (getYPos() - getSpeed() * movementMultiplier));
					setRotation(315);
				}
				else if(Keybinds.getActionValue("down") && Keybinds.getActionValue("right"))
				{
					setXPos((int) getLastGoodPos().getX());
					setYPos((int) getLastGoodPos().getY());
					
					setXPos((float) (getXPos() + getSpeed() * movementMultiplier));
					setYPos((float) (getYPos() + getSpeed() * movementMultiplier));
					setRotation(135);
				}
				else if(Keybinds.getActionValue("down") && Keybinds.getActionValue("left"))
				{
					setXPos((int) getLastGoodPos().getX());
					setYPos((int) getLastGoodPos().getY());
					
					setXPos((float) (getXPos() - getSpeed() * movementMultiplier));
					setYPos((float) (getYPos() + getSpeed() * movementMultiplier));
					setRotation(225);
				}
			}
			
			if (Logic.ControlsMove5)
			{			
				
				
				if(Keybinds.getActionValue("up") && !Keybinds.getActionValue("right") && !Keybinds.getActionValue("left"))
				{
					getLastGoodPos().setLocation((int)getXPos(),(int)getYPos());
					
					setRotation(0);
					addYVelocity((float) (-getSpeed()*movementMultiplier));
				}
				else if(Keybinds.getActionValue("down") && !Keybinds.getActionValue("right") && !Keybinds.getActionValue("left"))
				{
					getLastGoodPos().setLocation((int)getXPos(),(int)getYPos());
					
					setRotation(180);
					addYVelocity((float) (getSpeed()*movementMultiplier));
				}
				else if(Keybinds.getActionValue("right") && !Keybinds.getActionValue("up") && !Keybinds.getActionValue("down"))
				{
					getLastGoodPos().setLocation((int)getXPos(),(int)getYPos());
					
					setRotation(90);
					addXVelocity((float) (getSpeed()*movementMultiplier));
				}
				else if(Keybinds.getActionValue("left") && !Keybinds.getActionValue("up") && !Keybinds.getActionValue("down"))
				{
					getLastGoodPos().setLocation((int)getXPos(),(int)getYPos());
					
					setRotation(270);
					addXVelocity((float) (-getSpeed()*movementMultiplier));
				}
				else if(Keybinds.getActionValue("up") && Keybinds.getActionValue("right"))
				{
					getLastGoodPos().setLocation((int)getXPos(),(int)getYPos());
					
					setRotation(45);
					addXVelocity((float) ((float)(Math.cos(radsRotation))*getSpeed()*movementMultiplier));
					addYVelocity((float) ((float)(Math.sin(radsRotation))*getSpeed()*movementMultiplier));
				}
				else if(Keybinds.getActionValue("up") && Keybinds.getActionValue("left"))
				{
					getLastGoodPos().setLocation((int)getXPos(),(int)getYPos());
					
					setRotation(315);
					addXVelocity((float) ((float)(Math.cos(radsRotation))*getSpeed()*movementMultiplier));
					addYVelocity((float) ((float)(Math.sin(radsRotation))*getSpeed()*movementMultiplier));
				}
				else if(Keybinds.getActionValue("down") && Keybinds.getActionValue("right"))
				{
					getLastGoodPos().setLocation((int)getXPos(),(int)getYPos());
					
					setRotation(135);
					addXVelocity((float) ((float)(Math.cos(radsRotation))*getSpeed()*movementMultiplier));
					addYVelocity((float) ((float)(Math.sin(radsRotation))*getSpeed()*movementMultiplier));
				}
				else if(Keybinds.getActionValue("down") && Keybinds.getActionValue("left"))
				{
					getLastGoodPos().setLocation((int)getXPos(),(int)getYPos());
					
					setRotation(225);
					addXVelocity((float) ((float)(Math.cos(radsRotation))*getSpeed()*movementMultiplier));
					addYVelocity((float) ((float)(Math.sin(radsRotation))*getSpeed()*movementMultiplier));
				}
				
				if(getXVelocity() > 0 || getXVelocity() < 0)
				{
					if(getXVelocity() > 0)//if velocity is positive
						setXVelocity(getXVelocity()-getFriction());
					else if(getXVelocity() < 0)//if velocity is negative
						setXVelocity(getXVelocity()+getFriction());
					
					if(getXVelocity() > getMaxVelocity())//limits velocity
						setXVelocity(getMaxVelocity());
					else if(getXVelocity() < -getMaxVelocity())
						setXVelocity(-getMaxVelocity());
				
					if(getXVelocity() > -getMinVelocity() && getXVelocity() < getMinVelocity())
					{
						setXVelocity(0);
					}
				}
			
				if(getYVelocity() > 0 || getYVelocity() < 0)
				{
					if(getYVelocity() > 0)//if velocity is positive
						setYVelocity(getYVelocity()-getFriction());
					else if(getYVelocity() < 0)//if velocity is negative
						setYVelocity(getYVelocity()+getFriction());
					
					if(getYVelocity() > getMaxVelocity())//limits velocity
						setYVelocity(getMaxVelocity());
					else if(getYVelocity() < -getMaxVelocity())
						setYVelocity(-getMaxVelocity());
				
					if(getYVelocity() > -getMinVelocity() && getYVelocity() < getMinVelocity())
					{
						setYVelocity(0);
					}
				}
				
				setXPos((float)(getXPos()+getXVelocity()));
				setYPos((float)(getYPos()+getYVelocity()));
				
				if (collisionDetect())
				{
					setXPos((int) getLastGoodPos().getX());
					setYPos((int) getLastGoodPos().getY());
				}
				
			}
			
			if (collisionDetect())
			{
				setXPos((int) getLastGoodPos().getX());
				setYPos((int) getLastGoodPos().getY());
			}
			
		}
		else
		{
			setXPos((int) getLastGoodPos().getX());
			setYPos((int) getLastGoodPos().getY());
		}
	}

}

