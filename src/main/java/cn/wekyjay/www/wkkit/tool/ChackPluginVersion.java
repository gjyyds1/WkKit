package cn.wekyjay.www.wkkit.tool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import cn.wekyjay.www.wkkit.WkKit;

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
			wk.getLogger().info("§4检查更新失败，请检查您的网络！");
		}
		return ver;
	}
	@Override
	public void run() {
        wk.getLogger().info("§a正在进行检查更新...");
		String lver = getLastestVersion();
        if(lver == null) {return;}
        if(!lver.equals(wk.getDescription().getVersion())) {
        	wk.getLogger().info("§eWkKit有新版本发布啦！快来看看：§7https://www.mcbbs.net/thread-1282643-1-1.html");
        	wk.getLogger().info("最新版本："+lver + " 您的版本：" + wk.getDescription().getVersion());
        }else {
        	wk.getLogger().info("§a您的WkKit是最新版本！");
        }
	}

	@EventHandler
	public void onOpLogin(PlayerJoinEvent e) {
		if(wk.getConfig().getBoolean("Setting.CheckUpdate")) {
			String lver = getLastestVersion();
			if(e.getPlayer().isOp()) {
		        if(lver == null) {return;}
		        if(!lver.equals(wk.getDescription().getVersion())) {
		        	e.getPlayer().sendMessage("§eWkKit有新版本发布啦！快来看看：§7https://www.mcbbs.net/thread-1282643-1-1.html");
		        }
			}
		}

	}
}
