package cn.wekyjay.www.wkkit.menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	private String menuname,menutitle,type,menupermission;
	private int size;
	List<String> slots = new ArrayList<>();
	public Menu(String menuname,String menutitle,String type,String menupermission,int size,List<String> slots) {
		this.menuname = menuname;
		this.menutitle = menutitle;
		this.type = type;
		this.menupermission = menupermission;
		this.size = size;
		this.slots = slots;
		new MenuManager().loadMenu(this);
	}
	
	public String getMenuName() {
		return menuname;
	}

	public String getMenuTitle() {
		return menutitle;
	}

	public String getType() {
		return type;
	}

	public String getMenuPermission() {
		return menupermission;
	}

	public int getSize() {
		return size;
	}

	public List<String> getSlots() {
		return slots;
	}
	
}
