import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

//Alec Ibarra
public class Server {

	static final int PORT = 7777;
	static ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();
	static ArrayList<PClient> clients = new ArrayList<PClient>();
    
	public static void main(String[] args) throws Exception 
	{
		System.out.println("The server is running on port: "+PORT);
		ServerSocket listener = new ServerSocket(PORT);
		try
		{
        	while (true) 
        	{
        		new Handler(listener.accept()).start();
        		delay(100);
        	}
		} 
		finally 
		{
			listener.close();
		}
	}
	
	private static class PClient
	{
		float xpos = 0;
		float ypos = 0;
		float rotation = 0;
		int health = 0;
		int points = 0;
		//
		int id;
		String name = "";
		ArrayList<String> messages = new ArrayList<String>();
		
		private PClient(int id, String name)
		{
			this.id = id;
			this.name = name;
		}
	}
	
	private static class OutputHandler extends Thread
	{
		private Socket socket;
		private PrintWriter out;
		
		public OutputHandler(Socket socket)
		{
			this.socket = socket;
		}
		
		public void run()
		{
            try
            {
            	while(!socket.isClosed())
            	{
            		out = new PrintWriter(socket.getOutputStream(),true);
            		
            		//Process output (only send if value is different)
            		{
            			for(int k = 0; k < clients.size(); k++)
            			{
            				out.println(clients.get(k).id+"xpos"+clients.get(k).xpos);
            				out.println(clients.get(k).id+"ypos"+clients.get(k).ypos);
            				out.println(clients.get(k).id+"rota"+clients.get(k).rotation);
            				out.println(clients.get(k).id+"heal"+clients.get(k).health);
               				out.println(clients.get(k).id+"poin"+clients.get(k).points);
            				
            				for(int j = 0; j < clients.get(k).messages.size(); j++)
            				{
            					out.println(clients.get(k).id+"chat"+clients.get(k).name+": "+clients.get(k).messages.get(j));
            					clients.get(k).messages.remove(j);
            				}
            			}
            		}
            		delay(15);//64 tps (same as client)
            	}
            } catch(IOException e){}
		}
	}

	private static class Handler extends Thread 
	{
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		private String input;
		private String name;
		private int id;
		private int clientNum;

        public Handler(Socket socket) 
        {
            this.socket = socket;
        }
        
        public void run() 
        {
        	try
        	{
        		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        		out = new PrintWriter(socket.getOutputStream(),true);
        		id = Integer.parseInt(Integer.toString(new Random().nextInt()).substring(0,4));
        		input = in.readLine();
        		writers.add(out);
       			out.println("Server7777");//Verification that this is a game server
       			out.println(id);
        		
       			if(input.contains("ping"))
       			{
       				System.out.println("pinged");
       				clientNum = -1;
       				socket.close();
       				return;
       			}
       			else if(input.contains("name"))
        		{
        			name = input.substring(4);
        			clients.add(new PClient(id,name));
        			System.out.println(name+" has joined the game.");
        		}
        		new OutputHandler(socket).start();

            	while(!socket.isClosed())
                {
            		while((input = in.readLine()) != null)
                	{
                		input = input.toLowerCase();
                		//System.out.println(":IN:"+input);
                		
                		for(int k = 0; k < clients.size(); k++)//set correct clientNum via id lookup
                		{
                			if(Integer.toString(clients.get(k).id).equals(input.substring(0,4)))
                			{
                				input = input.substring(4);
                				clientNum = k;
                				break;
                			}
                			else
                			{
                				clientNum = -1;
                			}
                		}
                		
                		//Process input
                		{
                			if(input.contains("xpos"))
                			{
                				clients.get(clientNum).xpos = Float.parseFloat(input.substring(4));
                			}
                			else if(input.contains("ypos"))
                			{
                				clients.get(clientNum).ypos = Float.parseFloat(input.substring(4));
                			}
                			else if(input.contains("rota"))
                			{
                				clients.get(clientNum).rotation = Float.parseFloat(input.substring(4));
                			}
                			else if(input.contains("heal"))
                			{
                				clients.get(clientNum).health = (int)Float.parseFloat(input.substring(4));
                			}
                			else if(input.contains("poin"))
                			{
                				clients.get(clientNum).points = (int)Float.parseFloat(input.substring(4));
                			}
                			else if(input.contains("chat"))
                			{
                				clients.get(clientNum).messages.add(input.substring(4));
                			}
                			else if(input.contains("disconnect"))
                       		{
                       			out.println(clients.get(clientNum).id+"disc");
                       			socket.close();
                       		}
                		}
                	}
                }
            }
            catch (IOException e){}
        	finally
        	{
        		try
            	{
					socket.close();
				} catch (IOException e) {}
        		writers.remove(out);
        		if(clientNum > 0 && clientNum <= clients.size()-1)
        			clients.remove(clientNum);
        		System.out.println(name+" has left the game.");
        	}	
        }
    }

	public static void delay(long n)
	{
		try 
		{
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}