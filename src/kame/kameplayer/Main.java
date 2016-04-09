package kame.kameplayer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import kame.kameplayer.TabCompleter.CommandTabCompleter;
import kame.kameplayer.baseutils.Respawn;
import kame.kameplayer.baseutils.Timer;
import kame.kameplayer.baseutils.Util;
import kame.kameplayer.baseutils.Vx_x_Rx;
import kame.kameplayer.commands.CommandAddLore;
import kame.kameplayer.commands.CommandArmor;
import kame.kameplayer.commands.CommandAtack;
import kame.kameplayer.commands.CommandBroadcast;
import kame.kameplayer.commands.CommandDrop;
import kame.kameplayer.commands.CommandEntity;
import kame.kameplayer.commands.CommandEntityRun;
import kame.kameplayer.commands.CommandFill;
import kame.kameplayer.commands.CommandFirework;
import kame.kameplayer.commands.CommandItemRun;
import kame.kameplayer.commands.CommandItemTp;
import kame.kameplayer.commands.CommandParticle;
import kame.kameplayer.commands.CommandRespawn;
import kame.kameplayer.commands.CommandTpto;
import kame.kameplayer.commands.CommandVec;
import kame.kameplayer.commands.Commandexplode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Main
extends JavaPlugin
implements Listener
{
	public static final String ver = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3].substring(1);
	public static boolean bool = false;
	public static Plugin plugin = null;
	private static Main instance = null;
	public static Logger log;
	public static Vx_x_Rx obj;
	public static Map<UUID, Respawn> map = new HashMap<UUID, Respawn>();
	public static File file;
	//public static ConcurrentHashMap<UUID, Boolean> list = new ConcurrentHashMap<UUID, Boolean>();
	//public static ConcurrentHashMap<UUID, Location> loc = new ConcurrentHashMap<UUID, Location>();
	public void onEnable()
	{
		try {
			obj = (Vx_x_Rx) Class.forName("kame.kameplayer.baseutils.V" + Main.ver).newInstance();
		} catch (Exception e) {
			System.err.println("<kamePlayer> このBukkitのバージョンには対応していません " + Main.ver);
			System.err.println("<kamePlayer> 一部コマンドが正常に処理されない場合があります");

			System.err.println("<kamePlayer> Sorry... this version is not supported " + Main.ver);
		}
		file = getDataFolder();
		
		FileConfiguration config;
		this.saveDefaultConfig();
		config = getConfig();
		config.options().copyDefaults(true);
		bool = config.getBoolean("debug");
		instance = this;
		plugin = this;
		log = getLogger();
		new Timer().runTaskTimer(this,1,10);

		log.info("有効になりました ver" + ver + "です");
		getServer().getPluginManager().registerEvents(this, this);
		CommandTabCompleter CommandTabCompleter = new CommandTabCompleter();
		getCommand("armor").setTabCompleter(CommandTabCompleter);
		getCommand("broadcast").setTabCompleter(CommandTabCompleter);
		getCommand("tpto").setTabCompleter(CommandTabCompleter);
		getCommand("addlore").setTabCompleter(CommandTabCompleter);
		getCommand("vec").setTabCompleter(CommandTabCompleter);
		getCommand("drop").setTabCompleter(CommandTabCompleter);
		getCommand("particle").setTabCompleter(CommandTabCompleter);
		getCommand("entity").setTabCompleter(CommandTabCompleter);
		getCommand("explode").setTabCompleter(CommandTabCompleter);
		getCommand("firework").setTabCompleter(CommandTabCompleter);
		getCommand("itemtp").setTabCompleter(CommandTabCompleter);
		getCommand("respawn").setTabCompleter(CommandTabCompleter);
		getCommand("attack").setTabCompleter(CommandTabCompleter);
		getCommand("itemrun").setTabCompleter(CommandTabCompleter);
		getCommand("entityrun").setTabCompleter(CommandTabCompleter);
		getCommand("fill").setTabCompleter(CommandTabCompleter);

		getCommand("armor").setExecutor(new CommandArmor());
		getCommand("fill").setExecutor(new CommandFill());
		getCommand("broadcast").setExecutor(new CommandBroadcast());
		getCommand("tpto").setExecutor(new CommandTpto());
		getCommand("addlore").setExecutor(new CommandAddLore());
		getCommand("vec").setExecutor(new CommandVec());
		getCommand("drop").setExecutor(new CommandDrop());
		getCommand("entity").setExecutor(new CommandEntity());
		getCommand("itemtp").setExecutor(new CommandItemTp());
		getCommand("particle").setExecutor(new CommandParticle());
		getCommand("explode").setExecutor(new Commandexplode());
		getCommand("firework").setExecutor(new CommandFirework());
		getCommand("respawn").setExecutor(new CommandRespawn());
		getCommand("attack").setExecutor(new CommandAtack());
		getCommand("itemrun").setExecutor(new CommandItemRun());
		getCommand("entityrun").setExecutor(new CommandEntityRun());
		
		
		FileConfiguration respawn = YamlConfiguration.loadConfiguration(new File(file, "respawn.save"));
		if(respawn.contains("spawn")){
			
			System.out.println(respawn.getConfigurationSection("spawn").getKeys(false));
			for(String uuid : respawn.getConfigurationSection("spawn").getKeys(false))
			{
				UUID uid = UUID.fromString(uuid);
				String args[] = respawn.getString("spawn."+ uuid).split(" ");
				World world = Bukkit.getWorld(args[0]);
				double x = Double.parseDouble(args[1]);
				double y = Double.parseDouble(args[2]);
				double z = Double.parseDouble(args[3]);
				float yaw = Float.parseFloat(args[4]);
				float pitch=Float.parseFloat(args[5]);
				boolean keep = args[6].equals("true");
				Location loc=new Location(world, x, y, z, yaw, pitch);
				map.put(uid, new Respawn(loc, keep));
			}
		}
		try{
			respawn.save(new File(file, "respawn.save"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void onDisable()
	{
		log.info("無効になりました");
	}
	public static Plugin getInstance() {
		return instance;
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if(map.containsKey(event.getEntity().getUniqueId()))
		new CommandRespawn().onPlayerDeath(event.getEntity());
	}
	
	@EventHandler
	private void onDeath(VehicleDestroyEvent event)
	{
		Util.killed(event.getVehicle());
	}
	@EventHandler
	private void onDeath(EntityDeathEvent event)
	{
		Util.killed(event.getEntity());
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(cmd.getName().equals("kameplayer") && sender.isOp())
		{
			FileConfiguration config;
			this.saveDefaultConfig();
			config = YamlConfiguration.loadConfiguration(new File(file, "config.yml"));
			config.options().copyDefaults(true);
			bool = config.getBoolean("debug");
			sender.sendMessage(ChatColor.AQUA + "[kameplayer] Reload config.");
		}
		return true;
	}

}