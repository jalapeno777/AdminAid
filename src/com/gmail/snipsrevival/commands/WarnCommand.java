package com.gmail.snipsrevival.commands;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.snipsrevival.AdminAid;
import com.gmail.snipsrevival.CommonUtilities;
import com.gmail.snipsrevival.ConfigValues;
import com.gmail.snipsrevival.utilities.CommandUtilities;
import com.gmail.snipsrevival.utilities.FileUtilities;

public class WarnCommand implements CommandExecutor {
	
	private AdminAid plugin;
	private CommonUtilities common;
	
	public WarnCommand(AdminAid instance) {
		plugin = instance;
		plugin.getCommand("warn").setExecutor(this);
		if(plugin.getConfig().getBoolean("DisableCommand.Warn") == true) {
			PluginCommand warn = plugin.getCommand("warn");
			CommandUtilities.unregisterBukkitCommand(warn);
		}
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		
		common = new CommonUtilities(plugin);

		if(!sender.hasPermission("adminaid.warn")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/warn <playername> <reason> " + ChatColor.RED + "to warn player");
			return true;
		}
		if(common.nameContainsInvalidCharacter(args[0])) {
			sender.sendMessage(ChatColor.RED + "That is an invalid playername");
			return true;
		}
		
		final OfflinePlayer targetPlayer;
		if(Bukkit.getServer().getPlayer(args[0]) != null) targetPlayer = Bukkit.getServer().getPlayer(args[0]);
		else targetPlayer = Bukkit.getServer().getOfflinePlayer(args[0]);
		
		File file = new File(plugin.getDataFolder() + "/userdata/" + targetPlayer.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		List<String> noteList = userFile.getStringList("Notes");
		List<String> mailListNew = userFile.getStringList("NewMail");
		
		StringBuilder strBuilder = new StringBuilder();			
		String prefix = new ConfigValues(plugin).getPrefix(sender);
		
		for(int i = 1; i < args.length; i++) {
			strBuilder.append(args[i] + " ");
		}
		String message = strBuilder.toString().trim();
		sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has been warned for this reason: " + message);
		FileUtilities.createNewFile(file);
		
		if(plugin.getConfig().getBoolean("AutoRecordNotes.Warns") == true) {
			noteList.add(prefix + "has been warned for this reason: " + message);
			common.addStringStaffList(prefix + targetPlayer.getName() + " has been warned for this reason: " + message);
			userFile.set("Notes", noteList);
		}
		if(targetPlayer.isOnline()) {
			Bukkit.getServer().getPlayer(args[0]).sendMessage(ChatColor.RED + "You have been warned for this reason: " + message);
		}
		mailListNew.add(prefix + ChatColor.RED + "You have been warned for this reason: " + message);
		userFile.set("NewMail", mailListNew);
		FileUtilities.saveYamlFile(userFile, file);
		return true;
	}
}
