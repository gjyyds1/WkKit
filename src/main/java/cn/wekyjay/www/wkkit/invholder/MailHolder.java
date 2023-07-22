package cn.wekyjay.www.wkkit.invholder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public	class MailHolder implements InventoryHolder{
	private int page;
	@Override
	public Inventory getInventory() {
		return null;
	}

	public MailHolder(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
}
