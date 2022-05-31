import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * The Button class allows for an easy way of creating and using buttons in java AWT.
 * The class keeps track of an instance's important information such as icon, font,
 * size, location, and name.
 * 
 * @author Alec Ibarra
 */
public class Button {

	private BufferedImage currentIcon = ImageLoader.getImage("noTexture");
	private Rectangle2D.Double button = new Rectangle2D.Double();
	private String value = "";
	private String type = "";
	private boolean hasIcon = true;
	private boolean activated = false;
	private Font font = new Font("Ariel",Font.BOLD,(int)(getButton().getHeight()));
	private Color colorOutline = Color.BLACK;
	private Color colorFill = new Color(0,0,0,25);
	private Color colorText = new Color(0,0,0,175);
	
	public Button()
	{
		
	}
	
	public Button(int xPos, int yPos, int width, int height, boolean hasIcon, boolean startValue)
	{
		setButton(new Rectangle2D.Double(xPos,yPos,width,height));
		setHasIcon(hasIcon);
		setActivated(startValue);
		setFont(new Font("Ariel",Font.BOLD,(int)(getButton().getHeight())));
	}
	
	public Button(int xPos, int yPos, int width, int height, boolean hasIcon, boolean startValue, String buttonAction)
	{
		setButton(new Rectangle2D.Double(xPos,yPos,width,height));
		setHasIcon(hasIcon);
		setActivated(startValue);
		setValue(buttonAction);
		setFont(new Font("Ariel",Font.BOLD,(int)(getButton().getHeight())));
	}
	
	public Button(int xPos, int yPos, int width, int height, boolean hasIcon, String buttonAction)
	{
		setButton(new Rectangle2D.Double(xPos,yPos,width,height));
		setHasIcon(hasIcon);
		setActivated(false);
		setValue(buttonAction);
		setFont(new Font("Ariel",Font.BOLD,(int)(getButton().getHeight())));
	}
	
	public Button(int xPos, int yPos, int width, int height, String type, String buttonAction)
	{
		setButton(new Rectangle2D.Double(xPos,yPos,width,height));
		setActivated(false);
		setValue(buttonAction);
		setFont(new Font("Ariel",Font.BOLD,(int)(getButton().getHeight())));
		setType(type);
	}
	
	public Button(int xPos, int yPos, int width, int height, boolean hasIcon, String buttonAction, Font font)
	{
		setButton(new Rectangle2D.Double(xPos,yPos,width,height));
		setHasIcon(hasIcon);
		setActivated(false);
		setValue(buttonAction);
		setFont(font);
	}
	
	public Button(int xPos, int yPos, int width, int height, boolean hasIcon, String buttonAction, int fontSize)
	{
		setButton(new Rectangle2D.Double(xPos,yPos,width,height));
		setHasIcon(hasIcon);
		setActivated(false);
		setValue(buttonAction);
		setFont(new Font("Ariel",Font.BOLD,fontSize));
	}
	
	/**
	 * Draws the button
	 */
	public void draw(Graphics g2)
	{
		g2.setColor(getColorOutline());
		((Graphics2D) g2).draw(new Rectangle2D.Double(getButton().getX(),getButton().getY(),
				getButton().getWidth()-1,getButton().getHeight()-1));
		g2.setColor(getColorFill());
		((Graphics2D) g2).fill(new Rectangle2D.Double(getButton().getX()+1,getButton().getY()+1,
				getButton().getWidth()-2,getButton().getHeight()-2));
		g2.setColor(getColorText());
		g2.setFont(getFont());
		drawCenteredString(g2,getValue(),getButton(),getFont());
		if(getType().equalsIgnoreCase("normal") || getType().equalsIgnoreCase("action"))
			g2.drawImage(getIcon(),(int)getButton().getX(),(int)getButton().getY(),null);
	}
	
	/**
	 * Checks if mouse is on button, if so changes cursor look and highlights button
	 */
	public void checkHover(Graphics g2, int mousex, int mousey)
	{
		if(getButton().contains(mousex, mousey))
		{
			g2.setColor(new Color(255,255,255,200));
			((Graphics2D) g2).fill(new Rectangle2D.Double(getButton().getX()+1,getButton().getY()+1,
					getButton().getWidth()-2,getButton().getHeight()-2));
			Logic.cursorHand = true;
		}
	}
	
	/**
	 * Draws a string centered in given rectangle, uses given font if possible
	 */
	public void drawCenteredString(Graphics g2, String text, Rectangle2D.Double rect, Font font) {
	    FontMetrics metrics = g2.getFontMetrics(font);
	    int x = (int)(rect.getX()+(rect.getWidth()-metrics.stringWidth(text))/2);
	    int y = (int)(rect.getY()+((rect.getHeight()-metrics.getHeight())/2)+metrics.getAscent());
	    g2.setFont(font);
	    g2.drawString(text,x,y);
	}
	
	/**
	 * Handles button action when clicked
	 */
	public void clicked()
	{
		if(value.equalsIgnoreCase("BACK"))
		{
			currentIcon = ImageLoader.getImage("guiBack");
		}
		else if(value.equalsIgnoreCase("PAUSE"))
		{
			currentIcon = ImageLoader.getImage("guiPause");
		}
		else if(getType().equalsIgnoreCase("toggle"))
		{
			activated = !activated;
			if(activated)
				currentIcon = ImageLoader.getImage("guiCheckedCheckbox");
			else
				currentIcon = ImageLoader.getImage("guiCheckbox");
		}
	}
	
	public void setValue(String value)
	{
		this.value = value;
		if(value.equalsIgnoreCase("BACK"))
		{
			currentIcon = ImageLoader.getImage("guiBack");
		}
		else if(value.equalsIgnoreCase("PAUSE"))
		{
			currentIcon = ImageLoader.getImage("guiPause");
		}
		else
		{
			activated = !activated;
			if(activated)
				currentIcon = ImageLoader.getImage("guiCheckedCheckbox");
			else
				currentIcon = ImageLoader.getImage("guiCheckbox");
		}
	}
	
	public BufferedImage getIcon()
	{
		return currentIcon;
	}	
	
	public void setButton(Rectangle2D.Double button)
	{
		this.button = button;
	}
	
	public Rectangle2D.Double getButton()
	{
		return button;
	}
	
	public void setHasIcon(boolean hasIcon)
	{
		this.hasIcon = hasIcon;
	}
	
	public boolean isHasIcon()
	{
		return hasIcon;
	}
	
	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}
	
	public boolean isActivated()
	{
		return activated;
	}
	
	public String getValue()
	{
		return value;
	}

	public Color getColorFill() {
		return colorFill;
	}

	public void setColorFill(Color colorFill) {
		this.colorFill = colorFill;
	}

	public Color getColorOutline() {
		return colorOutline;
	}

	public void setColorOutline(Color colorOutline) {
		this.colorOutline = colorOutline;
	}

	public Color getColorText() {
		return colorText;
	}

	public void setColorText(Color colorText) {
		this.colorText = colorText;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
