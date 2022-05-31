import java.awt.Color;

//Alec Ibarra
public class Message {
	
	private String message = "";
	private double timeAdded = 0;
	private Color messageColor = new Color(255,255,255,100);
	
	public Message(String message, double timeAdded, Color messageColor)
	{
		setMessage(message);
		setTimeAdded(timeAdded);
		setMessageColor(messageColor);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getTimeAdded() {
		return timeAdded;
	}

	public void setTimeAdded(double timeAdded) {
		this.timeAdded = timeAdded;
	}

	public Color getMessageColor() {
		return messageColor;
	}

	public void setMessageColor(Color messageColor) {
		this.messageColor = messageColor;
	}
}