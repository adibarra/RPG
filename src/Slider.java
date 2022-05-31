import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

//Alec Ibarra
public class Slider {

	private String value = "";
	private Rectangle2D.Double extendedSlider = new Rectangle2D.Double(-10,-10,220,70);
	private Rectangle2D.Double slider = new Rectangle2D.Double(0,0,200,50);
	private Rectangle2D.Double pointer = new Rectangle2D.Double(0,0,10,10);
	private final int height = 25;
	private int totalOptions = 10;
	private String[] options = new String[totalOptions];
	private Color colorOutline = Color.BLACK;
	private Color colorFill = new Color(0,0,0,100);
	private Color colorText = new Color(0,0,0);
	private Color colorHoverExtendedFill = new Color(0,0,0,100);
	private Color colorHoverPointerFill = new Color(255,255,255,150);
	private Font font = new Font("Ariel",Font.BOLD,30);

	public Slider()
	{
		
	}
	
	public Slider(int XPos, int YPos, int width, int totalOptions, String[] options, int startValue, String value)
	{
		setValue(value);
		setOptions(options);
		setTotalOptions(totalOptions);
		setSlider(new Rectangle2D.Double(XPos,YPos,width,height));
		setExtendedSlider(new Rectangle2D.Double(XPos-15,YPos-5,width+30,height+10));
		setPointer(new Rectangle2D.Double(getSlider().getX()+startValue*getSpacing()-(getSlider().getWidth()/(getSlider().getWidth()/10)/2),
				getSlider().getY()+2,getSlider().getWidth()/(getSlider().getWidth()/10),getSlider().getHeight()-3));
	}
	
	public Slider(int XPos, int YPos, int width, String[] options, int startValue, String value)
	{
		setValue(value);
		setOptions(options);
		setTotalOptions(options.length-1);
		setSlider(new Rectangle2D.Double(XPos,YPos,width,height));
		setExtendedSlider(new Rectangle2D.Double(XPos-15,YPos-5,width+30,height+10));
		setPointer(new Rectangle2D.Double(getSlider().getX()+startValue*getSpacing()-(getSlider().getWidth()/(getSlider().getWidth()/10)/2),
				getSlider().getY()+2,getSlider().getWidth()/(getSlider().getWidth()/10),getSlider().getHeight()-3));
	}
	
	public void draw(Graphics g2, int mousex, int mousey)
	{
		g2.setFont(getFont());
		if(getExtendedSlider().contains(mousex,mousey))
		{
			g2.setColor(getColorHoverExtendedFill());
			((Graphics2D) g2).fill(getExtendedSlider());
		}
		g2.setColor(getColorFill());
		((Graphics2D) g2).fill(new Rectangle2D.Double(getSlider().getX(),getSlider().getY(),getSlider().getWidth(),getSlider().getHeight()));
		((Graphics2D) g2).fill(new Rectangle2D.Double(getSlider().getX(),getSlider().getY()+getSlider().getHeight()/10*5,
				getSlider().getWidth(),getSlider().getHeight()/10));
		
		g2.setColor(getColorOutline());
		((Graphics2D) g2).draw(new Rectangle2D.Double(getSlider().getX(),getSlider().getY(),getSlider().getWidth(),getSlider().getHeight()));
		for(int k = 0; k < getTotalOptions(); k++)
		{
			g2.drawLine((int)(getSlider().getX()+k*getSpacing()),(int)(getSlider().getY()),
					(int)(getSlider().getX()+k*getSpacing()),(int)(getSlider().getY()+getSlider().getHeight()-1));
		}
		
		g2.setColor(getColorFill());
		((Graphics2D) g2).fill(new Rectangle2D.Double(getPointer().getX(),getPointer().getY(),
				getPointer().getWidth(),getPointer().getHeight()));
		
		if(getExtendedSlider().contains(mousex,mousey))
		{
			g2.setColor(getColorHoverPointerFill());
			((Graphics2D) g2).fill(getPointer());
		}
		g2.setColor(getColorText());
		g2.drawString(getOptions()[getCurrentOption()],(int)(getSlider().getX()+getSlider().getWidth()+10),
				(int)(getSlider().getY()+getSlider().getHeight()-1));
	}
	
	public void dragging(Graphics g2, double mousex, double mousey)
	{
		if(mousex < getSlider().getX())
			mousex = getSlider().getX();
		
		if(mousex > getSlider().getX()+getSlider().getWidth())
			mousex = getSlider().getX()+getSlider().getWidth();
		
		setPointer(new Rectangle2D.Double(mousex-getPointer().getWidth()/2,getPointer().getY(),
				getPointer().getWidth(),getPointer().getHeight()));
	}
	
	public int getCurrentOption() 
	{
		return (int)Math.round(((getPointer().getX()-getPointer().getWidth()/2)-getSlider().getX())/getSpacing());
	}
	
	public double getSpacing()
	{
		return getSlider().getWidth()/(totalOptions-1);
	}
	
	public int getTotalOptions() {
		return totalOptions;
	}

	public void setTotalOptions(int totalOptions) {
		this.totalOptions = totalOptions;
	}

	public Rectangle2D.Double getSlider() {
		return slider;
	}

	public void setSlider(Rectangle2D.Double slider) {
		this.slider = slider;
	}

	public Rectangle2D.Double getPointer() {
		return pointer;
	}

	public void setPointer(Rectangle2D.Double pointer) {
		this.pointer = pointer;
	}

	public Rectangle2D.Double getExtendedSlider() {
		return extendedSlider;
	}

	public void setExtendedSlider(Rectangle2D.Double extendedSlider) {
		this.extendedSlider = extendedSlider;
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public Color getColorOutline() {
		return colorOutline;
	}

	public void setColorOutline(Color colorOutline) {
		this.colorOutline = colorOutline;
	}

	public Color getColorFill() {
		return colorFill;
	}

	public void setColorFill(Color colorFill) {
		this.colorFill = colorFill;
	}

	public Color getColorText() {
		return colorText;
	}

	public void setColorText(Color colorText) {
		this.colorText = colorText;
	}

	public int getHeight() {
		return height;
	}

	public Color getColorHoverExtendedFill() {
		return colorHoverExtendedFill;
	}

	public void setColorHoverExtendedFill(Color colorHoverExtendedFill) {
		this.colorHoverExtendedFill = colorHoverExtendedFill;
	}

	public Color getColorHoverPointerFill() {
		return colorHoverPointerFill;
	}

	public void setColorHoverPointerFill(Color colorHoverPointerFill) {
		this.colorHoverPointerFill = colorHoverPointerFill;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}
	
}
