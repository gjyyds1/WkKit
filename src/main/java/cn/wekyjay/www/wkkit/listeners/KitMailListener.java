package cn.wekyjay.www.wkkit.listeners;


import static org.bukkit.event.inventory.InventoryAction.NOTHING;
import static org.bukkit.event.inventory.InventoryAction.UNKNOWN;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.command.KitMail;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.handlerlist.PlayersReceiveKitEvent;
import cn.wekyjay.www.wkkit.handlerlist.ReceiveType;
import cn.wekyjay.www.wkkit.invholder.MailHolder;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.tool.WKTool;

public class KitMailListener implements Listener {
	static WkKit wk = WkKit.getWkKit();// 调用主类实例	
	
	Map<String,Integer> m = new HashMap<>();
	String guiname;
	
	
	/*玩家打开礼包邮箱事件*/
	@EventHandler
	public void onInventory(InventoryOpenEvent e) {
		String pagetitle = WKTool.replacePlaceholder("page", 1+"", LangConfigLoader.getString("GUI_PAGETITLE"));
		if(e.getView().getTitle().equals(LangConfigLoader.getString("KITMAIL_TITLE")) || e.getView().getTitle().equals(LangConfigLoader.getString("KITMAIL_TITLE") + " - " + pagetitle)) {
			guiname = LangConfigLoader.getString("KITMAIL_TITLE");
			m.put(e.getPlayer().getName(), 1);
		}
	}
	
	/*玩家礼包邮箱领取礼包事件*/
	@EventHandler(priority=EventPriority.LOWEST)
	public void onInventory(InventoryClickEvent e) {
		if(e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof MailHolder && e.getWhoClicked() == e.getView().getPlayer()) {
			e.setCancelled(true);
			if(e.getAction().equals(NOTHING) || e.getAction().equals(UNKNOWN)) {
		        return;
			}
			String name = e.getWhoClicked().getName();
			String pagetitle = WKTool.replacePlaceholder("page", m.get(name)+"", LangConfigLoader.getString("GUI_PAGETITLE"));
			String pages = " - " + pagetitle;
			Player p = (Player)e.getWhoClicked();
			
			if(e.getWhoClicked().getOpenInventory().getTitle().equals(guiname + pages) || e.getWhoClicked().getOpenInventory().getTitle().equals(guiname) ) {//获得打开的GUI
				if(e.getRawSlot() >= 8 & e.getRawSlot() <= 44 && WKTool.getItemNBT(e.getCurrentItem()).hasKey("wkkit") && WKTool.hasSpace(p, 1)) {
					p.getInventory().addItem(new ItemStack[] { e.getCurrentItem() });
			          String kitname = WKTool.getItemNBT(e.getCurrentItem()).getString("wkkit");
			          PlayersReceiveKitEvent event = new PlayersReceiveKitEvent(p, Kit.getKit(kitname), ReceiveType.MAIL);
			          Bukkit.getPluginManager().callEvent(event);
			          if (event.isCancelled())
			            return; 
			          WkKit.getPlayerData().delMailToFile(name, kitname);
			          p.closeInventory();
			          (new KitMail()).openKitMail(p, 1);
			          return;
				}
				List<String> set = WkKit.getPlayerData().getMailKits(name);
				if(!set.isEmpty() && e.getRawSlot() == 52) {
		          if (!WKTool.hasSpace(p, set.size())) {
		              p.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GET_FAILED", ChatColor.YELLOW));
		              return;
		            } 
		            for (ItemStack is : e.getInventory().getContents()) {
		              if (is != null && WKTool.getItemNBT(is).hasKey("wkkit").booleanValue()) {
		                String kitname = WKTool.getItemNBT(is).getString("wkkit");
		                // 回调事件
		                PlayersReceiveKitEvent event = new PlayersReceiveKitEvent(p, Kit.getKit(kitname), ReceiveType.MAIL);
		                Bukkit.getPluginManager().callEvent(event);
		                // 如果没有取消
		                if (!event.isCancelled()) {
		                  p.getInventory().addItem(new ItemStack[] { is });
		                  WkKit.getPlayerData().delMailToFile(name, kitname);
		                } 
		              } 
		            } 
		            p.closeInventory();
		            (new KitMail()).openKitMail(p, 1);
		            return;
				}
			}
		}
	}
}