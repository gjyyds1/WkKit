package cn.wekyjay.www.wkkit;

import cn.wekyjay.www.wkkit.api.PlayersKitRefreshEvent;
import cn.wekyjay.www.wkkit.command.*;
import cn.wekyjay.www.wkkit.config.ConfigManager;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.data.playerdata.PlayerData_MySQL;
import cn.wekyjay.www.wkkit.edit.EditGUI;
import cn.wekyjay.www.wkkit.edit.EditKit;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.kit.KitGetter;
import cn.wekyjay.www.wkkit.menu.MenuManager;
import cn.wekyjay.www.wkkit.menu.MenuOpenner;
import cn.wekyjay.www.wkkit.tool.KitCache;
import cn.wekyjay.www.wkkit.tool.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class KitCommand implements CommandExecutor{
	WkKit wk = WkKit.getWkKit();//创建一个主类的实例
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		/*指令帮助中心*/
		try {
			if(args.length <= 0 && sender.isOp() || args[0].equalsIgnoreCase("help") && sender.isOp()){
				sender.sendMessage("§a━━━━━━━━━━━━ WkKit Command ━━━━━━━━━━━━");
				sender.sendMessage(LangConfigLoader.getString("Commands.PS"));
				sender.sendMessage(LangConfigLoader.getString("Commands.help"));
				sender.sendMessage(LangConfigLoader.getString("Commands.edit"));
				sender.sendMessage(LangConfigLoader.getString("Commands.edit_kitname"));
				sender.sendMessage(LangConfigLoader.getString("Commands.savecache"));
				sender.sendMessage(LangConfigLoader.getString("Commands.create"));
				sender.sendMessage(LangConfigLoader.getString("Commands.delete"));
				sender.sendMessage(LangConfigLoader.getString("Commands.mail"));
				sender.sendMessage(LangConfigLoader.getString("Commands.info"));
				sender.sendMessage(LangConfigLoader.getString("Commands.kits"));
				sender.sendMessage(LangConfigLoader.getString("Commands.send"));
				sender.sendMessage(LangConfigLoader.getString("Commands.give"));
				sender.sendMessage(LangConfigLoader.getString("Commands.get"));
				sender.sendMessage(LangConfigLoader.getString("Commands.open"));
				sender.sendMessage(LangConfigLoader.getString("Commands.cdk_create"));
				sender.sendMessage(LangConfigLoader.getString("Commands.cdk_verify"));
				sender.sendMessage(LangConfigLoader.getString("Commands.cdk_exchange"));
				sender.sendMessage(LangConfigLoader.getString("Commands.cdk_export"));
				sender.sendMessage(LangConfigLoader.getString("Commands.group_create"));
				sender.sendMessage(LangConfigLoader.getString("Commands.group_delete"));
				sender.sendMessage(LangConfigLoader.getString("Commands.group_move"));
				sender.sendMessage(LangConfigLoader.getString("Commands.transfer"));
				sender.sendMessage(LangConfigLoader.getString("Commands.reload"));
				sender.sendMessage("§a━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
				return true;
			}
			else if(!sender.isOp() && args[0].equalsIgnoreCase("help")){
				sender.sendMessage("§a━━━━━━━━━━━━ WkKit Command ━━━━━━━━━━━━");
				sender.sendMessage(LangConfigLoader.getString("Commands.mail"));
				sender.sendMessage(LangConfigLoader.getString("Commands.open"));
				sender.sendMessage(LangConfigLoader.getString("Commands.get"));
				sender.sendMessage(LangConfigLoader.getString("Commands.cdk_verify"));
				sender.sendMessage(LangConfigLoader.getString("Commands.cdk_exchange"));
				sender.sendMessage("§a━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
				return true;
			}
		}catch(ArrayIndexOutOfBoundsException e) {return true;}
		
		/*重载插件*/
		if (args[0].equalsIgnoreCase("reload") && sender.isOp()){
			ConfigManager.reloadPlugin();
	        sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_NUM",ChatColor.GRAY) + Kit.getKits().size());
	        sender.sendMessage(LangConfigLoader.getStringWithPrefix("MENU_NUM",ChatColor.GRAY) + MenuManager.getInvs().size());
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("CONFIG_RELOAD",ChatColor.GREEN));
		    return true;
		}
		
		/*打开邮箱*/
		if(args[0].equalsIgnoreCase("mail") && sender.hasPermission("wkkit.mail") && sender instanceof Player) {
			new KitMail().onCommand(sender, command, label, args);
			return true;
		}
		
		/*发放礼包*/
		if(args[0].equalsIgnoreCase("send") && sender.isOp()) {
			KitSend ksd = new KitSend();
			ksd.onCommand(sender, command, label, args);
			return true;
		}
		
		
		
		/*create指令*/
		
		if(args[0].equalsIgnoreCase("create") && sender.isOp() && sender instanceof Player){//判断指令参数是否为/wkkit create <name>
			KitCreate kc = new KitCreate();
			kc.onCommand(sender, args);
			return true;
		}
	
		
		/*delete指令逻辑*/
		if(args[0].equalsIgnoreCase("delete") && sender.isOp()) {
				KitDelete kr = new KitDelete();
				kr.onCommand(sender, command, label, args);
				return true;
			}
		
		/*查看配置里的礼包*/
		if(args[0].equalsIgnoreCase("kits") && sender.isOp()) {
			StringBuilder allkit = new StringBuilder();
			int num = 0;
			List<Kit> kits = Kit.getKits();
			Iterator<Kit> it = kits.iterator(); //遍历list
			while(it.hasNext()){
				allkit.append(it.next().getKitname() + " ");
				num += 1;
			}
			if(num == 0) sender.sendMessage(LangConfigLoader.getStringWithPrefix("NO_KIT_CONFIG", ChatColor.YELLOW));
			sender.sendMessage(ChatColor.GREEN + "Kits(" + num + ")：" + allkit.toString());
			return true;
		}
		
		/*get获取礼包的内容*/
		if(args[0].equalsIgnoreCase("info")  && sender instanceof Player) {
			if(sender.hasPermission("wkkit.info") || sender.isOp()){
				new KitInfo().onCommand(sender, command, label, args);
				return true;
			}
		}
		
		
		/*直接发送礼包*/
		if(args[0].equalsIgnoreCase("give") && sender.isOp()) {
			KitGive ks = new KitGive();
			ks.onCommand(sender, command, label, args);
			return true;
		}
		
		/*数据迁移*/
		if(args[0].equalsIgnoreCase("transfer") && sender.isOp()) {
			KitTransfer kt = new KitTransfer();
			kt.onCommand(sender, command, label, args);
		}
		if(args[0].equalsIgnoreCase("open") && sender.hasPermission("wkkit.open") && sender instanceof Player) {
			if(args.length < 2) {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.open", ChatColor.GREEN));
				return true;
			}
			Player p = (Player)sender;
			if(MenuManager.getMenu(args[1]) != null) new MenuOpenner().openMenu(args[1], p);
			else sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_MENU_INVALID", ChatColor.RED));
			return true;

		}
		
		if(args[0].equalsIgnoreCase("group") && sender.isOp()) {
			new KitGroup().onCommand(sender, command, label, args);
			return true;
		}
		
		/** 礼包编辑 **/
		if(args[0].equalsIgnoreCase("edit") && sender.isOp()) {
			Player p = (Player)sender;
			if(args.length > 1){
				if(Kit.getKitNames().contains(args[1])){
					p.openInventory(new EditKit().editKit(args[1]));
				}else{
					MessageManager.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_DELETE_NONEXIST",ChatColor.YELLOW),p);
				}
				return true;
			}
			p.openInventory(EditGUI.getEditGUI().getEditInv());
			return true;
		}
		
		/* cdk系统 */
		if(args[0].equalsIgnoreCase("cdk")) {
			new KitCDK().onCommand(sender, args);
			return true;
		}

		/* 玩家模拟菜单领取指令 */
		if(wk.getConfig().getBoolean("Setting.UseCommandGet") && args.length >= 2 && args[0].equalsIgnoreCase("get") && sender instanceof Player && sender.hasPermission("wkkit.get")) {
			String kitName = args[1];
			Player p = (Player)sender;
			String playerName = p.getName();
			Kit kit = Kit.getKit(kitName);

			if (kit == null){
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("NO_KIT",ChatColor.RED) + " - " + kitName);
				return true;
			}

			// 检测礼包刷新
			if(kit != null && kit.getDocron() != null) {
				Calendar cnow = Calendar.getInstance();//玩家当前时间
				Calendar c_player = Calendar.getInstance();//玩家当前时间
				// 判断是否执行
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				try {
					if(WkKit.getPlayerData().contain_Kit(playerName, kitName)){
						c_player.setTime(sdf.parse(WkKit.getPlayerData().getKitData(playerName,kitName)));
					}
				} catch (ParseException ignored) {}

				// 判断是否执行
				if (cnow.getTimeInMillis() >= c_player.getTimeInMillis()) {
					OfflinePlayer[] playerlist = Bukkit.getOfflinePlayers();
					// 判断是否为首次不刷新礼包
					if (kit.isNoRefreshFirst()) kit.setNoRefreshFirst(false);
					// 有礼包数据的就刷新领取状态
					if (WkKit.getPlayerData().contain_Kit(playerName, kitName)) {
						// 异步中同步回调
						Bukkit.getScheduler().callSyncMethod(WkKit.getWkKit(), () -> {
							PlayersKitRefreshEvent.callEvent(p, kit); // 回调
							return true;
						});
						if (WkKit.getPlayerData() instanceof PlayerData_MySQL) { // 判断是否是数据库模式，如果是则使用锁模式。
							((PlayerData_MySQL) WkKit.getPlayerData()).setKitDataOfLock(playerName, kitName, "true");
						} else WkKit.getPlayerData().setKitData(playerName, kitName, "true");
						MessageManager.infoDeBug("已刷新礼包：" + kitName);
					}
					kit.restNextRC();
				}else {
					sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GET_CANTGET",ChatColor.RED));
					return true;
				}
			}

			// 查询玩家是否能够领取
			if (WkKit.getPlayerData().contain_Kit(sender.getName(),kitName)
					&& WkKit.getPlayerData().getKitData(sender.getName(),kitName) != null
					&& WkKit.getPlayerData().getKitData(sender.getName(),kitName).equals("false")) {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GET_CANTGET",ChatColor.RED));
				return true;
			}
			new KitGetter().getKit(kit,(Player)sender,null);
			return true;
		}
		
		/* 保存日志 */
		if(args[0].equalsIgnoreCase("SaveCache") && sender.isOp()) {
			try {
				KitCache.getCache().saveCache();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
//		/* 测试代码 */
//		if(args.length >= 1 && args[0].equalsIgnoreCase("head") && sender.isOp()) {
//			EditPlayer.selectPlayerGUI((Player)sender);
//		}
		
		/*到底了*/
		return true;
	}

}
