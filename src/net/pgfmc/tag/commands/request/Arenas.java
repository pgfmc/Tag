package net.pgfmc.tag.commands.request;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.pgfmc.tag.Main;
import net.pgfmc.tag.game.Arena;

public class Arenas implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		for (Arena arena : Main.ARENAS)
		{
			if (arena.active && arena.vacant)
			{
				sender.sendMessage("§2" + arena.name + " §a AVAILABLE");
			} else
			{
				sender.sendMessage("§2§m" + arena.name + "§r§a NOT AVAILABLE");
			}
		}
		
		if (Main.ARENAS.size() <= 0)
		{
			sender.sendMessage("§cNo arenas");
		}
		
		return true;
	}
	

}
