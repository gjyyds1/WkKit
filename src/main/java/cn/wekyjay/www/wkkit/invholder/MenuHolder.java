package cn.wekyjay.www.wkkit.invholder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuHolder implements InventoryHolder {
	public String menuname;
	public MenuHolder(String menuname){
		this.menuname = menuname;
	}

	public String getMenuname() {
		return menuname;
	}
	@Override
	public Inventory getInventory() {
		return null;
	}

}
