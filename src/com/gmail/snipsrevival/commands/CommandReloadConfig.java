package com.gmail.snipsrevival.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.snipsrevival.AdminAid;

public class CommandReloadConfig implements CommandExecutor {
	
	private AdminAid plugin;
	
	public CommandReloadConfig(AdminAid plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("adminaid").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("adminaid")) {
			if(sender.hasPermission("adminaid.reload")) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("reload")) {
						plugin.reloadConfig();
						sender.sendMessage(ChatColor.GREEN + "AdminAid config reloaded!");
					}
					else {
						sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/adminaid reload " + ChatColor.RED + "to reload the config");
					}
				}
				if (args.length > 1) {
					sender.sendMessage(ChatColor.RED + "Too many arguments!");
					sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/adminaid reload " + ChatColor.RED + "to reload the config");
				}
				if (args.length == 0) {
					sender.sendMessage(ChatColor.RED + "Too few arguments!");
					sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/adminaid reload " + ChatColor.RED + "to reload the config");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			}
		}
		return true;
	}
}
