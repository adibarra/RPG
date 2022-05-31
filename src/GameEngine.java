import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

//Alec Ibarra
public class GameEngine
{

	public static void main(String[] args)
    {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.startsWith("mac os x")) 
		{
			//Makes Command+Q activate the windowClosing windowEvent
			System.setProperty("apple.eawt.quitStrategy","CLOSE_ALL_WINDOWS");
		}
    	
		Logic game = new Logic();
    	
		game.setMinimumSize(new Dimension(1000+game.getInsets().left+game.getInsets().right,
				650+game.getInsets().top+game.getInsets().bottom));
		game.setVisible(true);
				
		game.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
		});
		
		game.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				game.cleanUp();
				System.exit(0);
			}
		});
    }
}

 /**
 * 
 * @author Alec Ibarra
 * 
 **/
class Logic extends JFrame implements MouseListener,MouseMotionListener,KeyListener,MouseWheelListener,Serializable
{
	//game initialization
	private static final long serialVersionUID = 1L;
	static BufferedImage offscreen = null;
    static Graphics g2;
    static RenderingHints rh;
    static int screenWidth = 1000;//start size will be adjusted
    static int screenHeight = 650;//start size will be adjusted
    static int currentWidth = screenWidth;
    static int currentHeight = screenHeight;
    static int offsetX = 0;//will be adjusted based on OS
    static int offsetY = 0;//will be adjusted based on OS
    static int offsetX2 = 0;//will be adjusted based on OS
    static int offsetY2 = 0;//will be adjusted based on OS
    static boolean cursorHand = false;
    static String backgroundType = "grass";//default,space,grass,sand,sandred,sandgreen,tile,(CUSTOM)
    static final double GAME_VERSION = 0.0;
    static final String PROJECT_NAME = "RPG";
    static String clientName = "Client";
    static Point currentWorld = new Point(0,0);//TODO REVERT THIS
    static Point lastCurrentWorld = new Point(0,0);
    
    //game state controls
    static boolean gameOver = false;
    static boolean paused = true;
    static boolean gameStarted = false;
    static boolean showControls = false;
    static boolean showSettings= false;
    static boolean typing = false;
    static boolean offlineMode = false;
    static boolean multiplayer = false;
    static boolean multiplayerEnabled = false;
    
    //game control
    static final float TARGET_FRAME_TIME_120 = 8.3f;//120FPS
    static final float TARGET_FRAME_TIME_60 = 16.7f;//60FPS
	static final float TARGET_FRAME_TIME_30 = 33.3f;//30FPS
	static final float TARGET_FRAME_TIME_15 = 66.7f;//15FPS
    static float TARGET_FRAME_TIME = TARGET_FRAME_TIME_60;//default option
	static double elapsed = TARGET_FRAME_TIME_60;
	static double start = System.nanoTime();

	//control settings
	static boolean ControlsSpinMouse = false; //Use mouse movement for spinning
	static boolean ControlsSpinKey1 = false;  //Use spinReset to snap rotation and spin right/left keys for spinning
	static boolean ControlsSpinKey2 = false;  //Use spin right/left keys to snap rotation counter-clockwise/clockwise 
	static boolean ControlsSpinKey3 = false;  //Use  A D keys to snap rotation counter-clockwise/clockwise 
	static boolean ControlsMove1 = false;     //Use WASD to move player no auto rotate
	static boolean ControlsMove2 = false;     //Use WASD to move player based on current rotation
	static boolean ControlsMove3 = false;     //Use W S  to move player based on current rotation
	static boolean ControlsMove4 = true;      //Use WASD to move player with auto rotate without velocity
	static boolean ControlsMove5 = false;     //Use WASD to move player with auto rotate and use velocity
	
	//game menu
	static boolean tsShown = true;
	static boolean option2 = false;
	static boolean option3 = false;
	static boolean option4 = false;
	static boolean option5 = false;
	static boolean option6 = false;
	static boolean option7 = false;
	static boolean option8 = false;
	
	//game mechanics control
	static int mousex = -100;
	static int mousey = -100;
	
	//class instances and etc.
	static Command cmd = new Command();
	static FileLoader fl = new FileLoader();
	static ImageLoader il = new ImageLoader();
	static AudioLoader al = new AudioLoader();
	static Player player = new Player(50,100,Tracker.r.nextInt(),Tracker.r.nextInt(),90,1.5f);
	static ChatTextScreen ts = new ChatTextScreen(new Rectangle2D.Double(0,475,300,175));
		
