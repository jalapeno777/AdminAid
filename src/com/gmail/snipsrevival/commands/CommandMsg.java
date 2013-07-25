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
import com.gmail.snipsrevival.CommonUtilities;

public class CommandMsg implements CommandExecutor {
	
	private AdminAid plugin;
	private CommonUtilities common;
	
	public CommandMsg(AdminAid instance) {
		plugin = instance;
		plugin.getCommand("msg").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
		common = new CommonUtilities(plugin);
		
		if(!sender.hasPermission("adminaid.msg")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/msg <playername> <message> " + ChatColor.RED + "to send private message");
			return true;
		}
		if(common.nameContainsInvalidCharacter(args[0])) {
			sender.sendMessage(ChatColor.RED + "That is an invalid playername");
			return true;
		}
		
		final Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
		
		if(targetPlayer == null && !args[0].equalsIgnoreCase("CONSOLE")) {
			sender.sendMessage(ChatColor.RED + args[0] + " is not online. Try sending a mail message instead");
			return true;
		}
		
		StringBuilder strBuilder = new StringBuilder();			
		
		for(int i = 1; i < args.length; i++) {
			strBuilder.append(args[i] + " ");
		}
		String message = strBuilder.toString().trim();
		
		if(args[0].equalsIgnoreCase("CONSOLE")) {
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
