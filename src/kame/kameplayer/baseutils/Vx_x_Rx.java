package kame.kameplayer.baseutils;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Vx_x_Rx {
	public abstract void sendPacket(Player player, String packet, float x, float y, float z,float x2, float y2, float z2, float speed, int amount);

	public abstract void sendRespawn(Player player);

	public abstract String getNBTTag(ItemStack bufferitem);

	public abstract Entity getNBTEntity(CommandSender sender, Location loc, String[] args);
}
