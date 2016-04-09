package kame.kameplayer.commands;

import java.util.List;

import kame.kameplayer.baseutils.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CommandEntity implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(!sender.isOp())return false;
		if(args.length >= 3)
		{
			String type = null;
			float r =  parse(args[2], 0f);
			if(args.length == 4)type = args[3];
			Entity e = Bukkit.getPlayer(args[1]);
			if(args[1].equals("@b") && sender instanceof BlockCommandSender)
			{
				Block commandblock = ((BlockCommandSender)sender).getBlock();
				e = commandblock.getWorld().spawnEntity(commandblock.getLocation().subtract(0.5, -0.5, 0.5), EntityType.FALLING_BLOCK);
			}
			if(args[1].equals("loc"))
			{
				if(args.length >= 6)
				{
					Block commandblock = ((BlockCommandSender)sender).getBlock();
					e = commandblock.getWorld().spawnEntity(new Location(commandblock.getWorld(), parse(args[2],commandblock.getX()), parse(args[3],commandblock.getY()), parse(args[4],commandblock.getZ())) , EntityType.FALLING_BLOCK);
					r =  parse(args[5], 0f);
					if(args.length == 7)type = args[6];
				}
				
				sender.sendMessage("パラメーターが違います! /entity remove loc x y z <radius> <type>");
			}
			if(e == null){sender.sendMessage(ChatColor.RED + "そのプレイヤーは存在しません");return false;}
			List<Entity> entitys = e.getNearbyEntities(r, r, r);
			if(e.getType().equals(EntityType.FALLING_BLOCK))e.remove();
			if(args[0].equals("remove"))
			{
				for(Entity entity : entitys)
				{
					if(!(entity instanceof Player)){
						if(entity.getType().toString().equalsIgnoreCase(type)){entity.remove();}
						else if(type == null)entity.remove();
					}
				}
				return true;
			}
		}
		sender.sendMessage("パラメーターが違います! /entity remove <player> <radius> <type>");
		return false;
	}
	public float parse(String str, float def)
	{
		return Util.parseRaw(str, def);
	}
	public double parse(String str, double vec)
	{
		return Util.parse(str, vec);
	}
}
