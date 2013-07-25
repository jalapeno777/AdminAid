package com.gmail.snipsrevival.commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.snipsrevival.AdminAid;

public class CommandReply implements CommandExecutor {
	
	private AdminAid plugin;
	
	public CommandReply(AdminAid instance) {
		plugin = instance;
		plugin.getCommand("reply").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
				
		if(!sender.hasPermission("adminaid.msg")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/reply <message> " + ChatColor.RED + "to reply to private message");
			return true;
		}
				
		if(!AdminAid.lastSender.containsKey(sender.getName())) {
			sender.sendMessage(ChatColor.RED + "There isn't a player you can reply to");
			return true;
		}
		
		StringBuilder strBuilder = new StringBuilder();			
		
		for(int i = 0; i < args.length; i++) {
			strBuilder.append(args[i] + " ");
		}
		String message = strBuilder.toString().trim();
		
		String name = AdminAid.lastSender.get(sender.getName());
		if(name.equalsIgnoreCase("CONSOLE")) {
			String prefix = ChatColor.GOLD + "[" + sender.getName() + " to CONSOLE] " + ChatColor.WHITE;
			plugin.getServer().getConsoleSender().sendMessage(prefix + message);
			AdminAid.lastSender.put("CONSOLE", sender.getName());
			for(Player spy : Bukkit.getServer().getOnlinePlayers()) {
				File file = new File(plugin.getDataFolder() + "/userdata/" + spy.getName().toLowerCase() + ".yml");
				YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
				if(userFile.getBoolean("ChatSpy") == true) {
					if(!spy.getName().equalsIgnoreCase(sender.getName())) {
						spy.sendMessage(prefix + message);
					}
				}
			}
		}
		else {
			Player targetPlayer = Bukkit.getServer().getPlayer(name);
			String prefix = ChatColor.GOLD + "[" + sender.getName() + " to you] " + ChatColor.WHITE;
			targetPlayer.sendMessage(prefix + message);
			AdminAid.lastSender.put(targetPlayer.getName(), sender.getName());
			for(Player spy : Bukkit.getServer().getOnlinePlayers()) {
				File file = new File(plugin.getDataFolder() + "/userdata/" + spy.getName().toLowerCase() + ".yml");
				YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
				if(userFile.getBoolean("ChatSpy") == true) {
					prefix = ChatColor.GOLD + "[" + sender.getName() + " to " + targetPlayer.getName() + "] " + ChatColor.WHITE;
					if(!spy.getName().equalsIgnoreCase(sender.getName()) &&
							!spy.getName().equalsIgnoreCase(targetPlayer.getName())) {
						spy.sendMessage(prefix + message);
					}
				}
			}
		}
		sender.sendMessage(ChatColor.GREEN + "Private message sent successfully");
		return true;
	}
}
