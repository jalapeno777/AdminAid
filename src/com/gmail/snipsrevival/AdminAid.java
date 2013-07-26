package com.gmail.snipsrevival;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
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
	 * or nearest air block if target location is in a wall
	 * - player will be teleported the surface of water if underwater
	 * - teleport will be aborted if overtop of lava
	 * - chatspy can now be toggled for other players
	 * - new adminaid.chatspy.others permission
	 * - added /staffchat (uses adminaid.staffmember permission) (ticket ID #1)
	 * with alias /sc
	 * - added message in replacement of an empty string if there is no
	 * ontime data for a specific timeframe
	 * - fixed "you do not have permission to use that command" message
	 * when players have at least 1 but not all tp permissions (ticket ID #4)
	 * - version checker should no longer say "checking for newer versions..."
	 * when an op logs in
	 * - minor changes to a couple of output messages
	 */
	
			
	@Override
	public void onEnable() {
		
		lastSender = new HashMap<String, String>();
		staffChat = new ArrayList<String>();
		onTime = Bukkit.getPluginManager().getPlugin("OnTime");
					
		Updater updater = new Updater(this);
		
		updater.performVersionCheck();
		updater.updateConfig();
					
		File userDataDir = new File(getDataFolder() + "/userdata/");
		FileUtilities.createNewDir(userDataDir);
		
		for(File f : userDataDir.listFiles()) {
			YamlConfiguration userFile = YamlConfiguration.loadConfiguration(f);
			userFile.set("mail", null);
			FileUtilities.saveYamlFile(userFile, f);
		}
		
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		new CommonUtilities(this);
		
		new AdminaidCommand(this);
		new BanCommand(this);
		new ChatspyCommand(this);
		new InfoCommand(this);
		new KickCommand(this);
		new MailCommand(this);
		new MsgCommand(this);
		new MuteCommand(this);
		new NoteCommand(this);
		new PlayerinfoCommand(this);
		new ReplyCommand(this);
		new RulesCommand(this);
		new StaffchatCommand(this);
		new TeleportCommand(this);
		new TempbanCommand(this);
		new TempmuteCommand(this);
		new UnbanCommand(this);
		new UnmuteCommand(this);
		new WarnCommand(this);
		
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
