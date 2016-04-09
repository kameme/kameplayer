package kame.kameplayer.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CommandDrop implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args)
	{
		if(!sender.isOp())return false;
		if(args.length >= 1)
		{
			Player player = Bukkit.getPlayer(args[0]);
			if(player == null){sender.sendMessage("そのプレイヤーは存在しません! " + args[0] + " is Offline!");return false;}
			int delay = 1;
			if(args.length > 3)delay = (int) parse(args[3],1d);
			
			PlayerInventory inv = player.getInventory();
			World world = player.getWorld();
			Location loc = player.getLocation().add(-0.5f, 0f, -0.5f);
			ItemStack item;
			if(args.length == 1)for(int i=0;i<40;i++){item = inv.getItem(i);
				if(item != null){world.dropItemNaturally(loc, item).setPickupDelay(delay);inv.setItem(i, new ItemStack(Material.AIR));}}
			
			else if(args.length >= 2 )
			{
				if(args[1].equalsIgnoreCase("all")){
					if(args.length == 2 || args[2].equalsIgnoreCase("all")){for(int i=0;i<40;i++){item = inv.getItem(i);
					if(item != null){world.dropItemNaturally(loc, item).setPickupDelay(delay);inv.setItem(i, new ItemStack(Material.AIR));}}}
					else{
						int itemdata = (int)parse(args[2],0d);for(int i=0;i<40;i++){item = inv.getItem(i);
						if(item != null && item.getDurability() == itemdata){
							world.dropItemNaturally(loc, item).setPickupDelay(delay);inv.setItem(i, new ItemStack(Material.AIR));}}}
				}else{
					Material material = Material.matchMaterial(args[1]);
					if (material == null){material = Bukkit.getUnsafe().getMaterialFromInternalName(args[1]);}
					
					if(args.length == 2 || args[2].equalsIgnoreCase("all")){
						for(int i=0;i<40;i++){item = inv.getItem(i);if(item != null && item.getType().equals(material)){
							world.dropItemNaturally(loc, item).setPickupDelay(delay);inv.setItem(i, new ItemStack(Material.AIR));}}
						
					}else{
						int itemdata = (int)parse(args[2],0d);for(int i=0;i<40;i++){item = inv.getItem(i);
						if(item != null && item.getType().equals(material) && item.getDurability() == itemdata){
							world.dropItemNaturally(loc, item).setPickupDelay(delay);inv.setItem(i, new ItemStack(Material.AIR));}}}}}
			player.updateInventory();
			return true;
		}
		sender.sendMessage("パラメーターが違います! /drop <player> (itemid) (itemdata) (pickup_delay)");
		return false;
	}
	public float parse(String str, double def)
	{
		try {return Float.valueOf(str);}
		catch (Exception e){return (float)def;}
	}
}
