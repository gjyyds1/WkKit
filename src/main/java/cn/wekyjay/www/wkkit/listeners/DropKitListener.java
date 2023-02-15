package cn.wekyjay.www.wkkit.listeners;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import cn.wekyjay.www.wkkit.api.PlayersReceiveKitEvent;
import cn.wekyjay.www.wkkit.api.ReceiveType;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.tool.CountDelayTime;
import cn.wekyjay.www.wkkit.tool.WKTool;
import de.tr7zw.nbtapi.NBTItem;
public class DropKitListener implements Listener{

	@EventHandler
	public void onPlayerClickKit(PlayerInteractEvent e) {
		if(!e.hasItem()) {return;}//手上没有东西就结束方法
		NBTItem nbti;
		try {nbti = new NBTItem(e.getItem());}catch(NullPointerException e2) {return;}// 交互的物品为NULL则取消
		if(WKTool.getVersion() > 9 && !e.getHand().equals(EquipmentSlot.HAND)) {// 如果是高版本不是副手的话就返回
			return;
		}
		if(nbti.hasKey("wkkit") ) {
			String kitname = nbti.getString("wkkit");
				if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
					PlayerInventory pinv = e.getPlayer().getInventory();
					Kit kit = Kit.getKit(kitname);
					// 判断是否有权限
					if(kit.getPermission() != null && !e.getPlayer().hasPermission(kit.getPermission())) {
						e.getPlayer().sendMessage(LangConfigLoader.getStringWithPrefix("NO_PERMISSION", ChatColor.RED));
						return;
					}
					// 判断是否有冷却
					if(kit.getDelay() != null) {
						if(CountDelayTime.getDelayInstance(e.getPlayer(), kit) == null) {
							CountDelayTime ct = new CountDelayTime(e.getPlayer(), kit);
							ct.isGet();
						}else {
							CountDelayTime ct = CountDelayTime.getDelayInstance(e.getPlayer(), kit);
							if(!ct.isGet()) {
								ct.whenGet();
								return;
							}
						}
					}
					
					if(WKTool.hasSpace(e.getPlayer(), Kit.getKit(kitname))) {
						int itemnum;
						if(WKTool.getVersion() >= 9) {
							ItemStack mainItem = e.getPlayer().getInventory().getItemInMainHand();
							if(mainItem != null && WKTool.getItemNBT(mainItem).hasKey("wkkit")) {
								itemnum = mainItem.getAmount();//获取主手物品数量
								e.getPlayer().getInventory().getItemInMainHand().setAmount(itemnum - 1);//主手物品数量-1
							}
						}else {
							ItemStack mainItem = e.getPlayer().getInventory().getItemInHand();
							itemnum = mainItem.getAmount();//获取主手物品数量
							if(mainItem != null && WKTool.getItemNBT(mainItem).hasKey("wkkit")) {
								e.getPlayer().getInventory().getItemInHand().setAmount(itemnum - 1);//主手物品数量-1
								if(itemnum - 1 == 0) {
									e.getPlayer().getInventory().remove(e.getPlayer().getInventory().getItemInHand());
								}
							}
						}

						//判断是否有Commands
						if(kit.getCommands() != null){
							List<String> cmdlist = kit.getCommands();
							for(String str : cmdlist) {
								String[] splitstr = str.split(":");
								String command = null;
								if(splitstr.length > 1) {//判断是否有指定的指令发送方式
									command = WKTool.replacePlaceholder("player", e.getPlayer().getName(), splitstr[1]);
								}else {
									command = WKTool.replacePlaceholder("player", e.getPlayer().getName(), splitstr[0]);
								}
								//根据不同的指令发送方式发送
								if(splitstr[0].equalsIgnoreCase("cmd")) {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
								}else if(splitstr[0].equalsIgnoreCase("op") && !e.getPlayer().isOp()) {
									e.getPlayer().setOp(true);
									Bukkit.dispatchCommand(e.getPlayer(), command);
									e.getPlayer().setOp(false);
								}else {
									e.getPlayer().performCommand(command);
								}
							}
						}
						//添加礼包
						for(ItemStack item : kit.getItemStack()) {
							if(item != null) {
								pinv.addItem(item);//添加物品至背包
							}

						}
						e.getPlayer().sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GET_SUCCESS",ChatColor.GREEN));
					}else {
						e.getPlayer().sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GET_FAILED",ChatColor.YELLOW));
					}
					
				}

		}
		return;
	}
	//检测是否是礼包，否则不可以放置
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		// 检测手中的物品是否为空
		if(e.getItemInHand().getAmount() > 0) {
			NBTItem nbti = new NBTItem(e.getItemInHand());
			if(nbti != null && nbti.hasKey("wkkit")) {
				e.setCancelled(true);
			}
		}
	}
	
	//检测死亡的实体是否是可掉落指定礼包的实体，是的话就掉落指定礼包
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		List<Kit> kits = Kit.getKits();
		
		Iterator<Kit> it = kits.iterator();
		while(it.hasNext()) {
			Kit kit = it.next();
			if(kit.getDrop() != null) {
				List<String> droplist = kit.getDrop();
				for(int i = 0; i < droplist.size(); i++) {
					String[] s = droplist.get(i).split("->");
					if(s.length == 2) { // 防止因为配置错误而报错
						String ename = s[0];
						float f = Float.parseFloat(s[1]);
						double d = Math.random();
						if(e.getEntity().getName().equals(ename) && d <= f) {
							e.getEntity().getWorld().dropItem(e.getEntity().getLocation(),kit.getKitItem());
							
						}
					}
				}
			}
		}
		return;
	}
}
