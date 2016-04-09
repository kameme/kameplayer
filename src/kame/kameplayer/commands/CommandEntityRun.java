package kame.kameplayer.commands;

import kame.kameplayer.baseutils.Entities;
import kame.kameplayer.baseutils.Timer;
import kame.kameplayer.baseutils.Util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class CommandEntityRun implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//cmdmonster entity x y z command
		//cm entity x y z
		if(!sender.isOp())return false;
		if(args.length > 0)
		{
			Location loc = null;
			if(sender instanceof BlockCommandSender)
			{
				loc = ((BlockCommandSender)sender).getBlock().getLocation();
				loc.add(0.5,0,0.5);
			}
			else if(sender instanceof Player)
			{
				loc = ((Player) sender).getLocation();
			}else {
				sender.sendMessage("ワールド内から送信してください");
				return false;
			}
			if(args.length > 3) {
				loc.setX(parse(args[1], loc.getX()));
				loc.setY(parse(args[2], loc.getY()));
				loc.setZ(parse(args[3], loc.getZ()));
			}
			Entity e = Util.getNBTEntity(sender, loc, args);
			if(e == null)
			{
				sender.sendMessage(ChatColor.RED + "処理に失敗しました、エンティティを確認してください");
				return false;
			}
			StringBuilder str = new StringBuilder();
			int i = a(args);
			if(i > 0)for(int j = i; args.length > j; j++)str.append(args[j]).append(" ");
			try{
				String command =  str.toString();
				if(command.length() > 0) Timer.entities.add(new Entities(e, sender, command));
			}catch(Exception ex){
				sender.sendMessage(ChatColor.RED + "処理に失敗しました、コマンドを確認してください");
				return false;
			}
			return true;
		}
		sender.sendMessage(ChatColor.RED + "パラメーターが一致しません /command entity x y z [cmd]" );
		return false;
	}

	private int a(String[] args) {
		int i = 0;
		for(String str: args)
		{
			if(str.startsWith("/"))return i;
			i++;
		}
		return i;
	}

	public double parse(String str, double vec)
	{
		return Util.parse(str, vec);
	}
	
	
}