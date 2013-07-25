package com.gmail.snipsrevival;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConfigValues {
	
	private AdminAid plugin;
	
	private boolean showOnlineStatus = false;
	private boolean showLastLogin = false;
	private boolean showFirstLogin = false;
	private boolean showIpAddress = false;
	private boolean showLocation = false;
	private boolean showBannedStatus = false;
	private boolean showMutedStatus = false;
	private boolean showStaffMemberStatus = false;
	private boolean showChatSpyStatus = false;
	private boolean showBanExemptStatus = false;
	private boolean showMuteExemptStatus = false;
	private boolean showKickExemptStatus = false;
	private boolean showTotalPlayTime = false;
	private boolean showMonthlyPlayTime = false;
	private boolean showWeeklyPlayTime = false;
	private boolean showDailyPlayTime = false;
	
	private double rulesPerPage = 0;
	private double infoPerPage = 0;
	private double mailPerPage = 0;
	private double notesPerPage = 0;
	
	private String prefix = "";
	
	private List<String> loginMessages = new ArrayList<String>();
		
	public ConfigValues(AdminAid instance) {
		plugin = instance;
		
		showOnlineStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowOnlineStatus");
		showLastLogin = plugin.getConfig().getBoolean("PlayerInfo.ShowLastLogin");
		showFirstLogin = plugin.getConfig().getBoolean("PlayerInfo.ShowFirstLogin");
		showIpAddress = plugin.getConfig().getBoolean("PlayerInfo.ShowIPAddress");
		showLocation = plugin.getConfig().getBoolean("PlayerInfo.ShowLocation");
		showBannedStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowBannedStatus");
		showMutedStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowMutedStatus");
		showStaffMemberStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowStaffMemberStatus");
		showChatSpyStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowChatSpyStatus");
		showBanExemptStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowBanExemptStatus");
		showMuteExemptStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowMuteExemptStatus");
		showKickExemptStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowKickExemptStatus");
		showTotalPlayTime = plugin.getConfig().getBoolean("PlayerInfo.ShowTotalPlayTime");
		showMonthlyPlayTime = plugin.getConfig().getBoolean("PlayerInfo.ShowMonthlyPlayTime");
		showWeeklyPlayTime = plugin.getConfig().getBoolean("PlayerInfo.ShowWeeklyPlayTime");
		showDailyPlayTime = plugin.getConfig().getBoolean("PlayerInfo.ShowDailyPlayTime");
		
		rulesPerPage = plugin.getConfig().getDouble("MessagesPerPage.Rules");
		infoPerPage = plugin.getConfig().getDouble("MessagesPerPage.Info");
		mailPerPage = plugin.getConfig().getDouble("MessagesPerPage.Mail");
		notesPerPage = plugin.getConfig().getDouble("MessagesPerPage.Notes");

		
		prefix = plugin.getConfig().getString("Prefix");
		
		loginMessages = plugin.getConfig().getStringList("LoginMessages");
	}
	
	public boolean showOnlineStatus() {
		return showOnlineStatus;
	}
	
	public boolean showLastLogin() {
		return showLastLogin;
	}
	
	public boolean showFirstLogin() {
		return showFirstLogin;
	}
	
	public boolean showIpAddress() {
		return showIpAddress;
	}
	
	public boolean showLocation() {
		return showLocation;
	}
	
	public boolean showBannedStatus() {
		return showBannedStatus;
	}
	
	public boolean showMutedStatus() {
		return showMutedStatus;
	}
	
	public boolean showStaffMemberStatus() {
		return showStaffMemberStatus;
	}
	
	public boolean showChatSpyStatus() {
		return showChatSpyStatus;
	}
	
	public boolean showBanExemptStatus() {
		return showBanExemptStatus;
	}
	
	public boolean showMuteExemptStatus() {
		return showMuteExemptStatus;
	}
	
	public boolean showKickExemptStatus() {
		return showKickExemptStatus;
	}
	
	public boolean showTotalPlayTime() {
		return showTotalPlayTime;
	}
	
	public boolean showMonthlyPlayTime() {
		return showMonthlyPlayTime;
	}
	
	public boolean showWeeklyPlayTime() {
		return showWeeklyPlayTime;
	}
	
	public boolean showDailyPlayTime() {
		return showDailyPlayTime;
	}
	
	public double getRulesPerPage() {
		return rulesPerPage;
	}
	
	public double getInfoPerPage() {
		return infoPerPage;
	}
	
	public double getMailPerPage() {
		return mailPerPage;
	}
	
	public double getNotesPerPage() {
		return notesPerPage;
	}
	
	public String getPrefix(CommandSender sender) {
		Date date = new Date();
		prefix = ChatColor.translateAlternateColorCodes('&', prefix);
		prefix = prefix.replace("<MM>", new SimpleDateFormat("MM").format(date));
		prefix = prefix.replace("<MMM>", new SimpleDateFormat("MMM").format(date));
		prefix = prefix.replace("<MMMM>", new SimpleDateFormat("MMMM").format(date));
		prefix = prefix.replace("<dd>", new SimpleDateFormat("dd").format(date));
		prefix = prefix.replace("<yyyy>", new SimpleDateFormat("yyyy").format(date));
		prefix = prefix.replace("<yy>", new SimpleDateFormat("yy").format(date));
		prefix = prefix.replace("<HH>", new SimpleDateFormat("HH").format(date));
		prefix = prefix.replace("<hh>", new SimpleDateFormat("hh").format(date));
		prefix = prefix.replace("<mm>", new SimpleDateFormat("mm").format(date));
		prefix = prefix.replace("<ss>", new SimpleDateFormat("ss").format(date));
		prefix = prefix.replace("<a>", new SimpleDateFormat("a").format(date));
		prefix = prefix.replace("<Z>", new SimpleDateFormat("z").format(date));
		prefix = prefix.replace("<player>", sender.getName());
		return prefix;
	}
	
	public List<String> getLoginMessages(Player player) {
		Date date = new Date();
		String dateString = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a z").format(date);
		
		int maxPlayerCount = Bukkit.getServer().getMaxPlayers();
		int onlinePlayerCount = 0; 
		
		StringBuilder sb1 = new StringBuilder();
		for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
			sb1.append(onlinePlayer.getName() + ", ");
			onlinePlayerCount++;
		}
		String playerList = "{" + sb1.toString().trim() + "}";
		Pattern pattern1 = Pattern.compile(",}");
		Matcher matcher1 = pattern1.matcher(playerList);
		playerList = matcher1.replaceAll("}");
		
		StringBuilder sb2 = new StringBuilder();
		for(World world : Bukkit.getServer().getWorlds()) {
			sb2.append(world.getName() + ", ");
		}
		String worldList = "{" + sb2.toString().trim() + "}";
		Pattern pattern2 = Pattern.compile(",}");
		Matcher matcher2 = pattern2.matcher(worldList);
		worldList = matcher2.replaceAll("}");
		
		for(int i = 0; i < loginMessages.size(); i++) {
			String line = loginMessages.get(i);
			line = ChatColor.translateAlternateColorCodes('&', line);
			line = line.replace("<player>", player.getName());
			line = line.replace("<playerlist>", playerList);
			line = line.replace("<world>", player.getWorld().getName());
			line = line.replace("<worldlist>", worldList);
			line = line.replace("<date>", dateString);
			line = line.replace("<playercount>", onlinePlayerCount + "/" + maxPlayerCount);
			loginMessages.set(i, line);
		}
		return loginMessages;
	}
}
