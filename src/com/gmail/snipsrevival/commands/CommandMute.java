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

import com.gmail.snipsrevival.CommonUtilities;
import com.gmail.snipsrevival.AdminAid;
import com.gmail.snipsrevival.ConfigValues;
import com.gmail.snipsrevival.utilities.CommandUtilities;
import com.gmail.snipsrevival.utilities.FileUtilities;

public class CommandMute implements CommandExecutor {
	
	AdminAid plugin;
	CommonUtilities common;
	
	public CommandMute(AdminAid plugin) {
		this.plugin = plugin;
		plugin.getCommand("mute").setExecutor(this);
		if(plugin.getConfig().getBoolean("DisableCommand.Mute") == true) {
			PluginCommand mute = plugin.getCommand("mute");
			CommandUtilities.unregisterBukkitCommand(mute);
		}
	}
		
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		
		this.common = new CommonUtilities(plugin);

		if(!sender.hasPermission("adminaid.mute")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/mute <playername> <reason> " + ChatColor.RED + "to mute player");
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
		
		if(userFile.getBoolean("MuteExempt") == true) {
			sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is exempt from being muted");
			return true;
		}

		if(common.isPermaMuted(targetPlayer)) {
			sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is already permanently muted");
			return true;
		}
		
		FileUtilities.createNewFile(file);
		userFile.set("PermaMuted", true);
		
		StringBuilder strBuilder = new StringBuilder();			
		String prefix = new ConfigValues(plugin).getPrefix(sender);
		
		for(int arg = 1; arg < args.length; arg = arg+1) {
			strBuilder.append(args[arg] + " ");
		}
		String message = strBuilder.toString().trim();
		
		userFile.set("PermaMuteReason", "muted for this reason: " + message);
		sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has been muted for this reason: " + message);
		
		if(plugin.getConfig().getBoolean("AutoRecordMutes") == true) {
			noteList.add(prefix + "has been muted for this reason: " + message);
			common.addStringStaffList(prefix + targetPlayer.getName() + " has been muted for this reason: " + message);
			userFile.set("Notes", noteList);
		}
		FileUtilities.saveYamlFile(userFile, file);
		return true;
	}
}
