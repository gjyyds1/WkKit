package cn.wekyjay.www.wkkit.tool;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import com.alibaba.druid.support.json.JSONParser;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ChackPluginVersion implements Listener,Runnable{
	WkKit wk = WkKit.getWkKit();
	File file = null;
	YamlConfiguration yaml = null;

	private Map<String, Object> resourceInfo = null;



	/**
	 * Modify of 1.2.5
	 * 1.2.5之后修改为Spigot Resource API
	 */
	public ChackPluginVersion(){
		resourceInfo = getResourceInfo("versions/latest");
	}


	public static Map<String,Object> getResourceInfo(String path){
		HttpURLConnection con = null;
		BufferedReader buffer = null;
		InputStream inputStream = null;


		//得到连接对象
		try {
			URL url = new URL("https://api.spiget.org/v2/resources/98415/" + path);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			//设置连接超时时间s
			con.setConnectTimeout(15000);
			//设置读取超时时间
			con.setReadTimeout(15000);
			//添加请求头
			con.setRequestProperty("Connection","keep-alive");
			//获取服务器返回的输入流
			inputStream = con.getInputStream();

			//得到响应码
			int responseCode = con.getResponseCode();
			// 如果响应码成功了则存储响应数据
			if (responseCode == HttpURLConnection.HTTP_OK){
				//读取输入流s
				buffer = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder respose = new StringBuilder();
				String line;
				while((line = buffer.readLine())!=null){
					respose.append(line);
				}
				return new JSONParser(respose.toString()).parseMap();
			}


		} catch (IOException e) {
			MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_FAILED"));
			return null;
		}finally {
			if (inputStream != null){
				try {
					inputStream.close();
				} catch (IOException ignored) {}
			}
			if (buffer != null){
				try {
					buffer.close();
				} catch (IOException ignored) {}
			}
		}
		MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_FAILED"));
		return null;
	}

	@Override
	public void run() {
        MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_IN"));
        if(resourceInfo == null) {return;}
		String lver = resourceInfo.get("name").toString();
        if(!wk.getDescription().getVersion().equals(lver)) { //判断版本是否与最新版本不同
			// 提示下载链接
			MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LINK") + "https://www.spigotmc.org/resources/%E2%9C%A8wkkit%E2%9C%A8-will-be-your-forever-gift-plugin-1-7-10-support-1-20-4%E2%9C%85.98415/");
			// 提示最新版本
        	MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LATESTVERSION") + lver + " "
					+ LangConfigLoader.getString("PLUGIN_CHACKUPDATE_CURRENTVERSION") + wk.getDescription().getVersion()
			);
        }else {
        	MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_ED"));
        }
	}


	@EventHandler
	public void onOpLogin(PlayerJoinEvent e) {
		if(wk.getConfig().getBoolean("Setting.CheckUpdate")) {
			String lver = resourceInfo.get("name").toString();
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
