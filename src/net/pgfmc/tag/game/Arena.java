package net.pgfmc.tag.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.pgfmc.tag.Main;

public class Arena {

	
	public String name;
	public Location pos1;
	public Location pos2;
	
	private List<Location> startLocations = new ArrayList<Location>();
	
	private Random r = new Random();
	
	private int taskID;
	
	public boolean active = true;
	public boolean vacant = true;
	
	
	public Arena(String name, Location pos1, Location pos2)
	{
		Main.ARENAS.add(this);
		
		this.name = name;
		this.pos1 = pos1;
		this.pos2 = pos2;
		
		Main.quickSave(this);
	}
	
	public Arena(String name, Location pos1, Location pos2, List<Location> startLocations, boolean active, boolean vacant)
	{
		Main.ARENAS.add(this);
		
		this.name = name;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.startLocations = startLocations;
		this.active = active;
		this.vacant = vacant;
	}
	
	
	public List<Object> serialize()
	{
		List<Object> object = new ArrayList<Object>();
		object.add(name);
		object.add(pos1.serialize());
		object.add(pos2.serialize());
		
		List<Object> tempStartLocations = new ArrayList<Object>();
		for (int i = 0; i < startLocations.size(); i++)
		{
			Location tempLoc = startLocations.get(i);
			tempStartLocations.add(tempLoc.serialize());
		}
		object.add(tempStartLocations);
		
		object.add(active);
		object.add(vacant);
		
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public static Arena deserialize(List<Object> object)
	{
		List<Location> tempStartLocations = new ArrayList<Location>();
		for (Object loc : (List<Object>) object.get(3))
		{
			tempStartLocations.add(Location.deserialize((Map<String, Object>) loc));
		}
		
		return new Arena((String) object.get(0), Location.deserialize((Map<String, Object>) object.get(1)), Location.deserialize((Map<String, Object>) object.get(2)), tempStartLocations, (boolean) object.get(4), (boolean) object.get(5));
	}
	
	
	public boolean inBounds(Location loc)
	{
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		
		double pos1x = pos1.getX();
		double pos1y = pos1.getY();
		double pos1z = pos1.getZ();
		
		double pos2x = pos2.getX();
		double pos2y = pos2.getY();
		double pos2z = pos2.getZ();
		
		if (pos1x > pos2x)
		{
			if (!(x <= pos1x && x >= pos2x)) { return false; }
		} else if (pos2x >= pos1x)
		{
			if (!(x <= pos2x && x >= pos1x)) { return false; }
		}
		
		if (pos1y > pos2y)
		{
			if (!(y <= pos1y && y >= pos2y)) { return false; }
		} else if (pos2y >= pos1y)
		{
			if (!(y <= pos2y && y >= pos1y)) { return false; }
		}
		
		if (pos1z > pos2z)
		{
			if (!(z <= pos1z && z >= pos2z)) { return false; }
		} else if (pos2z >= pos1z)
		{
			if (!(z <= pos2z && z >= pos1z)) { return false; }
		}
		
		return true;
		
	}
	
	
	public void teleportPlayers(Player p1, Player p2)
	{
		Location loc1 = startLocations.get(r.nextInt(startLocations.size()));
		
		taskID = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable()
		{
            public void run()
            {
            	Location loc2 = startLocations.get(r.nextInt(startLocations.size()));
            	if (loc2 != loc1)
            	{
            		p1.teleport(loc1);
            		p2.teleport(loc2);
            		
            		stopTask(); // Stops the task
            	}
            }
        }, 0, 1).getTaskId(); // delay before start, delay before next loop
		
		
	}
	
	public void stopTask()
	{
		Bukkit.getScheduler().cancelTask(taskID); // Stops the task
	}
	
	public static Arena findArena(String name)
	{
		for (Arena arena : Main.ARENAS)
		{
			if (arena.name.equals(name))
			{
				return arena;
			}
		}
		return null;
	}
	
	public void changeBounds(Location pos1, Location pos2)
	{
		this.pos1 = pos1;
		this.pos2 = pos2;
		
		Main.quickSave(this);
	}
	
	public void addStartLoc(Location loc)
	{
		startLocations.add(loc);
		
		Main.quickSave(this);
	}
	
	public List<Location> getStartLocs()
	{
		return startLocations;
	}
	
	public void delStartLoc(int index)
	{
		startLocations.remove(index - 1);
		
		Main.quickSave(this);
	}
	
	public void commit(boolean youAreSure)
	{
		if (youAreSure)
		{
			Main.ARENAS.remove(this);
		}
		
		Main.quickSave(this);		
	}
	
	public void toggle()
	{
		if (active)
		{
			active = false;
		} else
		{
			active = true;
		}
		
		
		
		Main.quickSave(this);
	}
		

}
