package kame.kameplayer.commands;

import kame.kameplayer.baseutils.Fill;
import kame.kameplayer.baseutils.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFill implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args)
	{//fill x y z dx dy dz block option
	//replace destroy keep replacement
		if(!sender.isOp())return false;
		if(args.length > 6)
		{
			Location loc;
			if(sender instanceof BlockCommandSender)
			{
				loc = ((BlockCommandSender) sender).getBlock().getLocation();
			}else if(sender instanceof Player){
				loc = ((Player) sender).getLocation();
			}else return result(sender, "ゲーム内よりコマンドを実行してください");

			Location vec = loc.clone();
			loc.setX(Util.parse(args[0], loc.getBlockX()));
			loc.setY(Util.parse(args[1], loc.getBlockY()));
			loc.setZ(Util.parse(args[2], loc.getBlockZ()));
			vec.setX(Util.parse(args[3], loc.getBlockX()));
			vec.setY(Util.parse(args[4], loc.getBlockY()));
			vec.setZ(Util.parse(args[5], loc.getBlockZ()));
			Material mate = null;
			Material material = getMaterial(args[6]);
			if(material == null)return result(sender, ChatColor.RED + "[kameplayer] ブロックを選択してください");
			int mode = 0;
			if(args.length > 7){
				switch(args[7])
				{
				case "replace": mode = 0;
					break;
				case "replacement": mode = 1;
					break;
				case "destroy": mode = 2;
					break;
				case "keep": mode = 3;
					break;
				default: return result(sender, ChatColor.RED + "[kameplayer] そのオプションは存在しません");
				}
				if(args.length > 8)mate = getMaterial(args[8]);
				if(mode == 1) {
					if(mate == null)return result(sender, ChatColor.RED + "[kameplayer] ブロックを選択してください");
				}
			}
			new Fill(loc, vec, material, mode, mate).start();
			return true;
		}
		sender.sendMessage(ChatColor.RED + "[kameplayer] パラメーターが違います " + cmd);
		sender.sendMessage(ChatColor.RED + "[kameplayer] /fill x y z dx dy dz block option");
		return false;
	}
	private Material getMaterial(String str)
	{
		Material m = Material.getMaterial(str);
		if(m == null || !m.isBlock())m = Bukkit.getUnsafe().getMaterialFromInternalName(str);
		if(m == null || !m.isBlock())return null;
		return m;
	}
	private boolean result(CommandSender sender, String str)
	{
		sender.sendMessage(str);
		return false;
	}

}
