import java.util.ArrayList;
public class Team
{
	private ArrayList<User> members;

	public Team(ArrayList<User> members)
	{
		this.members = members;
	}
	public Team()
	{
		this.members = new ArrayList<User>();
	}

	public void add(User member)
	{
		this.members.add(member);
	}

	public ArrayList<User> getMembers()
	{
		return this.members;
	}

	public String getTeamString()
	{
		String toReturn = "";
		for(User u : members)
		{
			toReturn += u.getUsername() + " ";
		}

		return toReturn;
	}

	public void setTeamFromString(String team)
	{
		for(String s : team.split(" "))
		{
			members.add(new User(s, ""));
		}
	}

	public void remove(User member)
	{
		this.members.remove(member);
	}
}