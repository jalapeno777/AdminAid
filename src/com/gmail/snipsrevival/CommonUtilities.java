package com.gmail.snipsrevival;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.snipsrevival.utilities.FileUtilities;

public class CommonUtilities {
	
	private AdminAid plugin;
		
	public CommonUtilities(AdminAid instance) {
		plugin = instance;
	}
	
	public boolean isPermaBanned(OfflinePlayer player) {
		File file = new File(plugin.getDataFolder() + "/userdata/" + player.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		if(player.isBanned()) return true;
		else if(userFile.getBoolean("PermaBanned") == true) return true;
		else return false;
	}
	
	public boolean isTempBanned(OfflinePlayer player) {
		File file = new File(plugin.getDataFolder() + "/userdata/" + player.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		if(userFile.getBoolean("TempBanned") == true) return true;
		else return false;
	}
	
	public boolean isPermaMuted(OfflinePlayer player) {
		File file = new File(plugin.getDataFolder() + "/userdata/" + player.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		if(userFile.getBoolean("PermaMuted") == true) return true;
		else return false;
	}
	
	public boolean isTempMuted(OfflinePlayer player) {
		File file = new File(plugin.getDataFolder() + "/userdata/" + player.getName().toLowerCase() + ".yml");
		YamlConfiguration userFile = YamlConfiguration.loadConfiguration(file);
		if(userFile.getBoolean("TempMuted") == true) return true;
		else return false;
	}
	
	
	public Location getSafeLocation(Location loc) {
		
		double y = loc.getY();
	
		while(y > -1) {
			
			if(loc.getBlock().getType() == Material.STATIONARY_LAVA || 
					loc.getBlock().getType() == Material.LAVA ||
					loc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.STATIONARY_LAVA ||
					loc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.LAVA) {
				return null;
			}
			
			if(loc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				y--;
				loc.setY(y);
				continue;
			}
			
			if(loc.getBlock().getType() == Material.STATIONARY_WATER || 
					loc.getBlock().getType() == Material.WATER) {
				y++;
				loc.setY(y);
				continue;
			}
			return loc;
		}
		return null;
	}
	
	/**
	 * Separates a List of Strings into pages and returns the specified page as a new List of Strings
	 * 
	 * @param list - List of Strings to make into pages
	 * @param pageString - the page number (as a String) of the new list that you would like returned
	 * @param stringsPerPage - the maximum number of strings contained in the returned list
	 * @return the desired page from the List of Strings
	 * @throws IllegalArgumentException if pageString cannot be converted into an <tt>int</tt>
	 * @throws IllegalStateException if list is empty
	 * @throws IndexOutOfBoundsException if pageString results in a page higher than the total number of pages
	 */
	
	public List<String> getListPage(List<String> list, String pageString, double stringsPerPage)
			throws IllegalArgumentException, IllegalStateException, IndexOutOfBoundsException {
		
		int page;
		if(isInt(pageString) && Integer.parseInt(pageString) > 0) {
			page = Integer.parseInt(pageString);
		}
		else {
			page = 0;
		}	
		
		double listSize = list.size();
		int totalPages = (int) Math.ceil(listSize/stringsPerPage);
		
		if(page == 0) {
			throw new IllegalArgumentException();
		}
		else if(listSize == 0) {
			throw new IllegalStateException();
		}
		else if(page > totalPages) {
			throw new IndexOutOfBoundsException();
		}
		
		List<String> listPage = new ArrayList<String>();
		
		for(int i = (int) ((page*stringsPerPage)-stringsPerPage); i < (page*stringsPerPage); i++) {
			if(i < listSize) {
				listPage.add(list.get(i));
			}
		}
		return listPage;
	}
	
	/**
	 * Will return the number of pages that a list could be made into
	 * 
	 * @param list - List of Strings that could be made into pages
	 * @param stringsPerPage - the maximum number of strings contained in the returned list
	 * @return the total number of pages if the list was separated into pages
	 */
	
	public int getTotalPages(List<String> list, double stringsPerPage) {
		double listSize = list.size();
		int totalPages = (int) Math.ceil(listSize/stringsPerPage);
		return totalPages;
	}
	
	/**
	 * Checks to see if the specified name contains an invalid character
	 * 
	 * Characters are only considered valid if they are a-z, A-Z, 0-9, or _
	 * 
	 * @param name - the name to check
	 * @return <tt>true</tt> if name contains an invalid character
	 */
	
	public boolean nameContainsInvalidCharacter(String name) {
		Pattern pattern = Pattern.compile("\\W+");
		Matcher matcher = pattern.matcher(name);
		if(matcher.find()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Adds the specified string to every staff member's mailbox
	 * if feature is enabled in the config. Otherwise, nothing will happen.
	 * @param message - the message to be sent to all staff members' mailboxes
	 */
		
	public void addStringStaffList(String message) {
		if(plugin.getConfig().getBoolean("NotifyStaffNewNote") == true) {
			File dir = new File(plugin.getDataFolder() + "/userdata/");
			File[] children = dir.listFiles();
			
			if(children != null) {
				for(int i = 0; i < children.length; i++) {
					
					File childFile = new File(plugin.getDataFolder() + "/userdata/" + children[i].getName());
					YamlConfiguration userFile = YamlConfiguration.loadConfiguration(childFile);
					ArrayList<String> noteListNew = (ArrayList<String>) userFile.getStringList("mail.new");
					
					if(userFile.getBoolean("StaffMember") == true) {
						noteListNew.add(message);
						userFile.set("NewMail", noteListNew);
						FileUtilities.saveYamlFile(userFile, childFile);	
					}
				}
			}
		}
	}
	
	/**
	 * Checks if list contains a string, ignoring case considerations
	 * 
	 * @param string - the string to look for
	 * @param list - the list of strings to look in
	 * 
	 * @return <tt>true</tt> if the string was found in the list
	 */
	
	public boolean containsIgnoreCase(String string, List<String> list) {
		for(String s : list) {
			if(s.equalsIgnoreCase(string)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the index of a specified <tt>String</tt>, ignoring case considerations,
	 * in a list of strings
	 * 
	 * @param string - the <tt>String</tt> to look for
	 * @param list - the list of strings to look in
	 * 
	 * @return the index of the string in the list or -1 if the list does not contain the string
	 */
	
	public int getIndexOfString(String string, List<String> list) {
		for(int s = 0; s < list.size(); s++) {
			if(string.equalsIgnoreCase(list.get(s))) {
				return s;
			}
		}
		return -1;
	}
	
	/**
	 * Checks if a string is an <tt>double</tt>
	 * 
	 * @param string - string to check
	 * 
	 * @return <tt>true</tt> if string is an <tt>double</tt>
	 */
	
	public boolean isDouble(String string) {
		try {
			Double.parseDouble(string);
		}
		catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if a string is an <tt>int</tt>
	 * 
	 * @param string - string to check
	 * 
	 * @return <tt>true</tt> if string is an <tt>int</tt>
	 */
	
	public boolean isInt(String string) {
		try {
			Integer.parseInt(string);
		}
		catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
