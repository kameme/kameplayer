package kame.kameplayer.baseutils;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.ChatComponentText;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityTypes;
import net.minecraft.server.v1_8_R1.EnumClientCommand;
import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.MojangsonParser;
import net.minecraft.server.v1_8_R1.NBTBase;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class V1_8_R1 extends Vx_x_Rx {
	public void sendPacket(Player player, String packet, float x, float y, float z,
			float x2, float y2, float z2, float speed, int amount) {
		EnumParticle particle = get(packet);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles(particle, true, x, y, z, x2, y2, z2, speed, amount));
		player.sendMessage(EnumParticle.a());
	}
	private EnumParticle get(String name)
	{
		int i = 0;
		for(String particle :EnumParticle.a())
		{
			if(particle.equals(name))return EnumParticle.a(i);
			i++;
		}
		return EnumParticle.BARRIER;

	}
	public
	String getNBTTag(ItemStack bufferitem) {
		NBTTagCompound tag = CraftItemStack.asNMSCopy(bufferitem).getTag();
		if(tag == null)return "";
		return tag.toString();
	}
	public void sendRespawn(Player player){
	((CraftPlayer)player).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
	}
	
	public org.bukkit.entity.Entity getNBTEntity(CommandSender sender, Location loc, String[] args) {

		World world = ((CraftWorld)loc.getWorld()).getHandle();
		NBTTagCompound NBTTag = new NBTTagCompound();
		boolean tag = true;
		try{
			if (args.length >= 5 && args[4].startsWith("{")) {
				int i;
				for(i = 4; args.length > i && !args[i].endsWith("}"); i++);

				ChatComponentText text = new ChatComponentText("");
				for (int j = 4; j < i+1; j++) {
					text.a(" ");
					text.addSibling(new ChatComponentText(args[j]));
				}
				NBTBase base = MojangsonParser.parse(((IChatBaseComponent)text).c());
				if ((base instanceof NBTTagCompound)) {
					NBTTag = (NBTTagCompound) base;
					tag = false;
				} else {
					sender.sendMessage(ChatColor.RED + "BadTag");
					return null;
				}
			}
			NBTTag.setString("id", args[0]);

			Entity entity = EntityTypes.a(NBTTag, world );
			if (entity != null) {
				(entity).setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), entity.yaw, entity.pitch);
				if (tag && entity instanceof EntityInsentient){((EntityInsentient) entity).prepare(world.E(new BlockPosition(entity)), null);}
				world.addEntity(entity);

				while ((entity != null) && (NBTTag.hasKeyOfType("Riding", 10))) {
					Entity tagentity = EntityTypes.a(NBTTag.getCompound("Riding"), world);
					if (tagentity != null) {
						tagentity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), tagentity.yaw, tagentity.pitch);
						world.addEntity(tagentity);
						entity.mount(tagentity);
					}
					entity = tagentity;
					NBTTag = NBTTag.getCompound("Riding");
				}
			}
			return entity.getBukkitEntity();

		}catch(Exception ex){
			System.err.println("コマンドの実行に失敗しましたデータタグを確認してください。");
			sender.sendMessage(ChatColor.RED + "コマンドの実行に失敗しましたデータタグを確認してください。");

		}
		return null;
	}
}