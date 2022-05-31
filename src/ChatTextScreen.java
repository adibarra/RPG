import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * The ChatTextScreen Class handles creation and usage of a client-system
 * command system. This can be used to send messages to other clients if
 * connected to a server. This can be used to execute code via use of the
 * Commands Class.
 * 
 * @author Alec Ibarra
 */
public class ChatTextScreen extends TextScreen {
	
	private ArrayList<Message> clientMessages = new ArrayList<Message>();
	private Rectangle2D inputRect = new Rectangle2D.Double();
	private Rectangle2D inputRectFill = new Rectangle2D.Double();
	private String last = "";
	private String typing = "";
	private String showString = "";
	
	public ChatTextScreen()
	{
		
	}
	
	public ChatTextScreen(Rectangle2D.Double rect)
	{
		setRect(rect);
		setInputRect(new Rectangle2D.Double(getRect().getX(),getRect().getY()+getRect().getHeight()-(getTextSize()+1),
				getRect().getWidth()-1,(getRect().getHeight()/getTextSize()*2)-6));
		setInputRectFill(new Rectangle2D.Double(getRect().getX()+1,getRect().getY()+getRect().getHeight()-(getTextSize()),
				getRect().getWidth()-2,(getRect().getHeight()/getTextSize()*2)-7));
		setTextSize(12);
		setFont(new Font("Ariel",Font.BOLD,getTextSize()));
		setMaxLength((int)(getRect().getHeight()/getTextSize())-1);
		setStarted(true);
		setT(new Thread()
		{
			public void run()
			{
				while(isStarted())
				{
					for(int k = 0; k < getMessages().size(); k++)
					{
						if(((Logic.getCurrentTime())-(getMessages().get(k).getTimeAdded())) > getTimer())
							getMessages().remove(k);
					}
					Logic.delay(100);//update ~10 times per second
				}
			}
		});
		getT().start();
	}
	
	/**
	 * Check what color the screen is where the screen will be placed.
	 * If the screen is dark the backgroun will be made bright and vice versa.
	 */
	public void updateBackgroundFill()
	{
		//test point in middle of chat text screen for brightness adjust text & background colors as needed
		int[] tsTestPoint1 = ImageLoader.getPixelColorAt((int)(getRect().getX()+2),(int)(getRect().getY()+5));
		int[] tsTestPoint2 = ImageLoader.getPixelColorAt((int)(getRect().getX()+getRect().getWidth()-2),
				(int)(getRect().getY()+getRect().getHeight()-5));
				
		int mixture = 
			((tsTestPoint1[0]+tsTestPoint1[1]+tsTestPoint1[2])
			+(tsTestPoint2[0]+tsTestPoint2[1]+tsTestPoint2[2]))/2;
						
		if(mixture <= 100)//if background is dark
		{
			setCurrentColorFill(getColorFillB());//set color bright
			setCurrentColorText(getColorTextNormal());
		}
		else//if background is bright
		{
			setCurrentColorFill(getColorFillD());//set color dark
			setCurrentColorText(getColorTextLight());
		}
	}
	
