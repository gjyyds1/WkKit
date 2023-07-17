package cn.wekyjay.www.wkkit.tool;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.config.MenuConfigLoader;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.tool.items.PlayerHead;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WKTool{


	
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
		for(int i = 0; i < kit.getItemStacks().length; i++) {
			if(kit.getItemStacks()[i] != null) {
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
	 * 添加物品至玩家背包
	 * @param itemStack
	 * @param player
	 */
	public static void addItem(ItemStack itemStack , Player player){
		if (WkKit.getWkKit().getConfig().getBoolean("Default.AutoEquipment")){ // 自动穿戴开启
			// 判断是否为护甲类型
			if(itemStack.getType().toString().contains("HELMET") && player.getEquipment().getHelmet() == null){
				player.getEquipment().setHelmet(itemStack);
			}
			else if (itemStack.getType().toString().contains("CHESTPLATE") && player.getEquipment().getChestplate() == null){
				player.getEquipment().setChestplate(itemStack);
			}
			else if (itemStack.getType().toString().contains("LEGGINGS") && player.getEquipment().getLeggings() == null){
				player.getEquipment().setLeggings(itemStack);
			}
			else if (itemStack.getType().toString().contains("BOOTS") && player.getEquipment().getBoots() == null){
				player.getEquipment().setBoots(itemStack);
			}else{
				player.getInventory().addItem(itemStack);//添加物品至背包
			}
		}else{//自动穿戴关闭,不自动穿戴.
			player.getInventory().addItem(itemStack);
		}
	}
	public static void addItem(Player player,ItemStack...itemStack){
		for (int i = 0;i<itemStack.length;i++){
			addItem(itemStack[i],player);
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
	 * 将一段文字加上前插件前缀
	 * @param text
	 * @return
	 */
	public static String ofTextWithPrefix(String text) {
		String msg = LangConfigLoader.getString("Prefix");
		return ChatColor.translateAlternateColorCodes('&', msg+ " " + text) ;
	}

	/**
	 * 将语言文件中的文字加上前插件前缀
	 * @param msgname
	 * @return
	 */
	public static String getMessageWithPrefix(String msgname) {
		String msg =  LangConfigLoader.getString("Prefix") + " " + LangConfigLoader.getString(msgname);
		return ChatColor.translateAlternateColorCodes('&', msg);
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
		
		Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        if(pattern.matcher(versions[1]).matches()) {
    		int versionsnum = Integer.parseInt(versions[1]);//16
    		return versionsnum;
        }
        // 否则默认
        return 99;
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

	/**
	 * 获取指定玩家的头颅
	 * @param offlinePlayer
	 * @return
	 */
	public static ItemStack getPlayerHead(OfflinePlayer offlinePlayer){
		ItemStack is = null;
		int version = getVersion();
		if (version < 16 && version >= 12){
			is = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short)3);
			SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
			skullMeta.setOwningPlayer(offlinePlayer);
			is.setItemMeta(skullMeta);
		}else if (version < 12){
			is = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short)3);
			SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
			skullMeta.setOwner(offlinePlayer.getName());
			is.setItemMeta(skullMeta);
		}else {
			is =new ItemStack(Material.PLAYER_HEAD);
			SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
			skullMeta.setOwningPlayer(offlinePlayer);
			is.setItemMeta(skullMeta);
		}

		return is;
	}


	
}
