package cn.wekyjay.www.wkkit.config;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import cn.wekyjay.www.wkkit.WkKit;

public class LangConfigLoader {
	static File langFile = null;
	static FileConfiguration langConfig = null;
	
	/**
	 * �����ļ����е������ļ�
	 * @param file
	 */
	public static void loadConfig() {
		String path = WkKit.getWkKit().getDataFolder().getAbsolutePath() + File.separator + "Language";
		File[] ff = new File(path).listFiles();
		 for(File fs : ff) {
			 if(fs.isFile() && fs.getName().equals(WkKit.getWkKit().getConfig().getString("Setting.Language") + ".yml")) {
				 langFile = fs;
				 langConfig = YamlConfiguration.loadConfiguration(fs);
			 }
		 }
	}
	/**
	 * ��������
	 */
	public static void reloadConfig() {
		LangConfigLoader.loadConfig();
	}
	
	// ��װBukkitAPI�ķ���
	
	public static Boolean contains(String path) {
		return langConfig.contains(path);
	}
	
	public static String getString(String path) {
		if(LangConfigLoader.contains(path)) {
			return langConfig.getString(path);
		}
		return null;
	}
	
	public static String getStringWithPrefix(String path,ChatColor color) {
		if(LangConfigLoader.contains(path)) {
			if(color == null) {
				return getString("Prefix") + " " + langConfig.getString(path);
			}
			return color + getString("Prefix") + " " + langConfig.getString(path);
			
		}
		return null;
	}
	public static List<String> getStringList(String path) {
		return langConfig.getStringList(path);
		
	}
	public static int getInt(String path) {
		return langConfig.getInt(path);
		
	}
	public static long getLong(String path) {
		return langConfig.getLong(path);
		
	}
	public static Boolean getBoolean(String path) {
		return langConfig.getBoolean(path);
		
	}
}