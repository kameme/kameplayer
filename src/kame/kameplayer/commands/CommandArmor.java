package kame.kameplayer.commands;

import kame.kameplayer.Main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CommandArmor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args)
	{
		if(!sender.isOp())return false;
		if(args.length == 1 || args.length == 2)
		{
			Player player = Bukkit.getPlayer(args[0]);
			if(player == null){sender.sendMessage("そのプレイヤーは存在しません! " + args[0] +  "is Offline!");return true;}
			boolean hold = false;
			PlayerInventory inv = player.getInventory();
			if(args.length == 2)
			{
				hold = true;
				if(args[1].equals("repair"))
				{
					if(inv.getHelmet() != null)inv.getHelmet().setDurability((short) 0);
					if(inv.getChestplate() != null)inv.getChestplate().setDurability((short) 0);
					if(inv.getLeggings() != null)inv.getLeggings().setDurability((short) 0);
					if(inv.getBoots() != null)inv.getBoots().setDurability((short) 0);
					if(Main.bool)sender.sendMessage("装備を修復しました");
					return true;
				}
			}
			ItemStack item;
			for(int i=0;i<36;i++)
			{
				
				item = inv.getItem(i);
				if(item != null)
				switch(item.getType())
				{
				case LEATHER_HELMET:if(inv.getHelmet() == null){inv.setHelmet(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case LEATHER_CHESTPLATE:if(inv.getChestplate() == null){inv.setChestplate(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case LEATHER_LEGGINGS:if(inv.getLeggings() == null){inv.setLeggings(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case LEATHER_BOOTS:if(inv.getBoots() == null){inv.setBoots(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
					
				case CHAINMAIL_HELMET:if(inv.getHelmet() == null){inv.setHelmet(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case CHAINMAIL_CHESTPLATE:if(inv.getChestplate() == null){inv.setChestplate(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case CHAINMAIL_LEGGINGS:if(inv.getLeggings() == null){inv.setLeggings(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case CHAINMAIL_BOOTS:if(inv.getBoots() == null){inv.setBoots(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
					
				case IRON_HELMET:if(inv.getHelmet() == null){inv.setHelmet(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case IRON_CHESTPLATE:if(inv.getChestplate() == null){inv.setChestplate(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case IRON_LEGGINGS:if(inv.getLeggings() == null){inv.setLeggings(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case IRON_BOOTS:if(inv.getBoots() == null){inv.setBoots(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
					
				case DIAMOND_HELMET:if(inv.getHelmet() == null){inv.setHelmet(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case DIAMOND_CHESTPLATE:if(inv.getChestplate() == null){inv.setChestplate(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case DIAMOND_LEGGINGS:if(inv.getLeggings() == null){inv.setLeggings(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case DIAMOND_BOOTS:if(inv.getBoots() == null){inv.setBoots(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
					
				case GOLD_HELMET:if(inv.getHelmet() == null){inv.setHelmet(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case GOLD_CHESTPLATE:if(inv.getChestplate() == null){inv.setChestplate(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case GOLD_LEGGINGS:if(inv.getLeggings() == null){inv.setLeggings(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				case GOLD_BOOTS:if(inv.getBoots() == null){inv.setBoots(item);if(!hold)inv.setItem(i, new ItemStack(Material.AIR));}break;
				default:break;
				}
			}
			if(Main.bool)sender.sendMessage("装備を装着しました");
			player.updateInventory();
			return true;
			
		}
		sender.sendMessage("パラメーターが違います!/armor <player>");
		return false;
	}

}
