package kame.kameplayer.baseutils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class Entities {
	private Entity entity;
	private CommandSender sender;
	private String cmd;
	public Entities(Entity entity, CommandSender sender, String cmd){
		this.entity = entity;
		this.sender = sender;
		this.cmd = cmd;
	}
	public Entity getEntity(){
		return this.entity;
	}
	public CommandSender getSender(){
		return this.sender;
	}
	public String getCommand(){
		return this.cmd;
	}
	public boolean isActive(){
		return entity.isValid();

	}
}