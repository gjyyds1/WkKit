package cn.wekyjay.www.wkkit.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.handlerlist.PlayersReceiveKitEvent;
import cn.wekyjay.www.wkkit.handlerlist.ReceiveType;

public class KitCache implements Listener{
	private static KitCache cache = null;
	private static List<String> cacheList = null;
	private final static String CACHEPATH = WkKit.getWkKit().getDataFolder().getAbsolutePath() + File.separatorChar + "Caches";
	private boolean isEnable = false;
	
	// 动态获取静态的Cache对象
	public static KitCache getCache() {
		return cache == null? cache = new KitCache() : cache;
	}
	
	public KitCache() {
		isEnable = WkKit.getWkKit().getConfig().getBoolean("Cache.Enable");
		if(isEnable) {
			WkKit.getWkKit().getLogger().info("启用日志管理");
			Bukkit.getPluginManager().registerEvents(this, WkKit.getWkKit());
			File file = new File(CACHEPATH);
			// 不存在该路径则创建一个
			if(!file.exists()) file.mkdir();
			// 初始化cacheYaml
			cacheList = new ArrayList<>();
		}else{
			WkKit.getWkKit().getLogger().info("日志管理已关闭");
		}

	}
	
	public void reloadCache() {
		cache = new KitCache();
	}
	/**
	 * 保存当前存储的日志
	 */
	public void saveCache() throws IOException {
		if(cacheList.size() <= 0) return; // 如果没有数据则不进行操作
		String filename = "[Cache]" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
		File file = new File(CACHEPATH,filename + ".log");
		if(!file.exists()) file.createNewFile();
		
		try {
			RandomAccessFile ra;
			ra = new RandomAccessFile(file,"rw");
			ra.seek(ra.length());//代表从文件的结尾写不会覆盖，也就是文件的追加
			for(String str : cacheList) {
				ra.write(str.getBytes("UTF-8"));
				ra.write("\n".getBytes("UTF-8"));
			}
			ra.close();
			WkKit.getWkKit().getLogger().info("当前日志已保存至 Caches\\"+filename+".log");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerRececiveKit(PlayersReceiveKitEvent e) {
		ReceiveType type = e.getType();
		String str = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] ";
		switch(type) {
			case MAIL: 
			{
				str += "[" + e.getEventName() + "] " + "玩家 \"" + e.getPlayer().getName() + "\" 从 \"礼包邮箱\" 中领取了礼包 \"" + e.getKit().getKitname() + "\".";
				cacheList.add(str);
				break;
			}
			case MENU:
			{
				str += "[" + e.getEventName() + "] " + "玩家 \"" + e.getPlayer().getName() + "\" 从 \"礼包菜单\"(" + e.getMenuname() + ") 中领取了礼包 \"" + e.getKit().getKitname() + "\".";
				cacheList.add(str);
				break;
			}
			case SEND:
			{
				str += "[" + e.getEventName() + "] " + "玩家 \"" + e.getMenuname() + "\" 收到 \"指令Send\" 的礼包 \"" + e.getKit().getKitname() + "\".";
				cacheList.add(str);
				break;
			}
			case GIVE:
			{
				str += "[" + e.getEventName() + "] " + "玩家 \"" + e.getPlayer().getName() + "\" 收到 \"指令Give\" 的礼包 \"" + e.getKit().getKitname() + "\".";
				cacheList.add(str);
				break;
			}
			default:
				break;
		}
	}
}
