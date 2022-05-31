import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

//Alec Ibarra
public class TextScreen {
	
	private ArrayList<Message> messages = new ArrayList<Message>();
	private Rectangle2D.Double rect = new Rectangle2D.Double();
	private Thread t = new Thread();
	private boolean started = false;
	private int maxLength = 10;
	private int textSize = 16;
	private int timer = 17;
	private Font font = new Font("Ariel",Font.BOLD,textSize);
	private Color colorOutline = Color.BLACK;
	private Color colorFillB = new Color(255,255,255,50);
	private Color colorFillM = new Color(128,128,128,50);
	private Color colorFillD = new Color(0,0,0,50);
	private Color currentColorFill = colorFillB;
	private Color colorTextNormal = new Color(0,0,0);
	private Color colorTextLight = new Color(255,255,255);
	private Color colorTextError = new Color(255,0,0);
	private Color colorTextInfo = new Color(0,0,255);
	private Color colorTextAttention = new Color(0,255,0);
	private Color colorTextCalm = new Color(255,165,0);
	private Color currentColorText = colorTextNormal;
	
	
	public TextScreen()
	{
		
	}
	
	public TextScreen(Rectangle2D.Double rect)
	{
		setRect(rect);
		setFont(new Font("Ariel",Font.BOLD,getTextSize()));
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
					Logic.delay(500);//update once per second
				}
			}
		});
		getT().start();
	}
	
	public void draw(Graphics g2)
	{
		g2.setColor(getColorOutline());
		((Graphics2D) g2).draw(new Rectangle2D.Double(getRect().getX(),getRect().getY(),
				getRect().getWidth()-1,getRect().getHeight()-1));
		g2.setColor(getCurrentColorFill());
		((Graphics2D) g2).fill(new Rectangle2D.Double(getRect().getX()+1,getRect().getY()+1,
				getRect().getWidth()-2,getRect().getHeight()-2));
		g2.setFont(getFont());
		for(int k = getMessages().size()-1; k >= 0; k--)
		{
			drawStringOutline(g2,k);
			g2.setColor(getMessages().get(k).getMessageColor());
			g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
					(int)getRect().getY()+(int)getRect().getHeight()-((k+1)*getTextSize())+12);
		}
	}
	
	public void drawStringOutline(Graphics g2,int k)
	{
		AffineTransform save = ((Graphics2D) g2).getTransform();
		g2.setColor(Color.BLACK);
		g2.translate(-1,1);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+(int)getRect().getHeight()-((k+1)*getTextSize())+12);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+(int)getRect().getHeight()-((k+1)*getTextSize())+12);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+(int)getRect().getHeight()-((k+1)*getTextSize())+12);
		g2.translate(-3,-1);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+(int)getRect().getHeight()-((k+1)*getTextSize())+12);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+(int)getRect().getHeight()-((k+1)*getTextSize())+12);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+(int)getRect().getHeight()-((k+1)*getTextSize())+12);
		g2.translate(-3,-1);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+(int)getRect().getHeight()-((k+1)*getTextSize())+12);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+(int)getRect().getHeight()-((k+1)*getTextSize())+12);
		g2.translate(1,0);
		g2.drawString(getMessages().get(k).getMessage(),(int)getRect().getX()+4,
				(int)getRect().getY()+(int)getRect().getHeight()-((k+1)*getTextSize())+12);
		((Graphics2D) g2).setTransform(save);
	}
	
	public void addMessage(String message) {
		getMessages().add(new Message(message,Logic.getCurrentTime(),getColorTextNormal()));
		if(getMessages().size() > getMaxLength())
		{
			getMessages().remove(0);
		}
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}

	public Rectangle2D.Double getRect() {
		return rect;
	}

	public void setRect(Rectangle2D.Double rect) {
		this.rect = rect;
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getColorOutline() {
		return colorOutline;
	}

	public void setColorOutline(Color colorOutline) {
		this.colorOutline = colorOutline;
	}

	public Color getColorTextNormal() {
		return colorTextNormal;
	}

	public void setColorTextNormal(Color colorTextNormal) {
		this.colorTextNormal = colorTextNormal;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public Color getColorTextError() {
		return colorTextError;
	}

	public void setColorTextError(Color colorTextError) {
		this.colorTextError = colorTextError;
	}

	public Color getColorTextInfo() {
		return colorTextInfo;
	}

	public void setColorTextInfo(Color colorTextInfo) {
		this.colorTextInfo = colorTextInfo;
	}

	public Color getColorTextAttention() {
		return colorTextAttention;
	}

	public void setColorTextAttention(Color colorTextAttention) {
		this.colorTextAttention = colorTextAttention;
	}
	
	public Color getColorTextCalm() {
		return colorTextCalm;
	}

	public void setColorTextCalm(Color colorTextCalm) {
		this.colorTextCalm = colorTextCalm;
	}

	public Color getColorFillB() {
		return colorFillB;
	}

	public void setColorFillB(Color colorFillB) {
		this.colorFillB = colorFillB;
	}

	public Color getColorFillD() {
		return colorFillD;
	}

	public void setColorFillD(Color colorFillD) {
		this.colorFillD = colorFillD;
	}

	public Color getCurrentColorFill() {
		return currentColorFill;
	}

	public void setCurrentColorFill(Color currentColorFill) {
		this.currentColorFill = currentColorFill;
	}

	public Color getColorFillM() {
		return colorFillM;
	}

	public void setColorFillM(Color colorFillM) {
		this.colorFillM = colorFillM;
	}

	public Color getColorTextLight() {
		return colorTextLight;
	}

	public void setColorTextLight(Color colorTextLight) {
		this.colorTextLight = colorTextLight;
	}

	public Color getCurrentColorText() {
		return currentColorText;
	}

	public void setCurrentColorText(Color currentColorText) {
		this.currentColorText = currentColorText;
	}
}




