package kame.kameplayer.commands;

import kame.kameplayer.Main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBroadcast implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args)
	{
		if(!sender.isOp())return false;
		if(args.length > 1)
		{
			boolean skip = false;
			StringBuilder message = new StringBuilder();
			for(int i = 1 ; i < args.length ; i++)
			{
				if(!skip)
				{
					String str = args[i];
					if(str.startsWith("score_") && args.length - 1 > i)
					{
						if(parse(Main.ver.replaceAll("[a-zA-Z_]", "")) <= 172)
							message.append(Bukkit.getScoreboardManager().getMainScoreboard().getObjective(str.replaceFirst("score_", "")).getScore(Bukkit.getOfflinePlayer(args[i+1])).getScore());
						else 
							message.append(Bukkit.getScoreboardManager().getMainScoreboard().getObjective(str.replaceFirst("score_", "")).getScore(args[i+1]).getScore());
						skip = true;
					}
					else if(str.equals("[+]"))
					{
						message.append(" ");
					}
					else if(str.equals("/n"))
					{
						if(args[0].equals("all"))for(Player player : Bukkit.getOnlinePlayers())player.sendMessage(message.toString());
						else if(args[0].equals("server"))Bukkit.broadcastMessage(message.toString());
						else {Player player = Bukkit.getPlayerExact(args[0]);if(player != null)player.sendMessage(message.toString());}
						message = new StringBuilder();
					}
					else
					{
						Player p = Bukkit.getPlayerExact(str);
						if(p == null)message.append(str);
						else message.append(p.getDisplayName());
					}
				}
				else skip = false;
			}if(args[0].equals("all"))for(Player player : Bukkit.getOnlinePlayers())player.sendMessage(message.toString());
			else if(args[0].equals("server"))Bukkit.broadcastMessage(message.toString());
			else {Player player = Bukkit.getPlayerExact(args[0]);if(player != null)player.sendMessage(message.toString());
			}
		}
		return false;
	}
	private int parse(String i){
		return Integer.parseInt(i);
	}

}
