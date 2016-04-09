package kame.kameplayer.commands;

import kame.kameplayer.baseutils.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class CommandItemRun implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args)
	{	//        0 1 2 3   4    5    6   args[]
		//   0           1        2   3   4       5     6
		//itemrun <player> item data amount {NBT} /command
		if(!sender.isOp())return false;
		if(args.length > 1)
		{
			Player player = Bukkit.getPlayerExact(args[0]);
			if(player == null) {
				sender.sendMessage(ChatColor.RED + "そのプレイヤーはいません");
				return false;
			}

			Material material = Material.matchMaterial(args[1]);
			if (material == null)material = Bukkit.getUnsafe().getMaterialFromInternalName(args[1]);

			if (!material.toString().equals("AIR")) {
				int deldata = -1, delamount = 0;
				if(args.length > 2)deldata = parse(args[2], -1);
				if(args.length > 3)delamount = parse(args[3], 0);

				if(deldata < -1) {
					sender.sendMessage(ChatColor.RED + "その数値は正しくありません" + args[2]);
					return false;
				}

				if(delamount < -1) {
					sender.sendMessage(ChatColor.RED + "その数値は正しくありません" + args[3]);
					return false;
				}

				ItemStack bufferitem = new ItemStack(material, delamount, (short)deldata);
				String tag = "";
				if(args.length > 4 && args[4].startsWith("{")) {
					bufferitem = Util.setNBTTag(player, bufferitem, args);
					tag = Util.getNBTTag(bufferitem);
					if(tag.length() < 1) {
						player.sendMessage(ChatColor.RED + "データタグに間違いがあります");
						return false;
					}
				}
				PlayerInventory inv = player.getInventory();
				ItemStack item;
				int itemamount = 0;
				for(int i=0;i<40;i++) {
					item = inv.getItem(i);
					if(item != null && item.getType().equals(material) && (deldata == -1 || item.getDurability() == deldata) && (tag.length() == 0 || tag.equals(Util.getNBTTag(item))))
					{
						int amount = item.getAmount();
						if(delamount > 0){
							if(amount <= delamount)inv.setItem(i, new ItemStack(Material.AIR));
							else item.setAmount(item.getAmount() - delamount);
						}
						delamount-=amount;
						itemamount+=amount;
					}
					if(delamount == 0)break;
				}
				sender.sendMessage(ChatColor.AQUA + player.getName() + "の手持ちに" + itemamount + "個見つかりました");
				if(delamount > 0) {
					sender.sendMessage(ChatColor.RED + player.getName() + "は指定した数値以上のアイテムを持っていませんでした");
					return false;
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + player.getName() + " アイテムは無効です。コマンドのみ実行します");
			}

			player.updateInventory();
			StringBuilder str = new StringBuilder();
			int i = a(args);
			if(i == 0)return true;
			for(int j = i; args.length >j+1; j++)str.append(args[j]).append(" ");
			if(args[args.length-1].equals("-toplayer")) {
				if (!player.isOp()) {
					try {
						player.setOp(true);
						Bukkit.dispatchCommand(player, str.toString().replaceFirst("/",""));
					}finally {player.setOp(false);}
				}
				else {
					Bukkit.dispatchCommand(player, str.toString().replaceFirst("/",""));
				}
			}
			else {
				Bukkit.dispatchCommand(sender, str.toString().replaceFirst("/",""));
			}
			return true;
		}
		sender.sendMessage(ChatColor.RED + "パラメーターが違います");
		sender.sendMessage(ChatColor.RED + "Usage: /itemrun <player> item data amount {NBT} /command [toplayer]");
		return false;
	}

	private int a(String[] args) {
		int i = 0;
		for(String str: args){
			if(str.startsWith("/"))return i;
			i++;
		}
		return 0;
	}

	private int parse(String str, int def) {
		str = str.replace("~","");
		return Util.parse(str, def);
	}


}
