package com.gmail.snipsrevival.commands;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.snipsrevival.AdminAid;
import com.gmail.snipsrevival.CommonUtilities;
import com.gmail.snipsrevival.ConfigValues;
import com.gmail.snipsrevival.utilities.OnTimeUtilities;

public class CommandPlayerinfo implements CommandExecutor {
	
	private AdminAid plugin;
	private CommonUtilities common;
	
	public CommandPlayerinfo(AdminAid plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("playerinfo").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		common = new CommonUtilities(plugin);
		
		if(!sender.hasPermission("adminaid.playerinfo")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command");
			return true;
		}
		if(args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/playerinfo <player> " + ChatColor.RED + "to view info about player");
			return true;
		}
		if(args.length > 1) {
			sender.sendMessage(ChatColor.RED + "Too many arguments!");
			sender.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/playerinfo <player> " + ChatColor.RED + "to view info about player");
			return true;
		}
		if(common.nameContainsInvalidCharacter(args[0])) {
			sender.sendMessage(ChatColor.RED + "That is an invalid playername");
			return true;
		}
		
		OfflinePlayer targetPlayer;
		if(Bukkit.getServer().getPlayer(args[0]) != null) targetPlayer = Bukkit.getServer().getPlayer(args[0]);
		else targetPlayer = Bukkit.getServer().getOfflinePlayer(args[0]);
		
		if(!targetPlayer.hasPlayedBefore()) {
			sender.sendMessage(ChatColor.RED + "That player has never played on this server before");
			return true;
		}
										
		File file = new File(plugin.getDataFolder() + "/userdata/" + targetPlayer.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a z");
		ConfigValues config = new ConfigValues(plugin);
		
		if(config.showOnlineStatus() == true) {
			if(Bukkit.getServer().getPlayer(args[0]) != null) {
				sender.sendMessage(ChatColor.YELLOW + "Online: " + ChatColor.WHITE + "yes");
			}
			else {
				sender.sendMessage(ChatColor.YELLOW + "Online: " + ChatColor.WHITE + "no");
			}
		}
		
		if(config.showLastLogin() == true) {
			if(Bukkit.getServer().getPlayer(args[0]) == null) {
				String date = sdf.format(new Date(targetPlayer.getLastPlayed()));
				sender.sendMessage(ChatColor.YELLOW + "Last Seen: " + ChatColor.WHITE + date);
			}	
		}
		
		if(config.showFirstLogin() == true) {
			String date = sdf.format(new Date(targetPlayer.getFirstPlayed()));
			sender.sendMessage(ChatColor.YELLOW + "First Seen: " + ChatColor.WHITE + date);
		}
		
		if(config.showIpAddress() == true) {
			sender.sendMessage(ChatColor.YELLOW + "IP Address: " 
					+ ChatColor.WHITE + userFile.getString("IPAddress", "unknown"));
		}
		
		if(config.showLocation() == true) {
			if(Bukkit.getServer().getPlayer(args[0]) != null) {
				Player player = Bukkit.getServer().getPlayer(args[0]);
				Location loc = player.getLocation();
				String xCoord = Integer.toString(loc.getBlockX());
				String yCoord = Integer.toString(loc.getBlockY());
				String zCoord = Integer.toString(loc.getBlockZ());
				String world = loc.getWorld().getName();
	
				sender.sendMessage(ChatColor.YELLOW + "X: " + ChatColor.WHITE + xCoord +
						ChatColor.YELLOW + " Y: " + ChatColor.WHITE + yCoord +
						ChatColor.YELLOW + " Z: " + ChatColor.WHITE + zCoord +
						ChatColor.YELLOW + " World: " + ChatColor.WHITE + world);
			}
			else if(userFile.get("Location") != null){
				String xCoord = Integer.toString(userFile.getInt("Location.X"));
				String yCoord = Integer.toString(userFile.getInt("Location.Y"));
				String zCoord = Integer.toString(userFile.getInt("Location.Z"));
				String world = userFile.getString("Location.World");
	
				sender.sendMessage(ChatColor.YELLOW + "X: " + ChatColor.WHITE + xCoord +
						ChatColor.YELLOW + " Y: " + ChatColor.WHITE + yCoord +
						ChatColor.YELLOW + " Z: " + ChatColor.WHITE + zCoord +
						ChatColor.YELLOW + " World: " + ChatColor.WHITE + world);
			}
		}
		
		if(config.showBannedStatus() == true) {
			if(common.isPermaBanned(targetPlayer)) {
				String defaultMessage = "permanently banned from this server";
				sender.sendMessage(ChatColor.YELLOW + "Banned: " 
						+ ChatColor.WHITE + "is " + userFile.getString("PermaBanReason", defaultMessage));
			}
			else if(common.isTempBanned(targetPlayer)) {
				String defaultMessage = "temporarily banned from this server";
				sender.sendMessage(ChatColor.YELLOW + "Banned: " 
						+ ChatColor.WHITE + "is " + userFile.getString("TempBanReason", defaultMessage));
			}
		}
		
		if(config.showMutedStatus() == true) {
			if(common.isPermaMuted(targetPlayer)) {
				String defaultMessage = "permanently muted";
				sender.sendMessage(ChatColor.YELLOW + "Muted: " 
						+ ChatColor.WHITE + "is " + userFile.getString("PermaMuteReason", defaultMessage));
			}
			else if(common.isTempMuted(targetPlayer)) {
				String defaultMessage = "temporarily muted";
				sender.sendMessage(ChatColor.YELLOW + "Muted: " 
						+ ChatColor.WHITE + "is " + userFile.getString("TempMuteReason", defaultMessage));
			}
		}
		
		if(config.showStaffMemberStatus() == true) {
			if(userFile.getBoolean("StaffMember") == true) {
				sender.sendMessage(ChatColor.YELLOW + "Staff Member: " + ChatColor.WHITE + "yes");
			}
			else {
				sender.sendMessage(ChatColor.YELLOW + "Staff Member: " + ChatColor.WHITE + "no");
			}
		}
		
		if(config.showChatSpyStatus() == true) {
			if(userFile.getBoolean("ChatSpy") == true) {
				sender.sendMessage(ChatColor.YELLOW + "ChatSpy Enabled: " + ChatColor.WHITE + "yes");
			}
			else {
				sender.sendMessage(ChatColor.YELLOW + "ChatSpy Enabled: " + ChatColor.WHITE + "no");
			}
		}
		
		if(config.showBanExemptStatus() == true) {
			if(userFile.getBoolean("BanExempt") == true) {
				sender.sendMessage(ChatColor.YELLOW + "Ban Exempt: " + ChatColor.WHITE + "yes");
			}
			else {
				sender.sendMessage(ChatColor.YELLOW + "Ban Exempt: " + ChatColor.WHITE + "no");
			}
		}
		
		if(config.showMuteExemptStatus() == true) {
			if(userFile.getBoolean("MuteExempt") == true) {
				sender.sendMessage(ChatColor.YELLOW + "Mute Exempt: " + ChatColor.WHITE + "yes");
			}
			else {
				sender.sendMessage(ChatColor.YELLOW + "Mute Exempt: " + ChatColor.WHITE + "no");
			}
		}
		
		if(config.showKickExemptStatus() == true) {
			if(userFile.getBoolean("KickExempt") == true) {
				sender.sendMessage(ChatColor.YELLOW + "Kick Exempt: " + ChatColor.WHITE + "yes");
			}
			else {
				sender.sendMessage(ChatColor.YELLOW + "Kick Exempt: " + ChatColor.WHITE + "no");
			}
		}
		
		if(AdminAid.onTime != null) {
			if(OnTimeUtilities.hasOnTimeRecord(targetPlayer.getName())) {
				if(config.showTotalPlayTime() == true) {
					String time = OnTimeUtilities.getTotalPlayTime(targetPlayer.getName());
					sender.sendMessage(ChatColor.YELLOW + "Total Play Time: " + ChatColor.WHITE + time);
				}
				if(config.showMonthlyPlayTime() == true) {
					String time = OnTimeUtilities.getMonthlyPlayTime(targetPlayer.getName());
					sender.sendMessage(ChatColor.YELLOW + "Monthly Play Time: " + ChatColor.WHITE + time);
				}
				if(config.showWeeklyPlayTime() == true) {
					String time = OnTimeUtilities.getWeeklyPlayTime(targetPlayer.getName());
					sender.sendMessage(ChatColor.YELLOW + "Weekly Play Time: " + ChatColor.WHITE + time);
				}
				if(config.showDailyPlayTime() == true) {
					String time = OnTimeUtilities.getDailyPlayTime(targetPlayer.getName());
					sender.sendMessage(ChatColor.YELLOW + "Daily Play Time: " + ChatColor.WHITE + time);
				}
			}
		}
		return true;
	}
}
