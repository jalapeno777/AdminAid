package com.gmail.snipsrevival;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.plugin.PluginDescriptionFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Updater {
	
	AdminAid plugin;
	
	public Updater(AdminAid plugin) {
		this.plugin = plugin;
	}
	
	public boolean isLatest() {
		plugin.getLogger().info("Checking for newer versions...");
		try {
			InputStream input = new URL("http://dev.bukkit.org/bukkit-plugins/adminaid/files.rss").openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			Node latestFile = document.getElementsByTagName("item").item(0);
			NodeList children = latestFile.getChildNodes();
			String[] updateVersion = children.item(1).getTextContent().replaceAll("[a-zA-Z ]", "").split("\\.");
			int updateMajorRelease = Integer.parseInt(updateVersion[0]);
			int updateMinorRelease = Integer.parseInt(updateVersion[1]);
			int updateBuild = Integer.parseInt(updateVersion[2]);
	
			PluginDescriptionFile pdf = plugin.getDescription();
			String[] currentVersion = pdf.getVersion().split("\\.");
			int currentMajorRelease = Integer.parseInt(currentVersion[0]);
			int currentMinorRelease = Integer.parseInt(currentVersion[1]);
			int currentBuild = Integer.parseInt(currentVersion[2]);
			
			if(updateMajorRelease > currentMajorRelease) return false;
			else {
				if((updateMinorRelease > currentMinorRelease) && updateMajorRelease == currentMajorRelease) return false;
				else {
					if((updateBuild > currentBuild) && updateMinorRelease == currentMinorRelease) return false;
					else return true;
				}
			}
		}
		catch (Exception e) {
			plugin.getLogger().warning("Something is wrong with the update checker. This can probably be ignored");
		}
		return true;
	}
	
	public String getDownloadLink() {
		try {
			InputStream input = new URL("http://dev.bukkit.org/bukkit-plugins/adminaid/files.rss").openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			Node latestFile = document.getElementsByTagName("item").item(0);
			NodeList children = latestFile.getChildNodes();
			String updateLink = children.item(3).getTextContent();
			return updateLink;
		}
		catch (Exception e) {
			return "http://dev.bukkit.org/server-mods/adminaid/";
		}
	}
}
