package cn.wekyjay.www.wkkit.tool;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class ItemEditer {
	private ItemStack itemStack;
	public ItemEditer(ItemStack itemStack) {
		this.itemStack = itemStack.clone();
	}
	public ItemEditer(ItemStack itemStack,String displayName){
		this.itemStack = itemStack.clone();
		ItemMeta im = itemStack.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		itemStack.setItemMeta(im);
	}
	
	/**
	 * 设置物品名称
	 * @param name
	 * @return
	 */
	public ItemEditer setDisplayName(String name) {
		ItemMeta im = itemStack.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		itemStack.setItemMeta(im);
		return this;
	}

	/**
	 * 设置物品Lore
	 * @param lore
	 * @return
	 */
	public ItemEditer setLore(List<String> lore) {
		ItemMeta im = itemStack.getItemMeta();
		List<String> newlore = new ArrayList<>();
		for(String str : lore) {
			newlore.add(ChatColor.translateAlternateColorCodes('&', str));
		}
		im.setLore(newlore);
		itemStack.setItemMeta(im);
		return this;
	}
	
	public ItemEditer setNBTString(String key,String value) {
		NBTItem nbti = getNBTItem();
		nbti.setString(key, value);
		itemStack = nbti.getItem();
		return this;
	}
	public ItemEditer setNBTInteger(String key,Integer value) {
		NBTItem nbti = getNBTItem();
		nbti.setInteger(key, value);
		itemStack = nbti.getItem();
		return this;
	}
	
	public ItemEditer removeNBT(String key) {
		NBTItem nbti = getNBTItem();
		nbti.removeKey(key);;
		itemStack = nbti.getItem();
		return this;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	public String getDisplayName() {
		return itemStack.getItemMeta().getDisplayName();
	}
	public List<String> getLore() {
		return itemStack.getItemMeta().getLore();
	}
	public NBTItem getNBTItem() {
		NBTItem nbti = new NBTItem(itemStack);
		return nbti;
	}

}
