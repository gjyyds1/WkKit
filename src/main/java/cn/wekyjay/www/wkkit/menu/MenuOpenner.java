package cn.wekyjay.www.wkkit.menu;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.api.PlayersKitRefreshEvent;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.config.MenuConfigLoader;
import cn.wekyjay.www.wkkit.data.playerdata.PlayerData_MySQL;
import cn.wekyjay.www.wkkit.invholder.MenuHolder;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.tool.CronManager;
import cn.wekyjay.www.wkkit.tool.MessageManager;
import cn.wekyjay.www.wkkit.tool.WKTool;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;


public class MenuOpenner {
	public void openMenu(String menuname,Player p) {
		// 判断是否存在权限
		if(!(MenuManager.getPermission(menuname) == null) && !p.hasPermission(MenuManager.getPermission(menuname))) {// 缺少权限
			p.sendMessage(LangConfigLoader.getStringWithPrefix("MENU_NEED_PERMISSION", ChatColor.RED )+ " - "  + MenuManager.getPermission(menuname));
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

		// 遍历礼包是否能打开
		kitlist.forEach(kitName->{
			Kit kit = Kit.getKit(kitName);
			if(kit != null && kit.getDocron() != null) {
				Calendar cnow = Calendar.getInstance();//玩家当前时间
				Calendar c_player = Calendar.getInstance();//玩家当前时间
				// 判断是否执行
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				try {
					if(WkKit.getPlayerData().contain_Kit(playername, kitName)){
						c_player.setTime(sdf.parse(WkKit.getPlayerData().getKitData(playername,kitName)));
					}
				} catch (ParseException ignored) {}

				if (cnow.getTimeInMillis() >= c_player.getTimeInMillis()) {
					// 判断是否为首次不刷新礼包
					if (kit.isNoRefreshFirst()) kit.setNoRefreshFirst(false);
					// 有礼包数据的就刷新领取状态
					if (WkKit.getPlayerData().contain_Kit(playername, kitName)) {
						// 异步中同步回调
						Bukkit.getScheduler().callSyncMethod(WkKit.getWkKit(), () -> {
							PlayersKitRefreshEvent.callEvent(p, kit); // 回调
							return true;
						});
						if (WkKit.getPlayerData() instanceof PlayerData_MySQL) { // 判断是否是数据库模式，如果是则使用锁模式。
							((PlayerData_MySQL) WkKit.getPlayerData()).setKitDataOfLock(playername, kitName, "true");
						} else WkKit.getPlayerData().setKitData(playername, kitName, "true");
						MessageManager.infoDeBug("已刷新礼包：" + kitName);
					}
					// 重新计算礼包的下次刷新时间
					kit.restNextRC();
				}
			}
		});
		
		// 展开类型菜单
		if(MenuConfigLoader.contains(menuname + ".Spread") && MenuConfigLoader.getBoolean(menuname + ".Spread") && kitlist.size() == 1) {
			// 领取按钮
			if(MenuConfigLoader.contains(menuname + ".Slots.Get")){
				String id = MenuConfigLoader.getString(menuname + ".Slots.Get.id");

				String kitname = kitlist.get(0);
				Kit kit = Kit.getKit(kitname);
				// 判断是否领过礼包，如果领过就重新判断权限
				if(Kit.getKit(kitname).isNoRefreshFirst()
						|| WkKit.getPlayerData().contain_Kit(playername, kitname)) {
					// [判断是否可以领取]
					// 不能领取的条件
					if(Kit.getKit(kitname).isNoRefreshFirst()
							|| !WkKit.getPlayerData().getKitData(playername, kitname).equalsIgnoreCase("true")
							|| WkKit.getPlayerData().getKitTime(playername, kitname) != null && WkKit.getPlayerData().getKitTime(playername, kitname) == 0) {
						for(int num : WKTool.getSlotNum(menuname + ".Slots.Get.slot")) {
							ItemStack item = new ItemStack(Material.BARRIER);
							// 设置自定义图标
							if(MenuConfigLoader.contains(menuname + ".Slots." + kitname + ".offid")) {
								item = new ItemStack(Material.getMaterial(MenuConfigLoader.getString(menuname + ".Slots." + kitname + ".offid")));
							}
							ItemMeta meta = item.getItemMeta();
							
							List<String> list = new ArrayList<String>();
							// 设置自定义lore
							if(MenuConfigLoader.contains(menuname + ".Slots." + kitname + ".offlore")) {
								list = MenuConfigLoader.getStringList(menuname + ".Slots." + kitname + ".offlore");
								if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
									list = PlaceholderAPI.setPlaceholders(p, list);
								}
							}else {// 没有设置就默认
								list.add(LangConfigLoader.getString("CLICK_GET_NEXT_STATUS"));
								if(kit.getDocron() != null) list.add(LangConfigLoader.getString("CLICK_GET_NEXT_DATE") + "§e" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(CronManager.getNextExecution(kit.getDocron())));
								else list.add(LangConfigLoader.getString("CLICK_GET_NEXT_NODATE"));
								if(kit.getTimes() != null) list.add(LangConfigLoader.getString("CLICK_GET_NEXT_TIMES") + "§e" + WkKit.getPlayerData().getKitTime(playername, kitname));
								else list.add(LangConfigLoader.getString("CLICK_GET_NEXT_NOTIMES"));
							}
							meta.setLore(list);
							meta.setDisplayName(kit.getDisplayName());
							item.setItemMeta(meta);
							inv.setItem(num, item);
						}
					}else {
						ItemStack is = new ItemStack(Material.getMaterial(id));
						List<Integer> slotnum = WKTool.getSlotNum(menuname + ".Slots.Get.slot");
						for(int num : slotnum) {
							// 设置自定义lore
							if(MenuConfigLoader.contains(menuname + ".Slots." + kitname + ".lore")) {
								ItemMeta meta = is.getItemMeta();
								List<String> list = MenuConfigLoader.getStringList(menuname + ".Slots." + kitname + ".lore");
								if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
									list = PlaceholderAPI.setPlaceholders(p, list);
								}
								meta.setLore(list);
								meta.setDisplayName(kit.getDisplayName());
								is.setItemMeta(meta);
							}
							// 设置NBT
							NBTItem nbti = new NBTItem(is);
							nbti.setString("wkkit", kitname);
							inv.setItem(num, nbti.getItem());
						}
					}
				}else {
					ItemStack is = new ItemStack(Material.getMaterial(id));
					List<Integer> slotnum = WKTool.getSlotNum(menuname + ".Slots.Get.slot");
					for(int num : slotnum) {
						// 设置自定义lore
						if(MenuConfigLoader.contains(menuname + ".Slots." + kitname + ".lore")) {
							ItemMeta meta = is.getItemMeta();
							List<String> list = MenuConfigLoader.getStringList(menuname + ".Slots." + kitname + ".lore");
							if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
								list = PlaceholderAPI.setPlaceholders(p, list);
							}
							meta.setLore(list);
							meta.setDisplayName(kit.getDisplayName());
							is.setItemMeta(meta);
						}
						// 设置NBT
						NBTItem nbti = new NBTItem(is);
						nbti.setString("wkkit", kitname);
						inv.setItem(num, nbti.getItem());
					}
				}
				
			}
			// 遍历检查
			for(String kitname : kitlist) {
				Kit kit = Kit.getKit(kitname);
				int itemnum = (int) Stream.of(kit.getItemStacks()).filter(item -> item != null).count();
				int nounnum = (int) Stream.of(inv.getContents()).filter(item -> item == null).count();
				// 比较大小，如果有足够空间
				if(itemnum <= nounnum) {
					List<Integer> slotsIndex = new ArrayList<>();
					// 遍历物品
					for(int i = 0; i < inv.getContents().length; i++) {
						if(inv.getItem(i) == null) slotsIndex.add(i);
					}
					// 添加物品
					for(int i = 0; i < kit.getItemStacks().length; i++) {
						inv.setItem(slotsIndex.get(i), kit.getItemStacks()[i]);
					}
				}
				
			}
			// 删除空气方块
			for(int i = 0; i < inv.getSize(); i++) {
				ItemStack is = inv.getItem(i);
				if(is != null && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equalsIgnoreCase("AIR")) {
					inv.clear(i);
				}
			}

