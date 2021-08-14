package net.pgfmc.tag.commands.request;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.tag.Main;
import net.pgfmc.tag.game.Arena;
import net.pgfmc.tag.game.Game;
import net.pgfmc.tag.game.Tagger;

public class Tag implements CommandExecutor, Listener {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be a player to execute this command");
		}
		
		if (args.length != 1)
		{
			return false;
		}
		
		if (Bukkit.getPlayer(args[0]) == null)
		{
			sender.sendMessage("Player could not be found");
			return true;
		}
		
		Player p1 = (Player) sender;
		Player p2 = Bukkit.getPlayer(args[0]);
		
		if (p1.equals(p2))
		{
			p1.sendMessage("§cYou cannot play with yourself ;o");
			return true;
		}
		
		
		if (Tagger.findTagger(p1).state == Tagger.State.inGame)
		{
			p1.sendMessage("§cFinish your game bruv");
			return true;
		}
		
		if (Tagger.findTagger(p2).state == Tagger.State.inGame)
		{
			p1.sendMessage("§cThis player is currently in a game, please wait for them to finish");
			return true;
		}
		
		
		
		if (Request.findRequestBySender(p1) != null && Request.findRequestBySender(p1).active)
		{
			Request.findRequestBySender(p1).commit(false);
			
		} else if (Request.findRequestByReceiver(p1) != null && Request.findRequestByReceiver(p1).active)
		{
			if (Request.findRequestByReceiver(p1).matches(p1, p2))
			{
				List<Arena> vacantArenas = new ArrayList<Arena>();
				for (Arena arena : Main.ARENAS)
				{
					if (arena.active && arena.vacant) { vacantArenas.add(arena); }
				}
				
				new Game(p1, p2, vacantArenas);
				return true;
			}
			Request.findRequestByReceiver(p1).commit(false);
		}
		
		
		
		new Request(p1, p2);
		
		return true;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) // Cancels any pending requests on quit
	{
		Player p = e.getPlayer();
		
		if (Request.findRequestBySender(p) != null && Request.findRequestBySender(p).active)
		{
			Request.findRequestBySender(p).commitWhenQuit(true);
			
		} else if (Request.findRequestByReceiver(p) != null && Request.findRequestByReceiver(p).active)
		{
			Request.findRequestBySender(p).commitWhenQuit(false);
		}
	}

}
