import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

//Alec Ibarra
public class Keybinds implements Serializable{
	
	private static final long serialVersionUID = 1L;
	static ArrayList<KeyBind> keybinds = new ArrayList<KeyBind>();
	static ArrayList<String> actions = new ArrayList<String>();
	
	
	public static void prepare()
	{
		actions.add("up");
		actions.add("down");
		actions.add("left");
		actions.add("right");
		actions.add("cmd");
		actions.add("chat");
		actions.add("pause");
		actions.add("action");
		actions.add("spinLeft");
		actions.add("spinRight");
		actions.add("spinReset");
		actions.add("debug1");
		actions.add("debug2");
		actions.add("debug3");
		actions.add("debug4");
		
		keybinds.add(new KeyBind("up",       KeyEvent.VK_W));
		keybinds.add(new KeyBind("down",     KeyEvent.VK_S));
		keybinds.add(new KeyBind("left",     KeyEvent.VK_A));
		keybinds.add(new KeyBind("right",    KeyEvent.VK_D));
		keybinds.add(new KeyBind("up",       KeyEvent.VK_UP));
		keybinds.add(new KeyBind("down",     KeyEvent.VK_DOWN));
		keybinds.add(new KeyBind("left",     KeyEvent.VK_LEFT));
		keybinds.add(new KeyBind("right",    KeyEvent.VK_RIGHT));
		keybinds.add(new KeyBind("cmd",      KeyEvent.VK_SLASH));
		keybinds.add(new KeyBind("chat",     KeyEvent.VK_T));
		keybinds.add(new KeyBind("pause",    KeyEvent.VK_P));
		keybinds.add(new KeyBind("pause",    KeyEvent.VK_ESCAPE));
		keybinds.add(new KeyBind("action",   KeyEvent.VK_SPACE));
		keybinds.add(new KeyBind("run",      KeyEvent.VK_SHIFT));
		keybinds.add(new KeyBind("spinLeft", KeyEvent.VK_Q));
		keybinds.add(new KeyBind("spinRight",KeyEvent.VK_E));
		
		keybinds.add(new KeyBind("debug1",   KeyEvent.VK_F1));
		keybinds.add(new KeyBind("debug2",   KeyEvent.VK_F2));
		keybinds.add(new KeyBind("debug3",   KeyEvent.VK_F3));
		keybinds.add(new KeyBind("debug4",   KeyEvent.VK_F4));
		//keybinds.add(new KeyBind("",       KeyEvent.VK_TAB));
		//keybinds.add(new KeyBind("",       KeyEvent.VK_CONTROL));
	}
	
	public static void runAction(KeyBind keybind, boolean value)
	{
		if(getKeyAction(keybind.getKey()).equals("up"))
		{
			keybind.setValue(value);
		}
		else if(getKeyAction(keybind.getKey()).equals("down"))
		{
			keybind.setValue(value);
		}
		else if(getKeyAction(keybind.getKey()).equals("left"))
		{
			keybind.setValue(value);
		}
		else if(getKeyAction(keybind.getKey()).equals("right"))
		{
			keybind.setValue(value);
		}
		else if(getKeyAction(keybind.getKey()).equals("cmd"))
		{
			keybind.setValue(value);
			Logic.ts.addTyping('/');
			Logic.typing = true;
		}
		else if(getKeyAction(keybind.getKey()).equals("chat"))
		{
			keybind.setValue(value);
			Logic.typing = true;
		}
		else if(getKeyAction(keybind.getKey()).equals("pause"))
		{
			keybind.setValue(value);
			if(value)
				Logic.paused = !Logic.paused;
		}
		else if(getKeyAction(keybind.getKey()).equals("action"))
		{
			keybind.setValue(value);
			
			//Other stuff
			if(!Logic.gameStarted && Logic.paused)
			{
				Logic.paused = false;
				Logic.gameStarted = true;
			}
			if(value)
				if(!Logic.offlineMode)
					Logic.multiplayer = !Logic.multiplayer;
				else
					Logic.multiplayer = false;
		}
		else if(getKeyAction(keybind.getKey()).equals("run"))
		{
			keybind.setValue(value);
			
			if(value)
				Logic.player.setSpeed(2.5f);
			else
				Logic.player.setSpeed(1.5f);
		}
		else if(getKeyAction(keybind.getKey()).equals("debug1"))
		{
			if(value)
			{
				keybind.setValue(!keybind.getValue());
			}
		}
		else if(getKeyAction(keybind.getKey()).equals("debug2"))
		{
			if(value)
			{
				keybind.setValue(!keybind.getValue());
			}
		}
		else if(getKeyAction(keybind.getKey()).equals("debug3"))
		{
			if(value)
			{
				keybind.setValue(!keybind.getValue());
			}
		}
		else if(getKeyAction(keybind.getKey()).equals("debug4"))
		{
			if(value)
			{
				keybind.setValue(!keybind.getValue());
			}
		}
	}
	
