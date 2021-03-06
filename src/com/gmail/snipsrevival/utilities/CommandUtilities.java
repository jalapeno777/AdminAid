package com.gmail.snipsrevival.utilities;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;

public class CommandUtilities {
	
	private static Object getPrivateField(Object object, String field) throws SecurityException,
    NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = object.getClass();
		Field objectField = clazz.getDeclaredField(field);
		objectField.setAccessible(true);
		Object result = objectField.get(object);
		objectField.setAccessible(false);
		return result;
	}
 
	public static void unregisterBukkitCommand(PluginCommand cmd) {
		try {
			Object result = getPrivateField(Bukkit.getServer().getPluginManager(), "commandMap");
			SimpleCommandMap commandMap = (SimpleCommandMap) result;
			Object map = getPrivateField(commandMap, "knownCommands");
			@SuppressWarnings("unchecked")
			HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
			knownCommands.remove(cmd.getName());
			for (String alias : cmd.getAliases()) {
				if(knownCommands.containsKey(alias) && knownCommands.get(alias).toString().contains(Bukkit.getName())) {
					knownCommands.remove(alias);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
