package kame.kameplayer.baseutils;

import kame.kameplayer.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Fill extends Thread{
	private Location loc;
	private Location toloc;
	private Material material;
	private Material formaterial;
	private int mode;
	private static World world;

	public Fill(Location loc, Location toloc, Material material, int mode, Material mate) {
		this.loc = loc;
		this.toloc = toloc;
		this.material = material;
		this.formaterial = mate;
		this.mode = mode;
		Fill.world = loc.getWorld();

	}
	private class setblock extends BukkitRunnable{
		int x,y,z;
		private setblock(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		@Override
		public void run(){
			block = world.getBlockAt(x, y, z);
			if(mode == 1 && formaterial != null && !block.getType().equals(formaterial))return;
			if(mode == 2)block.breakNaturally();
			if(mode == 3 && !block.getType().equals(Material.AIR))return;
			block.setType(material);
		}
	}
	Block block;
	@Override
	public void run()
	{
		int dx[] = maxmin(loc.getX(),toloc.getX());
		int dy[] = maxmin(loc.getY(),toloc.getY());
		int dz[] = maxmin(loc.getZ(),toloc.getZ());
		for(int x=dx[0];x<=dx[1];x++)
			for(int y=dy[0];y<=dy[1];y++)
				for(int z=dz[0];z<=dz[1];z++)
					new setblock(x, y, z).runTask(Main.plugin);
	}
	private int[] maxmin(Double a, Double b)
	{
		int[] i = {a.intValue(),b.intValue()};
		if(i[0] > i[1]){i[0]=b.intValue();i[1] = a.intValue();}
		return i;
	}
}
