package com.gmail.snipsrevival.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import com.gmail.snipsrevival.AdminAid;
import com.gmail.snipsrevival.CommonUtilities;
import com.gmail.snipsrevival.utilities.CommandUtilities;

public class CommandTeleport implements CommandExecutor {
	
	private AdminAid plugin;
	private CommonUtilities common;
	
	public CommandTeleport(AdminAid plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("teleport").setExecutor(this);
		if(plugin.getConfig().getBoolean("DisableCommand.Teleport") == true) {
			PluginCommand teleport = plugin.getCommand("teleport");
			CommandUtilities.unregisterBukkitCommand(teleport);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		common = new CommonUtilities(plugin);
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can teleport");
			return true;
		}
		if(!sender.hasPermission("adminaid.teleport") ||
				!sender.hasPermission("adminaid.teleport.others") ||
				!sender.hasPermission("adminaid.teleport.coordinates")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			if(sender.hasPermission("adminaid.teleport")) {
				sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/tp  <player> " + ChatColor.RED + "to teleport yourself to player");
			}
			if(sender.hasPermission("adminaid.teleport.others")) {
				sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/tp  <player1> <player2> " + ChatColor.RED + "to teleport player1 to player2");
			}
			if(sender.hasPermission("adminaid.teleport.coordinates")) {
				sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/tp  <x> <y> <z> [world]" + ChatColor.RED + "to teleport yourself to coordinates");
			}
			return true;		
			}
		if(args.length > 4) {
			sender.sendMessage(ChatColor.RED + "Too many arguments!");
			if(sender.hasPermission("adminaid.teleport")) {
				sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/tp  <player> " + ChatColor.RED + "to teleport yourself to player");
			}
			if(sender.hasPermission("adminaid.teleport.others")) {
				sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/tp  <player1> <player2> " + ChatColor.RED + "to teleport player1 to player2");
			}
			if(sender.hasPermission("adminaid.teleport.coordinates")) {
				sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/tp  <x> <y> <z> [world]" + ChatColor.RED + "to teleport yourself to coordinates");
			}
			return true;		
		}
		Player player = (Player) sender;
		if(args.length == 1) {
			if(!sender.hasPermission("adminaid.teleport")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
				return true;
			}
			Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
			if(common.nameContainsInvalidCharacter(args[0])) {
				sender.sendMessage(ChatColor.RED + "That is an invalid playername");
				return true;
			}
			if(targetPlayer == null) {
				sender.sendMessage(ChatColor.RED + args[0] + " is not online");
				return true;
			}
			
			Location loc = targetPlayer.getLocation();
			if(common.getSafeLocation(loc) == null) {
				sender.sendMessage(ChatColor.RED + "No safe location was found. Teleport aborted");
				return true;
			}
			player.teleport(common.getSafeLocation(loc));
			sender.sendMessage(ChatColor.GREEN + "You were teleported to " + targetPlayer.getName());
			return true;
		}
		if(args.length == 2) {
			if(!sender.hasPermission("adminaid.teleport.others")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
				return true;
			}
			Player targetPlayer1 = Bukkit.getServer().getPlayer(args[0]);
			Player targetPlayer2 = Bukkit.getServer().getPlayer(args[1]);
			if(common.nameContainsInvalidCharacter(args[0]) || 
					common.nameContainsInvalidCharacter(args[1])) {
				sender.sendMessage(ChatColor.RED + "1 or more playernames are invalid");
				return true;
			}
			
			if(targetPlayer1 == null){
				sender.sendMessage(ChatColor.RED + args[0] + " is not online");
				return true;
			}
			if(targetPlayer2 == null){
				sender.sendMessage(ChatColor.RED + args[1] + " is not online");
				return true;
			}
			
			Location loc = targetPlayer2.getLocation();
			if(common.getSafeLocation(loc) == null) {
				sender.sendMessage(ChatColor.RED + "No safe location was found. Teleport aborted");
				return true;
			}
			targetPlayer1.teleport(common.getSafeLocation(loc));
			
			sender.sendMessage(ChatColor.GREEN + targetPlayer1.getName() + "was teleported to " + targetPlayer2.getName());
			targetPlayer1.sendMessage(ChatColor.GREEN + "You were teleported to " + targetPlayer2.getName());
			return true;
		}
		if(args.length == 3) {
			if(!sender.hasPermission("adminaid.teleport.coordinates")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
				return true;
			}
			if(!common.isDouble(args[0]) || !common.isDouble(args[1]) || !common.isDouble(args[2])) {
				sender.sendMessage(ChatColor.RED + "1 or more coordinates are invalid");
				return true;
			}
			double x = Integer.parseInt(args[0]);
			double y = Integer.parseInt(args[1]);
			double z = Integer.parseInt(args[2]);
			World world = player.getWorld();
			Location loc = new Location(world, x, y, z);
			if(common.getSafeLocation(loc) == null) {
				sender.sendMessage(ChatColor.RED + "No safe location was found. Teleport aborted");
				return true;
			}
			player.teleport(common.getSafeLocation(loc));
			sender.sendMessage(ChatColor.GREEN + "You were teleported to a set of coordinates in world " + world.getName());
			return true;
		}
		if(args.length == 4) {
			if(!sender.hasPermission("adminaid.teleport.coordinates")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
				return true;
			}
			if(!common.isDouble(args[0]) || !common.isDouble(args[1]) || !common.isDouble(args[2])) {
				sender.sendMessage(ChatColor.RED + "1 or more coordinates are invalid");
				return true;
			}
			World world = Bukkit.getServer().getWorld(args[3]);
			if(world == null) {
				sender.sendMessage(ChatColor.RED + "That is an invalid world");
				return true;
			}
			double x = Integer.parseInt(args[0]);
			double y = Integer.parseInt(args[1]);
			double z = Integer.parseInt(args[2]);
			Location loc = new Location(world, x, y, z);
			if(common.getSafeLocation(loc) == null) {
				sender.sendMessage(ChatColor.RED + "No safe location was found. Teleport aborted");
				return true;
			}
			player.teleport(common.getSafeLocation(loc));
			sender.sendMessage(ChatColor.GREEN + "You were teleported to a set of coordinates in world " + world.getName());
			return true;
		}
		return true;
	}
}
