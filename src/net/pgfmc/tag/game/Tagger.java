package net.pgfmc.tag.game;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.pgfmc.tag.Main;

public class Tagger {
	
	private UUID player;
	private int win = 0;
	private int lose = 0;
	
	public enum State {
		inGame,
		lobby
	};
	
	public Game game;
	public State state = State.lobby;
	
	public Tagger(Player player) // If they've never played a game
	{
		Main.TAGGERS.add(this);
		
		this.player = player.getUniqueId();
	}
	
	public static Tagger findTagger(Player p)
	{
		for (Tagger tagger : Main.TAGGERS)
		{
			if (tagger.getPlayer() == p)
			{
				Main.TAGGERS.add(tagger);
				return tagger;
			}
		}
		
		// If they haven't played before (couldn't find)
		return new Tagger(p);
	}
	
	public Player getPlayer()
	{
		return Bukkit.getPlayer(player);
	}
	
	public int getWin()
	{
		return win;
	}
	
	public int getLose()
	{
		return lose;
	}
	
	public int getTotalGamesPlayed()
	{
		return lose + win;
	}
	
	public void endGame(boolean won)
	{
		if (won)
		{
			win++;
		} else
		{
			lose++;
		}
		
		state = State.lobby;
		game = null;
		
		Main.quickSave();
	}

}
