import java.text.DecimalFormat;

import javax.swing.JOptionPane;

/**
 * The Command Class handles the execution of commands which are parsed by the ChatTextScreen Class.
 * @author Alec Ibarra
 */
public class Command {
	
	private String allCommands = "[set,get,say,help]";//manually add actoins to code
	private String allSetSubcommands = "[background,keybind,delkeybind,help]";//manually add actions to code
	private String allGetSubcommands = "[background,frametime,rroulette,actions,keybinds,help]";//manually add actions to code
	private String backgroundSubcommands = "[default,tile,space,sand,sandred,sandgreen,custom[name]]";//auto works just bg from Tracker.genBackgrounds() add here
	
	public Command()
	{
		
	}
	
	/**
	 * The only method for the Command Class. Parses input and executed as command. 
	 */
	public void eval(String command)
	{
		String[] args = command.split(" ");
		
		if(args[0].contains("set"))
		{
			if(args.length > 1)
			{
				if(args[1].contains("help"))
				{
					Logic.ts.addMessage("Subcommands are",1);
					Logic.ts.addMessage(allSetSubcommands,1);
				}
				else if(args[1].contains("delkeybind") || args[1].contains("delkeybinds"))
				{
					Logic.ts.addMessage(Keybinds.getKeybinds(),2);
					if(Keybinds.delKeyAction(JOptionPane.showInputDialog("Enter action to unbind."),
							(int)JOptionPane.showInputDialog("Enter key to unbind.").toUpperCase().charAt(0)))
					{
						Logic.ts.addMessage("Done",2);
					}
					else
					{
						Logic.ts.addMessage("Failed",2);
					}
				}
				else if(args[1].contains("keybind") || args[1].contains("keybinds"))
				{
					Logic.ts.addMessage(Keybinds.getActions(),2);
					Keybinds.getNewKeybind(JOptionPane.showInputDialog("Enter action to keybind."));
				}
				else if(args.length > 2)
				{
					if(args[1].contains("background"))
					{
						if(args[2].contains("help"))
						{
							Logic.ts.addMessage("Subcommands are",2);
							Logic.ts.addMessage(backgroundSubcommands,2);
						}
						else
						{
							Logic.backgroundType = args[2];
							Tracker.genBackground(Logic.backgroundType);
							Logic.ts.addMessage("Done",2);
						}
					}
					else if(args[1].contains("help"))
					{
						Logic.ts.addMessage("Subcommands are",2);
						Logic.ts.addMessage(allSetSubcommands,2);
					}
					else
					{
						Logic.ts.addMessage("Subcommand not recognized",1);
						Logic.ts.addMessage("Try /set help",1);
					}
				}
				else
				{
					Logic.ts.addMessage("Try /set [subcommand] help",1);
				}
			}
			else
			{
				if(!args[0].contains("help"))
				{
					Logic.ts.addMessage("Subcommand not recognized",1);
					Logic.ts.addMessage("Subcommands are",1);
					Logic.ts.addMessage(allSetSubcommands,1);
				}
				else
				{
					Logic.ts.addMessage("Subcommands are",2);
					Logic.ts.addMessage(allSetSubcommands,2);
				}
			}
		}
		else if(args[0].contains("get"))
		{
			if(args.length > 1)
			{
				if(args[1].contains("background"))
				{
					Logic.ts.addMessage("Currently using \""+Logic.backgroundType+"\"",2);
				}
				else if(args[1].contains("frametime") || args[1].contains("looptime"))
				{
					Logic.ts.addMessage(new DecimalFormat("#.#########").format(Logic.elapsed)+" seconds",2);
				}
				else if(args[1].contains("russianroulette") || args[1].contains("rroulette") || args[1].contains("rr"))
				{
					if(Tracker.r.nextInt(6) == 0)
					{
						Logic.ts.addMessage("Dead",2);
					}
					else
					{
						Logic.ts.addMessage("*Click*",2);
					}
				}
				else if(args[1].contains("keybinds"))
				{
					if(Keybinds.getKeybinds().length() > 0)
						Logic.ts.addMessage(Keybinds.getKeybinds(),2);
					else
						Logic.ts.addMessage("No keybinds",2);
				}
				else if(args[1].contains("actions"))
				{
					if(Keybinds.getActions().length() > 0)
						Logic.ts.addMessage(Keybinds.getActions(),2);
					else
						Logic.ts.addMessage("No actions",2);
				}
				else
				{
					if(!args[1].contains("help"))
					{
						Logic.ts.addMessage("Subcommand not recognized",1);
						Logic.ts.addMessage("Subcommands are",1);
						Logic.ts.addMessage(allGetSubcommands,1);
					}
					else
					{
						Logic.ts.addMessage("Subcommands are",2);
						Logic.ts.addMessage(allGetSubcommands,2);
					}
				}
			}
			else 
			{
				Logic.ts.addMessage("Missing subcommand",1);
				Logic.ts.addMessage("Subcommands are",1);
				Logic.ts.addMessage(allGetSubcommands,1);
			}
		}
		else if(args[0].contains("say"))
		{
			if(args.length > 1)
			{
				if(args[1].contains("help"))
				{
					Logic.ts.addMessage("Really?",1);
					Logic.ts.addMessage("Literally do /say anything",1);
				}
				else
				{
					String temp = "";
					for(int k = 1; k < args.length; k++)
						temp += args[k]+" ";
					Logic.ts.addMessage(temp,2);
				}
			}
			else
			{
				Logic.ts.addMessage("Missing parameter",1);
				Logic.ts.addMessage("Try /say [word]",1);
			}
		}
		else
		{
			if(!args[0].contains("help"))
			{
				Logic.ts.addMessage("Command not recognized",1);
				Logic.ts.addMessage("Commands are",1);
				Logic.ts.addMessage(allCommands,1);
			}
			else
			{
				Logic.ts.addMessage("Commands are",2);
				Logic.ts.addMessage(allCommands,2);
			}
		}
		
	}
}