			// 打开菜单
			p.openInventory(inv);
			return;
		}
		
		// 普通类型菜单
		// 遍历检查
		for(String kitname : kitlist) {
			if(Kit.getKit(kitname).isNoRefreshFirst() ||WkKit.getPlayerData().contain_Kit(playername, kitname)) {
				// 如果不能领取
				if(Kit.getKit(kitname).isNoRefreshFirst()
						||WkKit.getPlayerData().getKitData(playername, kitname) != null && !WkKit.getPlayerData().getKitData(playername, kitname).equalsIgnoreCase("true")
						|| WkKit.getPlayerData().getKitTime(playername, kitname) != null && WkKit.getPlayerData().getKitTime(playername, kitname) == 0) {
						for(int num : WKTool.getSlotNum(menuname + ".Slots." + kitname + ".slot")) {
							ItemStack item = new ItemStack(Material.BARRIER);
							// 设置自定义图标
							if(MenuConfigLoader.contains(menuname + ".Slots." + kitname + ".offid")) {
								item = new ItemStack(Material.getMaterial(MenuConfigLoader.getString(menuname + ".Slots." + kitname + ".offid")));
							}
							ItemMeta meta = item.getItemMeta();
							Kit kit = Kit.getKit(kitname);
							List<String> list = new ArrayList<String>();
							// 设置自定义lore
							if(MenuConfigLoader.contains(menuname + ".Slots." + kitname + ".offlore")) {
								list = MenuConfigLoader.getStringList(menuname + ".Slots." + kitname + ".offlore");
								if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
									list = PlaceholderAPI.setPlaceholders(p, list);
								}
							}else {// 没有设置就默认
								list.add(LangConfigLoader.getString("CLICK_GET_NEXT_STATUS"));
								if(kit.getDocron() != null) list.add(LangConfigLoader.getString("CLICK_GET_NEXT_DATE") + "§e" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(CronManager.getNextExecution(kit.getDocron())));
								else list.add(LangConfigLoader.getString("CLICK_GET_NEXT_NODATE"));
								if(kit.getTimes() != null) list.add(LangConfigLoader.getString("CLICK_GET_NEXT_TIMES") + "§e" + WkKit.getPlayerData().getKitTime(playername, kitname));
								else list.add(LangConfigLoader.getString("CLICK_GET_NEXT_NOTIMES"));
							}
							
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
						// 设置自定义lore
						if(MenuConfigLoader.contains(menuname + ".Slots." + kitname + ".lore")) {
							ItemMeta meta = is.getItemMeta();
							List<String> list = MenuConfigLoader.getStringList(menuname + ".Slots." + kitname + ".lore");
							if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
								list = PlaceholderAPI.setPlaceholders(p, list);
							}
							meta.setLore(list);
							is.setItemMeta(meta);
						}
						inv.setItem(num, is);
					}
				}
			}else {
				ItemStack is = Kit.getKit(kitname).getKitItem();
				List<Integer> slotnum = WKTool.getSlotNum(menuname + ".Slots." + kitname + ".slot");
				for(int num : slotnum) {
					// 设置自定义lore
					if(MenuConfigLoader.contains(menuname + ".Slots." + kitname + ".lore")) {
						ItemMeta meta = is.getItemMeta();
						List<String> list = MenuConfigLoader.getStringList(menuname + ".Slots." + kitname + ".lore");
						if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
							list = PlaceholderAPI.setPlaceholders(p, list);
						}
						meta.setLore(list);
						is.setItemMeta(meta);
					}
					inv.setItem(num, is);
				}
			}
		}
		// 删除空气方块
		for(int i = 0; i < inv.getSize(); i++) {
			ItemStack is = inv.getItem(i);
			if(is != null && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equalsIgnoreCase("AIR")) {
				inv.clear(i);
			}
		}
		p.openInventory(inv);
		
	}
}
