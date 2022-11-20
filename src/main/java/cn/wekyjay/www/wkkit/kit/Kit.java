package cn.wekyjay.www.wkkit.kit;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.ConfigManager;
import cn.wekyjay.www.wkkit.tool.KitRefresh;
import cn.wekyjay.www.wkkit.tool.WKTool;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NbtApiException;

public class Kit {
	private String kitname;
	private String displayName;
	private String icon;
	private ItemStack[] itemStack;
	private List<String> commands;
	private List<String> lore;
	private List<String> drop;
	private String permission;
	private String docron;
	private Integer delay;
	private Integer times;

	
	public Kit(@NotNull String kitname,@NotNull String displayname,@NotNull String icon,ItemStack[] itemStack){
		this.kitname = kitname;
		this.displayName = displayname;
		this.icon = icon;
		this.itemStack = itemStack;
		this.loadKit(this);
	}
	
	
	public static List<Kit> getKits() {
		return ConfigManager.getKitconfig().getKitsList();
	}
	
	public static List<String> getKitNames(){
		List<String> list = new ArrayList<String>();
		for(Kit kit : Kit.getKits()) {
			list.add(kit.getKitname());
		}
		return list;
	}
	
	
	/**
	 * 通过礼包的名字返回Kit，如果没找到就返回null
	 * @param kitname
	 * @return
	 */
	public static Kit getKit(String kitname) {
		for(Kit kit : Kit.getKits()) {
			if(kit.getKitname().equals(kitname)) return kit;
		}
		return null;
	}
	
	
	
	
	/**
	 * 加载礼包
	 * @param kit
	 */
	public void loadKit(Kit kit) {
		if (kit.isKit()) {
			String kitname = kit.getKitname();
			if(ConfigManager.getKitconfig().contains(kitname + ".Commands")) commands = ConfigManager.getKitconfig().getStringList(kitname + ".Commands");
			if(ConfigManager.getKitconfig().contains(kitname + ".Lore")) lore = ConfigManager.getKitconfig().getStringList(kitname + ".Lore");
			if(ConfigManager.getKitconfig().contains(kitname + ".Drop")) {drop = ConfigManager.getKitconfig().getStringList(kitname + ".Drop");}
			if(ConfigManager.getKitconfig().contains(kitname + ".Permission")) permission =  ConfigManager.getKitconfig().getString(kitname + ".Permission");
			if(ConfigManager.getKitconfig().contains(kitname + ".Delay")) delay =  ConfigManager.getKitconfig().getInt(kitname + ".Delay");
			if(ConfigManager.getKitconfig().contains(kitname + ".Times")) times = ConfigManager.getKitconfig().getInt(kitname + ".Times");
			if(ConfigManager.getKitconfig().contains(kitname + ".DoCron")) docron = ConfigManager.getKitconfig().getString(kitname + ".DoCron");
			Kit.getKits().add(kit);
		}
	}
	
	/**
	 * 判断是否是一个有效礼包
	 * @return Boolean
	 */
	public Boolean isKit() {
		if(displayName != null & icon != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获得礼包的ID名(非展示名)
	 * @return
	 */
	public String getKitname() {
		return kitname;
	}
	
	
	
	// Getter & Setter
	/**
	 * 获得该礼包的文件块
	 * @return
	 */
	public ConfigurationSection getConfigurationSection() {
		String groupname = KitGroupManager.getContainName(this);
		ConfigurationSection cs = KitGroupManager.getGroup(groupname).getConfigurationSection(this.getKitname());
		return cs;
	}
	
	/**
	 * 设置完礼包属性之后一定要记得保存
	 * @throws IOException
	 */
	public final void saveConfig() {
		String groupname = KitGroupManager.getContainName(this);
		try {
			KitGroupManager.getGroup(groupname).save(ConfigManager.getKitconfig().getContainsFile(this.kitname));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName; // 更改栈内存
		this.getConfigurationSection().set("Name", displayName);    // 更改文件内存
	}

	public String getPermission() {
		return permission;
	}
	
	public void setPermission(String permission) {
		this.permission = permission;
		this.getConfigurationSection().set("Permission", permission); 
	}
	
	public Integer getTimes() {
		return times;
	}
	
	public void setTimes(Integer times) {
		this.times = times;
		this.getConfigurationSection().set("Times", times);
	}
	
	public List<String> getCommands() {
		return commands;
	}
	
	public void setCommands(List<String> commands) {
		this.commands = commands;
		this.getConfigurationSection().set("Commands", commands);
		
	}
	
	public Integer getDelay() {
		return delay;
	}
	
	public void setDelay(Integer delay) {
		this.delay = delay;
		this.getConfigurationSection().set("Delay", delay);
	}
	
	public String getDocron() {
		return docron;
	}
	
	public void setDocron(String docron) {
		this.docron = docron;
		this.getConfigurationSection().set("DoCron", docron);
		// 关闭该礼包线程自刷新
		for(String kitname : ConfigManager.tasklist.keySet()) {
			if(kitname.equals(this.kitname)) {
				ConfigManager.tasklist.get(kitname).cancel();
			}
		}
		// 如果传入值不是null就重新开始刷新该礼包
		if(docron != null) {
			KitRefresh.refreshDay(this);
		}
	}
	
	public List<String> getDrop() {
		return drop;
	}
	
	public void setDrop(List<String> drop) {
		this.drop = drop;
		this.getConfigurationSection().set("Drop", drop);
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
		this.getConfigurationSection().set("Icon", icon);
	}
	public ItemStack[] getItemStack() {
		return itemStack;
	}
	
	public void setItemStack(ItemStack[] itemStack) {
		this.itemStack = itemStack;
		List<String> list = new ArrayList<String>();
		for(ItemStack is : itemStack) {
			list.add(NBTItem.convertItemtoNBT(is).toString());
		}
		this.getConfigurationSection().set("Item", list);
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public void setLore(List<String> lore) {
		this.lore = lore;
		this.getConfigurationSection().set("Lore", lore);
	}
	
	public Map<String,Object> getFlags() {
		Map<String,Object> kitflags = new HashMap<>();
		kitflags.put("DisplayName", displayName);
		kitflags.put("Icon", icon);
		kitflags.put("Times", times);
		kitflags.put("Delay", delay);
		kitflags.put("Permission", permission);
		kitflags.put("DoCron", docron);
		kitflags.put("Commands", commands);
		kitflags.put("Lore", lore);
		kitflags.put("Drop", drop);
		kitflags.put("Item", itemStack);
		return kitflags;
	}

	/**
	 * 获得类型为Item的礼包实体
	 * @return
	 */
	public ItemStack getKitItem() {
		// 创建物品
		ItemStack item = null;
		try {
			if(icon.contains("[SKULL]")) {
				item = WKTool.nbtCovertoSkull(icon.substring(7));
			}else if(icon.contains("[NBT]")) {
				item = NBTItem.convertNBTtoItem(new NBTContainer(icon.substring(5)));
			}
		}catch(NbtApiException e) {
			e.printStackTrace();
		}
		if(item == null || item.getAmount() == 0) {
			// 如果出错或不存在就用默认配置的ICON替代
			if(Material.getMaterial(icon) == null) item = new ItemStack(Material.getMaterial(WkKit.getWkKit().getConfig().getString("Default.Icon")));
			else item = new ItemStack(Material.getMaterial(icon));
		}
		NBTItem im = new NBTItem(item);
		im.setString("wkkit", kitname);
		item = im.getItem();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	
	
	
	
	
}
