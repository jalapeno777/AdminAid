package com.gmail.snipsrevival.listeners;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.gmail.snipsrevival.AdminAid;
import com.gmail.snipsrevival.CommonUtilities;
import com.gmail.snipsrevival.utilities.FileUtilities;

public class ChatListener implements Listener {
	
	private AdminAid plugin;
	private CommonUtilities common;
	
	public ChatListener(AdminAid instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		
		common = new CommonUtilities(plugin);
		
		Player player = event.getPlayer();
		File file = new File(plugin.getDataFolder() + "/userdata/" + player.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		
		if(AdminAid.staffChat.contains(player.getName())) {
			String prefix = ChatColor.GOLD + "[StaffChat] " + player.getName() + ": " + ChatColor.WHITE;
			String message = event.getMessage();
			Bukkit.getServer().getConsoleSender().sendMessage(prefix + message);
			for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
				if(onlinePlayer.hasPermission("adminaid.staffmember")) {
					onlinePlayer.sendMessage(prefix + message);
				}
			}
			event.setCancelled(true);
		}
		
		if(common.isPermaMuted(player)) {
			event.setCancelled(true);
			String defaultMessage = "permanently muted";
			player.sendMessage(ChatColor.RED + "You are " + userFile.getString("PermaMuteReason", defaultMessage));
		}
		else if(common.isTempMuted(player)) {
			String defaultMessage = "temporarily muted";
			if(System.currentTimeMillis()/1000 >= userFile.getDouble("TempMuteEnd")) {
				userFile.set("TempMuted", null);
				userFile.set("TempMuteReason", null);
				userFile.set("TempMuteEnd", null);
				FileUtilities.saveYamlFile(userFile, file);
			}
			else {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You are " + userFile.getString("TempMuteReason", defaultMessage));
			}
		}
	}
}
