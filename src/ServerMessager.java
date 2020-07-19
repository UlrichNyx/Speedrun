import java.io.*;
import java.net.*;
import java.util.*;
public class ServerMessager
{
	private Socket server;
	private PrintWriter out;
	public ServerMessager(Socket server, PrintWriter out)
	{
		this.server = server;
		this.out = out;
	}

	public void connectionEstablished(Integer port)
	{
		out.println("[CONNECTION] Connection established at port " + port); out.flush();
		ResponseThread t = new ResponseThread(this.server, "Welcome user at port" + server.getPort());
		t.run();
	}

	public boolean requestedLogin(User usr)
	{
		out.println("[LOGIN] "+ usr.getUsername() + " " + usr.getPassword()); out.flush();
		ResponseThread t = new ResponseThread(this.server, "ACCEPTED");
		t.run();
		return t.getValidation();
	}

    public boolean requestedRegister(User usr)
    {
        Random rnd = new Random(System.currentTimeMillis());
        Integer num = rnd.nextInt(99999) + 100000;
        out.println("[REGISTER] "+ usr.getEmail() + " " + usr.getUsername() + " " + HashPassword.encrypt(usr.getPassword(), num.toString()) + " " + num); out.flush();
        ResponseThread t = new ResponseThread(this.server, "ACCEPTED");
        t.run();
        return t.getValidation();
    }

    public boolean checkUserExists(User usr)
    {
        out.println("[USER_EXISTS] "+ usr.getUsername()); out.flush();
        ResponseThread t = new ResponseThread(this.server, "ACCEPTED");
        t.run();
        return t.getValidation();
    }

    public boolean checkEmailExists(String email)
    {
        out.println("[EMAIL_EXISTS] "+ email); out.flush();
        ResponseThread t = new ResponseThread(this.server, "ACCEPTED");
        t.run();
        return t.getValidation();
    }

    public String getTimer(String email)
    {
        out.println("[GET_TIMER] "+ email); out.flush();
        ResponseThread t = new ResponseThread(this.server, "ACCEPTED");
        t.run();
        return t.getValue();
    }

    public boolean checkCode(String email, Integer code) 
    {
        out.println("[CHECK_CODE] " + email + " " + code); out.flush();
        ResponseThread t = new ResponseThread(this.server, "ACCEPTED");
        t.run();
        return t.getValidation();
    }

    public ArrayList<String> getAllUsernames(String email)
    {
        out.println("[GET_USERNAMES] " + email); out.flush();
        ResponseThread t = new ResponseThread(this.server, "ACCEPTED");
        t.run();
        ArrayList<String> toReturn = new ArrayList<String>();
        for(String s : t.getValue().split(" "))
        {
            toReturn.add(s);
        }
        return toReturn;
    }

    public boolean changePassword(User usr) 
    {
        Random rnd = new Random(System.currentTimeMillis());
        Integer num = rnd.nextInt(99999) + 100000;
        out.println("[CHANGE_PASSWORD] " + usr.getUsername() + " " + HashPassword.encrypt(usr.getPassword(), num.toString()) + " " + num); out.flush();
        ResponseThread t = new ResponseThread(this.server, "ACCEPTED");
        t.run();
        return t.getValidation();
    }

	static class ResponseThread implements Runnable
    {
        private Socket server;
        private String message;
        private boolean validation;
        private String value;
        public ResponseThread(Socket server, String message)
        {
            this.server = server;
            this.message = message;
            this.validation = false;
            this.value = "";

        }
        public void run()
        {
            try
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                //PrintWriter out = new PrintWriter(server.getOutputStream());
                String line;
                if((line = in.readLine()) != null)
                {
                    System.out.println(line + " - received");

                    if(line.equals(message))
                    {
                    	validation = true;
                    }
                    else
                    {
                        value = line;
                    }
                }
            }catch(Exception e){}
        }

        public boolean getValidation()
        {
        	return validation;
        }

        public String getValue()
        {
            return value;
        }
        
    }

}
