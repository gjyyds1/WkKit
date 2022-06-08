package cn.wekyjay.www.wkkit.edit;

import static org.bukkit.event.inventory.InventoryAction.NOTHING;
import static org.bukkit.event.inventory.InventoryAction.UNKNOWN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.edit.prompt.KitDeletePrompt;
import cn.wekyjay.www.wkkit.edit.prompt.KitFlagPrompt;
import cn.wekyjay.www.wkkit.invholder.EditKitGroupHolder;
import cn.wekyjay.www.wkkit.invholder.EditKitHolder;
import cn.wekyjay.www.wkkit.invholder.EditKitItemHolder;
import cn.wekyjay.www.wkkit.invholder.EditKitMainHolder;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.kit.KitGroupManager;
import cn.wekyjay.www.wkkit.tool.WKTool;
import cn.wekyjay.www.wkkit.tool.items.GlassPane;
import de.tr7zw.nbtapi.NBTItem;

public class EditKit implements Listener {
	private String editTitle;
	private Inventory[] invs;
	private List<ItemStack> itemlist = new ArrayList<>();
	/**
	 * 礼包管理主页
	 */
	public EditKit() {
		int kitnum = KitGroupManager.getGroups().size();
		itemlist = new ArrayList<>();
		editTitle = LangConfigLoader.getString("EDIT_KIT_TITLE");
		//判断可创建的gui个数
		int guinum = 0;
		if(kitnum % 45 == 0 && !(kitnum == 0)) guinum = kitnum / 45;
		else guinum = (kitnum / 45) + 1;
		
		invs = new Inventory[guinum];
		
		//添加物品到itemlist

		for(String kitGroupName : KitGroupManager.getGroups()) {
			NBTItem nbti = new NBTItem(WKTool.setItemName(new ItemStack(Material.BOOK), kitGroupName));
			nbti.setString("wkkit", kitGroupName);
			itemlist.add(nbti.getItem());
		}
		
		//创建gui到linv
		for(int i = 1; i <= guinum; i++) {
			Inventory inv;
			if(guinum == 1) {//如果只有一页就不加页数
				inv = Bukkit.createInventory(new EditKitMainHolder(), 6*9, editTitle); //创建一个GUI,操作人是强转成InventoryHolder的sender
			}else {
				String pagetitle = WKTool.replacePlaceholder("page", i+"", LangConfigLoader.getString("GUI_PAGETITLE"));
				inv = Bukkit.createInventory(new EditKitMainHolder(), 6*9, editTitle + " - " + pagetitle); //创建一个GUI,操作人是强转成InventoryHolder的sender
			}

			
			//添加物品：功能区
			ItemStack item_mn;
			if(WkKit.getWkKit().getConfig().getString("GUI.MenuMaterial").equalsIgnoreCase("Default")){
				item_mn = GlassPane.DEFAULT.getItemStack();
			}else {
				item_mn = new ItemStack(Material.getMaterial(WkKit.getWkKit().getConfig().getString("GUI.MenuMaterial")));
			}
			ItemMeta im = item_mn.getItemMeta();
			im.setDisplayName(LangConfigLoader.getString("DO_NOT_TOUCH"));
			item_mn.setItemMeta(im);
			//添加功能性物品：上一页
			ItemStack item_pre = new ItemStack(Material.getMaterial(WkKit.getWkKit().getConfig().getString("GUI.TurnPageMaterial")));
			ItemMeta ip = item_pre.getItemMeta();
			ip.setDisplayName(LangConfigLoader.getString("PREVIOUS_PAGE"));
			item_pre.setItemMeta(ip);
			//添加功能性物品：下一页
			ItemStack item_next = new ItemStack(Material.getMaterial(WkKit.getWkKit().getConfig().getString("GUI.TurnPageMaterial")));
			ItemMeta in = item_next.getItemMeta();
			in.setDisplayName(LangConfigLoader.getString("NEXT_PAGE"));
			item_next.setItemMeta(in);
			
			for(int i1 = 54 - 9; i1 < 54; i1++) {//最下一排
				inv.setItem(i1, item_mn);
			}

			invs[i-1] = inv;
		}
		//添加物品到指定的inv
		int num = 0;
		for(int invnum = 0; invnum < guinum; invnum++) {
			for(int i2 = 0; i2 < 45; i2++) {
				if(num >= kitnum || itemlist.size() == 0 ) {
					break;
				}
				invs[invnum].setItem(i2, itemlist.get(num));
				num += 1;
			}
		}
	}
	/**
	 * 编辑礼包组
	 * @param groupname
	 * @return
	 */
	public Inventory editGroup(String groupname) {
		List<String> kitsname = KitGroupManager.getGroupKits(groupname);
		int kitnum = kitsname.size();
		List<ItemStack> itemlist = new ArrayList<>();
		String title = LangConfigLoader.getString("EDIT_KIT_GROUP_TITLE") + " - " + groupname;
		List<Integer> slot = Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43);
		//判断可创建的gui个数
		int guinum = 0;
		if(kitnum % 28 == 0 && !(kitnum == 0)) guinum = kitnum / 28;
		else guinum = (kitnum / 28) + 1;
		Inventory[] invlist = new Inventory[guinum];
		