	//temp classes
	NPC tempNPC = new NPC();
	Wall tempWall = new Wall();
	Entity tempEntity = new Entity();
	Player tempPlayer = new Player();
	Button tempButton = new Button();
	Slider tempSlider = new Slider();
	Particle tempParticle = new Particle();
	Animator tempAnimator = new Animator();
	ProgressBar tempProgressBar = new ProgressBar();
	
	
	public Logic()
    {
		/*********************************Initialize Game***********************************/
        super("Game Engine");//sets program name
        List<Image> icons = new ArrayList<Image>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon2048x2048.png")));//loads icon 2048x2048
        //icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon1024x1024.png")));//loads icon 1024x1024
        //icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon0512x0512.png")));//loads icon 512x512
        //icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon0256x0256.png")));//loads icon 256x256
        //icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon0128x0128.png")));//loads icon 128x128
        super.setIconImages(icons);//loads icons
        addMouseListener(this);//adds neccessary listener
		addMouseMotionListener(this);//adds neccessary listener
		addMouseWheelListener(this);//adds neccessary listener
		addKeyListener(this);//adds neccessary listener
        requestFocusInWindow();//calls focus for window
        rh = new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        
        /*********************************Setting up JFrame*********************************/
        setResizable(false);
        pack();
        offsetX = super.getInsets().left;
        offsetY = super.getInsets().top;
        offsetX2 = super.getInsets().right;
        offsetY2 = super.getInsets().bottom;
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
        /*********************************Setting up GUIs***********************************/
        Tracker.hudButtons.add(      new Button(973,   2,  25, 25,     true, "PAUSE"       ));
        Tracker.titleButtons.add(    new Button(150, 380, 150, 75,    false, "CONTROLS" ,20));
        Tracker.titleButtons.add(    new Button(400, 380, 200, 75,    false, "PLAY"     ,48));
        Tracker.titleButtons.add(    new Button(700, 380, 150, 75,    false, "SETTINGS" ,20));
        Tracker.pauseButtons.add(    new Button(400,  80, 200, 75,    false, "CONTROLS" ,20));
        Tracker.pauseButtons.add(    new Button(400, 230, 200, 75,    false, "EXIT"     ,20));
        Tracker.pauseButtons.add(    new Button(400, 380, 200, 75,    false, "SETTINGS" ,20));
        Tracker.pauseButtons.add(    new Button(  2,   2,  25, 25,     true, "BACK"        ));
        Tracker.controlsButtons.add( new Button(  2,   2,  25, 25,     true, "BACK"        ));
        Tracker.settingsButtons.add( new Button(  2,   2,  25, 25,     true, "BACK"        ));
        Tracker.settingsButtons.add( new Button( 50, 130, 200, 25, "toggle", "Show Chat"   ));
        Tracker.settingsButtons.add( new Button( 50, 180, 200, 25, "toggle", "OPTION2"     ));
        Tracker.settingsButtons.add( new Button( 50, 230, 200, 25, "toggle", "OPTION3"     ));
        Tracker.settingsButtons.add( new Button( 50, 280, 200, 25, "toggle", "OPTION4"     ));
        Tracker.settingsButtons.add( new Button(400, 130, 200, 25, "toggle", "OPTION5"     ));
        Tracker.settingsButtons.add( new Button(400, 180, 200, 25, "toggle", "OPTION6"     ));
        Tracker.settingsButtons.add( new Button(400, 230, 200, 25, "toggle", "OPTION7"     ));
        Tracker.settingsButtons.add( new Button(400, 280, 200, 25, "toggle", "OPTION8"     ));
        
        Tracker.settingsSliders.add( new Slider(250, 350, 200,  4,new String[]{"120FPS","60FPS","30FPS","15FPS"},1,"TARGET FPS"));
        Tracker.settingsSliders.add( new Slider(250, 400, 200,  5,new String[]{"MUTE","25%","50%","75%","100%"},2,"VOLUME"));
                
        /*********************************Setting up Misc************************************/
        Keybinds.prepare();
        Tracker.prepare();
        Tracker.genWorldFromSave(this,currentWorld);
        Tracker.players.add(player);
        Quests.prepare();
        
        /*********************************Setting up Saves**********************************/
        fl.prepare();
        fl.firstLoad();
        
        /*********************************Start Server search*******************************/
        Client.prepare();
        Client.scan();
    }
	
	/***************************************** 
					Main Loop
	*****************************************/
	
	public void update(Graphics g)//main method
    {    	
		//prepare to run loop
		offscreen = (BufferedImage)createImage((int)getSize().getWidth(),(int)getSize().getHeight());
		g2 = offscreen.getGraphics();
		g2.translate(offsetX,offsetY);
	    ((Graphics2D) g2).setRenderingHints(rh);
		
		if(multiplayer && !offlineMode)
		{
			Client.start();
		}
		else if(!offlineMode)
		{
			Client.stopClient();
		}
		
		if(!gameOver)
		{	
			if(!paused)
			{				
				elapsed = (System.nanoTime()-start)/1000000000;//records looptime at this point in seconds
				runLogic();//main logic method
				display(g2);//main display method
			}
			else
			{				
				if(!gameStarted && !showControls && !showSettings)//draws splash screen
				{
					drawSplashScreen(g2);
				}
				else if(showControls && !showSettings)//draws splash screen
				{
					drawControlsScreen(g2);
				}
				else if(!showControls && showSettings)//draws splash screen
				{
					drawSettingsScreen(g2);
				}
				else if(!showSettings && !showControls && gameStarted && paused)//draws paused menu
				{	
					drawPauseScreen(g2);
				}
			}
		}
		else
		{	
			drawGameOver(g2);//draw GameOver
		}
		
		if(!cursorHand)
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		else
			cursorHand = false;
		
		g.drawImage(offscreen,0,0,this);//draw screen from buffer
		
		start = System.nanoTime();//start timer
		elapsed = (System.nanoTime()-start)/1000000000;//records looptime at this point in seconds
		delay(TARGET_FRAME_TIME-elapsed);//trys to get to the target frame time
		repaint();
	}
	
	
	/***************************************** 
					Logic
	*****************************************/
	
	public void runLogic()//gives methods a normalized (1-ish) number to use as a multiplier for movement
	{
		float timeMultiplier = (float)(elapsed*100);
		runWorld(timeMultiplier);
		runAI(timeMultiplier);
		runPlayers(timeMultiplier);
	}
	
	public void runWorld(float timeMultiplier)
	{
		if(currentWorld.getX() != lastCurrentWorld.getX() || currentWorld.getY() != lastCurrentWorld.getY())
		{
			Tracker.genWorldFromSave(this,currentWorld);
			lastCurrentWorld.setLocation(currentWorld);
		}
		else
		{
			Quests.updateQuests();
		}
	}
	
	public void runAI(float timeMultiplier)
	{
		for(int k = 0; k < Tracker.npcs.size(); k++)
		{
			Tracker.npcs.get(k).setMovementMultiplier(timeMultiplier);
		}
	}
	
	public void runPlayers(float timeMultiplier)
	{
		if(Quests.playerCanMove)
			player.move(timeMultiplier);
		
		if(multiplayer)
		{
			for(int k = 1; k < Tracker.players.size(); k++)//skip user controlled players
			{
				Tracker.players.get(k).moveTo(-1,-1,-1);//by sending -1-1-1 it uses stored values
			}
		}
	}
	
	/***************************************** 
					Display
	*****************************************/
	
	public void display(Graphics g2)
	{
		drawWorld(g2);
		drawEffects(g2);
		drawEntities(g2);
		drawNPCs(g2);
		drawPlayers(g2);
		drawRoofs(g2);
		drawHUD(g2);
	}
	
