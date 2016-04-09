package kame.kameplayer.commands;

import kame.kameplayer.baseutils.Util;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commandexplode implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args)
	{
		if(!sender.isOp())return false;
		if(args.length > 2 && args[0].equals("player"))
		{
			Player player = Bukkit.getPlayer(args[2]);
			if(player != null)
			{
				player.getWorld().createExplosion(player.getLocation().add(0, 1.5, 0), (int)parse(args[1], 0f));
				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "そのプレイヤーはいません！ player is offline!");
				return false;
			}
		}
		if(args.length > 0 && args[0].equals("loc"))
		{
			if(args.length > 4)
			{
				if(sender instanceof Player)
				{
					Player player = (Player)sender;
					Location loc = new Location(player.getWorld(),
							parse(args[2],player.getLocation().getX()),
							parse(args[3],player.getLocation().getY()),
							parse(args[4],player.getLocation().getZ()));
					loc.getWorld().createExplosion(loc, (int)parse(args[1], 0));
					return true;
				}
				if(sender instanceof BlockCommandSender)
				{
					Block block = ((BlockCommandSender) sender).getBlock();
					Location loc = new Location(block.getWorld(),
							parse(args[2],block.getX()+0.5),
							parse(args[3],block.getY()+0.5),
							parse(args[4],block.getZ()+0.5));
					loc.getWorld().createExplosion(loc, (int)parse(args[1], 0));
					return true;
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "コンソールからは送信できません");
					return false;
				}
			}
		}
		sender.sendMessage(ChatColor.RED + "パラメーターが違います");
		sender.sendMessage("explode player (amount) <player>");
		sender.sendMessage("explode loc    (amount)  x y z ");
		return false;
	}
	public float parse(String str, float def)
	{
		str = str.replace("~","");
		return Util.parse(str, def);
	}
	
	public double parse(String str, double vec)
	{
		return Util.parse(str, vec);
	}

}
