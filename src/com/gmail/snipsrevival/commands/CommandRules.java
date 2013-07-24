package com.gmail.snipsrevival.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import com.gmail.snipsrevival.CommonUtilities;
import com.gmail.snipsrevival.AdminAid;
import com.gmail.snipsrevival.ConfigValues;
import com.gmail.snipsrevival.utilities.CommandUtilities;

public class CommandRules implements CommandExecutor {
		
	AdminAid plugin;
	CommonUtilities common;
	ConfigValues config;
	
	public CommandRules(AdminAid plugin) {
		this.plugin = plugin;
		plugin.getCommand("rules").setExecutor(this);
		if(plugin.getConfig().getBoolean("DisableCommand.Rules") == true) {
			PluginCommand rules = plugin.getCommand("rules");
			CommandUtilities.unregisterBukkitCommand(rules);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		common = new CommonUtilities(plugin);
		config = new ConfigValues(plugin);

		if(!sender.hasPermission("adminaid.rules")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length > 1) {
			sender.sendMessage(ChatColor.RED + "Too many arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/rules [page #] " + ChatColor.RED + "to show the rules");
			return true;
		}
		List<String> inputList = plugin.getConfig().getStringList("Rules");
		double configNumber = config.getRulesPerPage();
		List<String> outputList;
		int totalPages = common.getTotalPages(inputList, configNumber);
		int page;
		try{
			if(args.length == 0) {
				outputList = common.getListPage(inputList, "1", configNumber);
				page = 1;
			}
			else {
				outputList = common.getListPage(inputList, args[0], configNumber);
				page = Integer.parseInt(args[0]);
			}
			
			sender.sendMessage(ChatColor.GOLD + "Rule Page " + page + " of " + totalPages);
			for(String output : outputList) {
				sender.sendMessage(output);
			}
		}
		catch (IllegalArgumentException e) { //ie args[0] is not a natural Number (1, 2, 3 etc)
			sender.sendMessage(ChatColor.RED + "That is an invalid page number");
		}
		catch (IllegalStateException e) { //ie the original list is empty
			sender.sendMessage(ChatColor.RED + "No rules have been listed");
		}
		catch (IndexOutOfBoundsException e) { //ie args[0] is higher than the amount of pages
			sender.sendMessage(ChatColor.RED + "There are only " + totalPages + " pages of rules");
		}
		return true;
	}
}
