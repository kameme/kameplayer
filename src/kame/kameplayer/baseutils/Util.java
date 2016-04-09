package kame.kameplayer.baseutils;

import java.util.Arrays;

import kame.kameplayer.Main;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Joiner;

public class Util {
	public static void killed(Entity e)
	{
		for(int i = 0;Timer.entities.size() > i;i++){
			Entities entity = Timer.entities.get(i);
			if(!entity.isActive()){
				CommandSender sender = entity.getSender();
				String cmd = entity.getCommand().replaceFirst("/", "");
				if(entity.getEntity().equals(e) || entity.getEntity().getLastDamageCause() != null)Bukkit.dispatchCommand(sender, cmd);
				Timer.entities.remove(entity);
				i--;
			}
			if(entity.getEntity().equals(e))
			{
				CommandSender sender = entity.getSender();
				String cmd = entity.getCommand().replaceFirst("/", "");
				Bukkit.dispatchCommand(sender, cmd);
				Timer.entities.remove(entity);
				return;
			}
		}
	}


	private static Vx_x_Rx convert = Main.obj;
	@SuppressWarnings("deprecation")
	public static ItemStack setNBTTag(Player player, ItemStack bufferitem, String[] args) {
		try {
			int i;
			for(i = 4; args.length > i && !args[i].endsWith("}"); i++);
			bufferitem = Bukkit.getUnsafe().modifyItemStack(
					bufferitem,Joiner.on(' ').join(Arrays.asList(args).subList(4, i+1)));
		} catch (Throwable t) {}
		return bufferitem;
	}

	public static String getNBTTag(ItemStack bufferitem) {
		if(bufferitem.getType().equals(Material.AIR))return "";
		return convert.getNBTTag(bufferitem);
	}

	public static void sendPacket(Player player, String packet, float x, float y, float z, float xt, float yt, float zt, float speed, int amount) {
		convert.sendPacket(player, packet, x, y, z, xt, yt, zt, speed, amount);
	}

	public static void sendRespawn(Player player) {
		convert.sendRespawn(player);
	}
	public static Entity getNBTEntity(CommandSender sender, Location loc, String[] args) {
		args[0] = StringUtils.capitalize(args[0]);
		return convert.getNBTEntity(sender, loc, args);
	}

	public static double parseRaw(String str, double vec) {
		try{
			if(str.contains("~")){
				str = str.replace("~","");
				return isNum(str) ? vec + Double.parseDouble(str) : vec;
			}
			return isNum(str) ? Double.parseDouble(str) : vec;
		}catch(Exception e){
			return vec;
		}

	}

	public static float parseRaw(String str, float vec) {
		try{
			if(str.contains("~")){
				str = str.replace("~","");
				return isNum(str) ? vec + Float.parseFloat(str) : vec;
			}
			return isNum(str) ? Float.parseFloat(str) : vec;
		}catch(Exception e){
			return vec;
		}
	}

	public static double parse(String str, double vec) {
		try{
			if(str.contains("~")){
				str = str.replace("~","");
				if(isNum(str))return vec + Double.parseDouble(str);return vec;
			}
			if(isNum(str)){if(str.contains("."))return Double.parseDouble(str); return Integer.parseInt(str) + 0.5f;}return vec;
		}catch(Exception e){
			return vec;
		}
	}

	public static float parse(String str, float vec) {
		try{
			if(str.contains("~")){
				str = str.replace("~","");
				if(isNum(str))return vec + Float.parseFloat(str);return vec;
			}
			if(isNum(str)){if(str.contains("."))return Float.parseFloat(str); return Integer.parseInt(str) + 0.5f;}return vec;
		}catch(Exception e){
			return vec;
		}
	}

	public static int parse(String str, int vec) {
		try{
			if(str.contains("~")){
				str = str.replace("~","");
				return isNum(str) ? vec + Integer.parseInt(str) : vec;
			}
			return isNum(str) ? Integer.parseInt(str) : vec;
		}catch(Exception e){
			return vec;
		}
	}
	private static boolean isNum(String str)
	{
		return NumberUtils.isNumber(str);
	}
}