	public static void getKeyAction(int keybind, boolean value)//run keybind action
	{
		for(int k = 0; k < keybinds.size(); k++)
		{
			if(keybinds.get(k).getKey() == keybind)
			{
				runAction(keybinds.get(k),value);
			}
		}
	}
	
	public static String getKeyAction(int keybind)//get action of keybind
	{
		for(int k = 0; k < keybinds.size(); k++)
		{
			if(keybinds.get(k).getKey() == keybind)
			{
				return keybinds.get(k).getAction();
			}
		}
		return "No keybind";
	}
	
	public static boolean delKeyAction(String action, int keybind)//delete keybind
	{
		for(int k = 0; k < keybinds.size(); k++)
		{
			if(keybinds.get(k).getKey() == keybind)
			{
				if(keybinds.get(k).getAction().equals(action))
				{
					keybinds.remove(k);
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean getActionValue(String action)//return value of action
	{
		boolean result = false;
		for(int k = 0; k < keybinds.size(); k++)
		{
			if(keybinds.get(k).getAction().equals(action))
			{
				if(keybinds.get(k).getValue())
					result = true;
			}
		}
		return result;
	}
	
	public static void setActionValue(String action, boolean value)//set value of action
	{
		for(int k = 0; k < keybinds.size(); k++)
		{
			if(keybinds.get(k).getAction().equals(action))
			{
				keybinds.get(k).setValue(value);
			}
		}
	}
	
	public static void getNewKeybind(String action)//create new keybind
	{
		int lastButton = -1;
		String input = "";
		boolean found = false;
		ArrayList<Integer> keys = new ArrayList<Integer>();
		ArrayList<Integer> toAdd = new ArrayList<Integer>();
		
		for(int k = 0; k < keybinds.size(); k++)
		{
			if(keybinds.get(k).getAction().equals(action))
			{
				lastButton = keybinds.get(k).getKey();
			}
		}
		
		input = JOptionPane.showInputDialog("Press a new keybind for: "+action,(char)lastButton);
		for(int k = 0; k < input.split(",").length; k++)
		{
			keys.add((int)input.toUpperCase().split(",")[k].charAt(0));
		}
		
		for(int k = 0; k < keys.size(); k++)
		{
			for(int j = 0; j < keybinds.size(); j++)
			{
				if(keybinds.get(j).getAction().equals(action))
				{
					toAdd.add(keys.get(k));
					found = true;
				}
			}
		}
		
		if(!found)
		{
			for(int k = 0; k < keys.size(); k++)
			{
				keybinds.add(new KeyBind(action,keys.get(k)));
			}
		}
		else
		{
			for(int k = 0; k < toAdd.size(); k++)
			{
				keybinds.add(new KeyBind(action,toAdd.get(k)));
			}
		}
	}
	
	public static String getKeybinds()//get list of keybinds
	{
		String list = "";
		for(int k = 0; k < keybinds.size(); k++)
		{
			list += "["+keybinds.get(k).getAction()+","+KeyEvent.getKeyText(keybinds.get(k).getKey())+"]";
		}
		return list;
	}
	
	public static String getActions()//get list of actions
	{
		String list = "";
		for(int k = 0; k < actions.size(); k++)
		{
			list += actions.get(k);
				if(k < actions.size()-1)
					list += ",";
		}
		return list;
	}
}

class KeyBind
{
	private int key = -1;
	private String action = "";
	private boolean value = false;
	
	public KeyBind(String action, int key)
	{
		this.action = action;
		this.key = key;
	}
	
	public void setKey(int key)
	{
		this.key = key;
	}
	
	public void setAction(String action)
	{
		this.action = action;
	}
	
	public void setValue(boolean value)
	{
		this.value = value;
	}
	
	public int getKey()
	{
		return key;
	}
	
	public String getAction()
	{
		return action;
	}
	
	public boolean getValue()
	{
		return value;
	}
}