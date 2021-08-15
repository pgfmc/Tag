package net.pgfmc.tag.game;

import java.util.ArrayList;
import java.util.List;
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
	
	public Tagger(UUID player, int win, int lose)
	{
		Main.TAGGERS.add(this);
		
		this.player = player;
		this.win = win;
		this.lose = lose;
	}
	
	public List<Object> serialize()
	{
		List<Object> object = new ArrayList<Object>();
		object.add(player.toString());
		object.add(String.valueOf(win));
		object.add(String.valueOf(lose));
		
		return object;
	}
	
	public static Tagger deserialize(List<Object> object)
	{
		return new Tagger(UUID.fromString((String) object.get(0)), Integer.valueOf((String) object.get(1)), Integer.valueOf((String) object.get(2)));
	}
	
	public static Tagger findTagger(Player p)
	{
		for (Tagger tagger : Main.TAGGERS)
		{
			if (tagger.getPlayer().equals(p))
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
	
	public void startGame(Game game)
	{
		this.game = game;
		state = State.inGame;
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
		game = null; // Might not need this
		
		Main.quickSave(serialize(), this.getClass().getSimpleName());
	}

}
