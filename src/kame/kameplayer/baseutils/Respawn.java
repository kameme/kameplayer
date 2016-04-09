package kame.kameplayer.baseutils;

import org.bukkit.Location;

public class Respawn {
	private Location loc;
	private boolean keep;
	public Respawn(Location loc, boolean keep)
	{
		this.loc = loc;
		this.keep = keep;
	}

	public Location getLocation(){
		return loc;
	}

	public boolean getKeep(){
		return keep;
	}

	public void setLocation(Location loc){
		this.loc = loc;
	}

	public void setKeep(boolean keep){
		this.keep = keep;
	}

}
