package cn.wekyjay.www.wkkit.tool;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.ConfigManager;
import cn.wekyjay.www.wkkit.kit.Kit;

public class KitRefresh {
		
	/**
	 * 将时间值转为毫秒
	 * @param value
	 * @return
	 */
	public long changeTomilli(String value,Calendar cal) {
		String[] part = value.split("(?<=\\d)(?=\\D)");
		int num = Integer.parseInt(part[0]);
		switch(part[1]) {
			case "d": cal.add(Calendar.DATE, num);break;
			case "w": cal.add(Calendar.WEEK_OF_MONTH, num);break;
			case "m": cal.add(Calendar.MONTH, num);break;
			case "y": cal.add(Calendar.YEAR, num);break;
		}
		return num;
	}
	
	/**
	 * 立刻刷新一个礼包
	 * @param kitname
	 */
	public static void refreshNow(Kit kit) {
		String kitname = kit.getKitname();
		OfflinePlayer[] playerlist = Bukkit.getOfflinePlayers();
		for(OfflinePlayer player : playerlist) {
			String playername = player.getName();
			// 有礼包数据的就刷新领取状态
			if(WkKit.getPlayerData().contain_Kit(playername, kitname)) {
				WkKit.getPlayerData().setKitData(playername, kitname, "true");
			}
		}
	}
	/**
	 * 开启自动刷新线程
	 * @param kit
	 */
	public static void refreshDay(Kit kit) {
		if(kit.getDocron() != null) {
			String kitname = kit.getKitname();
			WkKit.refreshCount++;
			String cron = kit.getDocron();
			Calendar cnext = Calendar.getInstance();//初始化时间
			cnext.setTime(CronManager.getNextExecution(cron)); // 初始化下次执行的时间
			// 添加匿名内部类
			ConfigManager.tasklist.put(kitname, 
				new BukkitRunnable() {
					@Override
					public void run() {
						Calendar cnow = Calendar.getInstance();//玩家当前时间
						// 时间到执行
						if(cnow.getTimeInMillis() >= cnext.getTimeInMillis()) {
							OfflinePlayer[] playerlist = Bukkit.getOfflinePlayers();
							for(OfflinePlayer player : playerlist) {
								if(player.getName() == null) continue; // 如果获取不到玩家姓名则取消该玩家的刷新
								String playername = player.getName();
								// 有礼包数据的就刷新领取状态
								if(WkKit.getPlayerData().contain_Kit(playername, kitname)) {
									WkKit.getPlayerData().setKitData(playername, kitname, "true");
								}
							}
							cnext.setTime(CronManager.getNextExecution(cron)); // 重置下次执行的时间（当前时间）
						}
					}
					
				}.runTaskTimerAsynchronously(WkKit.getWkKit(), 20, 20)
			);
		}
	}
	
	public void enable() {
		// it.next不能在同一个循环内出现两次，会导致最后一次的游标指向空值
		List<Kit> list = Kit.getKits();
		Iterator<Kit> it = list.iterator();
		while(it.hasNext()) {
			Kit kit = it.next();
			String shutdate = WkKit.getWkKit().getConfig().getString("Default.ShutDate");
			if(kit.getDocron() != null) {
				KitRefresh.refreshDay(kit);
				if(!shutdate.equalsIgnoreCase("None") && CronManager.isExecuted(shutdate,kit.getDocron())) {
					refreshNow(kit);
				}
			}
		}

	}
}
