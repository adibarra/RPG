import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

//Alec Ibarra
public class Quests implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static ArrayList<Quest> quests = new ArrayList<Quest>();
	static ArrayList<Integer> openGateIds = new ArrayList<Integer>();
	static boolean playerCanMove = false;
	
	public static void prepare()
	{	
		addQuest(new Quest("First Steps",0,true)
		{
			public void update()
			{
				if(Keybinds.getActionValue("up") || Keybinds.getActionValue("down") || Keybinds.getActionValue("left") || Keybinds.getActionValue("right"))
				{
					setComplete(true);
					new Thread()
					{
						public void run()
						{
							boolean done = false;
							NPC tempNPC = Tracker.getNPC("Harold");
							tempNPC.runScript("HaroldGreeting");
							
							while(!done)
							{								
								if(Logic.getDistance(tempNPC.getXPos(),tempNPC.getYPos(),Logic.player.getXPos(),Logic.player.getYPos()) < 75)
								{
									tempNPC.setMet(true);
									Logic.ts.addMessage("Harold: Hello there, stranger!",Color.YELLOW);
									Logic.ts.addMessage("Harold: I don't remember seeing you around before... You must be new here!",Color.YELLOW);
									Logic.ts.addMessage("Harold: I own this here shop but I seem to have lost the key to my chests around here somewhere... Could you help me find it?",Color.YELLOW);
									Quests.setAssigned("Find the key");
									Tracker.objects.add(new Entity(25,225,0,false,"key"));
									playerCanMove = true;
									done = true;
									tempNPC.runScript("HaroldGoodBye");
								}
							}
						}
					}.start();
				}
				completed();
			}
		});
		
		addQuest(new Quest("Find the key",0)
		{
			public void update()
			{
				if(Logic.currentWorld.getX() == 0 && Logic.currentWorld.getY() == 0)
				{
					//got key
					if(Logic.getDistance(25,225,Logic.player.getXPos(),Logic.player.getYPos()) < 25)
					{
						setComplete(true);
						Tracker.objects.remove(Tracker.objects.size()-1);
					}
				
					completed();
					if(isComplete())
					{
						Logic.ts.addMessage("You picked up a key! Bring it back to Harold.", Color.ORANGE);
						Quests.setAssigned("Bring the key to Harold");
					}
				}
			}
		});
		
		addQuest(new Quest("Bring the key to Harold",1000)
		{
			public void update()
			{
				if(Logic.currentWorld.getX() == 0 && Logic.currentWorld.getY() == 0)
				{
					NPC tempNPC = Tracker.getNPC("Harold");
					if(Logic.getDistance(tempNPC.getXPos(),tempNPC.getYPos(),Logic.player.getXPos(),Logic.player.getYPos()) < 50)
					setComplete(true);
				
					completed();
					if(isComplete())
					{
						Logic.ts.addMessage("Harold: Wow, You found it! Thanks a lot!", Color.YELLOW);
						Logic.ts.addMessage("Harold: This really helped me more than you can imagine, please take this money as a token of my appreciation.", Color.YELLOW);
						Logic.ts.addMessage("Harold: You should go meet some of the other townsfolk, that way I can let you through that gate up left.", Color.YELLOW);
						Logic.ts.addMessage("Harold: I'm pretty sure that one of them has the key for it.", Color.YELLOW);
						Quests.setAssigned("Meet the townsfolk");
					}
				}
			}
		});
		
		addQuest(new Quest("Meet the townsfolk",0)
		{
			int met = 1;
			String lastNPCMet = "";
			public void update()
			{
				if(Logic.currentWorld.getX() == 1 && Logic.currentWorld.getY() == 0)
				{
					for(int k = 0; k < Tracker.npcs.size(); k++)
					{
						NPC tempNPC = Tracker.npcs.get(k);
						if(Logic.getDistance(tempNPC.getXPos(),tempNPC.getYPos(),Logic.player.getXPos(),Logic.player.getYPos()) < 50 && !tempNPC.isMet())
						{
							tempNPC.setMet(true);
							met++;
							if(met >= 5)
								lastNPCMet = tempNPC.getName();
							Logic.ts.addMessage("Meet the townsfolk: "+met+"/5", Color.ORANGE);
						}
					}
				
					if(met >= 5)
						setComplete(true);
				
					completed();
					if(isComplete())
					{
						Logic.ts.addMessage(lastNPCMet+": Oh hello, I you must be who I heard the other folk talking about.", Color.YELLOW);
						Logic.ts.addMessage(lastNPCMet+": I heard Harold wanted the key for the gate, could you give it to him?", Color.YELLOW);
						Quests.setAssigned("Deliver the key to Harold");
					}
				}
			}
		});
		
		addQuest(new Quest("Deliver the key to Harold",0)
		{
			public void update()
			{
				if(Logic.currentWorld.getX() == 0 && Logic.currentWorld.getY() == 0)
				{
					for(int k = 0; k < Tracker.npcs.size(); k++)
					{
						NPC tempNPC = Tracker.getNPC("Harold");
						if(Logic.getDistance(tempNPC.getXPos(),tempNPC.getYPos(),Logic.player.getXPos(),Logic.player.getYPos()) < 50)
						{
							setComplete(true);
						}
					}
								
					completed();
					if(isComplete())
					{
						Logic.ts.addMessage("Harold: Ah! There you are, let me get that gate for you.", Color.YELLOW);
						new Thread()
						{
							public void run()
							{
								Tracker.getNPC("Harold").runScript("HaroldFromShackToGate");
								openGate(0);
								Tracker.getNPC("Harold").runScript("HaroldFromGateToShack");
							}
						}.start();
						Quests.setAssigned("Explore");
					}
				}
			}
		});
		
		addQuest(new Quest("Explore",0)
		{
			public void update()
			{
				if(Logic.currentWorld.getX() == 1 && Logic.currentWorld.getY() == 0)
				{
					for(int k = 0; k < Tracker.npcs.size(); k++)
					{
						NPC tempNPC = Tracker.getNPC("Harold");
						if(Logic.getDistance(tempNPC.getXPos(),tempNPC.getYPos(),Logic.player.getXPos(),Logic.player.getYPos()) < 50)
						{
							setComplete(true);
						}
					}
								
					completed();
					if(isComplete())
					{
						Logic.ts.addMessage("Harold: Ah! There you are, let me get that gate for you.", Color.YELLOW);
						Quests.setAssigned("???");
					}
				}
			}
		});
	}
	
	public static void setAssigned(String name)
	{
		for(int k = 0; k < quests.size(); k++)
		{
			if(quests.get(k).getName().equals(name))
			{
				quests.get(k).setAssigned(true);
				Logic.ts.addMessage("Quest Assigned: "+quests.get(k).getName(),Color.ORANGE);
			}
		}
	}
	
	public static void trigger(String name)
	{
		for(int k = 0; k < quests.size(); k++)
		{
			if(quests.get(k).getName().equals(name))
			{
				Logic.ts.addMessage("TRIGGERED: "+quests.get(k).getName(),Color.MAGENTA);
				quests.get(k).setComplete(true);
				quests.get(k).update();
			}
		}
	}
	
	public static void openGate(int id)
	{
		openGateIds.add(id);

		for(int k = 0; k < Tracker.gates.size(); k++)
		{
			if(openGateIds.contains(Tracker.gates.get(k).getID()))
			{
				Tracker.gates.remove(k);
				k--;//removing 1 changes order of array
			}
		}
	}
	
	public static void updateQuests()
	{
		for(int k = 0; k < quests.size(); k++)
		{
			if(quests.get(k).isAssigned())
				quests.get(k).update();
		}	
	}
	
	public static void addQuest(Quest quest) {
		Quests.quests.add(quest);
	}

	public static ArrayList<Quest> getQuests() {
		return quests;
	}

	public static void setQuests(ArrayList<Quest> quests) {
		Quests.quests = quests;
	}
	
}

class Quest
{
	private String name = "";
	private int reward = 0;
	private boolean complete = false;
	private boolean assigned = false;
	
	public Quest(String name, int reward, boolean assigned)
	{
		this.name = name;
		this.reward = reward;
		this.setAssigned(assigned);
	}
	
	public Quest(String name, int reward)
	{
		this.name = name;
		this.reward = reward;
	}
	
	public void update()
	{
		completed();
	}
	
	public void completed()
	{
		if(isComplete())
		{
			if(isAssigned())//some are used to control background and scripting
			{
				if(getReward() > 0)
					Logic.ts.addMessage("Quest Completed : "+getName()+" +$"+reward,Color.ORANGE);
				else
					Logic.ts.addMessage("Quest Completed : "+getName(),Color.ORANGE);
				Logic.player.addMoney(getReward());
			}
			Quests.getQuests().remove(this);
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public int getReward() {
		return reward;
	}
	public void setReward(int reward) {
		this.reward = reward;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}	
}
