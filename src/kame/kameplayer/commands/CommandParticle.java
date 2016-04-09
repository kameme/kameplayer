package kame.kameplayer.commands;

import kame.kameplayer.Main;
import kame.kameplayer.baseutils.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandParticle implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args) {
		if(!sender.isOp())return false;
		if(args.length >= 2) {
			Location loc = null;
			if(sender instanceof BlockCommandSender && args[0].equals("@b"))loc = ((BlockCommandSender) sender).getBlock().getLocation();
			Player player = Bukkit.getPlayer(args[0]);
			if(player != null)loc = player.getLocation();
			if(loc == null){sender.sendMessage("そのプレイヤーは存在しません! " + args[0] + " is Offline!");return false;}
			float x, y, z, speed;
			int amount, i = args.length;
			if(args[args.length - 1].equals("-a"))i--;
			if(i > 2)loc.add(0, parse(args[2],0d), 0);
			if(i < 4){amount = 10;}else{amount = (int)parse(args[3],10d);}
			if(i < 5){speed = 1;}else{speed = parse(args[4],1d);}
			if(i < 6){x = 0;}else{x = parse(args[5],0d);}
			if(i < 7){y = 0;}else{y = parse(args[6],0d);}
			if(i < 8){z = 0;}else{z = parse(args[7],0d);}
			if(i > 8) loc.setX(parse(args[8],(float)loc.getX()));
			if(i > 9) loc.setY(parse(args[9],(float)loc.getY()));
			if(i > 10)loc.setZ(parse(args[10],(float)loc.getZ()));
			if(args[1].contains("blockcrack_") || args[1].contains("blockdust_")) {
				boolean check = false;
				for(int checkid=255;checkid>=0;checkid--) if(args[1].contains("blockcrack_" + checkid) || args[1].contains("blockdust_" + checkid))
				for(int checkdat=0;checkdat<256;checkdat++)if(args[1].equals("blockcrack_" + checkid + "_" + checkdat) || args[1].equals("blockdust_" + checkid + "_" + checkdat))check = true;
				if(!check){sender.sendMessage("[kameplayer]" + args[1] + ChatColor.RED + " は未割当のIDです 0-255");return false;}
			}
			if(args[1].contains("iconcrack_")) {
				boolean check = false;
				for(int checkid=511;checkid>=256;checkid--)if(args[1].equals("iconcrack_" + checkid))check = true;
				if(!check){sender.sendMessage("[kameplayer]" + args[1] + ChatColor.RED + " は未割当のIDです 256-511");return false;}
			}
			if(i > 2 && args[2].equals("line")) {
				Location toloc = player.getLocation();
				if(amount > 10000)amount = 10000;
				if(i > 11)toloc.setX(parse(args[11], (float)loc.getX()));
				if(i > 12)toloc.setY(parse(args[12], (float)loc.getY()));
				if(i > 13)toloc.setZ(parse(args[13], (float)loc.getZ()));
				double x1=(toloc.getX() - loc.getX()) / amount;
				double y1=(toloc.getY() - loc.getY()) / amount;
				double z1=(toloc.getZ() - loc.getZ()) / amount;
				particleline(player, args, loc, x1, y1, z1, x, y, z, speed, amount);
				return true;
			}
			if(i >  2 && args[2].equals("circle")) {
				if(amount > 10000)amount = 10000;
				float from = 0, to = 360, sin = 1, cos = 0, tan = 1;
				if(i > 11)sin   = parse(args[11], 1d);
				if(i > 12)cos   = parse(args[12], 0d);
				if(i > 13)tan   = parse(args[13], 1d);
				if(i > 14)from  = parse(args[14], 0d);
				if(i > 15)to    = parse(args[15], 360d);
				particlecircle(player, args, loc, from, to, -sin, cos, tan, x, y, z, speed, amount);
				return true;
			}
			sendPacket(player, args, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), x, y, z, speed, amount);
			return true;
		}
		sender.sendMessage("パラメーターが違います!");
		sender.sendMessage("/particle <player> 種類 高さ  数 早さ x y z (x) (y) (z)");
		sender.sendMessage("/particle <player> 種類 circle 数 早さ x y z (x) (y) (z) [x] [y] [z] from to");
		sender.sendMessage("/particle <player> 種類 line   数 早さ x y z (x) (y) (z) [x] [y] [z]");
		return false;
	}

	private void particleline(final Player player, final String[] args, final Location loc, final double x1, final double y1, final double z1, final float x, final float y,final float z, final float speed, final int amount)
	{
    	new BukkitRunnable()
    	{
    		int d1=0;
			@Override
    		public void run() {
				int d;
				for(d = d1; d< d1+250; d++) {
					if(d > amount)break;
					sendPacket(player, args, (float)(loc.getX() + x1 * d), (float)(loc.getY() + y1 * d), (float)(loc.getZ() + z1 * d), x, y, z, speed, 1);
				}
				if(d > amount)this.cancel();
				d1 = d;
    		}
    	}.runTaskTimer(Main.plugin, 0, 0);
	}
	private void particlecircle(final Player player, final String[] args, final Location loc, final float from, final float to, final float sin, final float cos, final float tan, final float x, final float y,final float z, final float speed, final int amount)
	{
		new BukkitRunnable()
    	{
			float amount1 = from;
			@Override
    		public void run() {
				float o;
				int tr = 0;
				for(o = amount1; o < to; o += (to - from)/amount)
				{
					if(tr > 250)break;
					tr++;
					float x1, y1, z1;
					if(sin > 0){x1 = (float) (Math.sin(Math.toRadians(o)) * sin);}else{x1 = (float) (Math.cos(Math.toRadians(o)) * -sin);}
					if(cos > 0){y1 = (float) (Math.sin(Math.toRadians(o)) * cos);}else{y1 = (float) (Math.cos(Math.toRadians(o)) * -cos);}
					if(tan > 0){z1 = (float) (Math.sin(Math.toRadians(o)) * tan);}else{z1 = (float) (Math.cos(Math.toRadians(o)) * -tan);}
					sendPacket(player, args, (float)loc.getX() + x1, (float)loc.getY() + y1, (float)loc.getZ() + z1, x, y, z, speed, 1);
				}
				if(o > amount)this.cancel();
				amount1 = o;
			}
	    }.runTaskTimer(Main.plugin, 0, 0);
	}
	private void sendPacket(final Player player, final String[] args, final float x, final float y,final float z, final float x1, final float y1,final float z1, final float speed, final int amount)
	{
		new BukkitRunnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				if(player == null || args[args.length - 1].equals("-a"))
					for( Player packet : Bukkit.getOnlinePlayers())
						Util.sendPacket(packet, args[1], x, y, z, x1, y1, z1, speed, amount);
				else Util.sendPacket(player, args[1], x, y, z, x1, y1, z1, speed, amount);

			}
		}.runTask(Main.plugin);

	}

	public float parse(String str, double def)
	{
		str = str.replace("~","");
		return (float)Util.parseRaw(str, def);
	}

	public float parse(String str, float vec)
	{
		return Util.parse(str, vec);
	}

}
