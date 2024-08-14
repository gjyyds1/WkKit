package cn.wekyjay.www.wkkit.listeners;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.tool.ChackPluginVersion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ChackPluginListener implements Listener {
    WkKit wk = WkKit.getWkKit();
    @EventHandler
    public void onOpLogin(PlayerJoinEvent e) {
        if(wk.getConfig().getBoolean("Setting.CheckUpdate")) {
            String lver = ChackPluginVersion.getResourceInfo().get("name").toString();
            if(e.getPlayer().isOp()) {
                if(lver == null) {return;}
                if(!wk.getDescription().getVersion().equals(lver)) { //判断版本是否与最新版本不同
                    // 提示下载链接
                    e.getPlayer().sendMessage(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LINK") + "https://www.spigotmc.org/resources/%E2%9C%A8wkkit%E2%9C%A8-will-be-your-forever-gift-plugin-1-7-10-support-1-20-4%E2%9C%85.98415/");

                    // 提示最新版本
                    e.getPlayer().sendMessage(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LATESTVERSION") + lver + " " + LangConfigLoader.getString("PLUGIN_CHACKUPDATE_CURRENTVERSION") + wk.getDescription().getVersion());
                }
            }
        }

    }
}
