package cn.wekyjay.www.wkkit.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import cn.wekyjay.www.wkkit.WkKit;

public class CronManager {

  
    	//获取预定义的实例
		static CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
		//根据提供的预定义创建Cron解析器
		static CronParser parser = new CronParser(cronDefinition);
	
		/**
		 * 通过表达式获取下次执行的时间
		 * @param cron
		 * @return Date
		 */
	public static Date getNextExecution(String cron) {
		//获取当前时间类
		ZonedDateTime now = ZonedDateTime.now();
		//获取执行时间类
		ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cron));
		//获取下次执行的时间
		Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(now);
		//转成Date类
		Date date = Date.from(nextExecution.get().toInstant());
		return date;
	}
	
	/**
	 * 获取指定时间的下一次执行时间
	 * @param time
	 * @param cron
	 * @return
	 * @throws ParseException
	 */
	public static Date getNextExecution(String time,String cron){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date lastdate = null;
		try {
			lastdate = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ZonedDateTime ztime = ZonedDateTime.ofInstant(lastdate.toInstant(), ZoneId.systemDefault());
		//获取执行时间类
		ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cron));
		//获取下次执行的时间
		Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(ztime);
		//转成Date类
		Date date = Date.from(nextExecution.get().toInstant());
		return date;
	}
	
	/**
	 * 获取距离下次领取的时间描述
	 * @param time
	 * @param cron
	 * @return
	 */
	public static String getDescribeToNext(String cron) {
		//获取执行时间类
		ZonedDateTime now = ZonedDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
		ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cron));
		Optional<Duration> timeToNextExecution = executionTime.timeToNextExecution(now);
		// 获得持续时间
		Optional<Long> duration = timeToNextExecution.map(Duration::toMillis);
		Calendar newcalc = Calendar.getInstance();//创建一个时间容器
		newcalc.setTimeInMillis(duration.get());//将newlc的毫秒数转化为时间
		
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
		
		return str;
	}
	
	/**
	 * 判断是否超过了刷新时间
	 * @param shuttime
	 * @param cron
	 * @return
	 * @throws ParseException
	 */
	public static Boolean isExecuted(String shuttime,String cron) {
		Calendar cnow = Calendar.getInstance();
		Calendar cnext = Calendar.getInstance();
		cnext.setTime(getNextExecution(shuttime, cron));
		if(cnow.getTimeInMillis() >= cnext.getTimeInMillis()) {
			return true;
		}
		return false;
	}
	
}