	/**
	 * Draws the screen
	 */
	public void draw(Graphics g2, int mousex, int mousey)
	{	
		updateBackgroundFill();
		g2.setColor(getColorOutline());
		((Graphics2D) g2).draw(new Rectangle2D.Double(getRect().getX(),getRect().getY(),
				getRect().getWidth()-1,getRect().getHeight()-1));
		((Graphics2D) g2).draw(getInputRect());
		g2.setColor(getCurrentColorFill());
		((Graphics2D) g2).fill(new Rectangle2D.Double(getRect().getX()+1,getRect().getY()+1,
				getRect().getWidth()-2,getRect().getHeight()-2));
		g2.setFont(getFont());
		for(int k = getMessages().size()-1; k >= 0; k--)
		{
			//drawStringOutline(g2,k);
			g2.setColor(getMessages().get(k).getMessageColor());
			g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
					(int)getRect().getY()+((k+1)*getTextSize())-1);
		}
		g2.setColor(getCurrentColorText());
		if(g2.getFontMetrics().stringWidth(getTyping()) > getInputRect().getWidth())
		{
			int size;
			for(int k = getTyping().length(); k > 0; k--)
			{
				setShowString(getTyping().substring(k,getTyping().length()));
				size = g2.getFontMetrics().stringWidth(getShowString());
				if(size > getInputRect().getWidth())
				{
					setShowString(getTyping().substring(k+2,getTyping().length()));
					break;
				}
				else
				{
					size -= g2.getFontMetrics().charWidth(k);
				}
			}
		}
		else
		{
			setShowString(getTyping());
		}
		g2.drawString(getShowString(),(int)getRect().getX()+4,
				(int)getRect().getY()+getTextSize()*(getMaxLength()+1)+2);
		if(getInputRect().contains(mousex,mousey))
		{
			g2.setColor(new Color(255,255,255,100));
			((Graphics2D) g2).fill(getInputRectFill());
		}
	}
	
	/**
	 * Draws messages but outlined in white (Test Method)
	 */
	public void drawStringOutline(Graphics g2,int k)
	{
		AffineTransform save = ((Graphics2D) g2).getTransform();
		if(getMessages().get(k).getMessageColor().equals(getColorTextNormal()))
			g2.setColor(Color.WHITE);
		else if(getMessages().get(k).getMessageColor().equals(getColorTextError()))
			g2.setColor(Color.WHITE);
		else if(getMessages().get(k).getMessageColor().equals(getColorTextInfo()))
			g2.setColor(Color.WHITE);
		else if(getMessages().get(k).getMessageColor().equals(getColorTextAttention()))
			g2.setColor(Color.WHITE);
		
		g2.translate(-1,1);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+((k+1)*getTextSize())-1);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+((k+1)*getTextSize())-1);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+((k+1)*getTextSize())-1);
		g2.translate(-3,-1);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+((k+1)*getTextSize())-1);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+((k+1)*getTextSize())-1);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+((k+1)*getTextSize())-1);
		g2.translate(-3,-1);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+((k+1)*getTextSize())-1);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+((k+1)*getTextSize())-1);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+((k+1)*getTextSize())-1);
		((Graphics2D) g2).setTransform(save);
	}
	
	/**
	 * Checks if the screen text area was clicked in
	 */
	public void clickCheck(int mousex, int mousey)
	{
		if(getInputRectFill().contains(mousex,mousey))
			Logic.typing = true;
	}
	
	/**
	 * Add char to the message buffer
	 */
	public void addTyping(char toAdd)
	{
		typing += toAdd;
	}
	
	/**
	 * Delete last char from the message buffer
	 */
	public void delTyping()
	{
		if(typing.length() > 0)
		{
			typing = typing.substring(0,typing.length()-1);
		}
	}
	
	/**
	 * Clear the message buffer
	 */
	public void clearTyping()
	{
		typing = "";
	}
	
	/**
	 * Add a message to the screen
	 */
	public void addMessage(String message, int from)
	{
		if(from == 0)//normal chat
		{
			last = message;
			message = Logic.clientName+": "+message;
			
			if(Logic.g2.getFontMetrics().stringWidth(message) > getRect().getWidth())
			{
				int size = 0;
				for(int k = 0; k < message.length(); k++)
				{
					if(size > getRect().getWidth())
					{
						getMessages().add(new Message(message.substring(0,k-20),Logic.getCurrentTime(),getCurrentColorText()));
						message = message.substring(k-20);
						size = 0;
						k = 0;
					}
					else
					{
						size += Logic.g2.getFontMetrics().charWidth(k);
					}
				}
			}
			else
			{
				getMessages().add(new Message(message,Logic.getCurrentTime(),getColorTextNormal()));
			}
		}
		else if(from == 1)//red
		{
			message = "System: "+message;
			
			if(Logic.g2.getFontMetrics().stringWidth(message) > getRect().getWidth())
			{
				int size = 0;
				for(int k = 0; k < message.length(); k++)
				{
					if(size > getRect().getWidth())
					{
						getMessages().add(new Message(message.substring(0,k-20),Logic.getCurrentTime(),getColorTextError()));
						message = message.substring(k-20);
						size = 0;
						k = 0;
					}
					else
					{
						size += Logic.g2.getFontMetrics().charWidth(k);
					}
				}
			}
			else
			{
				getMessages().add(new Message(message,Logic.getCurrentTime(),getColorTextError()));
			}
		}
		else if(from == 2)//blue
		{
			message = "System: "+message;
			
			if(Logic.g2.getFontMetrics().stringWidth(message) > getRect().getWidth())
			{
				int size = 0;
				for(int k = 0; k < message.length(); k++)
				{
					if(size > getRect().getWidth())
					{
						getMessages().add(new Message(message.substring(0,k-20),Logic.getCurrentTime(),getColorTextAttention()));
						message = message.substring(k-20);
						size = 0;
						k = 0;
					}
					else
					{
						size += Logic.g2.getFontMetrics().charWidth(k);
					}
				}
			}
			else
			{
				getMessages().add(new Message(message,Logic.getCurrentTime(),getColorTextAttention()));
			}
		}
		else if(from == 3)//green
		{
			message = "System: "+message;
			
			if(Logic.g2.getFontMetrics().stringWidth(message) > getRect().getWidth())
			{
				int size = 0;
				for(int k = 0; k < message.length(); k++)
				{
					if(size > getRect().getWidth())
					{
						getMessages().add(new Message(message.substring(0,k-20),Logic.getCurrentTime(),getColorTextInfo()));
						message = message.substring(k-20);
						size = 0;
						k = 0;
					}
					else
					{
						size += Logic.g2.getFontMetrics().charWidth(k);
					}
				}
			}
			else
			{
				getMessages().add(new Message(message,Logic.getCurrentTime(),getColorTextInfo()));
			}
		}
		else if(from == 4)//for multiplayer use
		{
			String clientName = message.split(":")[0]+":";
			message = message.split(":")[1].substring(1);
			message = clientName+message;
			
			if(Logic.g2.getFontMetrics().stringWidth(message) > getRect().getWidth())
			{
				int size = 0;
				for(int k = 0; k < message.length(); k++)
				{
					if(size > getRect().getWidth())
					{
						getMessages().add(new Message(message.substring(0,k-20),Logic.getCurrentTime(),getColorTextNormal()));
						message = message.substring(k-20);
						size = 0;
						k = 0;
					}
					else
					{
						size += Logic.g2.getFontMetrics().charWidth(k);
					}
				}
			}
			else
			{
				getMessages().add(new Message(message,Logic.getCurrentTime(),getColorTextNormal()));
			}
		}
		
		for(int k = 0; k < getClientMessages().size(); k++)
		{
			if(getClientMessages().get(k).getMessage().startsWith("/"))
			{
				getClientMessages().remove(k);
			}
		}
		
		while(getMessages().size() > getMaxLength())
		{
			getMessages().remove(0);
		}
		
		message = message.substring((Logic.clientName+": ").length());
		if(message.startsWith("/") && from == 0)
			Logic.cmd.eval(message);
	}

	/**
	 * Adds a message to the screen
	 */
	public void addMessage(String message, Color color)
	{
		if(Logic.g2.getFontMetrics().stringWidth(message) > getRect().getWidth())
		{
			int size = 0;
			for(int k = 0; k < message.length(); k++)
			{
				if(size > getRect().getWidth()-20)
				{
					getMessages().add(new Message(message.substring(0,k),Logic.getCurrentTime(),color));
					message = message.substring(k);
					size = 0;
					k = 0;
				}
				else
				{
					size += Logic.g2.getFontMetrics().stringWidth(message.substring(k,k+1));
				}
			}
			
			if(message.length() > 0)
			{
				getMessages().add(new Message(message,Logic.getCurrentTime(),color));
			}
		}
		else
		{
			getMessages().add(new Message(message,Logic.getCurrentTime(),color));
		}
		
		while(getMessages().size() > getMaxLength())
		{
			getMessages().remove(0);
		}
	}
	
	public String getShowString() {
		return showString;
	}

	public void setShowString(String showString) {
		this.showString = showString;
	}
	
	public ArrayList<Message> getClientMessages() {
		return clientMessages;
	}

	public void setClientMessages(ArrayList<Message> clientMessages) {
		this.clientMessages = clientMessages;
	}

	public Rectangle2D getInputRect() {
		return inputRect;
	}

	public void setInputRect(Rectangle2D inputRect) {
		this.inputRect = inputRect;
	}

	public Rectangle2D getInputRectFill() {
		return inputRectFill;
	}

	public void setInputRectFill(Rectangle2D inputRectFill) {
		this.inputRectFill = inputRectFill;
	}

	public String getTyping() {
		return typing;
	}

	public void setTyping(String typing) {
		this.typing = typing;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}
	

}
