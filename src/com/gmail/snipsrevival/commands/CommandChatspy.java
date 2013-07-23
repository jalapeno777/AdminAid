package com.gmail.snipsrevival.commands;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.snipsrevival.AdminAid;
import com.gmail.snipsrevival.utilities.FileUtilities;

public class CommandChatspy implements CommandExecutor {
	
	AdminAid plugin;
	
	public CommandChatspy(AdminAid plugin) {
		this.plugin = plugin;
		plugin.getCommand("chatspy").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "The console can already see all private messages");
			return true;
		}
		if(!sender.hasPermission("adminaid.chatspy")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length > 1) {
			sender.sendMessage(ChatColor.RED + "Too many arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/chatspy [on|off] " + ChatColor.RED + "to enable/disable chat spy for yourself");
			return true;
		}
		
		File file = new File(plugin.getDataFolder() + "/userdata/" + sender.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		
		if(args.length == 0) {
			if(userFile.getBoolean("ChatSpy") == false) {
				userFile.set("ChatSpy", true);
				sender.sendMessage(ChatColor.GREEN + "ChatSpy enabled. You will now see everyone's private messages");
				FileUtilities.saveYamlFile(userFile, file);
				return true;
			}
			else {
				userFile.set("ChatSpy", false);
				sender.sendMessage(ChatColor.GREEN + "ChatSpy disabled. You will no longer see everyone's private messages");
				FileUtilities.saveYamlFile(userFile, file);
				return true;
			}
		}
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("on")) {
				userFile.set("ChatSpy", true);
				sender.sendMessage(ChatColor.GREEN + "ChatSpy enabled. You will now see everyone's private messages");
				FileUtilities.saveYamlFile(userFile, file);
				return true;
			}
			else if(args[0].equalsIgnoreCase("off")) {
				userFile.set("ChatSpy", false);
				sender.sendMessage(ChatColor.GREEN + "ChatSpy disabled. You will no longer see everyone's private messages");
				FileUtilities.saveYamlFile(userFile, file);
				return true;
			}
			else {
				sender.sendMessage(ChatColor.RED + "Invalid argument. Must be either on or off");
				return true;
			}
		}
		return true;
	}
}
