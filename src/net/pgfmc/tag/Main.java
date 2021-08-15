package net.pgfmc.tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
		
		AddStartLoc addStartLoc = new AddStartLoc();
		ChangeArenaLoc changeArenaLoc = new ChangeArenaLoc();
		CreateArena createArena = new CreateArena();
		DelArena delArena = new DelArena();
		DelStartLoc delStartLoc = new DelStartLoc();
		ToggleArena toggleArena = new ToggleArena();
		Tag tag = new Tag();
		
		getCommand("addStartLoc").setExecutor(addStartLoc);
		getCommand("changeArenaLoc").setExecutor(changeArenaLoc);
		getCommand("createArena").setExecutor(createArena);
		getCommand("delArena").setExecutor(delArena);
		getCommand("delStartLoc").setExecutor(delStartLoc);
		getCommand("toggleArena").setExecutor(toggleArena);
		getCommand("tag").setExecutor(tag);
		getCommand("arenas").setExecutor(new Arenas());
		
		getServer().getPluginManager().registerEvents(addStartLoc, this);
		getServer().getPluginManager().registerEvents(changeArenaLoc, this);
		getServer().getPluginManager().registerEvents(createArena, this);
		getServer().getPluginManager().registerEvents(delArena, this);
		getServer().getPluginManager().registerEvents(delStartLoc, this);
		getServer().getPluginManager().registerEvents(toggleArena, this);
		getServer().getPluginManager().registerEvents(tag, this);
		getServer().getPluginManager().registerEvents(new Game(), this);
		
		
		
		
		
		
		// bruv
		File file = new File(plugin.getDataFolder() + File.separator + "data");
		file.mkdirs();
		file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "data.yml");
		FileConfiguration db = YamlConfiguration.loadConfiguration(file);
		try { file.createNewFile(); } catch (IOException except) { except.printStackTrace(); }
		try { db.load(file); } catch (FileNotFoundException except) { except.printStackTrace(); } catch (IOException except) { except.printStackTrace(); } catch (InvalidConfigurationException except) { except.printStackTrace(); }
		// bruv
		
		
		LinkedList<Tagger> taggers = new LinkedList<Tagger>();
		TAGGERS.clear();
		LinkedList<Arena> arenas = new LinkedList<Arena>();
		ARENAS.clear();
		
		if ((List<Object>) db.getList("taggers") != null)
		{
			for (Object object : (List<Object>) db.getList("taggers"))
			{
				taggers.add(Tagger.deserialize((List<Object>) object));
			}
			
		}
		
		if ((List<Object>) db.getList("arenas") != null)
		{
			for (Object object : (List<Object>) db.getList("arenas"))
			{
				arenas.add(Arena.deserialize((List<Object>) object));
			}
			
		}
		
		TAGGERS = taggers;
		ARENAS = arenas;
		
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
		
		
		db.set("taggers", taggers);
		db.set("games", games);
		db.set("arenas", arenas);
		// db.set("requests", REQUESTS); // Probably don't save this lol
		
		try { db.save(file); } catch (IOException except) { except.printStackTrace(); }
	}
	
	@SuppressWarnings("unchecked")
	public static void quickSave(List<Object> object, String name)
	{
		// bruv
		File file = new File(plugin.getDataFolder() + File.separator + "data");
		file.mkdirs();
		file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "data.yml");
		FileConfiguration db = YamlConfiguration.loadConfiguration(file);
		try { file.createNewFile(); } catch (IOException except) { except.printStackTrace(); }
		try { db.load(file); } catch (FileNotFoundException except) { except.printStackTrace(); } catch (IOException except) { except.printStackTrace(); } catch (InvalidConfigurationException except) { except.printStackTrace(); }
		// bruv
		
		if (name.equals("Tagger"))
		{
			if ((List<Object>) db.getList("taggers") != null)
			{
				db.set("taggers", ((List<Object>) db.getList("taggers")).add(object));
			} else
			{
				List<Object> tempObject = new ArrayList<Object>();
				tempObject.add(object);

				db.set("taggers", tempObject);
			}
			
		}
		
		if (name.equals("Game"))
		{
			if ((List<Object>) db.getList("games") != null)
			{
				db.set("games", ((List<Object>) db.getList("games")).add(object));
			} else
			{
				List<Object> tempObject = new ArrayList<Object>();
				tempObject.add(object);

				db.set("games", tempObject);
			}
			
		}
		
		if (name.equals("Arena"))
		{
			if ((List<Object>) db.getList("arenas") != null)
			{
				db.set("arenas", ((List<Object>) db.getList("arenas")).add(object));
			} else
			{
				List<Object> tempObject = new ArrayList<Object>();
				tempObject.add(object);

				db.set("arenas", tempObject);
			}
			
		}
		
		try { db.save(file); } catch (IOException except) { except.printStackTrace(); }
		
		
	}

}
