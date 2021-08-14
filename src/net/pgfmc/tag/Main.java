package net.pgfmc.tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.tag.commands.arena.AddStartLoc;
import net.pgfmc.tag.commands.arena.ChangeArenaLoc;
import net.pgfmc.tag.commands.arena.CreateArena;
import net.pgfmc.tag.commands.arena.DelArena;
import net.pgfmc.tag.commands.arena.DelStartLoc;
import net.pgfmc.tag.commands.arena.ToggleArena;
import net.pgfmc.tag.commands.request.Arenas;
import net.pgfmc.tag.commands.request.Request;
import net.pgfmc.tag.commands.request.Tag;
import net.pgfmc.tag.game.Arena;
import net.pgfmc.tag.game.Game;
import net.pgfmc.tag.game.Tagger;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	public static LinkedList<Tagger> TAGGERS = new LinkedList<Tagger>();
	public static LinkedList<Game> GAMES = new LinkedList<Game>();
	public static LinkedList<Arena> ARENAS = new LinkedList<Arena>();
	public static LinkedList<Request> REQUESTS = new LinkedList<Request>();
	public static LinkedList<Object> DEVOBJS = new LinkedList<Object>();
	
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("addStartLoc").setExecutor(new AddStartLoc());
		getCommand("changeArenaLoc").setExecutor(new ChangeArenaLoc());
		getCommand("createArena").setExecutor(new CreateArena());
		getCommand("delArena").setExecutor(new DelArena());
		getCommand("delStartLoc").setExecutor(new DelStartLoc());
		getCommand("toggleArena").setExecutor(new ToggleArena());
		getCommand("tag").setExecutor(new Tag());
		getCommand("arenas").setExecutor(new Arenas());
		
		getServer().getPluginManager().registerEvents(new AddStartLoc(), this);
		getServer().getPluginManager().registerEvents(new ChangeArenaLoc(), this);
		getServer().getPluginManager().registerEvents(new CreateArena(), this);
		getServer().getPluginManager().registerEvents(new DelArena(), this);
		getServer().getPluginManager().registerEvents(new DelStartLoc(), this);
		getServer().getPluginManager().registerEvents(new ToggleArena(), this);
		getServer().getPluginManager().registerEvents(new Tag(), this);
		getServer().getPluginManager().registerEvents(new Game(), this);
		
		
		
		
		
		
		// bruv
		File file = new File(plugin.getDataFolder() + File.separator + "data");
		file.mkdirs();
		file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "data.yml");
		FileConfiguration db = YamlConfiguration.loadConfiguration(file);
		try { file.createNewFile(); } catch (IOException except) { except.printStackTrace(); }
		try { db.load(file); } catch (FileNotFoundException except) { except.printStackTrace(); } catch (IOException except) { except.printStackTrace(); } catch (InvalidConfigurationException except) { except.printStackTrace(); }
		// bruv
		
		TAGGERS.clear();
		ARENAS.clear();
		
		if ((List<Object>) db.get("taggers") != null)
		{
			for (Object object : (List<Object>) db.get("taggers"))
			{
				TAGGERS.add(Tagger.deserialize((List<Object>) object));
			}
			
		}
		
		if ((List<Object>) db.get("arenas") != null)
		{
			for (Object object : (List<Object>) db.get("arenas"))
			{
				ARENAS.add(Arena.deserialize((List<Object>) object));
			}
			
		}
		
		
		
	}
	
	@Override
	public void onDisable()
	{
		// bruv
		File file = new File(plugin.getDataFolder() + File.separator + "data");
		file.mkdirs();
		file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "data.yml");
		FileConfiguration db = YamlConfiguration.loadConfiguration(file);
		try { file.createNewFile(); } catch (IOException except) { except.printStackTrace(); }
		try { db.load(file); } catch (FileNotFoundException except) { except.printStackTrace(); } catch (IOException except) { except.printStackTrace(); } catch (InvalidConfigurationException except) { except.printStackTrace(); }
		// bruv
		
		List<Object> taggers = new ArrayList<Object>();
		List<Object> games = new ArrayList<Object>();
		List<Object> arenas = new ArrayList<Object>();
		
		for (Tagger tagger : TAGGERS)
		{
			taggers.add(tagger.serialize());
		}
		
		for (Game game : GAMES)
		{
			games.add(game.serialize());
		}
		
		for (Arena arena : ARENAS)
		{
			arenas.add(arena.serialize());
		}
		
		
		db.set("taggers", null);
		db.set("taggers", taggers);
		db.set("games", null);
		db.set("games", games);
		db.set("arenas", null);
		db.set("arenas", arenas);
		// db.set("requests", REQUESTS); // Probably don't save this lol
		
		// Might not need this here
		TAGGERS.clear();
		ARENAS.clear();
		
		try { db.save(file); } catch (IOException except) { except.printStackTrace(); }
	}
	
	@SuppressWarnings("unchecked")
	public static void quickSave(Object object)
	{
		// bruv
		File file = new File(plugin.getDataFolder() + File.separator + "data");
		file.mkdirs();
		file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "data.yml");
		FileConfiguration db = YamlConfiguration.loadConfiguration(file);
		try { file.createNewFile(); } catch (IOException except) { except.printStackTrace(); }
		try { db.load(file); } catch (FileNotFoundException except) { except.printStackTrace(); } catch (IOException except) { except.printStackTrace(); } catch (InvalidConfigurationException except) { except.printStackTrace(); }
		// bruv
		
		if (object.getClass().getSimpleName().equals("Tagger"))
		{
			if (((List<Object>) db.get("taggers")) != null)
			{
				db.set("taggers", null);
				db.set("taggers", ((List<Object>) db.get("taggers")).add(((Tagger) object).serialize()));
			} else
			{
				List<Object> tempObject = new ArrayList<Object>();
				tempObject.add(((Tagger) object).serialize());
				
				db.set("taggers", null);
				db.set("taggers", tempObject);
			}
			
		}
		
		if (object.getClass().getSimpleName().equals("Game"))
		{
			if (((List<Object>) db.get("games")) != null)
			{
				db.set("games", null);
				db.set("games", ((List<Object>) db.get("games")).add(((Game) object).serialize()));
			} else
			{
				List<Object> tempObject = new ArrayList<Object>();
				tempObject.add(((Game) object).serialize());
				
				db.set("games", null);
				db.set("games", tempObject);
			}
			
		}
		
		if (object.getClass().getSimpleName().equals("Arena"))
		{
			if (((List<Object>) db.get("arenas")) != null)
			{
				db.set("arenas", null);
				db.set("arenas", ((List<Object>) db.get("arenas")).add(((Arena) object).serialize()));
			} else
			{
				List<Object> tempObject = new ArrayList<Object>();
				tempObject.add(((Arena) object).serialize());
				
				db.set("arenas", null);
				db.set("arenas", tempObject);
			}
			
		}
		
		try { db.save(file); } catch (IOException except) { except.printStackTrace(); }
		
		
	}

}
