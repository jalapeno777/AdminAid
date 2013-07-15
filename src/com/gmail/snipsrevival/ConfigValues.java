package com.gmail.snipsrevival;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ConfigValues {
	
	AdminAid plugin;
	
	private boolean showOnlineStatus = false;
	private boolean showLastLogin = false;
	private boolean showFirstLogin = false;
	private boolean showIpAddress = false;
	private boolean showLocation = false;
	private boolean showBannedStatus = false;
	private boolean showMutedStatus = false;
	private boolean showStaffMemberStatus = false;
	private boolean showBanExemptStatus = false;
	private boolean showMuteExemptStatus = false;
	private boolean showKickExemptStatus = false;
	private boolean showTotalPlayTime = false;
	private boolean showMonthlyPlayTime = false;
	private boolean showWeeklyPlayTime = false;
	private boolean showDailyPlayTime = false;
	
	private String prefix = "";
	
	public ConfigValues(AdminAid plugin) {
		this.plugin = plugin;
		
		showOnlineStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowOnlineStatus");
		showLastLogin = plugin.getConfig().getBoolean("PlayerInfo.ShowLastLogin");
		showFirstLogin = plugin.getConfig().getBoolean("PlayerInfo.ShowFirstLogin");
		showIpAddress = plugin.getConfig().getBoolean("PlayerInfo.ShowIPAddress");
		showLocation = plugin.getConfig().getBoolean("PlayerInfo.ShowLocation");
		showBannedStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowBannedStatus");
		showMutedStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowMutedStatus");
		showStaffMemberStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowStaffMemberStatus");
		showBanExemptStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowBanExemptStatus");
		showMuteExemptStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowMuteExemptStatus");
		showKickExemptStatus = plugin.getConfig().getBoolean("PlayerInfo.ShowKickExemptStatus");
		showTotalPlayTime = plugin.getConfig().getBoolean("PlayerInfo.ShowTotalPlayTime");
		showMonthlyPlayTime = plugin.getConfig().getBoolean("PlayerInfo.ShowMonthlyPlayTime");
		showWeeklyPlayTime = plugin.getConfig().getBoolean("PlayerInfo.ShowWeeklyPlayTime");
		showDailyPlayTime = plugin.getConfig().getBoolean("PlayerInfo.ShowDailyPlayTime");
		
		prefix = plugin.getConfig().getString("Prefix");
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
		prefix = prefix.replace("<playername>", sender.getName());
		return prefix;
	}
}
