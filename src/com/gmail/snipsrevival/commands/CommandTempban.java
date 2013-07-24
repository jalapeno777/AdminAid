package com.gmail.snipsrevival.commands;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class CommandTempban implements CommandExecutor {

	AdminAid plugin;
	CommonUtilities common;
	
	public CommandTempban(AdminAid plugin) {
		this.plugin = plugin;
		plugin.getCommand("tempban").setExecutor(this);
		if(plugin.getConfig().getBoolean("DisableCommand.Tempban") == true) {
			PluginCommand tempban = plugin.getCommand("tempban");
			CommandUtilities.unregisterBukkitCommand(tempban);
		}
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {		
		
		this.common = new CommonUtilities(plugin);

		if(!sender.hasPermission("adminaid.tempban")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/tempban <playername> <time> <reason> " + ChatColor.RED + "to tempban player");
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
		
		if(userFile.getBoolean("BanExempt") == true) {
			sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is exempt from being banned");
			return true;
		}
		if(common.isPermaBanned(targetPlayer)) {
			sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is already permanently banned");
			return true;
		}
		double unbanTime = 0;
		Pattern pattern = Pattern.compile("(\\d+\\.?\\d*)([wdhms]{1})");
		Matcher matcher = pattern.matcher(args[1]);
		if(matcher.matches()) {
			int pos = matcher.start(2);
			String number = args[1].substring(0, pos);
			String letter = args[1].substring(pos);
			double i = Double.parseDouble(number);
			if(letter.equalsIgnoreCase("w")) {
				unbanTime = i*604800;
			}
			if(letter.equalsIgnoreCase("d")) {
				unbanTime = i*86400;
			}
			if(letter.equalsIgnoreCase("h")) {
				unbanTime = i*3600;
			}
			if(letter.equalsIgnoreCase("m")) {
				unbanTime = i*60;
			}
			if(letter.equalsIgnoreCase("s")) {
				unbanTime = i;
			}		
		}							
		if(unbanTime <= 0) {
			sender.sendMessage(ChatColor.RED + "That is an invalid time argument");
			sender.sendMessage(ChatColor.RED + "Time must be a number followed by a w, d, h, m, or s meaning weeks, days, hours, minutes, or seconds respectively");
			return true;
		}
		
		Date unbanDateUnformatted = new Date((long) (System.currentTimeMillis() + unbanTime*1000));
		String unbanDate = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a z").format(unbanDateUnformatted);
		
		StringBuilder strBuilder = new StringBuilder();			
		String prefix = new ConfigValues(plugin).getPrefix(sender);
		
		for(int i = 2; i < args.length; i++) {
			strBuilder.append(args[i] + " ");
		}
		String message = strBuilder.toString().trim();
		
		FileUtilities.createNewFile(file);
		userFile.set("TempBanned", true);
		userFile.set("TempBanReason", "tempbanned until " + unbanDate + " for this reason: " + message);
		userFile.set("TempBanEnd", (System.currentTimeMillis()/1000) + unbanTime);
		sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has been tempbanned until " + unbanDate + " for this reason: " + message);
		if(targetPlayer.isOnline()) {
			Bukkit.getServer().getPlayer(args[0]).kickPlayer("You are tempbanned until " + unbanDate + " for this reason: " + message);
		}
		
		if(plugin.getConfig().getBoolean("AutoRecordNotes.Tempbans") == true) {
			noteList.add(prefix + "has been tempbanned until " + unbanDate + " for this reason: " + message);
			common.addStringStaffList(prefix + targetPlayer.getName() + " has been tempbanned until " + unbanDate + " for this reason: " + message);
			userFile.set("Notes", noteList);
		}
		FileUtilities.saveYamlFile(userFile, file);
		return true;
	}
}
