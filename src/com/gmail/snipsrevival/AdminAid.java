package com.gmail.snipsrevival;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.snipsrevival.commands.*;
import com.gmail.snipsrevival.listeners.*;
import com.gmail.snipsrevival.utilities.FileUtilities;

public class AdminAid extends JavaPlugin {
		
	public static Plugin onTime;
	public static Map<String, String> lastSender;
	public static List<String> staffChat;
	
	//TODO: improve safe teleport location logic
	
	/*
	 * UPDATE LOG:
	 * - teleport is now a disableable command
	 * - player will now be teleported to nearest non-air block below target location
	 * - chatspy can now be toggled for other players
	 * - new adminaid.chatspy.others permission
	 * - added staffchat (uses adminaid.staffmember permission) (ticket ID #1)
	 * - added message in replacement of an empty string if there is no
	 * ontime data for a specific timeframe
	 */
	
			
	@Override
	public void onEnable() {
		
		lastSender = new HashMap<String, String>();
		staffChat = new ArrayList<String>();
		onTime = Bukkit.getPluginManager().getPlugin("OnTime");
			
		Updater updater = new Updater(this);
		
		updater.performVersionCheck();
		updater.updateConfig();
					
		File userDataDir = new File(this.getDataFolder() + "/userdata/");
		FileUtilities.createNewDir(userDataDir);
		
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		new CommonUtilities(this);
		
		new CommandBan(this);
		new CommandChatspy(this);
		new CommandInfo(this);
		new CommandKick(this);
		new CommandMail(this);
		new CommandMsg(this);
		new CommandMute(this);
		new CommandNote(this);
		new CommandPlayerinfo(this);
		new CommandReloadConfig(this);
		new CommandReply(this);
		new CommandRules(this);
		new CommandStaffchat(this);
		new CommandTeleport(this);
		new CommandTempban(this);
		new CommandTempmute(this);
		new CommandUnban(this);
		new CommandUnmute(this);
		new CommandWarn(this);
		
		new ChatListener(this);
		new PlayerListener(this);
	}
	
	@Override
	public void onDisable() {
		lastSender = null;
		staffChat = null;
		onTime = null;
	}
}
