package cn.wekyjay.www.wkkit.tool;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.ConfigManager;
import cn.wekyjay.www.wkkit.kit.Kit;

public class KitRefresh {
	private static BukkitTask task = null;
	
	public static BukkitTask getTask() {
		return task;
	}
	
	/**
	 * 取消当前礼包自刷新线程
	 */
	public static void cancelTask() {
		WkKit.getWkKit().getLogger().info("礼包自刷新线程取消！");
		task.cancel();
	}
		
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
	public static void enableRefresh() {
				task = new BukkitRunnable() {
					@Override
					public void run() {
						List<Kit> list = Kit.getKits();
						// 遍历检查每个礼包
						list.forEach(kit->{
							if(kit.getDocron() != null) {
								String kitname = kit.getKitname();
								Calendar cnow = Calendar.getInstance();//玩家当前时间
								// 判断是否执行
								if(cnow.getTimeInMillis() >= kit.getNextRC().getTimeInMillis()) {
									OfflinePlayer[] playerlist = Bukkit.getOfflinePlayers();
									for(OfflinePlayer player : playerlist) {
										if(player.getName() == null) continue; // 如果获取不到玩家姓名则取消该玩家的刷新
										String playername = player.getName();
										// 有礼包数据的就刷新领取状态
										if(WkKit.getPlayerData().contain_Kit(playername, kitname)) {
											WkKit.getPlayerData().setKitData(playername, kitname, "true");
										}
									}
									kit.restNextRC();
								}
							}		
						});		
					}
					
				}.runTaskTimerAsynchronously(WkKit.getWkKit(), 20, 20);
	}
}
