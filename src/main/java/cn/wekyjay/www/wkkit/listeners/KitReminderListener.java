package cn.wekyjay.www.wkkit.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;

/**
 * 礼包邮箱登录提醒领取
 * @author WekyJay
 *
 */
public class KitReminderListener implements Listener{
	@EventHandler
	public void whenPlayerOnline(PlayerJoinEvent e) {
		if(WkKit.getWkKit().getConfig().getBoolean("Setting.OnlineReminder") == false) return;
		
		Player player = e.getPlayer();
		int kitnum = 0;
		if(player == null) return;
		// 遍历礼包数据
		for(String kitname : WkKit.getPlayerData().getMailKits(player.getName())) {
			kitnum += WkKit.getPlayerData().getMailKitNum(player.getName(), kitname);
		}
		if(kitnum <= 0) return;
		player.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_MAIL_REMINDER", ChatColor.YELLOW));
	}
}
