package net.pgfmc.tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.tag.commands.arena.AddStartLoc;
import net.pgfmc.tag.commands.arena.ChangeArenaLoc;
import net.pgfmc.tag.commands.arena.CreateArena;
import net.pgfmc.tag.commands.arena.DelArena;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("addStartLoc").setExecutor(new AddStartLoc());
		getCommand("changeArenaLoc").setExecutor(new ChangeArenaLoc());
		getCommand("createArena").setExecutor(new CreateArena());
		getCommand("delArena").setExecutor(new DelArena());
		getCommand("toggleArena").setExecutor(new ToggleArena());
		getCommand("tag").setExecutor(new Tag());
		getCommand("arenas").setExecutor(new Arenas());
		
		getServer().getPluginManager().registerEvents(new AddStartLoc(), this);
		getServer().getPluginManager().registerEvents(new ChangeArenaLoc(), this);
		getServer().getPluginManager().registerEvents(new CreateArena(), this);
		getServer().getPluginManager().registerEvents(new DelArena(), this);
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
		
		TAGGERS = (LinkedList<Tagger>) db.get("taggers");
		GAMES = (LinkedList<Game>) db.get("games");
		ARENAS = (LinkedList<Arena>) db.get("arenas");
		REQUESTS = (LinkedList<Request>) db.get("requests");
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
		
		
		db.set("taggers", TAGGERS);
		db.set("games", GAMES);
		db.set("arenas", ARENAS);
		// db.set("requests", REQUESTS); // Probably don't save this lol
		
		try { db.save(file); } catch (IOException except) { except.printStackTrace(); }
	}
	
	public static void quickSave()
	{
		// bruv
		File file = new File(plugin.getDataFolder() + File.separator + "data");
		file.mkdirs();
		file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "data.yml");
		FileConfiguration db = YamlConfiguration.loadConfiguration(file);
		try { file.createNewFile(); } catch (IOException except) { except.printStackTrace(); }
		try { db.load(file); } catch (FileNotFoundException except) { except.printStackTrace(); } catch (IOException except) { except.printStackTrace(); } catch (InvalidConfigurationException except) { except.printStackTrace(); }
		// bruv
		
		db.set("taggers", TAGGERS);
		db.set("games", GAMES);
		db.set("arenas", ARENAS);
		// db.set("requests", REQUESTS); // Probably don't save this lol
		
		try { db.save(file); } catch (IOException except) { except.printStackTrace(); }
	}

}
