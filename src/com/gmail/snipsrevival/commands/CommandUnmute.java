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

public class CommandUnmute implements CommandExecutor {
	
	AdminAid plugin;
	CommonUtilities common;
	
	public CommandUnmute(AdminAid plugin) {
		this.plugin = plugin;
		plugin.getCommand("unmute").setExecutor(this);
		if(plugin.getConfig().getBoolean("DisableCommand.Unmute") == true) {
			PluginCommand unmute = plugin.getCommand("unmute");
			CommandUtilities.unregisterBukkitCommand(unmute);
		}
	}
		
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		
		this.common = new CommonUtilities(plugin);

		if(!sender.hasPermission("adminaid.unmute")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/unmute <playername> <reason> " + ChatColor.RED + "to unmute player");
			return true;
		}
		if(common.nameContainsInvalidCharacter(args[0])) {
			sender.sendMessage(ChatColor.RED + "That is an invalid playername");
			return true;
		}
		
		final OfflinePlayer targetPlayer;
		if(Bukkit.getServer().getPlayer(args[0]) != null) targetPlayer = Bukkit.getServer().getPlayer(args[0]);
		else targetPlayer = Bukkit.getServer().getOfflinePlayer(args[0]);
		
		if(!common.isPermaMuted(targetPlayer) && !common.isTempMuted(targetPlayer)) {
			sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is not muted");
			return true;
		}
								
		File file = new File(plugin.getDataFolder() + "/userdata/" + targetPlayer.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		List<String> noteList = userFile.getStringList("Notes");
		
		userFile.set("PermaMuted", null);
		userFile.set("PermaMuteReason", null);
		userFile.set("TempMuted", null);
		userFile.set("TempMuteReason", null);
		userFile.set("TempMuteEnd", null);
		
		StringBuilder strBuilder = new StringBuilder();			
		String prefix = new ConfigValues(plugin).getPrefix(sender);
		
		for(int i = 1; i < args.length; i++) {
			strBuilder.append(args[i] + " ");
		}
		String message = strBuilder.toString().trim();
		
		sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has been unmuted for this reason: " + message);
		
		if(plugin.getConfig().getBoolean("AutoRecordUnmutes") == true) {
			noteList.add(prefix + "has been unmuted for this reason: " + message);
			common.addStringStaffList(prefix + targetPlayer.getName() + " has been unmuted for this reason: " + message);
			userFile.set("Notes", noteList);
		}
		FileUtilities.saveYamlFile(userFile, file);
		return true;
	}
}