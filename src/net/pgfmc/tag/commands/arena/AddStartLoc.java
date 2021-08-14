package net.pgfmc.tag.commands.arena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.tag.Main;
import net.pgfmc.tag.game.Arena;

public class AddStartLoc implements CommandExecutor, Listener {
	
	public enum State {
		notReady,
		ready
	};
	
	
	
	public State state = State.notReady;
	public Player p;
	public Arena arena;
	public Location loc;
	
	public AddStartLoc(Player p, Arena arena)
	{
		Main.DEVOBJS.add(this);
		
		this.p = p;
		this.arena = arena;
		state = State.ready;
		
		p.sendMessage("§2Stand at the desired location, then send any message in chat");
		p.sendMessage("§a§oType \"stop\" to end this execution");
	}
	
	public AddStartLoc()
	{
		// e
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be a player to execute this command");
			return true;
		}
		
		if (args.length != 1)
		{
			return false;
		}
		
		List<String> numbers = new ArrayList<String>(Arrays.asList("1","2","3","4","5","6","7","8","9","0"));
		if (numbers.contains(args[0].substring(0, 1)))
		{
			sender.sendMessage("Please start the name with a letter");
			return true;
		}
		
		if (Arena.findArena(args[0]) == null)
		{
			sender.sendMessage("§cCould not find arena");
			return true;
		}
		
		AddStartLoc object = getObject((Player) sender);
		if (!object.equals(this))
		{
			object.state = State.notReady;
			((Player) sender).sendMessage("§cCanceled last execution");
		}
		
		new AddStartLoc((Player) sender, Arena.findArena(args[0])); // This looks weird, but it allows multiple people to make arenas at the same time // stfu
		
		return true;
	}
	
	public AddStartLoc getObject(Player p)
	{
		AddStartLoc object = this;
		
		for (Object devObject : Main.DEVOBJS)
		{
			if (devObject instanceof AddStartLoc)
			{
				if (p != ((AddStartLoc) devObject).p) { continue; }
				if (((AddStartLoc) devObject).state == State.notReady) { continue; }
				
				object = (AddStartLoc) devObject;
				
				return object;
			}
		}
		
		return this;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		
		AddStartLoc object = getObject(e.getPlayer());
		
		if (object.equals(this)) { return; }
		
		e.setCancelled(true);
		
		
		if (e.getMessage().toLowerCase().equals("stop") || e.getMessage().toLowerCase().equals("cancel"))
		{
			object.p.sendMessage("§cStopped the execution");
			object.state = State.notReady;
			
			return;
		}
		
		object.loc = object.p.getLocation();
		object.p.sendMessage("§2Location selected");
		object.p.sendMessage("§aYou can delete this location later with /DelStartLoc <arena name>");
		object.state = State.notReady;
		
		object.arena.addStartLoc(object.loc);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		
		AddStartLoc object = getObject(e.getPlayer());
		
		if (object.equals(this)) { return; }
		
		object.state = State.notReady;
	}

}
