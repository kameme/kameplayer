package kame.kameplayer.commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kame.kameplayer.Main;
import kame.kameplayer.baseutils.Respawn;
import kame.kameplayer.baseutils.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandRespawn implements CommandExecutor {

	public static Map<UUID, Location> map = new HashMap<UUID, Location>();
	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args)
	{
		if(!sender.isOp())return false;
		//respawn @p (world) x y z pitch yaw

		if(args.length > 0)
		{
			Player player = Bukkit.getPlayerExact(args[0]);
			if(player != null)
			{
				if(args.length > 1)
				{
					if(args[1].equals("set"))
					{
						boolean value = false;
						if(args[args.length-1].equals("keep"))value = true;
						Location location = null;
						if(player.getBedSpawnLocation() != null)location = player.getBedSpawnLocation().add(0.5, 0, 0.5);
						if(args.length > 5)
						{
							World world = Bukkit.getWorld(args[2]);
							Double x = parse(args[3]), y = parse(args[4]), z = parse(args[5]);
							Float pitch =0f, yaw = 0f;
							if(args.length > 6) yaw = parse2(args[6]);
							if(args.length > 7) pitch = parse2(args[7]);
							if(world != null && x != null && y != null && z != null)
							{
								location = new Location(world, x, y, z, yaw, pitch);
								if(Main.bool)sender.sendMessage(ChatColor.GREEN + "リスポーン座標を登録しました");
							}
						}
						if(location != null)Main.map.put(player.getUniqueId(), new Respawn(location, value));
						else sender.sendMessage(ChatColor.RED + "座標が無効でした");
						if(Main.bool)sender.sendMessage(ChatColor.GREEN + "リスポーン設定を登録しました");
						saveConfig();
						return true;
					}
					if(args[1].equals("remove"))
					{
						Main.map.remove(player.getUniqueId());
						if(Main.bool)sender.sendMessage(ChatColor.GREEN + "リスポーン設定を削除しました");
						saveConfig();
						return true;
					}
				}
				if(player.isDead())
				{
					Util.sendRespawn(player);
					if(args.length >= 5)
					{
						World world = Bukkit.getWorld(args[1]);
						Double x = parse(args[2]), y = parse(args[3]), z = parse(args[4]);
						Float pitch =0f, yaw = 0f;
						if(args.length > 5) pitch = parse2(args[5]);
						if(args.length > 6) yaw = parse2(args[6]);
						if(world == null || x == null || y == null || z == null)
						{
							if(Main.bool)sender.sendMessage(ChatColor.GREEN + "プレイヤーをリスポーンさせましたが座標が無効でした");
							return true;
						}
						player.teleport(new Location(world, x, y, z, pitch, yaw));
					}

					if(Main.bool)sender.sendMessage(ChatColor.GREEN + "プレイヤーをリスポーンさせました");
					return true;
				}
			}
			if(Main.bool)sender.sendMessage(ChatColor.RED + "そのプレイヤーは死んでいないかオフラインです");
			return false;
		}
		sender.sendMessage(ChatColor.RED + "パラメーターが違います");
		sender.sendMessage(ChatColor.RED + "Usage: /respawn <player> (world) x y z yaw pitch");
		return false;
	}

	private void saveConfig(){
		YamlConfiguration respawn = new YamlConfiguration();
		for(Map.Entry<UUID, Respawn> mp : Main.map.entrySet())
		{
			Respawn res = mp.getValue();
			Location loc = res.getLocation();
			
			respawn.set("spawn." + mp.getKey().toString(), 
			new StringBuilder()
			.append(loc.getWorld().getName()).append(" ")
			.append(loc.getX()).append(" ")
			.append(loc.getY()).append(" ")
			.append(loc.getZ()).append(" ")
			.append(loc.getYaw()).append(" ")
			.append(loc.getPitch()).append(" ")
			.append(res.getKeep()).toString());
		}
		try{
			respawn.save(new File(Main.file, "respawn.save"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onPlayerDeath(final Player player) {

		new BukkitRunnable(){
			@Override
			public void run(){
				Util.sendRespawn(player);
				if(Main.map.containsKey(player.getUniqueId()))
				{
					Respawn respawn = Main.map.get(player.getUniqueId());
					player.teleport(respawn.getLocation());
					if(!respawn.getKeep())Main.map.remove(player.getUniqueId());
					saveConfig();
				}
				return;
			}
		}.runTask(Main.plugin);
	}

	private Double parse(String str) {
		try {if(str.contains("."))return Double.valueOf(str); return Integer.valueOf(str) + 0.5d;}
		catch (Exception e){return null;}
	}
	private float parse2(String str) {
		try {return Float.valueOf(str);}
		catch (Exception e){return 0;}
	}
}
