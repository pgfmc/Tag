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

public class ChangeArenaLoc implements CommandExecutor, Listener {
	
	public enum State {
		notReady,
		pos1,
		pos2
	};
	
	
	
	public State state = State.notReady;
	public Player p;
	public Arena arena;
	public Location pos1;
	public Location pos2;
	
	public ChangeArenaLoc(Player p, Arena arena)
	{
		this.p = p;
		this.arena = arena;
		state = State.pos1;
		
		p.sendMessage("�2Stand at position 1, then send any message in chat");
		p.sendMessage("�a�oType \"stop\" to end this execution");
	}
	
	public ChangeArenaLoc()
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
		
		if (String.valueOf(args[0].substring(0, 1)) != null)
		{
			sender.sendMessage("Please start the name with a letter");
			return true;
		}
		
		if (Arena.findArena(args[0]) == null)
		{
			sender.sendMessage("�cCould not find arena");
			return true;
		}
		
		new ChangeArenaLoc((Player) sender, Arena.findArena(args[0])); // This looks weird, but it allows multiple people to make arenas at the same time // stfu
		
		return true;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		if (e.getPlayer() != p) { return; }
		if (state == State.notReady) { return; }
		if (e.getMessage().toLowerCase() == "stop" || e.getMessage().toLowerCase() == "cancel")
		{
			p.sendMessage("�cStopped the execution");
			state = State.notReady;
			return;
		}
		
		if (state == State.pos1)
		{
			pos1 = p.getLocation();
			p.sendMessage("�2Position 1 selected");
			p.sendMessage("�aStand at position 2, then send any message in chat");
			state = State.pos2;
		}
		
		if (state == State.pos2)
		{
			pos2 = p.getLocation();
			p.sendMessage("�2Position 2 selected");
			p.sendMessage("�aYou can edit these locations whenever with /ChangeArenaLoc <arena name>");
			state = State.notReady;
			
			arena.changeBounds(pos1, pos2);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		if (!(this.p == e.getPlayer())) { return; }
		
		state = State.notReady;
	}

}
