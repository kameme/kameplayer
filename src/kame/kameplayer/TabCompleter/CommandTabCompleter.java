package kame.kameplayer.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kame.kameplayer.baseutils.Util;
//import net.minecraft.util.org.apache.commons.lang3.StringUtils;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.BlockIterator;

import com.google.common.collect.ImmutableList;

public class CommandTabCompleter implements TabCompleter {

	private String[] particles= new String[]{
			"angryVillager","blockcrack_0_0","blockdust_0_0","bubble","cloud","crit",
			"depthsuspend","dripLava","dripWater","enchantmenttable","explode","fireworksSpark","flame",
			"footstep","happyVillager","heart","hugeexplosion","iconcrack_0","instantSpell","largeexplode",
			"largesmoke","lava","magicCrit","mobSpell","mobSpellAmbient","note","portal","reddust","slime",
			"smoke","snowballpoof","snowshovel","spell","splash","suspended","townaura","witchMagic"};
	private List<String> materials, entitytypes;
	ArrayList<String> materialList = new ArrayList<String>(), entityList = new ArrayList<String>();
	{
		for (Material material : Material.values())materialList.add(material.name());
		Collections.sort(materialList);materials = ImmutableList.copyOf(materialList);
		for (EntityType entity: EntityType.values())entityList.add(StringUtils.capitalize(entity.name().toLowerCase()));
		Collections.sort(entityList);entitytypes = ImmutableList.copyOf(entityList);
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		int l = args.length-1;
		ArrayList<String> completion = new ArrayList<String>();
		Player player = (Player)sender;
		if(!player.isOp() && !player.hasPermission("kameplayer.tabcompleter"))return completion;
		if(cmd.getName().equalsIgnoreCase("fill"))
		{
			//fill x y z dx dy dz block option block
			//replace destroy keep replacement
			if(args.length == 1)completion.addAll(completer(completion, args[l], new String[]{player.getLocation().getBlockX()+""}));
			if(args.length == 2)completion.addAll(completer(completion, args[l], new String[]{player.getLocation().getBlockY()-1+""}));
			if(args.length == 3)completion.addAll(completer(completion, args[l], new String[]{player.getLocation().getBlockZ()+""}));
			if(args.length > 3 && args.length < 7)
			{
				Location loc = lockAt(player);
				if(args.length == 4)completion.addAll(completer(completion, args[l], new String[]{loc.getBlockX()+""}));
				if(args.length == 5)completion.addAll(completer(completion, args[l], new String[]{loc.getBlockY()+""}));
				if(args.length == 6)completion.addAll(completer(completion, args[l], new String[]{loc.getBlockZ()+""}));
			}
			if(args.length == 7 || args.length == 9){
				if (!args[l].contains("minecraft:") && "minecraft".startsWith(args[l])){completion.add("minecraft:");return completion;}
				for (String material : materials)if (material.startsWith(args[l]))completion.add(material);
				return Bukkit.getUnsafe().tabCompleteInternalMaterialName(args[l], completion);
			}
			if(args.length == 8)completion.addAll(completer(completion, args[l], new String[]{"destroy", "replace", "replacement", "keep"}));
		}
		if(cmd.getName().equalsIgnoreCase("attack"))
		{
			//attack @r x y z damage distance type
			if(args.length == 1)return null;
			if(args.length == 2)completion.addAll(completer(completion, args[1], new String[]{player.getLocation().getBlockX()+""}));
			if(args.length == 3)completion.addAll(completer(completion, args[2], new String[]{player.getLocation().getBlockY()+""}));
			if(args.length == 4)completion.addAll(completer(completion, args[3], new String[]{player.getLocation().getBlockZ()+""}));

			if(args.length == 5)completion.addAll(completer(completion, args[4], new String[]{0+""}));
			if(args.length == 6)completion.addAll(completer(completion, args[5], new String[]{0+""}));
			if(args.length == 7)for (String entity : entitytypes)if (entity.startsWith(args[6]))completion.add(entity);
		}
		if(cmd.getName().equalsIgnoreCase("armor"))
		{
			//armor <repair>
			if(args.length == 1)return null;
			if(args.length == 2)completion.add("repair");
		}

		if(cmd.getName().equalsIgnoreCase("itemrun"))
		{ //itemrun <player> item data amount {NBT} /command
			if(args.length == 1)return null;
			if(args.length == 2){
				if (!args[1].contains("minecraft:") && "minecraft".startsWith(args[1])){completion.add("minecraft:");return completion;}
				for (String material : materials)if (material.startsWith(args[1]))completion.add(material);
				return Bukkit.getUnsafe().tabCompleteInternalMaterialName(args[1], completion);
			}
			if(args.length == 3)completion.addAll(completer(completion, args[2], new String[]{"-1"}));
			if(args.length == 4)completion.addAll(completer(completion, args[3], new String[]{"1"}));
			if(args.length == 5)completion.addAll(completer(completion, args[4], new String[]{
					Util.getNBTTag(((Player)sender).getItemInHand())}));
		}

		if(cmd.getName().equalsIgnoreCase("entityrun"))
		{ //entityrun <player> item data amount {NBT} /command
			ArrayList<String> list = new ArrayList<String>();
			if(args.length == 1)for(String type : entityList)if(type.startsWith(args[0]))list.add(type);
			if(args.length == 2)list.addAll(completer(list, args[1], new String[]{player.getLocation().getBlockX()+""}));
			if(args.length == 3)list.addAll(completer(list, args[2], new String[]{player.getLocation().getBlockY()+""}));
			if(args.length == 4)list.addAll(completer(list, args[3], new String[]{player.getLocation().getBlockZ()+""}));
			return list;
		}

		if(cmd.getName().equalsIgnoreCase("broadcast"))
		{
			//bloadcast <> <> <>
			if(args.length == 1)completion.addAll(completer(completion, args[0], new String[]{"all","server","@a","@p"}));
			if(args.length > 1)
			{
				if(args[args.length-1].startsWith("score_"))
				{
					String str = args[args.length-1].replaceFirst("score_", "");
					for(Objective object : Bukkit.getScoreboardManager().getMainScoreboard().getObjectives())if(object.getName().startsWith(str))completion.add("score_" + object.getName());
					return completion;
				}
				completion.addAll(completer(completion, args[0], new String[]{"@p","[+]","score_"}));
			}
		}
		if(cmd.getName().equalsIgnoreCase("drop"))
		{
			//drop player minecraft:item id time
			if(args.length == 1)return null;
			if(args.length > 2)return completion;
			if (!args[1].contains("minecraft:") && "minecraft".startsWith(args[1])){completion.add("minecraft:");return completion;}
			for (String material : materials)if (material.startsWith(args[1]))completion.add(material);
			return Bukkit.getUnsafe().tabCompleteInternalMaterialName(args[1], completion);
		}
		if(cmd.getName().equalsIgnoreCase("entity"))
		{
			//entity remove
			if(args.length == 1)completion.add("remove");
			if(args.length == 2)return null;
			if(args.length == 3 && args[1].equals("loc"))completion.addAll(completer(completion, args[2], new String[]{player.getLocation().getBlockX()+""}));
			if(args.length == 4 && args[1].equals("loc"))completion.addAll(completer(completion, args[3], new String[]{player.getLocation().getBlockY()+""}));
			if(args.length == 5 && args[1].equals("loc"))completion.addAll(completer(completion, args[4], new String[]{player.getLocation().getBlockZ()+""}));
			if((args.length ==3 &&!args[1].equals("loc")) || (args.length == 6 && args[1].equals("loc")))completion.add("0");
			if((args.length ==4 &&!args[1].equals("loc")) || (args.length == 7 && args[1].equals("loc")))for (String entity : entitytypes)if (entity.startsWith(args[3]))completion.add(entity);
		}
		if(cmd.getName().equalsIgnoreCase("explode"))
		{
			//explode loc,player power  player, location
			if(args.length == 1){completion.add("player");completion.add("loc");}
			if(args.length == 2)completion.add("0");
			if(args.length == 3 && args[0].equals("player"))return null;
			if(args.length == 3 && args[0].equals("loc"))completion.addAll(completer(completion, args[2], new String[]{player.getLocation().getBlockX()+""}));
			if(args.length == 4 && args[0].equals("loc"))completion.addAll(completer(completion, args[3], new String[]{player.getLocation().getBlockY()+""}));
			if(args.length == 5 && args[0].equals("loc"))completion.addAll(completer(completion, args[4], new String[]{player.getLocation().getBlockZ()+""}));
		}
		if(cmd.getName().equalsIgnoreCase("firework"))
		{
			//firework 1~4
			if(args.length == 1)return null;
			if(args.length == 2)completion.add("1~4");
		}

		if(cmd.getName().equalsIgnoreCase("itemtp"))
		{
			if(args.length == 1)return null;
			if(args.length == 2)completion.addAll(completer(completion, args[1], new String[]{player.getLocation().getBlockX()+""}));
			if(args.length == 3)completion.addAll(completer(completion, args[2], new String[]{player.getLocation().getBlockY()+""}));
			if(args.length == 4)completion.addAll(completer(completion, args[3], new String[]{player.getLocation().getBlockZ()+""}));
			if(args.length == 5)
			{
				if (!args[4].contains("minecraft:") && "minecraft".startsWith(args[4])){completion.add("minecraft:");return completion;}
				for (String material : materials)if (material.startsWith(args[4]))completion.add(material);
				return Bukkit.getUnsafe().tabCompleteInternalMaterialName(args[4], completion);
			}
			if(args.length == 6)completion.addAll(completer(completion, args[5], new String[]{"0","data"}));
			if(args.length == 7)completion.addAll(completer(completion, args[5], new String[]{"amount"}));
			if(args.length == 8)completion.addAll(completer(completion, args[5], new String[]{"flag"}));

		}
		if(cmd.getName().equalsIgnoreCase("particle"))
		{
			if(args.length == 1)return null;
			if(args.length == 2)for(String name : particles)if(name.startsWith(args[1]))completion.add(name);
			if(args.length == 3){for(String name : new String[]{"0","circle","line"})if(name.startsWith(args[2]))completion.add(name);return completion;}
			if(args.length == 4)completion.add("1");
			if(args.length == 5)completion.add("0");
			if(args.length == 6)completion.add("0");
			if(args.length == 7)completion.add("0");
			if(args.length == 8)completion.add("0");

			if(args.length == 9 )completion.addAll(completer(completion, args[8], new String[]{player.getLocation().getBlockX()+""}));
			if(args.length == 10)completion.addAll(completer(completion, args[9], new String[]{player.getLocation().getBlockY()+""}));
			if(args.length == 11)completion.addAll(completer(completion, args[10], new String[]{player.getLocation().getBlockZ()+""}));

			if(args.length == 12 && (args[2].equals("circle") || args[2].equals("line")))completion.addAll(completer(completion, args[11], new String[]{player.getLocation().getBlockX()+""}));
			if(args.length == 13 && (args[2].equals("circle") || args[2].equals("line")))completion.addAll(completer(completion, args[12], new String[]{player.getLocation().getBlockY()+""}));
			if(args.length == 14 && (args[2].equals("circle") || args[2].equals("line")))completion.addAll(completer(completion, args[13], new String[]{player.getLocation().getBlockZ()+""}));
			if(args.length == 15 && args[2].equals("circle"))completion.addAll(completer(completion, args[14], new String[]{"from"}));
			if(args.length == 16 && args[2].equals("circle"))completion.addAll(completer(completion, args[15], new String[]{"to"}));
		}
		if(cmd.getName().equalsIgnoreCase("respawn"))
		{
			if(args.length == 1)return null;
			if(args.length == 2)
			{
				if("set".startsWith(args[1]))completion.add("set");
				if("remove".startsWith(args[1]))completion.add("remove");
				for(World name : Bukkit.getWorlds())if(name.getName().startsWith(args[1]))completion.add(name.getName());
			}
			if(args.length > 2 && (args[1].equals("set") || args[1].equals("remove")))
			{
				if(args.length == 3)for(World name : Bukkit.getWorlds())if(name.getName().startsWith(args[2]))completion.add(name.getName());
				if(args.length == 4)completion.addAll(completer(completion, args[3], new String[]{player.getLocation().getBlockX()+""}));
				if(args.length == 5)completion.addAll(completer(completion, args[4], new String[]{player.getLocation().getBlockY()+""}));
				if(args.length == 6)completion.addAll(completer(completion, args[5], new String[]{player.getLocation().getBlockZ()+""}));
				if(args.length == 7)completion.addAll(completer(completion, args[6], new String[]{player.getLocation().getYaw()+""}));
				if(args.length == 8)completion.addAll(completer(completion, args[7], new String[]{player.getLocation().getPitch()+""}));
				if(args.length == 9 && "keep".startsWith(args[8]))completion.add("keep");
			}
			else
			{
				if(args.length == 3)completion.addAll(completer(completion, args[2], new String[]{player.getLocation().getBlockX()+""}));
				if(args.length == 4)completion.addAll(completer(completion, args[3], new String[]{player.getLocation().getBlockY()+""}));
				if(args.length == 5)completion.addAll(completer(completion, args[4], new String[]{player.getLocation().getBlockZ()+""}));
				if(args.length == 6)completion.addAll(completer(completion, args[5], new String[]{player.getLocation().getYaw()+""}));
				if(args.length == 7)completion.addAll(completer(completion, args[6], new String[]{player.getLocation().getPitch()+""}));
			}
		}
		if(cmd.getName().equalsIgnoreCase("tpto"))
		{
			if(args.length == 1)return null;
			if(args.length == 2)completion.addAll(completer(completion, args[1], new String[]{player.getLocation().getBlockX()+""}));
			if(args.length == 3)completion.addAll(completer(completion, args[2], new String[]{player.getLocation().getBlockY()+""}));
			if(args.length == 4)completion.addAll(completer(completion, args[3], new String[]{player.getLocation().getBlockZ()+""}));
			if(args.length == 5)completion.addAll(completer(completion, args[4], new String[]{player.getLocation().getYaw()+""}));
			if(args.length == 6)completion.addAll(completer(completion, args[5], new String[]{player.getLocation().getPitch()+""}));
			if(args.length == 7)for(World name : Bukkit.getWorlds())if(name.getName().startsWith(args[6]))completion.add(name.getName());
		}
		if(cmd.getName().equalsIgnoreCase("Vec"))
		{
			if(args.length == 1)return null;
			if(args.length > 1 && args.length < 5)completion.add("1");
		}
		if(cmd.getName().equalsIgnoreCase("addlore"))
		{
			if(args.length == 1)return null;
			if(args.length == 2)
			{
				if (!args[1].contains("minecraft:") && "minecraft".startsWith(args[1])){completion.add("minecraft:");return completion;}
				for (String material : materials)if (material.startsWith(args[1]))completion.add(material);
				return Bukkit.getUnsafe().tabCompleteInternalMaterialName(args[1], completion);
			}
			if(args.length == 3)completion.add("-1");
		}
		return completion;
	}
	private List<String> completer(ArrayList<String> completion, String cmd , String[] args)
	{
		for(String text : args)if(text.startsWith(cmd))completion.add(text);
		return completion;
	}
	private Location lockAt(Player player)
	{
		BlockIterator it = new BlockIterator(player, Bukkit.getViewDistance()*16);
		while (it.hasNext()){
			Block block = it.next();
			if (block.getType()!=Material.AIR)return block.getLocation();
		}
		return player.getLocation();
	}
}
