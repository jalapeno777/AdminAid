package com.gmail.snipsrevival.utilities;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class FileUtilities {
	
	/**
	 * Creates a new file if it does not already exist
	 * 
	 * @param file - the location of the file to create
	 */
	
	public static void createNewFile(File file) {
		if(!file.exists()) {
			try {
				file.createNewFile();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Creates a new directory if it does not already exist
	 * 
	 * @param dir - the directory to create
	 */
	
	public static void createNewDir(File dir) {
		if(!dir.exists()) {
			try {
				dir.mkdir();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * saves a YamlConfiguration
	 * 
	 * @param yamlConfig - the configuration to save
	 * @param file - the location of the file to save
	 */
	
	public static void saveYamlFile(YamlConfiguration yamlConfig, File file) {
		try {
			yamlConfig.save(file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
