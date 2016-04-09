package kame.kameplayer.commands;

import java.util.Random;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class CommandFirework implements CommandExecutor {

	static private Random rand = new Random();
	static private FireworkEffect.Type[] types = { FireworkEffect.Type.BALL,
		FireworkEffect.Type.BALL_LARGE, FireworkEffect.Type.BURST,
		FireworkEffect.Type.CREEPER, FireworkEffect.Type.STAR, };
	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args)
	{
		if(!sender.isOp())return false;
		String str = "1~4";
		if(args.length > 0 && args[0].equals("@b") && sender instanceof BlockCommandSender)
		{	
			if(args.length > 1 && str.contains("~"))str = args[1];
			launch(((BlockCommandSender) sender).getBlock().getLocation().add(0.5, 0.5, 0.5), str);
			return true;
		}
		if(args.length > 0)
		{
			Player player = Bukkit.getPlayerExact(args[0]);
			if(player == null)
			{
				sender.sendMessage(ChatColor.RED + "そのプレイヤーはいません！ player is offline!");
				return false;
			}
			if(args.length > 1 && str.contains("~"))str = args[1];
			launch(((Player) sender).getLocation(), str);
			return true;
		}
		if(sender instanceof Player)
		{
			if(args.length > 1 && str.contains("~"))str = args[1];
			launch(((Player) sender).getLocation(), str);
			return true;
		}
		return false;
	}
	private void launch(Location loc, String str)
	{
		String[] s = str.split("~");
		int i = 1, j = 0;
		if(s.length > 1)
		{
			i =   parse(s[0], 1);
			j = i-parse(s[1], 1);
			if(i < 1)i=1;
			if(j < 0)j=0;
		}
		Firework firework = loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		Builder effect = FireworkEffect.builder();
		effect.with(types[rand.nextInt(types.length)]);
		effect.withColor(getRandomCrolors(1 + rand.nextInt(5)));
		effect.withFade(getRandomCrolors(1 + rand.nextInt(3)));
		effect.flicker(rand.nextBoolean());
		effect.trail(rand.nextBoolean());
		if(j==0)meta.setPower(i);
		else meta.setPower(i + rand.nextInt(j));
		meta.addEffect(effect.build());
		firework.setFireworkMeta(meta);
	}
	public int parse(String str, int def)
	{
		for(int i=1; i<5;i++)if(str.equals(i+""))return i;
		return 1;
	}
	private Color[] getRandomCrolors(int length) {
		Color[] colors = new Color[length];
		for (int n = 0; n != length; n++)colors[n] = Color.fromBGR(rand.nextInt(1 << 24));
		return colors;
	}
}