	public void drawWorld(Graphics g2)
	{	
		g2.setColor(Color.BLACK);
		g2.fillRect(-100,-100,currentWidth+500,currentHeight+500);
		((Graphics2D) g2).drawImage(Tracker.backgroundPic,0,0,this);
		
		if(Keybinds.getActionValue("debug3"))
		{
			g2.setColor(Color.BLUE);
			for(int k = 0; k < screenWidth/25; k++)
			{
				for(int j = 0; j < screenHeight/25; j++)
				{
					g2.drawLine(k*25,j*25,k*25,screenHeight);
					g2.drawLine(k*25,j*25,screenWidth,j*25);
				}
			}
		}
		
		if(Keybinds.getActionValue("debug4"))
		{
			g2.setColor(new Color(255,0,0,100));
			((Graphics2D) g2).fill(Tracker.wallZone);
		}
	}
	
	public void drawEntities(Graphics g2)
	{
		for (int k = 0; k < Tracker.objects.size(); k++)
        {
			tempEntity = Tracker.objects.get(k);
			drawEntity(g2,tempEntity);
        }
		
		for (int k = 0; k < Tracker.gates.size(); k++)
        {
			tempEntity = Tracker.gates.get(k);
			drawEntity(g2,tempEntity);
        }
	}
	
	public void drawPlayers(Graphics g2)
	{		
		for (int k = 0; k < Tracker.players.size(); k++)
        {
			tempPlayer = Tracker.players.get(k);
			drawPlayer(g2,tempPlayer);
        }		
	}
	
	public void drawNPCs(Graphics g2)
	{		
		for (int k = 0; k < Tracker.npcs.size(); k++)
        {
			tempNPC = Tracker.npcs.get(k);
			drawNPC(g2,tempNPC);
        }		
	}
	
