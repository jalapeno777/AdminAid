package com.gmail.snipsrevival.commands;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.snipsrevival.CommonUtilities;
import com.gmail.snipsrevival.AdminAid;
import com.gmail.snipsrevival.ConfigValues;
import com.gmail.snipsrevival.utilities.CommandUtilities;
import com.gmail.snipsrevival.utilities.FileUtilities;

public class KickCommand implements CommandExecutor {
	
	private AdminAid plugin;
	private CommonUtilities common;
	
	public KickCommand(AdminAid instance) {
		plugin = instance;
		plugin.getCommand("kick").setExecutor(this);
		if(plugin.getConfig().getBoolean("DisableCommand.Kick") == true) {
			PluginCommand kick = plugin.getCommand("kick");
			CommandUtilities.unregisterBukkitCommand(kick);
		}
	}
			
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		
		common = new CommonUtilities(plugin);
		
		if(!sender.hasPermission("adminaid.kick")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/kick <playername> <reason> " + ChatColor.RED + "to kick player");
			return true;
		}
					
		final Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
		
		if(targetPlayer == null) {
			sender.sendMessage(ChatColor.RED + args[0] + " is not online");
			return true;
		}
		
		File file = new File(plugin.getDataFolder() + "/userdata/" + targetPlayer.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		List<String> noteList = userFile.getStringList("Notes");
		
		if(userFile.getBoolean("KickExempt") == true) {
			sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is exempt from being kicked");
			return true;
		}
		
		FileUtilities.createNewFile(file);
		
		StringBuilder strBuilder = new StringBuilder();			
		String prefix = new ConfigValues(plugin).getPrefix(sender);
		
		for(int i = 1; i < args.length; i++) {
			strBuilder.append(args[i] + " ");
		}
		String message = strBuilder.toString().trim();
		
		sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has been kicked for this reason: " + message);
		targetPlayer.kickPlayer("You were kicked for this reason: " + message);

		if(plugin.getConfig().getBoolean("AutoRecordNotes.Kicks") == true) {
			noteList.add(prefix + "has been kicked for this reason: " + message);
			common.addStringStaffList(prefix + targetPlayer.getName() + " has been kicked for this reason: " + message);
			userFile.set("Notes", noteList);
		}
		FileUtilities.saveYamlFile(userFile, file);
		return true;
	}
}