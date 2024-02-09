package cn.wekyjay.www.wkkit.command;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.ConfigManager;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.kit.KitGroupManager;
import cn.wekyjay.www.wkkit.tool.WKTool;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KitCreate {
	static WkKit wk = WkKit.getWkKit();// ???????????		

	public Boolean onCommand(CommandSender sender, String[] args) {
		if(args.length < 2) {
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.create", ChatColor.GREEN));
			return true;
		}
		
		Player player = (Player) sender;
		String kitname = args[1];
		String target = "Default";
		String displayname = wk.getConfig().getString("Default.Name");
		String itemtype = wk.getConfig().getString("Default.Icon");
		List<String> lore = new ArrayList<>();
		
		// ?????д????????
		if(args.length >= 3) {
			displayname = ChatColor.translateAlternateColorCodes('&', args[2]);
		}
		
		//?ж???????????
		if(Kit.getKit(kitname) != null && args[0].equals("create")) {
			player.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_CREATE_EXISTS", ChatColor.YELLOW));
			return true;
		}
		
		// ?ж????????飬?????????default??
		if(args.length >= 4 && KitGroupManager.contains(args[3])) {
			target = args[3];// ??????
		}else {
			File file = new File(WkKit.kitFile.getAbsolutePath(),"Default.yml");
			if(!file.exists())new KitGroupManager("Default");
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GROUP_DEFAULT", ChatColor.YELLOW));
		}

		// ?????????е???????
		ItemStack is;
		if(WKTool.getVersion() >= 9) {
			is =  player.getInventory().getItemInMainHand();
		}else {
			is =  player.getInventory().getItemInHand();
		}

		if(is.getAmount() == 0) {
			lore = new ArrayList<String>();
		}else {
			ItemMeta im =is.getItemMeta();
			itemtype = is.getType().toString();
			if(im.hasLore()) lore = im.getLore();
		}
		
		
		/*?????????д?????*/
		
		//??÷?????getplayer?????????????
		PlayerInventory pinv = ((Player) sender).getInventory();
		
		List<String> l = new ArrayList<String>();
		
		//??????????????е?????
		ItemStack[] obj = pinv.getContents();
		
		for(int i = 0; i < obj.length; i++) {
			if(obj[i] != null) {
				//????NBT?
				l.add(NBTItem.convertItemtoNBT(obj[i]).toString());
			}
		}
		ItemStack[] iss = new ItemStack[l.size()];
		// ItemStack???????
		for(int i = 0; i < l.size(); i++) {
			iss[i] = NBTItem.convertNBTtoItem(new NBTContainer(l.get(i)));
		}
		
			// ???д?????
			ConfigManager.getKitconfig().set(kitname + ".Name", displayname, target);
			ConfigManager.getKitconfig().set(kitname + ".Icon", itemtype, target);
			//?ж??List????????????
			if(lore.isEmpty()) {
				ConfigManager.getKitconfig().set(kitname + ".Lore", "", target);	
			}else {
				ConfigManager.getKitconfig().set(kitname + ".Lore", lore, target);	
			}
			//?ж??List????????????
			if(l.isEmpty()) {
				ConfigManager.getKitconfig().set(kitname + ".Item", "", target);
			}else {
				ConfigManager.getKitconfig().set(kitname + ".Item", l, target);
			}


		
		//????????
		try {
			ConfigManager.getKitconfig().save(target + ".yml");
			new Kit(kitname, displayname, itemtype, iss);
			player.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_CREATE_SUCCESS",ChatColor.GREEN));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