		//添加物品到itemlist
		for(String kitname : kitsname) {
			Kit kit = Kit.getKit(kitname);
			ItemStack is = kit.getKitItem();
			ItemMeta im = is.getItemMeta();
			List<String> lore = new ArrayList<String>();
			lore.add(LangConfigLoader.getString("EDIT_CLICK_EDITKIT"));
			im.setLore(lore);
			is.setItemMeta(im);
			itemlist.add(is);
		}
		
		//创建gui到linv
		for(int i = 1; i <= guinum; i++) {
			Inventory inv;
			if(guinum == 1) {//如果只有一页就不加页数
				inv = Bukkit.createInventory(new EditKitGroupHolder(), 6*9, title); //创建一个GUI,操作人是强转成InventoryHolder的sender
			}else {
				String pagetitle = WKTool.replacePlaceholder("page", i+"", LangConfigLoader.getString("GUI_PAGETITLE"));
				inv = Bukkit.createInventory(new EditKitGroupHolder(), 6*9, title + " - " + pagetitle); //创建一个GUI,操作人是强转成InventoryHolder的sender
			}

			
			//添加物品：功能区
			ItemStack item_mn;
			if(WkKit.getWkKit().getConfig().getString("GUI.MenuMaterial").equalsIgnoreCase("Default")){
				item_mn = GlassPane.DEFAULT.getItemStack();
			}else {
				item_mn = new ItemStack(Material.getMaterial(WkKit.getWkKit().getConfig().getString("GUI.MenuMaterial")));
			}
			// 修改名称
			item_mn = WKTool.setItemName(item_mn, LangConfigLoader.getString("DO_NOT_TOUCH"));
			
			//添加功能性物品：上一页
			ItemStack item_pre = new ItemStack(Material.getMaterial(WkKit.getWkKit().getConfig().getString("GUI.TurnPageMaterial")));
			item_pre = WKTool.setItemName(item_pre, LangConfigLoader.getString("PREVIOUS_PAGE"));
			
			//添加功能性物品：下一页
			ItemStack item_next = new ItemStack(Material.getMaterial(WkKit.getWkKit().getConfig().getString("GUI.TurnPageMaterial")));
			item_next = WKTool.setItemName(item_next, LangConfigLoader.getString("NEXT_PAGE"));
			
			for(int j = 0; j < 54; j++) {//最上一排
				if(!slot.contains(j)) {
					inv.setItem(j, item_mn);
				}
			}

			invlist[i-1] = inv;
		}
		//添加物品到指定的inv
		for(int invnum = 0; invnum < guinum; invnum++) {
			for(int si = 0; si < itemlist.size(); si++) {
				if(si == slot.size() -1 || itemlist.size() == 0 ) break;
				invlist[invnum].setItem(slot.get(si), itemlist.get(si));
			}
		}
		return invlist[0];
	}
	
	/**
	 * 单个礼包管理页面
	 * @param kitname
	 * @return
	 */
	public Inventory editKit(String kitname) {
		Kit kit = Kit.getKit(kitname);
		Inventory kitinv = Bukkit.createInventory(new EditKitHolder(), 4*9, LangConfigLoader.getString("EDIT_KIT_TITLE") + " - " + kitname);
		List<Integer> slot = Arrays.asList(0,2,3,5,6,8,13,22,31);
		List<Integer> hasflags = Arrays.asList(9,10,11,12,18,19,20,21,27,28,29,30);
		List<Integer> nonflags = Arrays.asList(14,15,16,17,23,24,25,26,32,33,34,35);
		// 填充物品
		for(int i = 0; i < 36; i++) {
			if(slot.contains(i)) {
				ItemStack is = GlassPane.DEFAULT.getItemStack();
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(LangConfigLoader.getString("DO_NOT_TOUCH"));
				is.setItemMeta(im);
				kitinv.setItem(i, is);
				continue;
			}
			if(i == 1) {
				ItemStack is = GlassPane.BLACK.getItemStack();
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(LangConfigLoader.getString("EDIT_BACK"));
				is.setItemMeta(im);
				NBTItem nbti = new NBTItem(is);
				nbti.setString("wkkit", kitname);
				kitinv.setItem(i, nbti.getItem());
				continue;
			}
			if(i == 4) {
				kitinv.setItem(i, Kit.getKit(kitname).getKitItem());
				continue;
			}
			if(i == 7) {
				ItemStack is = GlassPane.RED.getItemStack();
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(LangConfigLoader.getString("EDIT_DELETE_KIT"));
				is.setItemMeta(im);
				NBTItem nbti = new NBTItem(is);
				nbti.setString("wkkit", kitname);
				kitinv.setItem(i, nbti.getItem());
				continue;
			}
		}
		// 添加flag
		int hascount = 0;
		int noncount = 0;
		for(String key : kit.getFlags().keySet()) {
			if(kit.getFlags().get(key) != null) {
				ItemStack is = new ItemStack(Material.NAME_TAG); // 可能会报错
				ItemMeta im = is.getItemMeta();
				Object obj = kit.getFlags().get(key);
				im.setDisplayName("§e§l[√]§f§l " + key);
				if(obj instanceof String)im.setLore(Arrays.asList((String)obj));
				if(obj instanceof Integer) im.setLore(Arrays.asList(String.valueOf((int)obj)));
				if(obj instanceof List) im.setLore((List<String>)obj);
				if(obj instanceof ItemStack[]) im.setLore(Arrays.asList(LangConfigLoader.getString("EDIT_CLICK_EDIT")));
				is.setItemMeta(im);
				NBTItem nbti = WKTool.getItemNBT(is);
				nbti.setString("wkkit", key);
				kitinv.setItem(hasflags.get(hascount),nbti.getItem());
				hascount++;
			}else {
				ItemStack is = new ItemStack(Material.NAME_TAG); // 可能会报错
				ItemMeta im = is.getItemMeta();
				im.setDisplayName("§e§l[§a§l+§e§l]§f§l " + key);
				is.setItemMeta(im);
				NBTItem nbti = WKTool.getItemNBT(is);
				nbti.setString("wkkit", key);
				kitinv.setItem(nonflags.get(noncount),nbti.getItem());
				noncount++;
			}
			
		}
		return kitinv;
	}
	/**
	 * 管理礼包的Item内容
	 * @param kitname
	 * @return
	 */
	public Inventory editKitItem(String kitname) {
		Kit kit = Kit.getKit(kitname);
		Inventory kitinv = Bukkit.createInventory(new EditKitItemHolder(), 5*9, LangConfigLoader.getString("EDIT_KIT_ITEM_TITLE") + " - " + kitname);
		// 填充物品
		kitinv.addItem(kit.getItemStack());
		for(int i = 36; i < 45; i++) {
			if(i == 40) {
				ItemStack is = GlassPane.GREEN.getItemStack();
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(LangConfigLoader.getString("EDIT_SAVE"));
				is.setItemMeta(im);
				NBTItem nbti = new NBTItem(is);
				nbti.setString("wkkit", kitname);
				kitinv.setItem(i, nbti.getItem());
				continue;
			}else {
				ItemStack is = GlassPane.DEFAULT.getItemStack();
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(LangConfigLoader.getString("DO_NOT_TOUCH"));
				is.setItemMeta(im);
				kitinv.setItem(i, is);
				continue;
			}
		}
		return kitinv;
	}
	
	
	public Inventory getInventory() {
		return invs[0];
		
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		// 礼包管理界面
		if(e.getInventory().getHolder() instanceof EditKitMainHolder) {
			e.setCancelled(true);
			if(e.getAction().equals(NOTHING) || e.getAction().equals(UNKNOWN)) return;
			if( WKTool.getItemNBT(e.getCurrentItem()).hasKey("wkkit")) {
				String groupname = e.getCurrentItem().getItemMeta().getDisplayName();
				e.getWhoClicked().openInventory(this.editGroup(groupname));
			}
			return;
		}
		// 礼包组界面
		if(e.getInventory().getHolder() instanceof EditKitGroupHolder) {
			e.setCancelled(true);
			if(e.getAction().equals(NOTHING) || e.getAction().equals(UNKNOWN)) return;
			if( WKTool.getItemNBT(e.getCurrentItem()).hasKey("wkkit")) {
				NBTItem nbti = new NBTItem( e.getCurrentItem());
				e.getWhoClicked().openInventory(this.editKit(nbti.getString("wkkit")));
			}
		}
		if(e.getInventory().getHolder() instanceof EditKitHolder) {
			e.setCancelled(true);
			if(e.getAction().equals(NOTHING) || e.getAction().equals(UNKNOWN)) return;
			if( WKTool.getItemNBT(e.getCurrentItem()).hasKey("wkkit") && e.getClick().equals(ClickType.LEFT)) {
				String kitname = WKTool.getItemNBT(e.getInventory().getItem(1)).getString("wkkit");
				String key = WKTool.getItemNBT(e.getCurrentItem()).getString("wkkit"); // flag值
				if(e.getRawSlot() == 1) {e.getWhoClicked().openInventory(this.editGroup(KitGroupManager.getContainName(Kit.getKit(kitname))));return;}
				if(e.getRawSlot() == 7) {
					e.getWhoClicked().closeInventory();
					KitDeletePrompt.newConversation((Player)e.getWhoClicked(), kitname);
					return;
				}
				if(key.equals("DisplayName")) {e.getWhoClicked().closeInventory();KitFlagPrompt.setFlag((Player)e.getWhoClicked(), kitname, "DisplayName");return;}
				if(key.equals("Icon")) {e.getWhoClicked().closeInventory();KitFlagPrompt.setFlag((Player)e.getWhoClicked(), kitname, "Icon");return;}
				if(key.equals("Times")) {e.getWhoClicked().closeInventory();KitFlagPrompt.setFlag((Player)e.getWhoClicked(), kitname, "Times");return;}
				if(key.equals("Delay")) {e.getWhoClicked().closeInventory();KitFlagPrompt.setFlag((Player)e.getWhoClicked(), kitname, "Delay");return;}
				if(key.equals("Permission")) {e.getWhoClicked().closeInventory();KitFlagPrompt.setFlag((Player)e.getWhoClicked(), kitname, "Permission");return;}
				if(key.equals("DoCron")) {e.getWhoClicked().closeInventory();KitFlagPrompt.setFlag((Player)e.getWhoClicked(), kitname, "DoCron");return;}
				if(key.equals("Commands")) {e.getWhoClicked().closeInventory();KitFlagPrompt.setFlag((Player)e.getWhoClicked(), kitname, "Commands");return;}
				if(key.equals("Lore")) {e.getWhoClicked().closeInventory();KitFlagPrompt.setFlag((Player)e.getWhoClicked(), kitname, "Lore");return;}
				if(key.equals("Drop")) {e.getWhoClicked().closeInventory();KitFlagPrompt.setFlag((Player)e.getWhoClicked(), kitname, "Drop");return;}
				if(key.equals("Item")) {e.getWhoClicked().openInventory(this.editKitItem(kitname));return;}
			}
			if( WKTool.getItemNBT(e.getCurrentItem()).hasKey("wkkit") && e.getClick().equals(ClickType.RIGHT)) {
				List<Integer> hasflags = Arrays.asList(9,10,11,12,18,19,20,21,27,28,29,30);
				if(hasflags.contains(e.getRawSlot())) {
					String kitname = WKTool.getItemNBT(e.getInventory().getItem(1)).getString("wkkit");
					String key = WKTool.getItemNBT(e.getCurrentItem()).getString("wkkit"); // flag值
					if(key.equals("DisplayName")) {e.getWhoClicked().closeInventory();KitFlagPrompt.deFlag((Player)e.getWhoClicked(), kitname, "DisplayName");return;}
					if(key.equals("Icon")) {e.getWhoClicked().closeInventory();KitFlagPrompt.deFlag((Player)e.getWhoClicked(), kitname, "Icon");return;}
					if(key.equals("Times")) {e.getWhoClicked().closeInventory();KitFlagPrompt.deFlag((Player)e.getWhoClicked(), kitname, "Times");return;}
					if(key.equals("Delay")) {e.getWhoClicked().closeInventory();KitFlagPrompt.deFlag((Player)e.getWhoClicked(), kitname, "Delay");return;}
					if(key.equals("Permission")) {e.getWhoClicked().closeInventory();KitFlagPrompt.deFlag((Player)e.getWhoClicked(), kitname, "Permission");return;}
					if(key.equals("DoCron")) {e.getWhoClicked().closeInventory();KitFlagPrompt.deFlag((Player)e.getWhoClicked(), kitname, "DoCron");return;}
					if(key.equals("Commands")) {e.getWhoClicked().closeInventory();KitFlagPrompt.deFlag((Player)e.getWhoClicked(), kitname, "Commands");return;}
					if(key.equals("Lore")) {e.getWhoClicked().closeInventory();KitFlagPrompt.deFlag((Player)e.getWhoClicked(), kitname, "Lore");return;}
					if(key.equals("Drop")) {e.getWhoClicked().closeInventory();KitFlagPrompt.deFlag((Player)e.getWhoClicked(), kitname, "Drop");return;}
				}else {
					return;
				}
			}
		}
		if(e.getInventory().getHolder() instanceof EditKitItemHolder) {
			if(e.getAction().equals(NOTHING) || e.getAction().equals(UNKNOWN)) return;
			if(e.getRawSlot() > 35 && e.getRawSlot() < 45) {
				e.setCancelled(true);
				if(e.getRawSlot() == 40) {
					Kit kit = Kit.getKit(WKTool.getItemNBT(e.getCurrentItem()).getString("wkkit"));
					List<ItemStack> list = new ArrayList<ItemStack>();
					for(int i = 0;i < 36; i++) {
						if(e.getInventory().getItem(i) == null) continue;
						list.add(e.getInventory().getItem(i));
					}
					kit.setItemStack(list.toArray(new ItemStack[list.size()]));
					kit.saveConfig();
					e.getWhoClicked().openInventory(this.editKit(kit.getKitname()));
					e.getWhoClicked().sendMessage(LangConfigLoader.getStringWithPrefix("SAVE_SUCCESS", ChatColor.GREEN));
				}
			}

		}
	}
	
}
