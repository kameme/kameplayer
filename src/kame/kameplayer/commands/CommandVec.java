package kame.kameplayer.commands;

import kame.kameplayer.baseutils.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CommandVec implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args)
	{
		if(!sender.isOp())return false;
		if(args.length == 3 || args.length == 4)
		{
			Player player = Bukkit.getPlayer(args[0]);
			if(args.length == 3 && player == null && sender instanceof Player)player = (Player) sender;
			if(player == null){sender.sendMessage("そのプレイヤーは存在しません! " + args[0] + " is Offline!");return false;}
			if(args.length == 4)player.setVelocity(new Vector(
					parse(args[1], (float)player.getVelocity().getX()),
					parse(args[2], (float)player.getVelocity().getY()),
					parse(args[3], (float)player.getVelocity().getZ())));
			if(args.length == 3)player.setVelocity(new Vector(
					parse(args[0], (float)player.getVelocity().getX()),
					parse(args[1], (float)player.getVelocity().getY()),
					parse(args[2], (float)player.getVelocity().getZ())));
			return true;
		}
		
		sender.sendMessage("パラメーターが違います!/vec <player> x y z");
		return false;
	}
	public float parse(String str, float vec)
	{
		return Util.parseRaw(str, vec);
	}

}
