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

public class CommandBan implements CommandExecutor {
	
	private AdminAid plugin;
	private CommonUtilities common;
	
	public CommandBan(AdminAid plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("ban").setExecutor(this);
		if(plugin.getConfig().getBoolean("DisableCommand.Ban") == true) {
			PluginCommand ban = plugin.getCommand("ban");
			CommandUtilities.unregisterBukkitCommand(ban);
		}
	}
					
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {	
		
		this.common = new CommonUtilities(plugin);
		
		if(!sender.hasPermission("adminaid.ban")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/ban <playername> <reason> " + ChatColor.RED + "to ban player");
			return true;
		}
		if(common.nameContainsInvalidCharacter(args[0])) {
			sender.sendMessage(ChatColor.RED + "That is an invalid playername");
			return true;
		}
		
		OfflinePlayer targetPlayer;
		if(Bukkit.getServer().getPlayer(args[0]) != null) targetPlayer = Bukkit.getServer().getPlayer(args[0]);
		else targetPlayer = Bukkit.getServer().getOfflinePlayer(args[0]);
										
		File file = new File(plugin.getDataFolder() + "/userdata/" + targetPlayer.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		List<String> noteList = userFile.getStringList("Notes");
		
		if(userFile.getBoolean("BanExempt") == true) {
			sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is exempt from being banned");
			return true;
		}
		if(common.isPermaBanned(targetPlayer)) {
			sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is already permanently banned");
			return true;
		}
		
		FileUtilities.createNewFile(file);
		targetPlayer.setBanned(true);
		
		StringBuilder strBuilder = new StringBuilder();			
		String prefix = new ConfigValues(plugin).getPrefix(sender);
		
		for(int arg = 1; arg < args.length; arg = arg+1) {
			strBuilder.append(args[arg] + " ");
		}
		String message = strBuilder.toString().trim();
		
		userFile.set("PermaBanned", true);
		userFile.set("PermaBanReason", "banned for this reason: " + message);
		sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has been banned for this reason: " + message);
		if(targetPlayer.isOnline()) {
			Bukkit.getServer().getPlayer(args[0]).kickPlayer("You are banned for this reason: " + message);
		}
		
		if(plugin.getConfig().getBoolean("AutoRecordNotes.Bans") == true) {
			noteList.add(prefix + "has been banned for this reason: " + message);
			common.addStringStaffList(prefix + targetPlayer.getName() + " has been banned for this reason: " + message);
			userFile.set("Notes", noteList);
		}
		FileUtilities.saveYamlFile(userFile, file);
		return true;
	}
}
