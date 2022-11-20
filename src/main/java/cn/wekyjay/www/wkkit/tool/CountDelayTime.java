package cn.wekyjay.www.wkkit.tool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.kit.Kit;
/**
 * 用于操作配置内的日期
 * @author Administrator
 *
 */
public class CountDelayTime {
	// 调用主类实例
	WkKit wk = WkKit.getWkKit();
	
	private long delay;//礼包冷却时间
	private Calendar cala;
	private Player p;
	private Kit kit;
	private static List<CountDelayTime> ctlist = new ArrayList<CountDelayTime>();
	
	/**
	 * 使用前要确保该Kit有Delay参数
	 * @param p
	 * @param kit
	 */
	public CountDelayTime(Player p,Kit kit) {
		cala = null;
		this.kit = kit;
		this.p = p;
		delay = kit.getDelay();
		ctlist.add(this);
	}
	
	
	public Kit getKit() {
		return kit;
	}
	public Player getPlayer() {
		return p;
	}
	/**
	 * 判断玩家是否能领取该礼包
	 * @return
	 */
	public boolean isGet() {
		if(cala == null) {
			cala = Calendar.getInstance();
			return true;
		}
		// 否则
		long lc = cala.getTimeInMillis();//获取玩家上次时间毫秒数
		long ln = Calendar.getInstance().getTimeInMillis();//获取玩家当前时间毫秒数
		
		if((ln - lc) >= (delay * 1000)) {
			System.out.println((ln - lc));
			cala = Calendar.getInstance();
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * 返回玩家领取该礼包还剩余的大致时间
	 */
	public void whenGet() {
		long lc = cala.getTimeInMillis();//获取玩家配置时间毫秒数
		long ln = Calendar.getInstance().getTimeInMillis();//获取当前时间
		long newlc = lc + delay * 1000;//配置时间加上冷却时间之后的时间毫秒数
		
		Calendar newcalc = Calendar.getInstance();//创建一个时间容器
		newcalc.setTimeInMillis(newlc - ln);//将newlc的毫秒数转化为时间
		
		//获得可以领取礼包的时间差
		List<Integer> list = new ArrayList<Integer>();
		list.add(newcalc.get(Calendar.YEAR) - 1970);
		list.add((newcalc.get(Calendar.MONTH)) - 0);
		list.add(newcalc.get(Calendar.DATE) - 1);
		list.add(newcalc.get(Calendar.HOUR_OF_DAY) - 8);
		list.add(newcalc.get(Calendar.MINUTE) - 0);
		list.add(newcalc.get(Calendar.SECOND) - 0);
		
		//为时间差加上单位
		String lang = WkKit.getWkKit().getConfig().getString("Setting.Language");
		List<String> slist = new ArrayList<String>();
		if(!lang.equals("zh_CN")) {
			slist.add(list.get(0) + "Y");
			slist.add(list.get(1) + "M");
			slist.add(list.get(2) + "D");
			slist.add(list.get(3) + "H");
			slist.add(list.get(4) + "MIN");
			slist.add(list.get(5) + "S");
		}else {
			slist.add(list.get(0) + "年");
			slist.add(list.get(1) + "个月");
			slist.add(list.get(2) + "天");
			slist.add(list.get(3) + "小时");
			slist.add(list.get(4) + "分钟");
			slist.add(list.get(5) + "秒");
		}

		
		//判断是否为空
		String str = "";
		for(int i = 0; i <= 5; i++) {
			if(!(list.get(i) <= 0)) {
				str += slist.get(i);
				break;
			}
		}
		String lefttime = WKTool.replacePlaceholder("lefttime", str, LangConfigLoader.getStringWithPrefix("KIT_GET_LEFTTIME",ChatColor.RED));
		p.sendMessage(lefttime);
	}
	
	/**
	 * 获取可以领取礼包的具体日期
	 * @return String
	 */
	public String getData(){
		long lc = cala.getTimeInMillis();//获取玩家配置时间毫秒数
		long newlc = lc + delay * 1000;//配置时间加上冷却时间之后的时间毫秒数
		Calendar newcalc = Calendar.getInstance();//创建一个时间容器
		newcalc.setTimeInMillis(newlc);//将newlc的毫秒数转化为时间
		
		//获得可以领取礼包的时间
		List<Integer> list = new ArrayList<Integer>();
		list.add(newcalc.get(Calendar.YEAR));
		list.add(newcalc.get(Calendar.MONTH) + 1);
		list.add(newcalc.get(Calendar.DATE));
		list.add(newcalc.get(Calendar.HOUR_OF_DAY));
		list.add(newcalc.get(Calendar.MINUTE));
		list.add(newcalc.get(Calendar.SECOND));
		
		return String.join("/", list.get(0)+"",list.get(1)+"",list.get(2)+"",list.get(3)+"",list.get(4)+"",list.get(5)+"");
	}
	
	/**
	 * 获得当前的时间为格式“YY-MM-DD-HH-MM-SS”
	 * @return String
	 */
	public String getLocalTime(){
		Calendar caln = Calendar.getInstance();//玩家当前时间
		//获得当前的时间为格式“YY-MM-DD-HH-MM-SS”
		int year = caln.get(Calendar.YEAR);
		int mon = caln.get(Calendar.MONTH) + 1;
		int day = caln.get(Calendar.DATE);
		int hour = caln.get(Calendar.HOUR_OF_DAY);
		int min = caln.get(Calendar.MINUTE);
		int sec = caln.get(Calendar.SECOND);
		
		return String.join("-", year+"",mon+"",day+"",hour+"",min+"",sec+"");
	}
	
	/**
	 * 获得当前的时间为格式“YY-MM-DD-HH-MM-SS”
	 * @return String
	 */
	public static String toLocalTime(){
		Calendar caln = Calendar.getInstance();//玩家当前时间
		//获得当前的时间为格式“YY-MM-DD-HH-MM-SS”
		int year = caln.get(Calendar.YEAR);
		int mon = caln.get(Calendar.MONTH) + 1;
		int day = caln.get(Calendar.DATE);
		int hour = caln.get(Calendar.HOUR_OF_DAY);
		int min = caln.get(Calendar.MINUTE);
		int sec = caln.get(Calendar.SECOND);
		
		return String.join("-", year+"",mon+"",day+"",hour+"",min+"",sec+"");
	}
	
	/**
	 * 获取冷却储存的对象列表
	 * @return
	 */
	public static List<CountDelayTime> getDelaylist() {
		return ctlist;
	}
	public static CountDelayTime getDelayInstance(Player p, Kit kit) {
		for(CountDelayTime ct : getDelaylist()) {
			if(ct.getKit().equals(kit) && ct.getPlayer().equals(p)) {
				return ct;
			}
		}
		return null;
	}
}