	public void drawRoofs(Graphics g2)
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int k = 0; k < Tracker.roofs.size(); k++)
        {
			tempEntity = Tracker.roofs.get(k);
			if(!ids.contains(tempEntity.getID()))
			{
				if(getDistance(tempEntity.getXPos(),tempEntity.getYPos(),player.getXPos(),player.getYPos()) < 25)
				{
					ids.add(tempEntity.getID());
				}
        	}
        }
		
		for (int k = 0; k < Tracker.roofs.size(); k++)
        {
			tempEntity = Tracker.roofs.get(k);
			if(ids.contains(tempEntity.getID()))
			{
				tempEntity.setOpacity(0f);
			}
			else
			{
				tempEntity.setOpacity(1f);
			}
			
			drawEntity(g2,tempEntity);
        }
	}
	
	public void drawEffects(Graphics g2)
	{
		for (int k = 0; k < Tracker.particles.size(); k++)
        {
			tempParticle = Tracker.particles.get(k);
			
			AffineTransform at = new AffineTransform();
			at.translate(tempParticle.getXPos()+tempParticle.getIconWidth()/2,
					tempParticle.getYPos()+tempParticle.getIconHeight()/2);
			at.rotate(Math.toRadians(tempParticle.getRotation()));
			at.translate(-tempParticle.getIconWidth()/2,-tempParticle.getIconHeight()/2);
			
    		// draw the image
			float alpha = (float)tempParticle.getHealth()/(float)tempParticle.getMaxHealth();//calculate transparency to draw with
			if(alpha >= 0)
			{
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);//set transparency
				((Graphics2D) g2).setComposite(ac);//set transparency
    		
				((Graphics2D) g2).drawImage(tempParticle.getIcon(),at,this);
				//at.rotate(Math.toRadians(-tempParticle.getRotation()));				
				ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f);//reset transparency
				((Graphics2D) g2).setComposite(ac);//reset transparency
				
				tempParticle.setHealth(tempParticle.getHealth()-1);
			}
			else
				Tracker.particles.remove(k);
        }
	}
	
	public void drawHUD(Graphics g2)
	{	
		if(tsShown)
			ts.draw(g2,mousex,mousey);
		
		for(int k = 0; k < Tracker.hudButtons.size(); k++)
		{
			tempButton = Tracker.hudButtons.get(k);
			
			if(!(paused && tempButton.getValue().equalsIgnoreCase("PAUSE")))
			{
				tempButton.checkHover(g2,mousex,mousey);
			
				if(cursorHand)
					setCursor(new Cursor(Cursor.HAND_CURSOR));
			
				if(tempButton.isHasIcon())
				{
					if(tempButton.getType().equalsIgnoreCase("toggle"))
						tempButton.draw(g2);
					g2.drawImage(tempButton.getIcon(),(int)tempButton.getButton().getX(),(int)tempButton.getButton().getY(),this);
				}
				else
					tempButton.draw(g2);
			}
		}
		
		for(int k = 0; k < Tracker.hudSliders.size(); k++)
		{
			tempSlider = Tracker.hudSliders.get(k);
			
			g2.setColor(Color.BLACK);
			g2.setFont(tempSlider.getFont());
			g2.drawString(tempSlider.getValue(),(int)tempSlider.getSlider().getX()-g2.getFontMetrics().stringWidth(tempSlider.getValue())-10,
					(int)tempSlider.getSlider().getY()+g2.getFontMetrics().getHeight()-g2.getFontMetrics().getHeight()/3);
			tempSlider.draw(g2,mousex,mousey);
			if(tempSlider.getExtendedSlider().contains(mousex,mousey))
			{
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				cursorHand = true;
			}
		}
		
		for(int k = 0; k < Tracker.hudProgressBars.size(); k++)
		{
			tempProgressBar = Tracker.hudProgressBars.get(k);
			
			tempProgressBar.draw(g2);
		}
	}
	
	public void drawSplashScreen(Graphics g2)
	{	
		g2.setColor(new Color(200,200,200));
		g2.fillRect(-100,-100,currentWidth+500,currentHeight+500);
		//g2.drawImage(ImageLoader.getImage("SplashScreen"), 0, 0, this); //TODO uncomment
		g2.setColor(Color.BLACK);
		g2.drawString("Press any button to continue...", 400, 330);
		
		for(int k = 0; k < Tracker.titleButtons.size(); k++)
		{
			tempButton = Tracker.titleButtons.get(k);
			
			tempButton.checkHover(g2,mousex,mousey);
			
			if(cursorHand)
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			if(tempButton.isHasIcon())
			{
				if(tempButton.getType().equalsIgnoreCase("toggle"))
					tempButton.draw(g2);
				g2.drawImage(tempButton.getIcon(),(int)tempButton.getButton().getX(),(int)tempButton.getButton().getY(),this);
			}
			else
				tempButton.draw(g2);
		}
		
		for(int k = 0; k < Tracker.titleSliders.size(); k++)
		{
			tempSlider = Tracker.titleSliders.get(k);
			
			g2.setColor(Color.BLACK);
			g2.setFont(tempSlider.getFont());
			g2.drawString(tempSlider.getValue(),(int)tempSlider.getSlider().getX()-g2.getFontMetrics().stringWidth(tempSlider.getValue())-10,
					(int)tempSlider.getSlider().getY()+g2.getFontMetrics().getHeight()-g2.getFontMetrics().getHeight()/3);
			tempSlider.draw(g2,mousex,mousey);
			if(tempSlider.getExtendedSlider().contains(mousex,mousey))
			{
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				cursorHand = true;
			}
		}
		
		for(int k = 0; k < Tracker.titleProgressBars.size(); k++)
		{
			tempProgressBar = Tracker.titleProgressBars.get(k);
			
			tempProgressBar.draw(g2);
		}
		
	}
	
	public void drawControlsScreen(Graphics g2)
	{
		g2.setColor(Color.BLACK);
		g2.fillRect(-100,-100,currentWidth+500,currentHeight+500);
		g2.setColor(new Color(200,200,200));
		g2.fillRect(0,0,screenWidth,screenHeight);
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Ariel",Font.BOLD,30));
		g2.drawString("Controls", 430, 28);
		g2.drawString("_____________", 395, 32);
		
		//organizes list for easier viewing
		int x = -1;//controls x distance
		int y = 0;//controls y distance
		for(int k = 0; k < Keybinds.keybinds.size(); k++)
		{
			y++;
			if(k % 10 == 0)
			{
				x++;
				y = 0;
			}
			
			g2.drawString(KeyEvent.getKeyText(Keybinds.keybinds.get(k).getKey()),100+(500*x),50*(y+3));
			g2.drawString("   |  "+Keybinds.keybinds.get(k).getAction(),200+(500*x),50*(y+3));
		}
		
		for(int k = 0; k < Tracker.controlsButtons.size(); k++)
		{
			tempButton = Tracker.controlsButtons.get(k);
			
			tempButton.checkHover(g2,mousex,mousey);
			
			if(cursorHand)
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			if(tempButton.isHasIcon())
			{
				if(tempButton.getType().equalsIgnoreCase("toggle"))
					tempButton.draw(g2);
				g2.drawImage(tempButton.getIcon(),(int)tempButton.getButton().getX(),(int)tempButton.getButton().getY(),this);
			}
			else
				tempButton.draw(g2);
		}
		
		for(int k = 0; k < Tracker.controlsSliders.size(); k++)
		{
			tempSlider = Tracker.controlsSliders.get(k);
			
			g2.setColor(Color.BLACK);
			g2.setFont(tempSlider.getFont());
			g2.drawString(tempSlider.getValue(),(int)tempSlider.getSlider().getX()-g2.getFontMetrics().stringWidth(tempSlider.getValue())-10,
					(int)tempSlider.getSlider().getY()+g2.getFontMetrics().getHeight()-g2.getFontMetrics().getHeight()/3);
			tempSlider.draw(g2,mousex,mousey);
			if(tempSlider.getExtendedSlider().contains(mousex,mousey))
			{
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				cursorHand = true;
			}
		}
		
		for(int k = 0; k < Tracker.titleProgressBars.size(); k++)
		{
			tempProgressBar = Tracker.titleProgressBars.get(k);
			
			tempProgressBar.draw(g2);
		}
		
	}
	
	public void drawSettingsScreen(Graphics g2)
	{
		g2.setColor(Color.BLACK);
		g2.fillRect(-100,-100,currentWidth+500,currentHeight+500);
		g2.setColor(new Color(200,200,200));
		g2.fillRect(0,0,screenWidth,screenHeight);
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Ariel",Font.BOLD,30));
		g2.drawString("Settings", 430, 28);
		g2.drawString("_____________", 395, 32);
		
		for(int k = 0; k < Tracker.settingsButtons.size(); k++)
		{
			tempButton = Tracker.settingsButtons.get(k);
			
			tempButton.checkHover(g2,mousex,mousey);
			
			if(cursorHand)
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			if(tempButton.isHasIcon())
			{
				if(tempButton.getType().equalsIgnoreCase("toggle"))
					tempButton.draw(g2);
				g2.drawImage(tempButton.getIcon(),(int)tempButton.getButton().getX(),(int)tempButton.getButton().getY(),this);
			}
			else
				tempButton.draw(g2);
		}
		
		for(int k = 0; k < Tracker.settingsSliders.size(); k++)
		{
			tempSlider = Tracker.settingsSliders.get(k);
			
			g2.setColor(Color.BLACK);
			g2.setFont(tempSlider.getFont());
			g2.drawString(tempSlider.getValue(),(int)tempSlider.getSlider().getX()-g2.getFontMetrics().stringWidth(tempSlider.getValue())-10,
					(int)tempSlider.getSlider().getY()+g2.getFontMetrics().getHeight()-g2.getFontMetrics().getHeight()/3);
			tempSlider.draw(g2,mousex,mousey);
			if(tempSlider.getExtendedSlider().contains(mousex,mousey))
			{
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				cursorHand = true;
			}
		}
		
		for(int k = 0; k < Tracker.titleProgressBars.size(); k++)
		{
			tempProgressBar = Tracker.titleProgressBars.get(k);
			
			tempProgressBar.draw(g2);
		}
		
	}
	
	public void drawPauseScreen(Graphics g2)
	{
		display(g2);
		g2.setColor(new Color(255,255,255,100));
		g2.fillRect(0,0,screenWidth,screenHeight);
		
		for(int k = 0; k < Tracker.pauseButtons.size(); k++)
		{
			tempButton = Tracker.pauseButtons.get(k);
			
			tempButton.checkHover(g2,mousex,mousey);
			
			if(cursorHand)
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			if(tempButton.isHasIcon())
			{
				if(tempButton.getType().equalsIgnoreCase("toggle"))
					tempButton.draw(g2);
				g2.drawImage(tempButton.getIcon(),(int)tempButton.getButton().getX(),(int)tempButton.getButton().getY(),this);
			}
			else
				tempButton.draw(g2);
		}
		
		for(int k = 0; k < Tracker.pauseSliders.size(); k++)
		{
			tempSlider = Tracker.pauseSliders.get(k);
			
			g2.setColor(Color.BLACK);
			g2.setFont(tempSlider.getFont());
			g2.drawString(tempSlider.getValue(),(int)tempSlider.getSlider().getX()-g2.getFontMetrics().stringWidth(tempSlider.getValue())-10,
					(int)tempSlider.getSlider().getY()+g2.getFontMetrics().getHeight()-g2.getFontMetrics().getHeight()/3);
			tempSlider.draw(g2,mousex,mousey);
			if(tempSlider.getExtendedSlider().contains(mousex,mousey))
			{
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				cursorHand = true;
			}
		}
		
		for(int k = 0; k < Tracker.titleProgressBars.size(); k++)
		{
			tempProgressBar = Tracker.titleProgressBars.get(k);
			
			tempProgressBar.draw(g2);
		}
	}
	
	public void drawGameOver(Graphics g2)
	{
		g2.setColor(Color.BLACK);
		g2.fillRect(-100,-100,currentWidth+500,currentHeight+500);
		g2.setColor(new Color(200,200,200));
		g2.fillRect(0,0,screenWidth,screenHeight);
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Ariel",Font.BOLD,72));
		g2.drawString("GAME OVER", 250, 350);
	}
	
	public void drawPlayer(Graphics g2, Player player)
	{		
		player.updateImage();
		//for each player draw with correct orientation and location
		AffineTransform at = new AffineTransform();
		at.translate(player.getXPos()+player.getIconWidth()/2,player.getYPos()+player.getIconHeight()/2);//position image
	
		at.rotate(Math.toRadians(player.getRotation()));
		at.translate(-player.getIcon().getWidth()/2, -player.getIcon().getHeight()/2);//translate image to use center as point of rotation
		// draw the image
		((Graphics2D) g2).drawImage(player.getIcon(), at, this);
		at.rotate(Math.toRadians(-player.getRotation()));
		
		/*double healthBarLength = (((double)player.getHealth())/((double)player.getMaxHealth())*25);
		
		if (healthBarLength > 15)//healthBarColorSelector
			g2.setColor(new Color(0,255,0,200));
		else if (healthBarLength > 8)
			g2.setColor(new Color(255,255,0,200));
		else
			g2.setColor(new Color(255,0,0,200));
		//Healthbar drawing
		g2.fillRect((int)player.getXPos(), (int)player.getYPos()-10, (int)healthBarLength, 5);
		g2.setColor(Color.BLACK);
		g2.drawRect((int)player.getXPos(), (int)player.getYPos()-10, 25, 5);
		
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Ariel", Font.PLAIN, 10));
		g2.drawString(String.valueOf(player.getPoints()), (int)player.getXPos()+27, (int)player.getYPos()-3);
		*/
		if(Keybinds.getActionValue("debug2"))
		{
			g2.setColor(Color.BLUE);
			AffineTransform at2 = AffineTransform.getRotateInstance(Math.toRadians(player.getRotation()),
					player.getXPos()+player.getIconWidth()/2,player.getYPos()+player.getIconHeight()/2);
			Shape rotatedHitbox = at2.createTransformedShape(player.getHitBox());
			((Graphics2D) g2).draw(rotatedHitbox);
		}
	}
	
	public void drawNPC(Graphics g2, NPC npc)
	{		
		npc.updateImage();
		//for each npc draw with correct orientation and location
		AffineTransform at = new AffineTransform();
		at.translate(npc.getXPos()+npc.getIconWidth()/2,npc.getYPos()+npc.getIconHeight()/2);//position image
	
		at.rotate(Math.toRadians(npc.getRotation()));
		at.translate(-npc.getIcon().getWidth()/2, -npc.getIcon().getHeight()/2);//translate image to use center as point of rotation
		// draw the image
		((Graphics2D) g2).drawImage(npc.getIcon(), at, this);
		at.rotate(Math.toRadians(-npc.getRotation()));
		
		g2.setColor(Color.ORANGE);
		Font font = new Font("Ariel", Font.BOLD, 10);
		drawCenteredString(g2,npc.getName(),new Rectangle2D.Double(npc.getXPos()+npc.getIconWidth()/2+1,npc.getYPos()-3,0,0),font);
		
		if(Keybinds.getActionValue("debug2"))
		{
			g2.setColor(Color.BLUE);
			AffineTransform at2 = AffineTransform.getRotateInstance(Math.toRadians(npc.getRotation()),
					npc.getXPos()+npc.getIconWidth()/2,npc.getYPos()+npc.getIconHeight()/2);
			Shape rotatedHitbox = at2.createTransformedShape(npc.getHitBox());
			((Graphics2D) g2).draw(rotatedHitbox);
		}
	}
	
	public void drawEntity(Graphics g2, Entity entity)
	{		
		entity.updateImage();
		//for each entity draw with correct orientation and location
		AffineTransform at = new AffineTransform();
		at.translate(entity.getXPos()+entity.getIconWidth()/2,entity.getYPos()+entity.getIconHeight()/2);//position image
	
		at.rotate(Math.toRadians(entity.getRotation()));
		at.translate(-entity.getIcon().getWidth()/2, -entity.getIcon().getHeight()/2);//translate image to use center as point of rotation
		
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,entity.getOpacity());//set transparency
		((Graphics2D) g2).setComposite(ac);//set transparency
	
		// draw the image
		((Graphics2D) g2).drawImage(entity.getIcon(), at, this);
		at.rotate(Math.toRadians(-entity.getRotation()));
				
		//at.rotate(Math.toRadians(-tempParticle.getRotation()));				
		ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f);//reset transparency
		((Graphics2D) g2).setComposite(ac);//reset transparency
		
		if(Keybinds.getActionValue("debug2"))
		{
			g2.setColor(Color.BLUE);
			AffineTransform at2 = AffineTransform.getRotateInstance(Math.toRadians(entity.getRotation()),
					entity.getXPos()+entity.getIconWidth()/2,entity.getYPos()+entity.getIconHeight()/2);
			Shape rotatedHitbox = at2.createTransformedShape(entity.getHitBox());
			((Graphics2D) g2).draw(rotatedHitbox);
		}
		
		if(Keybinds.getActionValue("debug4") && entity.isSolid())
		{
			g2.setColor(new Color(255,0,0,100));
			AffineTransform at2 = AffineTransform.getRotateInstance(Math.toRadians(entity.getRotation()),
					entity.getXPos()+entity.getIconWidth()/2,entity.getYPos()+entity.getIconHeight()/2);
			Shape rotatedHitbox = at2.createTransformedShape(new Rectangle2D.Double(entity.getHitBox().getX(),
					entity.getHitBox().getY(),entity.getHitBox().getWidth()+1,entity.getHitBox().getHeight()+1));
			((Graphics2D) g2).fill(rotatedHitbox);
		}
	}
	
	/***************************************** 
					Utilities
	*****************************************/
	public void cleanUp()
	{
		Client.stopClient();
		AudioLoader.cleanUp();
		fl.saveGame();
	}
	
	public void drawCenteredString(Graphics g2, String text, Rectangle2D.Double rect, Font font) 
	{
	    FontMetrics metrics = g2.getFontMetrics(font);
	    int x = (int)(rect.getX()+(rect.getWidth()-metrics.stringWidth(text))/2);
	    int y = (int)(rect.getY()+((rect.getHeight()-metrics.getHeight())/2)+metrics.getAscent());
	    g2.setFont(font);
	    g2.drawString(text,x,y);
	}
	
	public static void addParticlesBy(float XPos, float YPos, int numberOfPoints, int radius, float rotation, String type, int health)
    {
    	
    	for(int k = 0; k < numberOfPoints; k++)
    	{
    		//int newX = (int)(Math.random()*radius + mapPosX - Math.random()*radius);
    		//int newY = (int)(Math.random()*radius + mapPosY - Math.random()*radius);
    		int newX = (int)(Tracker.r.nextInt(radius) + XPos - Tracker.r.nextInt(radius)/2);
    		int newY = (int)(Tracker.r.nextInt(radius) + YPos - Tracker.r.nextInt(radius)/2);
    		
    		Tracker.particles.add(new Particle(newX, newY, (int)rotation, type, health));
    	}
    }
	
	public static double getDistance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
	}
	
	public static long getCurrentTime()
	{
		return (long)(System.nanoTime()/1000000000);
	}
	
	public static void delay(double dt)//better time delay method
	{
		try 
		{
			if(dt < 0)
				dt = 0;
			
			Thread.sleep((long)dt);
		} catch (InterruptedException e) {}
	}
	
	
	/***************************************** 
					Listeners
	*****************************************/
	
	public void mouseDragged(MouseEvent e)
	{	
		mousex = e.getX()-Logic.offsetX;
		mousey = e.getY()-Logic.offsetY;
		
		if(gameStarted && !paused && !showSettings && !showControls)//in game
		{
			for(int j = 0; j < Tracker.hudStuff.size(); j++)
			{
				for(int k = 0; k < Tracker.hudStuff.get(j).size(); k++)
				{
					if(Tracker.hudStuff.get(j).get(k).getClass().toString().equalsIgnoreCase("class Button"))
					{
						tempButton = (Button)Tracker.hudStuff.get(j).get(k);
				
						if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("PAUSE"))
						{
							paused = true;
						}
					}
					
					if(Tracker.hudStuff.get(j).get(k).getClass().toString().equalsIgnoreCase("class Slider"))
					{
						tempSlider = (Slider)Tracker.hudStuff.get(j).get(k);
				
						if(tempSlider.getExtendedSlider().contains(mousex,mousey))
						{
							tempSlider.dragging(g2,mousex,mousey);
							tempSlider.draw(g2,mousex,mousey);							
							g2.setColor(Color.BLACK);
							g2.setFont(tempSlider.getFont());
							g2.drawString(tempSlider.getValue(),(int)tempSlider.getSlider().getX()-g2.getFontMetrics().stringWidth(tempSlider.getValue())-10,
									(int)tempSlider.getSlider().getY()+g2.getFontMetrics().getHeight()-g2.getFontMetrics().getHeight()/3);
							setCursor(new Cursor(Cursor.HAND_CURSOR));
							cursorHand = true;
						}
					}
				}	
			}
			
			if(tsShown)
				ts.clickCheck(mousex,mousey);
		}
		else if(gameStarted && paused && !showSettings && !showControls)//pause menu
		{	
			for(int j = 0; j < Tracker.pauseStuff.size(); j++)
			{
				for(int k = 0; k < Tracker.pauseStuff.get(j).size(); k++)
				{
					if(Tracker.pauseStuff.get(j).get(k).getClass().toString().equalsIgnoreCase("class Button"))
					{
						tempButton = (Button)Tracker.pauseStuff.get(j).get(k);
				
						if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("EXIT"))
						{
							showControls = false;
							showSettings = false;
							gameStarted = false;
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("CONTROLS"))
						{
							showControls = true;
							showSettings = false;
							gameStarted = true;
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("SETTINGS"))
						{
							showSettings = true;
							showControls = false;
							gameStarted = true;
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("BACK"))
						{
							paused = false;
							showSettings = false;
							showControls = false;
						}
					}
					
					if(Tracker.pauseStuff.get(j).get(k).getClass().toString().equalsIgnoreCase("class Slider"))
					{
						tempSlider = (Slider)Tracker.pauseStuff.get(j).get(k);
				
						if(tempSlider.getExtendedSlider().contains(mousex,mousey))
						{
							tempSlider.dragging(g2,mousex,mousey);
							tempSlider.draw(g2,mousex,mousey);
							g2.setColor(Color.BLACK);
							g2.setFont(tempSlider.getFont());
							g2.drawString(tempSlider.getValue(),(int)tempSlider.getSlider().getX()-g2.getFontMetrics().stringWidth(tempSlider.getValue())-10,
									(int)tempSlider.getSlider().getY()+g2.getFontMetrics().getHeight()-g2.getFontMetrics().getHeight()/3);
							setCursor(new Cursor(Cursor.HAND_CURSOR));
							cursorHand = true;
						}
					}
				}	
			}
			
		}
		else if(showControls && !showSettings)//show controls if paused and enabled
		{	
			for(int j = 0; j < Tracker.controlsStuff.size(); j++)
			{
				for(int k = 0; k < Tracker.controlsStuff.get(j).size(); k++)
				{
					if(Tracker.controlsStuff.get(j).get(k).getClass().toString().equalsIgnoreCase("class Button"))
					{
						tempButton = (Button)Tracker.controlsStuff.get(j).get(k);
				
						if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("BACK"))
						{
							showControls = false;
						}
					}
					
					if(Tracker.controlsStuff.get(j).get(k).getClass().toString().equalsIgnoreCase("class Slider"))
					{
						tempSlider = (Slider)Tracker.controlsStuff.get(j).get(k);
				
						if(tempSlider.getExtendedSlider().contains(mousex,mousey))
						{
							tempSlider.dragging(g2,mousex,mousey);
							tempSlider.draw(g2,mousex,mousey);
							g2.setColor(Color.BLACK);
							g2.setFont(tempSlider.getFont());
							g2.drawString(tempSlider.getValue(),(int)tempSlider.getSlider().getX()-g2.getFontMetrics().stringWidth(tempSlider.getValue())-10,
									(int)tempSlider.getSlider().getY()+g2.getFontMetrics().getHeight()-g2.getFontMetrics().getHeight()/3);
							setCursor(new Cursor(Cursor.HAND_CURSOR));
							cursorHand = true;
						}
					}
				}	
			}
		}
		else if(showSettings && !showControls)//show settings if paused and enabled
		{
			for(int j = 0; j < Tracker.settingsStuff.size(); j++)
			{
				for(int k = 0; k < Tracker.settingsStuff.get(j).size(); k++)
				{
					if(Tracker.settingsStuff.get(j).get(k).getClass().toString().equalsIgnoreCase("class Button"))
					{
						tempButton = (Button)Tracker.settingsStuff.get(j).get(k);
				
						if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("BACK"))
						{
							showSettings = false;
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("Show Chat"))
						{
							tempButton.clicked();
							tsShown = tempButton.isActivated();
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("OPTION2"))
						{
							tempButton.clicked();
							option2 = tempButton.isActivated();
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("OPTION3"))
						{
							tempButton.clicked();
							option3 = tempButton.isActivated();
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("OPTION4"))
						{
							tempButton.clicked();
							option4 = tempButton.isActivated();
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("OPTION5"))
						{
							tempButton.clicked();
							option5 = tempButton.isActivated();
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("OPTION6"))
						{
							tempButton.clicked();
							option6 = tempButton.isActivated();
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("OPTION7"))
						{
							tempButton.clicked();
							option7 = tempButton.isActivated();
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("OPTION8"))
						{
							tempButton.clicked();
							option8 = tempButton.isActivated();
						}
					}
					
					if(Tracker.settingsStuff.get(j).get(k).getClass().toString().equalsIgnoreCase("class Slider"))
					{
						tempSlider = (Slider)Tracker.settingsStuff.get(j).get(k);
				
						if(tempSlider.getExtendedSlider().contains(mousex,mousey))
						{
							tempSlider.dragging(g2,mousex,mousey);
							tempSlider.draw(g2,mousex,mousey);
							g2.setColor(Color.BLACK);
							g2.setFont(tempSlider.getFont());
							g2.drawString(tempSlider.getValue(),(int)tempSlider.getSlider().getX()-g2.getFontMetrics().stringWidth(tempSlider.getValue())-10,
									(int)tempSlider.getSlider().getY()+g2.getFontMetrics().getHeight()-g2.getFontMetrics().getHeight()/3);
							setCursor(new Cursor(Cursor.HAND_CURSOR));
							cursorHand = true;
							
							if(tempSlider.getCurrentOption() == 0 && tempSlider.getValue().equalsIgnoreCase("TARGET FPS"))
							{
								TARGET_FRAME_TIME = TARGET_FRAME_TIME_120;
							}
							else if(tempSlider.getCurrentOption() == 1 && tempSlider.getValue().equalsIgnoreCase("TARGET FPS"))
							{
								TARGET_FRAME_TIME = TARGET_FRAME_TIME_60;
							}
							else if(tempSlider.getCurrentOption() == 2 && tempSlider.getValue().equalsIgnoreCase("TARGET FPS"))
							{
								TARGET_FRAME_TIME = TARGET_FRAME_TIME_30;
							}
							else if(tempSlider.getCurrentOption() == 3 && tempSlider.getValue().equalsIgnoreCase("TARGET FPS"))
							{
								TARGET_FRAME_TIME = TARGET_FRAME_TIME_15;
							}
							else if(tempSlider.getCurrentOption() == 0 && tempSlider.getValue().equalsIgnoreCase("VOLUME"))
							{
								AudioLoader.setVolume(0f);
							}
							else if(tempSlider.getCurrentOption() == 1 && tempSlider.getValue().equalsIgnoreCase("VOLUME"))
							{
								AudioLoader.setVolume(0.5f);
							}
							else if(tempSlider.getCurrentOption() == 2 && tempSlider.getValue().equalsIgnoreCase("VOLUME"))
							{
								AudioLoader.setVolume(1f);
							}
							else if(tempSlider.getCurrentOption() == 3 && tempSlider.getValue().equalsIgnoreCase("VOLUME"))
							{
								AudioLoader.setVolume(1.5f);
							}
							else if(tempSlider.getCurrentOption() == 4 && tempSlider.getValue().equalsIgnoreCase("VOLUME"))
							{
								AudioLoader.setVolume(2f);
							}
						}
					}
				}	
			}
		}
		else if(!gameStarted && !showControls && !showSettings && paused)//splash screen
		{	
			for(int j = 0; j < Tracker.titleStuff.size(); j++)
			{
				for(int k = 0; k < Tracker.titleStuff.get(j).size(); k++)
				{
					if(Tracker.titleStuff.get(j).get(k).getClass().toString().toString().equalsIgnoreCase("class Button"))
					{
						tempButton = (Button)Tracker.titleStuff.get(j).get(k);
						
						if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("PLAY"))
						{
							paused = false;
							gameStarted = true;
							showControls = false;
							showSettings = false;
					        AudioLoader.startBGMusic();
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("CONTROLS"))
						{
							showControls = true;
							showSettings = false;
						}
						else if(tempButton.getButton().contains(mousex,mousey) && tempButton.getValue().equalsIgnoreCase("SETTINGS"))
						{
							showSettings = true;
							showControls = false;
						}
					}
					
					if(Tracker.titleStuff.get(j).get(k).getClass().toString().equalsIgnoreCase("class Slider"))
					{
						tempSlider = (Slider)Tracker.titleStuff.get(j).get(k);
				
						if(tempSlider.getExtendedSlider().contains(mousex,mousey))
						{
							tempSlider.dragging(g2,mousex,mousey);
							tempSlider.draw(g2,mousex,mousey);
							g2.setColor(Color.BLACK);
							g2.setFont(tempSlider.getFont());
							g2.drawString(tempSlider.getValue(),(int)tempSlider.getSlider().getX()-g2.getFontMetrics().stringWidth(tempSlider.getValue())-10,
									(int)tempSlider.getSlider().getY()+g2.getFontMetrics().getHeight()-g2.getFontMetrics().getHeight()/3);
							setCursor(new Cursor(Cursor.HAND_CURSOR));
							cursorHand = true;
						}
					}
				}	
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) 
	{

	}
	
	public void mouseMoved(MouseEvent e) 
	{
		mousex = e.getX()-Logic.offsetX;
		mousey = e.getY()-Logic.offsetY;
	}

	public void keyPressed(KeyEvent e)
	{
		if(typing && tsShown)
		{	
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				ts.addTyping(' ');
			}
			else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			{
				ts.delTyping();
				
				if(ts.getTyping().length() == 0)
				{
					typing = false;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				ts.addMessage(ts.getTyping(),0);
				ts.clearTyping();
				typing = false;
			}
			else if(e.getKeyCode() == KeyEvent.VK_SLASH)
			{
				ts.addTyping('/');
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				ts.setTyping(ts.getLast());
			}
			else//catch all other chars
			{
				for(int k = 40; k < 225; k++)//For numbers, letters, and symbols
				{
					if(e.getKeyCode() == k)
					{
						ts.addTyping(e.getKeyChar());
					}
				}
			}
		}
		else//if not typing then send to Keybinds
		{ 
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				Keybinds.getKeyAction(KeyEvent.VK_SPACE,true);
			}
			else if(e.getKeyCode() == KeyEvent.VK_TAB)
			{
				Keybinds.getKeyAction(KeyEvent.VK_TAB,true);
			}
			else if(e.getKeyCode() == KeyEvent.VK_SHIFT)
			{
				Keybinds.getKeyAction(KeyEvent.VK_SHIFT,true);
			}
			else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			{
				Keybinds.getKeyAction(KeyEvent.VK_CONTROL,true);
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				Keybinds.getKeyAction(KeyEvent.VK_UP,true);
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			{
				Keybinds.getKeyAction(KeyEvent.VK_DOWN,true);
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				Keybinds.getKeyAction(KeyEvent.VK_RIGHT,true);
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT)
			{
				Keybinds.getKeyAction(KeyEvent.VK_LEFT,true);
			}
			else
			{
				for(int k = 0; k < 250; k++)//For numbers, letters, and symbols
				{
					if(e.getKeyCode() == k)
					{
						Keybinds.getKeyAction(e.getKeyCode(),true);
					}	
				}
			}
			if(Keybinds.getActionValue("debug1"))
				System.out.println(e.getKeyChar()+" "+e.getKeyCode()+" "+Keybinds.getKeyAction(e.getKeyCode())
						+" "+Keybinds.getActionValue(Keybinds.getKeyAction(e.getKeyCode())));
		}
	}
	
	public void keyReleased(KeyEvent e) 
	{
		if(!(typing && tsShown))//if typing then dont send to Keybinds
		{
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				Keybinds.getKeyAction(KeyEvent.VK_SPACE,false);
			}
			else if(e.getKeyCode() == KeyEvent.VK_TAB)
			{
				Keybinds.getKeyAction(KeyEvent.VK_TAB,false);
			}
			else if(e.getKeyCode() == KeyEvent.VK_SHIFT)
			{
				Keybinds.getKeyAction(KeyEvent.VK_SHIFT,false);
			}
			else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			{
				Keybinds.getKeyAction(KeyEvent.VK_CONTROL,false);
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP)
			{
				Keybinds.getKeyAction(KeyEvent.VK_UP,false);
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			{
				Keybinds.getKeyAction(KeyEvent.VK_DOWN,false);
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				Keybinds.getKeyAction(KeyEvent.VK_RIGHT,false);
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT)
			{
				Keybinds.getKeyAction(KeyEvent.VK_LEFT,false);
			}
			else
			{
				for(int k = 0; k < 250; k++)//For numbers, letters, and symbols
				{
					if(e.getKeyCode() == k)
					{
						Keybinds.getKeyAction(e.getKeyCode(),false);
					}	
				}
			}
			if(Keybinds.getActionValue("debug1"))
				System.out.println(e.getKeyChar()+" "+e.getKeyCode()+" "+Keybinds.getKeyAction(e.getKeyCode())
						+" "+Keybinds.getActionValue(Keybinds.getKeyAction(e.getKeyCode())));
		}	
	}
	
	public void mousePressed(MouseEvent e) {
		mouseDragged(e);
	}
	
	public void paint(Graphics g){
		update(g);
	}

	public void keyTyped(KeyEvent e) {}
	
	public void mouseWheelMoved(MouseWheelEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
}
