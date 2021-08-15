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

public class CreateArena implements CommandExecutor, Listener {
	
	public enum State {
		notReady,
		pos1,
		pos2
	};
	
	public State state = State.notReady;
	public Player p;
	public String name;
	public Location pos1;
	public Location pos2;
	
	public CreateArena(Player p, String name)
	{
		Main.DEVOBJS.add(this);
		this.p = p;
		this.name = name;
		state = State.pos1;
		
		p.sendMessage("�2Stand at position 1, then send any message in chat");
		p.sendMessage("�a�oType \"stop\" to end this execution");
	}
	
	public CreateArena()
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
		
		CreateArena object = getObject((Player) sender);
		if (!object.equals(this))
		{
			object.state = State.notReady;
			((Player) sender).sendMessage("�cCanceled last execution");
		}
		
		new CreateArena((Player) sender, args[0]); // This looks weird, but it allows multiple people to make arenas at the same time // stfu
		
		
		return true;
	}
	
	public CreateArena getObject(Player p)
	{
		CreateArena object = this;
		
		for (Object devObject : Main.DEVOBJS)
		{
			if (devObject instanceof CreateArena)
			{
				if (p != ((CreateArena) devObject).p) { continue; }
				if (((CreateArena) devObject).state == State.notReady) { continue; }
				
				object = (CreateArena) devObject;
				
				return object;
			}
		}
		
		return this;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		CreateArena object = getObject(e.getPlayer());
		
		if (object.equals(this)) { return; }
		
		e.setCancelled(true);
		
		
		if (e.getMessage().toLowerCase().equals("stop") || e.getMessage().toLowerCase().equals("cancel"))
		{
			object.p.sendMessage("�cStopped the execution");
			object.state = State.notReady;
			
			return;
		}
		
		if (object.state == State.pos1)
		{
			object.pos1 = object.p.getLocation();
			object.p.sendMessage("�2Position 1 selected");
			object.p.sendMessage("�aStand at position 2, then send any message in chat");
			object.state = State.pos2;
			
			return;
		}
		
		if (object.state == State.pos2)
		{
			object.pos2 = object.p.getLocation();
			object.p.sendMessage("�2Position 2 selected");
			object.p.sendMessage("�aYou can edit these locations whenever with /ChangeArenaLoc <arena name>");
			object.p.sendMessage("�a�oPlease add start locations with /AddStartLoc <arena name>");
			object.state = State.notReady;
			
			new Arena(object.name, object.pos1, object.pos2);
			
			return;
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		CreateArena object = getObject(e.getPlayer());
		
		if (object.equals(this)) { return; }
		
		object.state = State.notReady;
	}

}
