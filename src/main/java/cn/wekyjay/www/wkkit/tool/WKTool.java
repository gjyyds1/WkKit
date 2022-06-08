package cn.wekyjay.www.wkkit.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import cn.wekyjay.www.wkkit.config.MenuConfigLoader;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.tool.items.PlayerHead;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;

public class WKTool{

	/**
	 * 添加指定的物品到指定的玩家背包
	 * @param e - 指定的玩家
	 * @param i - 指定的物品
	 */
	public static void addItem(Player e, ItemStack i) {
		InventoryHolder invholder = (InventoryHolder) e;
		Inventory inv = invholder.getInventory();
		PlayerInventory playerinv = (PlayerInventory) inv;
		playerinv.addItem(i);
	}
	
	/**
	 * 判断是否有足够的空间领取指定的礼包
	 * @param p
	 * @param kit
	 * @return
	 */
	public static boolean hasSpace(Player p,Kit kit) {
		int nullNum = 0;
		int kitItemNum = 0;
		//计算出背包有多少空间
		for(int i = 0;i < 36; i++) {
			try {
				p.getInventory().getItem(i).getType();
			}catch(NullPointerException e) {
				nullNum += 1;
			}
		}
		//计算出礼包中有多少空间
		for(int i = 0;i < kit.getItemStack().length;i++) {
			if(kit.getItemStack()[i] != null) {
				kitItemNum++;
			}
		}
		if(nullNum >= kitItemNum) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 判断是否有足够的空间领取足够多数量的物品
	 * @param p
	 * @param kit
	 * @return
	 */
	public static boolean hasSpace(Player p,int num) {
		int nullNum = 0;
		int ItemNum = num;
		//计算出背包有多少空间
		for(int i = 0;i < 36; i++) {
			try {
				p.getInventory().getItem(i).getType();
			}catch(NullPointerException e) {
				nullNum += 1;
			}
		}
		if(nullNum >= ItemNum) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 更改占位符为指定的值
	 * @param key 占位符的key(不加{})
	 * @param value 要替换的值
	 * @param msg 要替换的消息段
	 * @return
	 */
	public static String replacePlaceholder(String key,String value,String msg) {
	    msg = msg.replace("{"+ key +"}", value);
		return msg;
		
	}
	/**
	 * NBT转换成头颅
	 * @param skullnbt
	 * @return ItemStack
	 */
	public static ItemStack nbtCovertoSkull(String skullnbt) {
		ItemStack head = PlayerHead.DEFAULT.getItemStack();//新建一个头颅物品
	    NBTItem nbti = new NBTItem(head);//传入头颅的NBT到NBTItem
	    String nbt = skullnbt;
		NBTContainer c = new NBTContainer(nbt);//添加一个NBT容器
	    nbti.mergeCompound(c);
	    head = nbti.getItem();
	    return head;
	}
	
	public static NBTItem getItemNBT(ItemStack is) {
		if(is == null) {
			return null;
		}else {
			NBTItem nbt = new NBTItem(is);
			return nbt;
		}
	}
	
	/**
	 * 获得服务器所有玩家的名字（包括离线）
	 * @return
	 */
    public static List<String> getPlayerNames() {
    	List<String> players = new ArrayList<String>();
    	for(OfflinePlayer offp : Bukkit.getOfflinePlayers()) {
    		if(offp.getName() == null) {continue;}
    		players.add(offp.getName());
    	}
    	
    	return players;
    }
	
	/**
	 * 获取slot的位置
	 * @param path
	 * @return
	 */
	public static List<Integer> getSlotNum(String path){
		String slotnum = MenuConfigLoader.getString(path);
		List<Integer> list = new ArrayList<>();
		if(slotnum.contains(",")) {// 有分隔符
			for(String s : slotnum.split(",")) {
				if(s.contains("-") && s.split("-").length == 2) {
					String[] s1 = s.split("-");
					int begain = Integer.parseInt(s1[0]);
					int end = Integer.parseInt(s1[1]);
					// 遍历数字区间
					for(int i = begain;i <= end;i++) {
						if(!list.contains(i)) {
							list.add(i);
						}
					}
				}else if(!list.contains(Integer.parseInt(s))) {// 没有数字区间就直接加入
					list.add(Integer.parseInt(s));
				}
			}
		}else {// 没有分隔符
			if(slotnum.contains("-") && slotnum.split("-").length == 2) {
				String[] s1 = slotnum.split("-");
				int begain = Integer.parseInt(s1[0]);
				int end = Integer.parseInt(s1[1]);
				// 遍历数字区间
				for(int i = begain;i <= end;i++) {
					if(!list.contains(i)) {
						list.add(i);
					}
				}
			}else {// 没有数字区间就直接加入
				if(!list.contains(Integer.parseInt(slotnum))) {
					list.add(Integer.parseInt(slotnum));
				}
			}
		}
		return list;
	}
	/**
	 * 获取服务器版本号
	 * @return x.xx.x
	 */
	public static int getVersion() {
		String[] versions = Bukkit.getBukkitVersion().split("\\.");//获得版本号并且分割给version字符串组
		int versionsnum = Integer.parseInt(versions[1]);//16
		return versionsnum;
	}
	
	/**
	 * 设置Item的DisplayName
	 * @param is
	 * @param name
	 * @return
	 */
	public static ItemStack setItemName(ItemStack is,String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	/**
	 * 设置Item的Lore
	 * @param is
	 * @param lore
	 * @return
	 */
	public static ItemStack setItemLore(ItemStack is,List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	/**
	 * 判断文本是否符合正则表达式
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Boolean ismatche(String str, String pattern) {
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		return m.matches();
	}
	
}
