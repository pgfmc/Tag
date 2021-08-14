package net.pgfmc.tag.commands.arena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class ToggleArena implements CommandExecutor, Listener {
	
	public enum State {
		notReady,
		ready
	};
	
	
	
	public State state = State.notReady;
	public Player p;
	public Arena arena;
	
	
	public ToggleArena(Player p, Arena arena)
	{
		Main.DEVOBJS.add(this);
		
		this.p = p;
		this.arena = arena;
		state = State.ready;
		
		p.sendMessage("§4WARNING: YOU ARE ABOUT TO TOGGLE AN ARENA'S ACTIVE STATE");
		p.sendMessage("§c§oIf you are sure, type the arena name in chat");
		p.sendMessage("§a§oType \"stop\" to end this execution");
	}
	
	public ToggleArena()
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
		
		ToggleArena object = getObject((Player) sender);
		if (!object.equals(this))
		{
			object.state = State.notReady;
			((Player) sender).sendMessage("§cCanceled last execution");
		}
		
		new ToggleArena((Player) sender, Arena.findArena(args[0])); // This looks weird, but it allows multiple people to make arenas at the same time // stfu
		
		return true;
	}
	
	public ToggleArena getObject(Player p)
	{
		ToggleArena object = this;
		
		for (Object devObject : Main.DEVOBJS)
		{
			if (devObject instanceof ToggleArena)
			{
				if (p != ((ToggleArena) devObject).p) { continue; }
				if (((ToggleArena) devObject).state == State.notReady) { continue; }
				
				object = (ToggleArena) devObject;
				
				return object;
			}
		}
		
		return this;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		ToggleArena object = getObject(e.getPlayer());
		
		if (object.equals(this)) { return; }
		
		e.setCancelled(true);
		
		if (e.getMessage().toLowerCase().equals("stop") || e.getMessage().toLowerCase().equals("cancel"))
		{
			object.p.sendMessage("§cStopped the execution");
			object.state = State.notReady;
			return;
		}
		
		if (!e.getMessage().toLowerCase().equals(object.arena.name.toLowerCase()))
		{
			object.p.sendMessage("\"" + e.getMessage().toLowerCase() + "\"");
			object.p.sendMessage("\"" + object.arena.name + "\"");
			object.p.sendMessage("§cMismatched names, please try again with /ToggleArena <arena name>");
			object.state = State.notReady;
			return;
		}

		object.p.sendMessage("§2Arena toggled");
		object.p.sendMessage("§aYou can toggle this Arena back with /ToggleArena <arena name>");
		object.state = State.notReady;
		
		object.arena.toggle();
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		ToggleArena object = getObject(e.getPlayer());
		
		if (object.equals(this)) { return; }
		
		object.state = State.notReady;
	}

}
