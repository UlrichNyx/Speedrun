import java.io.*;
import java.util.*;
import java.net.*;
public class Server
{
	public static void main(String [] args)
	{
		try
		{
			ServerSocket ss = new ServerSocket(4323);
			System.out.println("Server started!");
			for(;;)
			{
				try
				{
					Socket client = ss.accept();
					System.out.println("Connection established!");
					new Thread(new ServiceThread(client)).start();
				}catch(Exception e){System.out.println("error "+e);}
			}
		}catch(Exception e){System.out.println("error "+e);}
	}
	static class ServiceThread implements Runnable
	{
		private Socket client;
		public ServiceThread(Socket c)
		{
			client = c;
		}
		public void run()
		{
			try
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter out = new PrintWriter(client.getOutputStream());
				String line;
				while((line = in.readLine()) != null)
				{
					System.out.println(line + " - received");
					String[] message = line.split(" ");
					if(message[0].equals("[CONNECTION]"))
					{
						out.println("Welcome user at port " +  client.getPort()); out.flush();
					}

					if(message[0].equals("[LOGIN]"))
					{
						User usr = new User(message[1], message[2]);

						if(usr.validateCredentials("../inp/users.txt"))
						{
							out.println("ACCEPTED"); 
						}

						else
						{
							out.println("DENIED"); 
						}
						out.flush();
					}
					if(message[0].equals("[USER_EXISTS]"))
					{
						User usr = new User(message[1], "");

						if(usr.userExists("../inp/users.txt"))
						{
							out.println("ACCEPTED"); 
						}

						else
						{
							out.println("DENIED"); 
						}
						out.flush();
					}

					if(message[0].equals("[REGISTER]"))
					{

						User usr = new User(message[1], message[2], message[3]);
						if(User.registerUser(usr, message[4]))
						{
							out.println("ACCEPTED"); 
						}

						else
						{
							out.println("DENIED"); 
						}
						out.flush();
					}

					if(message[0].equals("[EMAIL_EXISTS]"))
					{
						if(!User.emailExists(message[1], "../inp/users.txt").equals(""))
						{
							out.println("ACCEPTED"); out.flush();
							Random rnd = new Random(System.currentTimeMillis());
                          	Integer num = rnd.nextInt(99999) + 100000;
                         	System.out.println(num);
                          	User.makeBinding(message[1], num);
                          	new EmailSender(message[1], Integer.toString(num));
						}
						else
						{
							out.println("DENIED"); out.flush();
						}
						
						
					}

					if(message[0].equals("[GET_TIMER]")) 
					{
						if(User.getTimer(message[1]) != null)
						{
							User.decrementTimer(message[1]);
							out.println(User.getTimer(message[1])); 
						}
						else
						{
							out.println("DENIED"); 
						}
						out.flush();
					}

					if(message[0].equals("[CHECK_CODE]")) 
					{
						if(User.checkBinding(message[1], Integer.parseInt(message[2])))
						{
							out.println("ACCEPTED"); 
						}
						else
						{
							out.println("DENIED"); 
						}
						out.flush();
					}

					if(message[0].equals("[GET_USERNAMES]")) 
					{
						if(User.getAllUsernames(message[1], "../inp/users.txt") != null)
						{
							String toPrint = "";
							for(String u : (User.getAllUsernames(message[1], "../inp/users.txt")))
							{
								toPrint += u + " ";
							}
							out.println(toPrint);
						}
						else
						{
							out.println("DENIED"); 
						}
						out.flush();
					}

					if(message[0].equals("[CHANGE_PASSWORD]")) 
					{
						if(User.changeUserPassword(message[1], message[2], message[3], "../inp/users.txt"))
						{
							out.println("ACCEPTED"); 
						}
						else
						{
							out.println("DENIED"); 
						}
						out.flush();
					}
				}
				client.close(); 
			}catch(Exception e){}
		}
		
	}
}