package net.pgfmc.tag.commands.request;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.pgfmc.tag.Main;

public class Request {
	
	public UUID sender;
	public UUID receiver;
	private int taskID;
	private int time;
	public boolean active = true;
	
	public Request(Player sender, Player receiver)
	{
		Main.REQUESTS.add(this);
		
		this.sender = sender.getUniqueId();
		this.receiver = receiver.getUniqueId();
		
		sender.sendMessage("§aTag request -> " + receiver.getName());
		receiver.sendMessage("§aTag request <- " + sender.getName());
		
		init();
	}
	
	
	public void init()
	{
		taskID = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable()
		{
            public void run()
            {
            	if (time == 30) // 30 second timeout
            	{
            		commit(false);
            	}
            	
            	time += 1;
            }
        }, 0, 20).getTaskId(); // delay before start, delay before next loop
	}
	

	public static Request findRequestBySender(Player sender)
	{
		for (Request request : Main.REQUESTS)
		{
			if (sender.getUniqueId() == request.sender)
			{
				return request;
			}
		}
		
		return null;
	}
	
	public static Request findRequestByReceiver(Player receiver)
	{
		for (Request request : Main.REQUESTS)
		{
			if (receiver.getUniqueId() == request.receiver)
			{
				return request;
			}
		}
		
		return null;
	}
	
	public boolean matches(Player receiver, Player sender)
	{
		if (this.receiver == receiver.getUniqueId() && this.sender == sender.getUniqueId())
		{
			commit(true);
			
			return true;
		}
		
		return false;
	}
	
	public void commit(boolean matched) // timeouts the request
	{
		Bukkit.getScheduler().cancelTask(taskID); // Stops the task
		active = false;
		Main.REQUESTS.remove(this);
		
		if (!matched)
		{
			Bukkit.getPlayer(sender).sendMessage("§cTag request timeout -> " + Bukkit.getOfflinePlayer(receiver).getName());
			Bukkit.getPlayer(receiver).sendMessage("§cTag request timeout <- " + Bukkit.getOfflinePlayer(sender).getName());
		} else
		{
			Bukkit.getPlayer(sender).sendMessage("§aTag request accepted -> " + Bukkit.getOfflinePlayer(receiver).getName());
			Bukkit.getPlayer(receiver).sendMessage("§aTag request accepted <- " + Bukkit.getOfflinePlayer(sender).getName());
		}
		
		
		
	}
	
	public void commitWhenQuit(boolean sender)
	{
		Bukkit.getScheduler().cancelTask(taskID);
		active = false;
		Main.REQUESTS.remove(this);
		
		if (sender)
		{
			Bukkit.getPlayer(receiver).sendMessage("§cTag request timeout (They left) <- " + Bukkit.getOfflinePlayer(this.sender).getName());
		} else
		{
			Bukkit.getPlayer(this.sender).sendMessage("§cTag request timeout (They left) -> " + Bukkit.getOfflinePlayer(receiver).getName());
		}
	}
}
