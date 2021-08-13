package net.pgfmc.tag.commands.arena;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.tag.game.Arena;

public class DelStartLoc implements CommandExecutor, Listener {
	
	public enum State {
		notReady,
		ready
	};
	
	
	
	public State state = State.notReady;
	public Player p;
	public Arena arena;
	public Location pos1;
	public Location pos2;
	
	public DelStartLoc(Player p, Arena arena)
	{
		this.p = p;
		this.arena = arena;
		state = State.ready;
		
		for (Location loc : arena.getStartLocs())
		{
			p.sendMessage("§2" + arena.getStartLocs().indexOf(loc) + 1 + " §o" + loc.toString());
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
		
		if (String.valueOf(args[0].substring(0, 1)) != null)
		{
			sender.sendMessage("Please start the name with a letter");
			return true;
		}
		
		if (Arena.findArena(args[0]) == null)
		{
			sender.sendMessage("§cCould not find arena");
			return true;
		}
		
		
		new DelStartLoc((Player) sender, Arena.findArena(args[0])); // This looks weird, but it allows multiple people to make arenas at the same time // stfu
		
		return true;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		if (e.getPlayer() != p) { return; }
		if (state == State.notReady) { return; }
		if (e.getMessage().toLowerCase() == "stop" || e.getMessage().toLowerCase() == "cancel")
		{
			p.sendMessage("§cStopped the execution");
			state = State.notReady;
			return;
		}
		if (String.valueOf(e.getMessage()) == null)
		{
			p.sendMessage("§cYou've entered a non-number");
			p.sendMessage("§a§oType \"stop\" to end this execution");
		}
		
		int msgNum = Integer.parseInt(e.getMessage());
		
		if (msgNum <= 0 || msgNum > arena.getStartLocs().size())
		{
			p.sendMessage("§cYou've entered an invalid number");
			p.sendMessage("§a§oType \"stop\" to end this execution");
			return;
		}

		pos1 = p.getLocation();
		p.sendMessage("§2Location removed");
		p.sendMessage("§aYou can add this location back with /AddStartLoc <arena name>");
		state = State.notReady;
		
		arena.delStartLoc(msgNum);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		if (!(this.p == e.getPlayer())) { return; }
		
		state = State.notReady;
	}

}
