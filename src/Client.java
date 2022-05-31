import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The Client class manages the client-side connection to a server.
 * This class is capable to scanning the local network for a gameserver.
 * It is also capable of connecting to online games.
 * @author Alec Ibarra
 */
public class Client {
	
	private static int id;
	private static int idNum;
	private static String input;
	private static int port = 7777;
	private static PrintWriter out;
	private static BufferedReader in;
	private static boolean started = false;
	private static boolean scanning = false;
	private static boolean scanningEnabled = true;
	private static String serverAddress = "";
	private static Socket socket = new Socket();
	private static ArrayList<Integer> ids = new ArrayList<Integer>();
	private static ArrayList<String> queue = new ArrayList<String>();
	private static ArrayList<String> localServers = new ArrayList<String>();
		
	/**
	 * Prepare the Client Class for connection
	 */
	public static void prepare()
	{
		try
        {
			socket.close();
		} catch (IOException e) {}
	}
	
	/**
	 * Scan the local network for a gameserver
	 */
	public static void scan()
	{
		if(!scanning && scanningEnabled && Logic.multiplayerEnabled)
		{
			scanning = true;
			Logic.ts.addMessage("Searching for game...",2);
			for(int k = 0; k < 256; k++)//about (256*100)/1000 = 25.6 seconds to run
			{
				new FindHost().setNum(k);
			}
		}
	}
	
	/**
	 * Start up the Client
	 */
	public static void start()
	{
		if(socket.isClosed() && !started && !Logic.offlineMode && Logic.multiplayerEnabled)//if client is not running, return true if started else false
		{	
			scan();
			
			new Thread()//run scan in seperate thread as to not block anything
			{
				public void run()
				{
					for(int k = 0; k < 26; k++)
					{
						if(k % 5 == 0)
						{
							Logic.ts.addMessage(Math.abs((k-255)*100)/1000+" seconds remaining.",2);
						}
						Logic.delay(1000);
					}
					
					if(localServers.size() == 1)
					{
						serverAddress = localServers.get(0);
					}
					else if(localServers.size() > 1)
					{
						Logic.ts.addMessage("Multiple servers found on LAN,1");
						serverAddress = JOptionPane.showInputDialog("Enter server IP to join,\n"+localServers.toString(),"localhost");
					}
					else if(localServers.size() == 0 || !Logic.offlineMode)
					{
						Logic.ts.addMessage("No server found on LAN",1);
						serverAddress = JOptionPane.showInputDialog("No server found on LAN\nEnter a server IP address.","localhost");
					}
					else if(Logic.offlineMode || !Logic.multiplayer)
					{
						Logic.ts.addMessage("Client is in offline mode.",1);
						return;
					}
					new Handler().start();
					started = true;
				}
			}.start();
		}
	}
	
	/**
	 * Stops the Client
	 */
	public static void stopClient()
	{
		try
		{
			if(!socket.isClosed())
			{
				out.println(id+"disconnect");
				socket.close();
				ids.clear();
				queue.clear();
				localServers.clear();
				Logic.ts.addMessage("Disconnected from: "+serverAddress,1);
				serverAddress = "";
				started = false;
				id = -1;
			}
		} catch (IOException e){}
	}
	
	/**
	 * Used in the Client Class in the scan method. This is used to enable multithreading
	 * when searching for a gameserver, resulting in quicker scans. If the computer running
	 * the game is found to be offline, offlineMode will be automatically enabled.
	 */
	private static class FindHost extends Thread
	{
		private int k;
		
		public void run()
		{		
			for(int j = 0; j < 256; j++)
			{
				if(Logic.offlineMode)
				{
					return;
				}
				else try
				{
					String ip = "";
					String localIP = InetAddress.getLocalHost().toString().split("/")[1];
					String[] splitIP = localIP.split("\\.");
					try
					{
						ip = splitIP[0]+"."+splitIP[1]+"."+k+"."+j;
					} catch(IndexOutOfBoundsException e)
					{
						Logic.offlineMode = true;
						Logic.multiplayer = false;
						Logic.ts.addMessage("Game is in offline mode",1);
						return;
					}
				
					Socket s = new Socket();
					s.connect(new InetSocketAddress(ip,port),100);
					new PrintWriter(s.getOutputStream()).println("ping");
					//System.out.println(ip+" < Server Found");
					localServers.add(ip);
					s.close();
										
					if(k == 255 && j == 255)//last ping just finished
					{
						scanning = false;
					}
				
				} catch (IOException e) {}	
			}
		}
		
