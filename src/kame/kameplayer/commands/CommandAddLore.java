package kame.kameplayer.commands;

import java.util.ArrayList;
import java.util.List;

import kame.kameplayer.Main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandAddLore implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (sender.isOp())
		{
			if(args.length == 4)
			{
				Player player = Bukkit.getPlayerExact(args[0]);
				Material material = Bukkit.getUnsafe().getMaterialFromInternalName(args[1]);
				if(player == null){
					sender.sendMessage("そのプレイヤーはいません");
					return false;
				}
				if(material == null){
					sender.sendMessage("そのアイテムは存在しません");
					return false;
				}
				PlayerInventory inv = player.getInventory();
				ItemStack item;
				int j = parse(args[2], -1), items = 0;
				String str = args[3].replaceAll("<player>", player.getName());
				for(int i=0;i<40;i++)
				{
					item = inv.getItem(i);
					if(item != null && item.getType().equals(material) && (j == -1 || item.getDurability() == j))
					{
						ItemMeta im = item.getItemMeta();
						List<String> lore = new ArrayList<String>();
						if(im.hasLore())lore = im.getLore();
						lore.add(str);
						im.setLore(lore);
						item.setItemMeta(im);
						inv.setItem(i, item);
						items++;
					}
				}
				if(items > 0)
				{
					if(Main.bool)sender.sendMessage(new StringBuilder(player.getName()).append("のインベントリに").append(items).append("スタック見つかりました").toString());
					return true;
				}
				if(Main.bool)sender.sendMessage(new StringBuilder(player.getName()).append("の持ち物にアイテムが見つかりませんでした").toString());
				return false;
			}
			sender.sendMessage("パラメーターが違います");
			sender.sendMessage("/addlore <player> material (data) lore");
		}
		return false;
	}
	private int parse(String str, int i)
	{
		try{
			int j = Integer.valueOf(str);
			if(j<i)return i;
			return j;
		}
		catch(Exception e){return i;}
	}

}
