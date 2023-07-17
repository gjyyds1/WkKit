package cn.wekyjay.www.wkkit.tool;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

public class ChackPluginVersion implements Listener,Runnable{
	WkKit wk = WkKit.getWkKit();
	File file = null;
	YamlConfiguration yaml = null;

	public ChackPluginVersion(){
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			URL url = new URL("https://forum.wekyjay.cn/version/wkkit.yml");
			file = File.createTempFile("tmp", null);
			URLConnection urlConn = null;
			urlConn = url.openConnection();
			is = urlConn.getInputStream();
			fos = new FileOutputStream(file);
			byte[] buffer = new byte[4096];
			int length;
			while ((length = is.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}

			yaml = YamlConfiguration.loadConfiguration(file);

		} catch (IOException e) {
			MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_FAILED"));
			return;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}

	}
	public String getLatestVersion() {
		return yaml.getString("Versions.Latest_Version");
	}
	@Override
	public void run() {
        MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_IN"));
		String lver = getLatestVersion();
        if(lver == null) {return;}
        if(compareVersionOrder(lver,wk.getDescription().getVersion()) > 0) { //判断版本是否落后
			// 判断操作语言
			Locale locale = Locale.getDefault();
			if(locale.toString().equals(new Locale("zh","CN").toString())) {
				MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LINK") + yaml.getString("URL.DOWNLOAD_URL_CHINA"));
			}else{
				MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LINK") + yaml.getString("URL.DOWNLOAD_URL"));
			}
			// 提示最新版本
        	MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LATESTVERSION") + lver + " "
					+ LangConfigLoader.getString("PLUGIN_CHACKUPDATE_CURRENTVERSION") + wk.getDescription().getVersion()  + " "
					+ LangConfigLoader.getString("PLUGIN_CHACKUPDATE_BETAVERSION") + yaml.getString("Versions.Beta_Version")
			);

			// 提示落后版本
			int d_value = getVersionOrder(lver) - getVersionOrder(wk.getDescription().getVersion());
			MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_BACKWARD").replaceAll("[{]num[}]",d_value+""));
        }else {
        	MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_ED"));
        }
	}

	/**
	 * 获取指定版本的版本Order，如果没有找到则为null;
	 * @param version
	 * @return
	 */
	public Integer getVersionOrder(String version){
		List<String> list = yaml.getStringList("Versions.Order");
		String result = null;
		for (String order : list) {
			String order_version = order.split(":")[0];
			String order_order = order.split(":")[1];
			if (version.equals(order_version) && order_order != null) {
				result = order_order;
			}
		}
		return Integer.parseInt(result);
	}

	/**
	 * 比较两个版本的大小：<br/>
	 * 如果v1大于v2，返回1。<br/>
	 * 如果v1等于v2，返回0。<br/>
	 * 如果v1小于v2，返回-1。
	 * @param v1
	 * @param v2
	 * @return
	 */
	public int compareVersionOrder(String v1,String v2){
		int int_v1 = getVersionOrder(v1),int_v2 = getVersionOrder(v2);
		if (int_v1 > int_v2) return 1;
		else if (int_v1 < int_v2) return -1;
		else return 0;
	}

	@EventHandler
	public void onOpLogin(PlayerJoinEvent e) {
		if(wk.getConfig().getBoolean("Setting.CheckUpdate")) {
			String lver = getLatestVersion();
			if(e.getPlayer().isOp()) {
		        if(lver == null) {return;}
				if(compareVersionOrder(lver,wk.getDescription().getVersion()) > 0) { //判断版本是否落后
					// 判断操作语言
					if(Locale.getDefault().toString().equals(new Locale("zh","CN").toString())) {
						e.getPlayer().sendMessage(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LINK") + yaml.getString("URL.DOWNLOAD_URL_CHINA"));

					}else{
						e.getPlayer().sendMessage(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LINK") + yaml.getString("URL.DOWNLOAD_URL"));
					}
					// 提示最新版本
					e.getPlayer().sendMessage(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LATESTVERSION") + lver + " " + LangConfigLoader.getString("PLUGIN_CHACKUPDATE_CURRENTVERSION") + wk.getDescription().getVersion());

					// 提示落后版本
					int d_value = getVersionOrder(lver) - getVersionOrder(wk.getDescription().getVersion());
					e.getPlayer().sendMessage(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_BACKWARD").replaceAll("[{]num[}]",d_value+""));
				}
			}
		}

	}
}
