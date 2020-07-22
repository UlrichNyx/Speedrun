import java.util.ArrayList;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
public class Project
{
	private String name;
	private String description;
	private Team team;
	private LocalDate beginDate;
	private LocalDate endDate;


	public Project(String name, String description, Team team, LocalDate beginDate, LocalDate endDate)
	{
		this.name = name;
		this.description = description;
		this.team = team;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}

	public Project()
	{
		this.name = new String();
		this.description = new String();
		this.team = new Team();
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public Team getTeam()
	{
		return team;
	}

	public LocalDate getBeginDate()
	{
		return beginDate;
	}

	public LocalDate getEndDate()
	{
		return endDate;
	}

	public boolean save()
	{
		try
        {
        	String filename = "../inp/prj/user " + getTeam().getMembers().get(0).getUsername() + " project " + getName() + ".txt";
            FileWriter fwriter = new FileWriter(filename);
            BufferedWriter writer = new BufferedWriter(fwriter);
            writer.write(getName() + " [ " + getDescription() + "] " + getBeginDate().toString() + " " + getEndDate().toString() + " " + getTeam().getTeamString() + "\n");
            writer.flush();
            for(User u : getTeam().getMembers())
            {
            	User.addProject(u.getUsername(), getName());
            }
            return true;
        }catch(Exception e){System.out.println(e);}
        return false;
	}

	public static boolean checkProjectExists(String filename)
	{
		try
		{
			FileReader freader = new FileReader(filename);
			
		}catch(FileNotFoundException e){return false;}
		return true;
	}

	public static ArrayList<String> getAllProjects(String username)
	{
		ArrayList<String> projects = new ArrayList<String>();
		try
		{
			File folder = new File("../inp/usr/");
			File[] files = folder.listFiles();

			
			for(File f : files)
			{
				
				if (f.getName().split("\\.")[0].equals(username)) 
				{
					try
					{
						FileReader freader = new FileReader(f);
						BufferedReader reader = new BufferedReader(freader);
						String line;
						try
						{
							while((line = reader.readLine()) != null)
							{
								if(line.split(" ")[0].equals("Project:"))
								{
									projects.add(line.split(" ")[1]);
								}
							}
						}catch(IOException ioe){System.out.println("IOException while trying to read " + "../inp/users.txt");}
						
					}catch(FileNotFoundException e){System.out.println("FileNotFoundException while trying to read " + "../inp/users.txt");	}
				}
			}
			return projects;
		}catch(Exception e){System.out.println(e); return projects;}
		
	}

	public static ArrayList<String> loadProject(String projectName)
	{
		ArrayList<String> projects = new ArrayList<String>();
		try
		{
			File folder = new File("../inp/prj/");
			File[] files = folder.listFiles();

			
			for(File f : files)
			{
				
				System.out.println(f.getName());
				if (f.getName().split(" ")[3].split("\\.")[0].equals(projectName)) 
				{
					try
					{
						FileReader freader = new FileReader(f);
						BufferedReader reader = new BufferedReader(freader);
						String line;
						try
						{
							while((line = reader.readLine()) != null)
							{
								String description = "";
								int i = 2;

								while(!line.split(" ")[i].equals("]"))
								{
									description += line.split(" ")[i] + " ";
									i++;
								}

								int datesBegin = i+1;
								
								Team team = new Team();
								for(i = datesBegin + 2; i < line.split(" ").length; i++)
								{
									team.add(new User(line.split(" ")[i], ""));
								}
								projects.add(line.split(" ")[0]);
								projects.add("[" + description + "]");
								projects.add("[" + team.getTeamString() + "]");
								projects.add(line.split(" ")[datesBegin]);
								projects.add(line.split(" ")[datesBegin + 1]);
								return projects;

							}
						}catch(IOException ioe){System.out.println("IOException while trying to read " + "../inp/users.txt");}
						
					}catch(FileNotFoundException e){System.out.println("FileNotFoundException while trying to read " + "../inp/users.txt");	}
				}
			}
			return projects;
		}catch(Exception e){System.out.println(e); return projects;}

	}
	
}