		public void setNum(int k)
		{
			this.k = k;
			this.start();
		}
	}
	
	/**
	 * Used in the Client Class. Handles all of the actual data processing for input.
	 */
	private static class Handler extends Thread
	{	
		public void run()
		{
			try
			{					
				socket = new Socket(serverAddress,port);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(),true);
				out.println("name"+Logic.clientName);
				if(!in.readLine().equals("Server7777"))//Check if this is a server for this game
				{
					socket.close();	
				}
				else
				{
					id = Integer.parseInt(in.readLine());
					ids.add(id);
					new OutputHandler(socket,out).start();
				}
				
			} catch (IOException e){
				Logic.ts.addMessage("Failed to connect, please retry.",1);
				Logic.multiplayer = false;
				return;
			}
			Logic.ts.addMessage("Connected to server: "+serverAddress,2);
			
			try{
				while(!socket.isClosed())
				{	
					while((input = in.readLine()) != null)
					{
						input = input.toLowerCase();
						//System.out.println(":IN:"+input);
						
						boolean found = false;
						int tempID = Integer.parseInt(input.substring(0,4));
						for(int k = 0; k < ids.size(); k++)
						{
							if(tempID == ids.get(k))
							{
								found = true;
								idNum = k;
								break;
							}
						}
						if(!found)
						{
							ids.add(tempID);
							idNum = ids.size()-1;
							Tracker.players.add(new Player(false));
						}
						
						input = input.substring(4);
						
						//Process input
						{
							if(input.contains("xpos") && tempID != id)
							{
								//Tracker.players.get(idNum).setXPos(Float.parseFloat(input.substring(4)));
								Tracker.players.get(idNum).moveTo(Float.parseFloat(input.substring(4)),-1,3f);
							}
							else if(input.contains("ypos") && tempID != id)
							{
								//Tracker.players.get(idNum).setYPos(Float.parseFloat(input.substring(4)));
								Tracker.players.get(idNum).moveTo(-1,Float.parseFloat(input.substring(4)),3f);
							}
							else if(input.contains("rota") && tempID != id)
							{
								Tracker.players.get(idNum).setRotation(Float.parseFloat(input.substring(4)));
							}
							else if(input.contains("heal") && tempID != id)
							{
								Tracker.players.get(idNum).setHealth(Integer.parseInt(input.substring(4)));
							}
							else if(input.contains("poin") && tempID != id)
							{
								Tracker.players.get(idNum).setPoints(Integer.parseInt(input.substring(4)));
							}
							else if(input.contains("chat") && tempID != id)
							{
								Logic.ts.addMessage(input.substring(4),4);
							}
							else if(input.contains("disc") && tempID != id)
							{
								if(idNum != 0 && idNum != -1)
									Tracker.players.remove(idNum);
							}
						}
					}
				}
				
			} catch (IOException e){}	
		}
	}
	
	/**
	 * Used in Client Class. Handles all of the data processing for output.
	 */
	private static class OutputHandler extends Thread
	{
		private Socket socket;
		private PrintWriter out;
		
		public OutputHandler(Socket socket, PrintWriter out)
		{
			this.socket = socket;
			this.out = out;
		}
		
		public void run()
		{
            while(!socket.isClosed())
            {		            
				//Process output (only send if value is different)
				{
					out.println(id+"xpos"+Logic.player.getXPos());
					out.println(id+"ypos"+Logic.player.getYPos());
					out.println(id+"rota"+Logic.player.getRotation());
					out.println(id+"heal"+Logic.player.getHealth());
					out.println(id+"poin"+Logic.player.getPoints());
						
					for(int k = 0; k < Logic.ts.getClientMessages().size(); k++)
					{
						//System.out.println(id+"chat"+Logic.ts.getClientMessages().get(k).getMessage());
						out.println(id+"chat"+Logic.ts.getClientMessages().get(k).getMessage());
						Logic.ts.getClientMessages().remove(k);
					}
				}
           		Logic.delay(15);//64 tps (same as server)
           	}
		}
	}
	
	/**
	 * Queues a command for client-side execution.
	 */
	public static void addCommand(String command) {
		queue.add(command);
	}
	
	public static void setServerAddress(String serverAddress) {
		Client.serverAddress = serverAddress;
	}
	
	public static String getServerAddress() {
		return serverAddress;
	}

	public static ArrayList<String> getLocalServers() {
		return localServers;
	}

	public static ArrayList<String> getQueue() {
		return queue;
	}
}
