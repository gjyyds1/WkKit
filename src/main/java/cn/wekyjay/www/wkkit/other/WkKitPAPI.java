package cn.wekyjay.www.wkkit.other;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.OfflinePlayer;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.ConfigManager;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.menu.MenuManager;
import cn.wekyjay.www.wkkit.tool.CronManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class WkKitPAPI extends PlaceholderExpansion {

	private final WkKit plugin;
    
    public WkKitPAPI(WkKit plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getAuthor() {
        return "WekyJay";
    }
    
    @Override
    public String getIdentifier() {
        return "wkkit";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
    	String playername = player.getName();
    	// 服务器礼包数
    	if(params.equalsIgnoreCase("serverkits")) {
    		return ConfigManager.getKitconfig().getKitsList().size() + "";
    	}
    	// 服务器菜单数
    	if(params.equalsIgnoreCase("servermenus")) {
    		return MenuManager.getMenus().size() + "";
    	}
    	// 自刷新礼包数
    	if(params.equalsIgnoreCase("cronkits")) {
    		return WkKit.refreshCount + "";
    	}
        //玩家礼包邮箱的总礼包数量
        if(params.equalsIgnoreCase("kitmail")) {
        	int kitnum = 0;
        	List<String> kits = WkKit.getPlayerData().getMailKits(player.getName());
        	if(kits != null) {
            	for(String kitname : kits) {
            		kitnum += WkKit.getPlayerData().getMailKitNum(playername, kitname);
            	}
            	return kitnum + "";
        	}
        	return "0";
        }
        // 指定礼包的信息
        String[] s = params.split("_");
        // 领取次数
        if(s.length == 2 && s[0].equalsIgnoreCase("times")) {
        	String kitname = s[1];
        	Kit kit = Kit.getKit(kitname);
        	if(kit != null) {
        		if(kit.getTimes() != null) return kit.getTimes() + "";
        		else return "-1";
        	}
        	return null;
        }
        // 指定礼包的冷却时间
        if(s.length == 2 && s[0].equalsIgnoreCase("delay")) {
        	String kitname = s[1];
        	Kit kit = Kit.getKit(kitname);
        	if(kit != null) {
        		if(kit.getDelay() != null) return kit.getDelay() + "";
        		else return "0";
        	}
        	return null;
        }
        // 指定礼包的展示名
        if(s.length == 2 && s[0].equalsIgnoreCase("name")) {
        	String kitname = s[1];
        	Kit kit = Kit.getKit(kitname);
        	if(kit != null) return kit.getDisplayName();
        	return null;
        }
        // 指定礼包所需要的权限
        if(s.length == 2 && s[0].equalsIgnoreCase("permission")) {
        	String kitname = s[1];
        	Kit kit = Kit.getKit(kitname);
        	if(kit != null) {
        		if(kit.getPermission() != null) return kit.getPermission();
        	}
        	return "None";
        }
        // 获得指定礼包距离下次刷新的大致时间
        if(s.length == 2 && s[0].equalsIgnoreCase("tonext")) {
        	String kitname = s[1];
        	Kit kit = Kit.getKit(kitname);
        	if(kit != null) {
        		if(kit.getDocron() != null) {
        			return CronManager.getDescribeToNext(kit.getDocron());
        		}
        	}
        	return "None";
        }
        // 获得指定礼包的下次刷新日期
        if(s.length == 2 && s[0].equalsIgnoreCase("next")) {
        	String kitname = s[1];
        	Kit kit = Kit.getKit(kitname);
        	if(kit != null) {
        		if(kit.getDocron() != null) {
        			Date date = CronManager.getNextExecution(kit.getDocron());
        			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        		}
        	}
        	return "None";
        }
        return null; // Placeholder is unknown by the Expansion
    }
}
