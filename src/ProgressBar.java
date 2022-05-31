import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import java.awt.FontMetrics;

//Alec Ibarra
public class ProgressBar {
	
	private double progress = 0;
	private double progressMax = 0;
	private Rectangle2D.Double progressBar = new Rectangle2D.Double();
	private Font font = new Font("Ariel",Font.BOLD,(int)(getProgressBar().getHeight()));
	private Color colorOutline = Color.BLACK;
	private Color colorFill = new Color(0,200,0,200);
	private Color colorText = new Color(0,0,0,175);
	
	public ProgressBar()
	{
		
	}
	
	public ProgressBar(int XPos, int YPos, int width, int height, double progressMax)
	{
		setProgressMax(progressMax);
		setProgressBar(new Rectangle2D.Double(XPos,YPos,width,height));
		setFont(new Font("Ariel",Font.BOLD,(int)(getProgressBar().getHeight())));
	}
	
	public void draw(Graphics g2)
	{
		g2.setColor(getColorOutline());
		((Graphics2D) g2).draw(new Rectangle2D.Double(getProgressBar().getX(),getProgressBar().getY(),
				getProgressBar().getWidth()-1,getProgressBar().getHeight()-1));
		g2.setColor(getColorFill());
		((Graphics2D) g2).fill(new Rectangle2D.Double(getProgressBar().getX()+1,getProgressBar().getY()+1,
				((getProgress()/getProgressMax())*getProgressBar().getWidth())-2,getProgressBar().getHeight()-2));
		g2.setColor(getColorText());
		g2.setFont(getFont());
		drawCenteredString(g2,Integer.toString((int)((getProgress()/getProgressMax())*100))+"%",getProgressBar(),getFont());
	}
	
	public void draw(Graphics g2, double addProgress)
	{
		addProgress(addProgress);
		g2.setColor(getColorOutline());
		((Graphics2D) g2).draw(new Rectangle2D.Double(getProgressBar().getX(),getProgressBar().getY(),
				getProgressBar().getWidth()-1,getProgressBar().getHeight()-1));
		g2.setColor(getColorFill());
		((Graphics2D) g2).fill(new Rectangle2D.Double(getProgressBar().getX()+1,getProgressBar().getY()+1,
				((getProgress()/getProgressMax())*getProgressBar().getWidth())-2,getProgressBar().getHeight()-2));
		g2.setColor(getColorText());
		g2.setFont(getFont());
		drawCenteredString(g2,Integer.toString((int)((getProgress()/getProgressMax())*100))+"%",getProgressBar(),getFont());
	}
	
	public void drawCenteredString(Graphics g2, String text, Rectangle2D.Double rect, Font font) {
	    FontMetrics metrics = g2.getFontMetrics(font);
	    int x = (int)(rect.getX()+(rect.getWidth()-metrics.stringWidth(text))/2);
	    int y = (int)(rect.getY()+((rect.getHeight()-metrics.getHeight())/2)+metrics.getAscent());
	    g2.setFont(font);
	    g2.drawString(text,x,y);
	}

	public void addProgress(double addProgress) {
		if(getProgress()+addProgress < getProgressMax())
			setProgress(getProgress() + addProgress);
		else
			setProgress(getProgressMax());
	}
	
	public double getProgress() {
		return progress;
	}
	
	public void setProgress(double progress) {
		this.progress = progress;
	}

	public double getProgressMax() {
		return progressMax;
	}

	public void setProgressMax(double progressMax) {
		this.progressMax = progressMax;
	}

	public Rectangle2D.Double getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(Rectangle2D.Double progressBar) {
		this.progressBar = progressBar;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
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
}
