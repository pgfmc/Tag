package net.pgfmc.tag.game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.tag.Main;

public class Game implements Listener {
	
	private Random r = new Random();
	
	public Arena arena;
	
	private UUID tagger;
	private UUID runner;
	public UUID winner;
	public LocalDateTime date;
	private double time = 0.0;
	private int taskID;
	private boolean active = true;
	
	Tagger taggerTagger;
	Tagger runnerTagger;
	// private OfflinePlayer loser; // redundant, but will include if it makes things easier
	
	public Game()
	{
		// e
	}
	
	public Game(Player p1, Player p2, List<Arena> vacantArenas)
	{
		Main.GAMES.add(this);	
		
		this.date = LocalDateTime.now();
		
		arena = vacantArenas.get(r.nextInt(vacantArenas.size())); // Randomizes arena to play in
		arena.vacant = false;
		
		startGame(p1, p2);
		
		taskID = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable()
		{
            public void run()
            {
            	if (!arena.inBounds(Bukkit.getPlayer(tagger).getLocation()) || !arena.inBounds(Bukkit.getPlayer(runner).getLocation())) // Ends the game immediately if any player goes out of bounds
            	{
            		endGame();
            	}
            	
            	if (time == 2 * 60) // 2 minute limit
            	{
            		endGame();
            	}
            	
            	time += 0.1;
            }
        }, 0, 2).getTaskId(); // delay before start, delay before next loop
	}
	
	public Player getTagger()
	{
		return Bukkit.getPlayer(tagger);
	}
	
	public Player getRunner()
	{
		return Bukkit.getPlayer(runner);
	}
	
	public void swapTagger()
	{
		UUID tempPlayer = tagger;
		Tagger tempTagger = taggerTagger;
		
		tagger = runner;
		taggerTagger = runnerTagger;
		runner = tempPlayer;
		runnerTagger = tempTagger;
		
		getTagger().playSound(getTagger().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
		getRunner().playSound(getRunner().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
		
		getTagger().sendMessage("You are the tagger!");
		getRunner().sendMessage("RUN!!!");
	}
	
	public void startGame(Player p1, Player p2)
	{
		
		if (r.nextInt(2) == 0) // "Returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence."
		{
			this.tagger = p1.getUniqueId();
			this.runner = p2.getUniqueId();
			taggerTagger = Tagger.findTagger(p1);
			runnerTagger = Tagger.findTagger(p2);
			
			getTagger().sendMessage("You are the tagger!");
			getRunner().sendMessage("RUN!!!");
		} else
		{
			this.tagger = p2.getUniqueId();
			this.runner = p1.getUniqueId();
			taggerTagger = Tagger.findTagger(p2);
			runnerTagger = Tagger.findTagger(p1);
			
			getTagger().sendMessage("You are the tagger!");
			getRunner().sendMessage("RUN!!!");
		}
		
		arena.teleportPlayers(p1, p2); // this will randomly teleport the players to pre-defined locations
		
		taggerTagger.game = this;
		runnerTagger.game = this;
		
		taggerTagger.state = Tagger.State.inGame;
		runnerTagger.state = Tagger.State.inGame;
		
		p1.playSound(p1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
		p2.playSound(p2.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
		
	}
	
	public void endGame()
	{
		Bukkit.getScheduler().cancelTask(taskID); // Stops the task
		
		winner = runner;
		
		active = false;
		arena.vacant = true;
		
		// All this does is teleport the tagger and runner to the world spawn of the world they are in
		getTagger().teleport(getTagger().getWorld().getSpawnLocation());
		getRunner().teleport(getRunner().getWorld().getSpawnLocation());
		
		taggerTagger.endGame(false);
		runnerTagger.endGame(true);
		
		getTagger().sendMessage("Lose!");
		getRunner().sendMessage("Win!");
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e)
	{
		if (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player)) { return; }
		
		Player damager = (Player) e.getDamager();
		Player receiver = (Player) e.getEntity();
		
		if (!(damager.equals(getTagger()) && receiver.equals(getRunner()))) { return; }
		
		if (!active) { return; }
		
		swapTagger();
		
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		UUID p = e.getPlayer().getUniqueId();
		if (!(tagger == p ||runner == p)) { return; }
		
		if (tagger == p)
		{
			Bukkit.getScheduler().cancelTask(taskID); // Stops the task
			
			winner = runner;
			
			active = false;
			arena.vacant = true;
			
			// All this does is teleport the tagger and runner to the world spawn of the world they are in
			getRunner().teleport(getRunner().getWorld().getSpawnLocation());
			
			runnerTagger.state = Tagger.State.lobby;
			
			getRunner().sendMessage("Opponent left the match! No points rewarded");
		} else
		{
			Bukkit.getScheduler().cancelTask(taskID); // Stops the task
			
			winner = runner;
			
			active = false;
			arena.vacant = true;
			
			// All this does is teleport the tagger and runner to the world spawn of the world they are in
			getTagger().teleport(getTagger().getWorld().getSpawnLocation());
			
			taggerTagger.state = Tagger.State.lobby;
			
			getTagger().sendMessage("Opponent left the match! No points rewarded");
		}
	}

}
