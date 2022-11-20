package cn.wekyjay.www.wkkit.tool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;

public class ChackPluginVersion implements Listener,Runnable{
	WkKit wk = WkKit.getWkKit();
	public String getLastestVersion() {
		String ver = null;
		try {
			URL url = new URL("https://www.wekyjay.cn/version/wkkit.txt");
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			ver = br.readLine();
			br.close();
			is.close();
		} catch (Exception e) {
			wk.getLogger().info(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_FAILED"));
		}
		return ver;
	}
	@Override
	public void run() {
        wk.getLogger().info(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_IN"));
		String lver = getLastestVersion();
        if(lver == null) {return;}
        if(!lver.equals(wk.getDescription().getVersion())) {
        	wk.getLogger().info(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LINK") + "§7https://www.spigotmc.org/resources/%E2%9C%A8wkkit%E2%9C%A8-will-be-your-forever-gift-plugin-1-7-10-support-1-19%E2%9C%85.98415/");
        	wk.getLogger().info(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LATESTVERSION") + lver + " " + LangConfigLoader.getString("PLUGIN_CHACKUPDATE_CURRENTVERSION") + wk.getDescription().getVersion());
        }else {
        	wk.getLogger().info(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_ED"));
        }
	}

	@EventHandler
	public void onOpLogin(PlayerJoinEvent e) {
		if(wk.getConfig().getBoolean("Setting.CheckUpdate")) {
			String lver = getLastestVersion();
			if(e.getPlayer().isOp()) {
		        if(lver == null) {return;}
		        if(!lver.equals(wk.getDescription().getVersion())) {
		        	e.getPlayer().sendMessage(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LINK") + "§7https://www.spigotmc.org/resources/%E2%9C%A8wkkit%E2%9C%A8-will-be-your-forever-gift-plugin-1-7-10-support-1-19%E2%9C%85.98415/");
		        }
			}
		}

	}
}
