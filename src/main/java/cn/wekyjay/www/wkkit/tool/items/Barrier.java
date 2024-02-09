package cn.wekyjay.www.wkkit.tool.items;

import cn.wekyjay.www.wkkit.tool.WKTool;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Barrier {
		DEFAULT();
		private ItemStack item;
		Barrier() {
			if(WKTool.getVersion() <= 7) {
				NBTContainer c = new NBTContainer("{id:30s,Count:1b,Damage:0s,}");
				item = NBTItem.convertNBTtoItem(c);
			}else {
				item = new ItemStack(Material.BARRIER);
			}
		}
		public ItemStack getItemStack() {
			return item;
		}
}
