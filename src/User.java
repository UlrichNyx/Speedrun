import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class User
{
	private String email;
	private String username;
	private String password;
	private static HashMap<String, Integer> bindings = new HashMap<String, Integer>();
	private static HashMap<String, Integer> timers = new HashMap<String, Integer>();
	//private static String userFileName;
	//private static String 


	public User(String email, String username, String password)
	{
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public String getEmail()
	{
		return this.email;
	}

	public String getUsername()
	{
		return this.username;
	}

	public String getPassword()
	{
		return this.password;
	}

	public static boolean registerUser(User usr, String salt)
	{
		try
        {
            FileWriter fwriter = new FileWriter("../inp/users.txt", true);
            BufferedWriter writer = new BufferedWriter(fwriter);
            writer.write(usr.getEmail() + " " + usr.getUsername() + " " + usr.getPassword() + " " + salt +"\n");
            writer.flush();
            return true;
        }catch(Exception e){System.out.println(e);}
        return false;
	}

	public boolean userExists(String filename)
	{
		try
		{
			FileReader freader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(freader);
			String line;
			try
			{
				while((line = reader.readLine()) != null)
				{
					if(line.split(" ")[1].equals(this.username))
					{
						return true;
					}
				}
				return false;
			}catch(IOException ioe){System.out.println("IOException while trying to read " + filename); return false;}
			
		}catch(FileNotFoundException e){System.out.println("FileNotFoundException while trying to read " + filename); return false;}
		
		
	}

	public boolean validateCredentials(String filename)
	{
		try
		{
			FileReader freader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(freader);
			String line;
			try
			{
				while((line = reader.readLine()) != null)
				{
					if(line.split(" ")[1].equals(this.username))
					{
						//line.split(" ")[2].equals(this.password)
						//
						if(HashPassword.isEqual(HashPassword.hexStringToByteArray(line.split(" ")[2]), HashPassword.hexStringToByteArray(HashPassword.encrypt(this.password, line.split(" ")[3]))))
						{
							this.email = line.split(" ")[0];
							return true;
						}
					}
				}
				return false;
			}catch(IOException ioe){System.out.println("IOException while trying to read " + filename); return false;}
			
		}catch(FileNotFoundException e){System.out.println("FileNotFoundException while trying to read " + filename); return false;}
	}

	public void setLogged(String filename, boolean flag)
	{
		try
        {
        	FileWriter fwriter = new FileWriter(filename);
	        BufferedWriter writer = new BufferedWriter(fwriter);
	        if(flag)
	        {
	            writer.write(username + " " + password + "\n");
	        }
	        else
	        {
	           	writer.write("");
	        }
	            
	        writer.flush();

       	}catch(IOException ioe){System.out.println(ioe + " while trying to write to " + filename); return;}
            
	}

	public static String getLogged(String filename)
	{
		try
		{
			FileReader freader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(freader);
			String line;
			try
			{
				while((line = reader.readLine()) != null)
				{
					return line;
				}
				return "";
			}catch(IOException ioe){System.out.println("IOException while trying to read " + filename); return "";}
			
		}catch(FileNotFoundException e){System.out.println("FileNotFoundException while trying to read " + filename); return "";}
	}

	public static String emailExists(String email, String filename)
	{
		try
		{
			FileReader freader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(freader);
			String line;
			try
			{
				while((line = reader.readLine()) != null)
				{
					if(line.split(" ")[0].equals(email))
					{
						return line.split(" ")[1];
					}
				}
				return "";
			}catch(IOException ioe){System.out.println("IOException while trying to read " + filename); return "";}
			
		}catch(FileNotFoundException e){System.out.println("FileNotFoundException while trying to read " + filename); return "";}

	}

	public static void makeBinding(String email, Integer num)
	{
		if(bindings.containsKey(email))
		{
			bindings.replace(email, num);
			timers.replace(email, 60);
		}
		bindings.put(email, num);
		timers.put(email, 60);
	}

	public static boolean checkBinding(String email, Integer num)
	{
		if(String.valueOf(bindings.get(email)).equals(String.valueOf(num)))
		{
			if(timers.get(email) > 0)
			{
				bindings.remove(email);
				timers.remove(email);
				
			}
			return true;
		}

		return false;
	}

	public static void decrementTimer(String email)
	{
		timers.replace(email, timers.get(email) - 1);
		if(timers.get(email) < 1)
		{
			timers.remove(email);
		}
	}

	public static Integer getTimer(String email)
	{
		return timers.get(email);
	}

	public static ArrayList<String> getAllUsernames(String email, String filename)
	{
		ArrayList<String> usernames = new ArrayList<String>();
		try
		{
			FileReader freader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(freader);
			String line;
			try
			{
				while((line = reader.readLine()) != null)
				{
					if(line.split(" ")[0].equals(email))
					{
						usernames.add(line.split(" ")[1]);
					}
				}
				return usernames;
			}catch(IOException ioe){System.out.println("IOException while trying to read " + filename); return usernames;}
			
		}catch(FileNotFoundException e){System.out.println("FileNotFoundException while trying to read " + filename); return usernames;}

	}

	public static boolean changeUserPassword(String username, String password, String salt, String filename)
	{
		ArrayList<String> lines = new ArrayList<String>();
		try
		{
			FileReader freader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(freader);
			
			String line;
			try
			{
				while((line = reader.readLine()) != null)
				{
					lines.add(line);
				}
				boolean overwrote = false;

				FileWriter fwriter = new FileWriter(filename);
           		BufferedWriter writer = new BufferedWriter(fwriter);
				for(String l : lines)
				{
					if(l.split(" ")[1].equals(username))
					{
						writer.write(l.split(" ")[0] + " " + username + " " + password + " " + salt + "\n");
						overwrote = true;
					}
					else
					{
						writer.write(l + "\n");
					}
				}
				writer.flush();
				writer.close();
				if(overwrote)
				{
					return true;
				}

				return false;
			}catch(IOException ioe){System.out.println("IOException while trying to write/read to " + filename); return false;}
			
		}catch(FileNotFoundException e){System.out.println("FileNotFoundException while trying to write/read to " + filename); return false;}

	}
}