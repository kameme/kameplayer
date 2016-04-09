package kame.kameplayer.commands;


import kame.kameplayer.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CommandItemTp implements CommandExecutor {

	@Deprecated
	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args)
	{	//        0 1 2 3   4    5    6   args[]
		//   0    1 2 3 4   5    6    7   length
		//itemtp @p x y z item data amount
		if(!sender.isOp())return false;
		sender.sendMessage(ChatColor.RED + "itemtpは非推奨コマンドです、推奨コマンド /itemrun");
		if(args.length > 4)
		{
			Material material = Material.matchMaterial(args[4]);
			if (material == null)material = Bukkit.getUnsafe().getMaterialFromInternalName(args[4]);
			if (material == null)
			{
				sender.sendMessage(ChatColor.RED + "そのアイテムは存在しません");
				return false;
			}
			Player player = Bukkit.getPlayerExact(args[0]);
			if(player == null)
			{
				sender.sendMessage(ChatColor.RED + "そのプレイヤーはいません");
				return false;
			}
			int deldata = -1, delamount = 1;
			if(args.length > 5)deldata = (int) parse(args[5]);
			if(args.length > 6)delamount = (int) parse(args[6]);
			if(deldata < -1)
			{
				sender.sendMessage(ChatColor.RED + "その数値は正しくありません" + args[5]);
				return false;
			}
			if(delamount < 0)
			{
				sender.sendMessage(ChatColor.RED + "その数値は正しくありません" + args[6]);
				return false;
			}
			PlayerInventory inv = player.getInventory();
			ItemStack item;
			int itemamount = 0;
			if(deldata == -1)
			{
				for(int i=0;i<40;i++)
				{
					item = inv.getItem(i);
					if(item != null && item.getType().equals(material))
					{
						int amount = item.getAmount();
						if(args.length < 8 && delamount > 0){
							if(amount <= delamount)inv.setItem(i, new ItemStack(Material.AIR));
							else item.setAmount(item.getAmount() - delamount);
						}
						delamount-=amount;
						itemamount+=amount;
					}
				}
				if(Main.bool)sender.sendMessage(ChatColor.AQUA + player.getName() + "の手持ちに" + itemamount + "個見つかりました");
				if(delamount > 0)
				{
					if(Main.bool)sender.sendMessage(ChatColor.RED + player.getName() + "は指定した数値以上のアイテムを持っていませんでした");
					return false;
				}
				player.updateInventory();
				player.teleport(new Location(player.getWorld(), parse(args[1], player.getLocation().getX()), parse(args[2], player.getLocation().getY()), parse(args[3], player.getLocation().getZ())));
				return true;
			}else
			{
				for(int i=0;i<40;i++)
				{
					item = inv.getItem(i);
					if(item != null && item.getType().equals(material) && item.getDurability() == deldata)
					{
						int amount = item.getAmount();
						if(args.length < 8 && delamount > 0){
							if(amount <= delamount)inv.setItem(i, new ItemStack(Material.AIR));
							else item.setAmount(item.getAmount() - delamount);
						}
						delamount-=amount;
						itemamount+=amount;
					}
					if(delamount == 0)break;
				}
				if(Main.bool)sender.sendMessage(ChatColor.AQUA + player.getName() + "の手持ちに" + itemamount + "個見つかりました");
				if(delamount > 0)
				{
					if(Main.bool)sender.sendMessage(ChatColor.RED + player.getName() + "は指定した数値以上のアイテムを持っていませんでした");
					return false;
				}
				player.updateInventory();
				player.teleport(new Location(player.getWorld(), parse(args[1], player.getLocation().getX()), parse(args[2], player.getLocation().getY()), parse(args[3], player.getLocation().getZ())));
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + "パラメーターが違います");
		sender.sendMessage(ChatColor.RED + "Usage: itemtp <player> x y z item data amount");
		return false;
	}

	private double parse(String str) {
		try {return Double.valueOf(str);}
		catch (Exception e){return -1;}
	}

	private double parse(String str, double vec)
	{
		if(str.contains("~")){
			str = str.replace("~","");
			try {if(str.contains("."))return vec + Double.valueOf(str); return vec + Integer.valueOf(str) + 0.5f;}
			catch (Exception e) {return vec;}
		}
		try {if(str.contains("."))return Double.valueOf(str); return Integer.valueOf(str) + 0.5f;}
		catch (Exception e) {return vec;}
	}

}
