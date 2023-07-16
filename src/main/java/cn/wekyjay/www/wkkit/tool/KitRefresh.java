package cn.wekyjay.www.wkkit.tool;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.api.PlayersKitRefreshEvent;
import cn.wekyjay.www.wkkit.data.playerdata.PlayerData_MySQL;
import cn.wekyjay.www.wkkit.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Calendar;
import java.util.List;

public class KitRefresh {
	private static BukkitTask task = null;
	
	public static BukkitTask getTask() {
		return task;
	}
	
	/**
	 * 取消当前礼包自刷新线程
	 */
	public static void cancelTask() {
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
	 * @param kit 礼包
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
									// 判断是否为首次不刷新礼包
									if (kit.isNoRefreshFirst()) kit.setNoRefreshFirst(false);
									// 遍历刷新玩家数据
									for(OfflinePlayer player : playerlist) {
										if(player.getName() == null) continue; // 如果获取不到玩家姓名则取消该玩家的刷新
										String playername = player.getName();
										// 有礼包数据的就刷新领取状态
										if(WkKit.getPlayerData().contain_Kit(playername, kitname)) {
											// 异步中同步回调
											Bukkit.getScheduler().callSyncMethod(WkKit.getWkKit(), ()->{
												PlayersKitRefreshEvent.callEvent(player,kit); // 回调
												return true;
											});
											if (WkKit.getPlayerData() instanceof PlayerData_MySQL){ // 判断是否是数据库模式，如果是则使用锁模式。
												((PlayerData_MySQL)WkKit.getPlayerData()).setKitDataOfLock(playername, kitname, "true");
											}else WkKit.getPlayerData().setKitData(playername, kitname, "true");
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
