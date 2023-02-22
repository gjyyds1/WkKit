package cn.wekyjay.www.wkkit.config;

import java.io.File;
import java.sql.Savepoint;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import cn.wekyjay.www.wkkit.WkKit;

public class LangConfigLoader {
	static File langFile = null;
	static FileConfiguration langConfig = null;
	
	/**
	 * 加载文件夹中的配置文件
	 */
	public static void loadConfig() {
		String path = WkKit.getWkKit().getDataFolder().getAbsolutePath() + File.separator + "Language";
		String lang = WkKit.getWkKit().getConfig().getString("Setting.Language");
		if(lang.equals("none")) {
			Locale locale = Locale.getDefault();
			if(locale.toString().equals(new Locale("zh","CN").toString())) lang = "zh_CN";
			else if(locale.toString().equals(new Locale("zh","TW").toString())) lang = "zh_TW";
			else if(locale.toString().equals(new Locale("zh","HK").toString())) lang = "zh_HK";
			else lang = "en_US";
			WkKit.getWkKit().getConfig().set("Setting.Language", lang);
			WkKit.getWkKit().saveConfig();
		}
		File[] ff = new File(path).listFiles();
		 for(File fs : ff) {
			 if(fs.isFile() && fs.getName().equals(lang + ".yml")) {
				 langFile = fs;
				 langConfig = YamlConfiguration.loadConfiguration(fs);
			 }
		 }
	}
	/**
	 * 重载配置
	 */
	public static void reloadConfig() {
		LangConfigLoader.loadConfig();
	}
	
	// 封装BukkitAPI的方法
	
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
