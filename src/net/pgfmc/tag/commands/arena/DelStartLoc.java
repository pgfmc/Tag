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

public class DelStartLoc implements CommandExecutor, Listener {
	
	public enum State {
		notReady,
		ready
	};
	
	
	
	public State state = State.notReady;
	public Player p;
	public Arena arena;
	
	public DelStartLoc(Player p, Arena arena)
	{
		Main.DEVOBJS.add(this);
		
		this.p = p;
		this.arena = arena;
		state = State.ready;
		
		for (int i = 0; i < arena.getStartLocs().size(); i++)
		{
			Location loc = arena.getStartLocs().get(i);
			p.sendMessage("§2" + (i + 1) + " §o" + String.valueOf(loc.getBlockX()) + ", " + String.valueOf(loc.getBlockY()) + ", " + String.valueOf(loc.getBlockZ()));
		}
		
		p.sendMessage("§aType the number in chat that corresponds to the start location you would like to delete");
		p.sendMessage("§a§oType \"stop\" to end this execution");
	}
	
	public DelStartLoc()
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
		
		if (args.length > 2)
		{
			sender.sendMessage("§cPlease use /DelStartLoc <arena name>");
			return true;
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
		
		DelStartLoc object = getObject((Player) sender);
		if (!object.equals(this))
		{
			object.state = State.notReady;
			((Player) sender).sendMessage("§cCanceled last execution");
		}
		
		new DelStartLoc((Player) sender, Arena.findArena(args[0])); // This looks weird, but it allows multiple people to make arenas at the same time // stfu
		
		return true;
	}
	
	public DelStartLoc getObject(Player p)
	{
		DelStartLoc object = this;
		
		for (Object devObject : Main.DEVOBJS)
		{
			if (devObject instanceof DelStartLoc)
			{
				if (p != ((DelStartLoc) devObject).p) { continue; }
				if (((DelStartLoc) devObject).state == State.notReady) { continue; }
				
				object = (DelStartLoc) devObject;
				
				return object;
			}
		}
		
		return this;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		DelStartLoc object = getObject(e.getPlayer());
		
		if (object.equals(this)) { return; }
		
		e.setCancelled(true);
		
		if (e.getMessage().toLowerCase().equals("stop") || e.getMessage().toLowerCase().equals("cancel"))
		{
			object.p.sendMessage("§cStopped the execution");
			object.state = State.notReady;
			
			return;
		}
		if (String.valueOf(e.getMessage()) == null)
		{
			object.p.sendMessage("§cYou've entered a non-number");
			object.p.sendMessage("§a§oType \"stop\" to end this execution");
			
			return;
		}
		
		int msgNum = Integer.parseInt(e.getMessage());
		
		if (msgNum <= 0 || msgNum > object.arena.getStartLocs().size())
		{
			object.p.sendMessage("§cYou've entered an invalid number");
			object.p.sendMessage("§a§oType \"stop\" to end this execution");
			
			return;
		}

		object.p.sendMessage("§2Location removed");
		object.p.sendMessage("§aYou can add this location back with /AddStartLoc <arena name>");
		object.state = State.notReady;
		
		object.arena.delStartLoc(msgNum);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		DelStartLoc object = getObject(e.getPlayer());
		
		if (object.equals(this)) { return; }
		
		object.state = State.notReady;
	}

}
