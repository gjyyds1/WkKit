package cn.wekyjay.www.wkkit.config;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.kit.KitGroupManager;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitConfigLoader {
	private List<File> fileList = new ArrayList<>();
	private Map<String, FileConfiguration> fileConfigMap = new HashMap<>();
	private List<Kit> kits = new ArrayList<>();

	protected KitConfigLoader() {}

	public List<Kit> getKitsList() {
		return kits;
	}

	public List<File> getFileList() {
		return fileList;
	}

	public Map<String, FileConfiguration> getFileConfigMap() {
		return fileConfigMap;
	}

	/**
	 * 加载文件夹中的配置文件
	 */
	public void loadConfig() {
		String path = WkKit.getWkKit().getDataFolder().getAbsolutePath() + File.separator + "Kits";
		File[] ff = new File(path).listFiles();
		if (ff != null) {
			for (File fs : ff) {
				if (fs.isFile() && fs.getName().endsWith(".yml")) {
					fileList.add(fs);
					fileConfigMap.put(fs.getName(), YamlConfiguration.loadConfiguration(fs));
				}
			}
		} else {
			Bukkit.getLogger().warning("[WkKit] Kits 文件夹不存在或无法访问: " + path);
		}
		readKitConfig();
	}

	/**
	 * 返回存在该路径的文件名
	 * @param path
	 * @return
	 */
	public String getContainsFilename(String path) {
		for (File fl : fileList) {
			FileConfiguration config = fileConfigMap.get(fl.getName());
			if (config != null && config.contains(path)) {
				return fl.getName();
			}
		}
		return null;
	}

	/**
	 * 返回存在该路径的文件
	 * @param path
	 * @return
	 */
	public File getContainsFile(String path) {
		for (File fl : fileList) {
			FileConfiguration config = fileConfigMap.get(fl.getName());
			if (config != null && config.contains(path)) {
				return fl;
			}
		}
		return null;
	}

	/**
	 * 判断是否存在该路径
	 * @param path
	 * @return
	 */
	public Boolean contains(String path) {
		for (File fl : fileList) {
			FileConfiguration config = fileConfigMap.get(fl.getName());
			if (config != null && config.contains(path)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 文件名返回对象
	 * @param filename
	 * @return
	 */
	public FileConfiguration getConfig(String filename) {
		return fileConfigMap.get(filename);
	}

	/**
	 * 路径返回对象
	 * @param path
	 * @return
	 */
	public FileConfiguration getConfigWithPath(String path) {
		String filename = getContainsFilename(path);
		return fileConfigMap.get(filename);
	}

	/**
	 * 获得文件中的存在的礼包名
	 * @return List<String>
	 */
	public List<String> getKits() {
		List<String> list = new ArrayList<>();
		for (File fl : fileList) {
			FileConfiguration config = fileConfigMap.get(fl.getName());
			if (config != null) {
				Set<String> set = config.getKeys(false);
				for (String key : set) {
					if (!list.contains(key)) {
						list.add(key);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 判断是否存在重复的礼包，并且返回值。
	 * @return int
	 */
	public int checkRepeatKits() {
		List<String> list = new ArrayList<>();
		int num = 0;
		for (File fl : fileList) {
			FileConfiguration config = fileConfigMap.get(fl.getName());
			if (config != null) {
				Set<String> set = config.getKeys(false);
				for (String key : set) {
					if (!list.contains(key)) {
						list.add(key);
					} else {
						Bukkit.getLogger().warning(LangConfigLoader.getString("CONTAIN_REPEAT_KIT") + key);
						num++;
					}
				}
			}
		}
		return num;
	}

	/**
	 * 读取配置中存在的礼包,并检测有效性
	 */
	public void readKitConfig() {
		List<String> kitlist = this.getKits();
		int repeatnum = this.checkRepeatKits();
		// 查找重复的礼包
		if (repeatnum != 0) {
			Bukkit.getLogger().warning("[WkKit]" + repeatnum + "个礼包加载失败！");
		}

		for (String kitname : kitlist) {
			String displayName = this.getString(kitname + ".Name");
			String icon = this.getString(kitname + ".Icon");
			List<String> itemNBT = this.getStringList(kitname + ".Item");
			if (itemNBT == null) {
				Bukkit.getLogger().warning("[WkKit] 礼包 " + kitname + " 缺少 Item 配置项");
				continue;
			}
			ItemStack[] itemStack = new ItemStack[itemNBT.size()]; // 0-9 刚好十个
			int num = 0;
			for (String nbt : itemNBT) {
				NBTContainer c = new NBTContainer(nbt);
				itemStack[num] = NBTItem.convertNBTtoItem(c);
				num++;
			}
			new Kit(kitname, displayName, icon, itemStack);
		}
	}

	// 封装BukkitAPI的方法
	public String getString(String path) {
		if (this.contains(path)) {
			FileConfiguration config = getConfigWithPath(path);
			if (config != null) {
				return config.getString(path);
			}
		}
		return null;
	}

	public List<String> getStringList(String path) {
		FileConfiguration config = getConfigWithPath(path);
		if (config != null) {
			return config.getStringList(path);
		}
		return Collections.emptyList(); // 或者返回 null 根据你的需求
	}

	public int getInt(String path) {
		FileConfiguration config = getConfigWithPath(path);
		if (config != null) {
			return config.getInt(path);
		}
		return 0; // 或者返回默认值
	}

	public long getLong(String path) {
		FileConfiguration config = getConfigWithPath(path);
		if (config != null) {
			return config.getLong(path);
		}
		return 0L; // 或者返回默认值
	}

	public Boolean getBoolean(String path) {
		FileConfiguration config = getConfigWithPath(path);
		if (config != null) {
			return config.getBoolean(path);
		}
		return false; // 或者返回默认值
	}

	public void set(String path, Object value) {
		FileConfiguration config = getConfigWithPath(path);
		if (config != null) {
			config.set(path, value);
		}
	}

	/**
	 * 添加一段信息到指定路径，如果没有该路径就默认创建一个文件存放信息
	 * @param path
	 * @param value
	 * @param def
	 */
	public void set(String path, Object value, String filename) {
		FileConfiguration config = getConfigWithPath(path);
		if (config == null) {
			File file = new File(WkKit.getWkKit().getDataFolder().getAbsolutePath() + File.separator + "Kits" + File.separator + filename + ".yml");
			if (!file.exists()) {
				try {
					file.createNewFile();
					new KitGroupManager(filename);
					fileConfigMap.put(file.getName(), YamlConfiguration.loadConfiguration(file));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			config = getConfig(file.getName());
		}
		if (config != null) {
			config.set(path, value);
		}
	}

	public void save(String filename) throws IOException {
		String path = WkKit.getWkKit().getDataFolder().getAbsolutePath() + File.separator + "Kits" + File.separator + filename;
		FileConfiguration config = getConfig(filename);
		if (config != null) {
			config.save(new File(path));
		}
	}
}
