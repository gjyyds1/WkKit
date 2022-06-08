package cn.wekyjay.www.wkkit.menu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.invholder.MenuHolder;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.tool.CronManager;
import cn.wekyjay.www.wkkit.tool.WKTool;
import de.tr7zw.nbtapi.NBTItem;


public class MenuOpenner {
	public void openMenu(String menuname,Player p) {
		if(!(MenuManager.getPermission(menuname) == null) && !p.hasPermission(MenuManager.getPermission(menuname))) {// 缺少权限
			System.out.println(LangConfigLoader.getStringWithPrefix("MENU_NEED_PERMISSION", ChatColor.RED) + MenuManager.getPermission(menuname));
			return;
		} 
		String playername = p.getName();
		Inventory inv;
		if(MenuManager.getType(menuname).equals(InventoryType.CHEST)){
			inv = Bukkit.createInventory(new MenuHolder(menuname), MenuManager.getInvs().get(menuname).getSize(), MenuManager.getTitle(menuname));
		}else {
			inv = Bukkit.createInventory(new MenuHolder(menuname), MenuManager.getType(menuname), MenuManager.getTitle(menuname));
		}
		inv.setContents(MenuManager.getMenu(menuname).getStorageContents());
		List<String> kitlist = MenuManager.getSlotOfKit(menuname);
		// 遍历检查
		for(String kitname : kitlist) {
			if(WkKit.getPlayerData().contain_Kit(playername, kitname)) {
				if(WkKit.getPlayerData().getKitData(playername, kitname).equalsIgnoreCase("false") || WkKit.getPlayerData().getKitTime(playername, kitname) != null && WkKit.getPlayerData().getKitTime(playername, kitname) == 0) {
						for(int num : WKTool.getSlotNum(menuname + ".Slots." + kitname + ".slot")) {
							ItemStack item = new ItemStack(Material.BARRIER);
							ItemMeta meta = item.getItemMeta();
							// 添加Lore
							Kit kit = Kit.getKit(kitname);
							List<String> list = new ArrayList<String>();
							list.add(LangConfigLoader.getString("CLICK_GET_NEXT_STATUS"));
							if(kit.getDocron() != null) list.add(LangConfigLoader.getString("CLICK_GET_NEXT_DATE") + "§e" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(CronManager.getNextExecution(kit.getDocron())));
							else list.add(LangConfigLoader.getString("CLICK_GET_NEXT_NODATE"));
							if(kit.getTimes() != null) list.add(LangConfigLoader.getString("CLICK_GET_NEXT_TIMES") + "§e" + kit.getTimes());
							else list.add(LangConfigLoader.getString("CLICK_GET_NEXT_NOTIMES"));
							meta.setLore(list);
							meta.setDisplayName(kit.getDisplayName());
							item.setItemMeta(meta);
							NBTItem nbti = new NBTItem(item);
							nbti.removeKey("wkkit");
							inv.setItem(num, nbti.getItem());
						}
				}else {
					ItemStack is = Kit.getKit(kitname).getKitItem();
					List<Integer> slotnum = WKTool.getSlotNum(menuname + ".Slots." + kitname + ".slot");
					for(int num : slotnum) {
						inv.setItem(num, is);
					}
				}
			}else {
				ItemStack is = Kit.getKit(kitname).getKitItem();
				List<Integer> slotnum = WKTool.getSlotNum(menuname + ".Slots." + kitname + ".slot");
				for(int num : slotnum) {
					inv.setItem(num, is);
				}
			}
		}
		p.openInventory(inv);
		
	}
}
