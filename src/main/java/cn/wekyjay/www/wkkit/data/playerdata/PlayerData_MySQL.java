package cn.wekyjay.www.wkkit.data.playerdata;

import java.util.List;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.mysql.mailsqldata.MailSQLData;
import cn.wekyjay.www.wkkit.mysql.playersqldata.PlayerSQLData;

public class PlayerData_MySQL implements PlayerData{

	@Override
	public void setKitToFile(String playername, String kitname, String data, int time) {
		new PlayerSQLData().insertData(kitname, playername, data, time);
	}

	@Override
	public List<String> getKits(String playername) {
		return new PlayerSQLData().findKitName(playername);
	}

	@Override
	public String getKitData(String playername, String kitname) {
		return new PlayerSQLData().findKitData(playername, kitname);
	}

	@Override
	public Integer getKitTime(String playername, String kitname) {
		return new PlayerSQLData().findKitTime(playername, kitname);
	}

	@Override
	public void delKitToFile(String playername, String kitname) {
		new PlayerSQLData().deleteData(kitname, playername);
	}

	@Override
	public void setKitData(String playername, String kitname, String value) {
		if(!contain_Kit(playername, kitname)) {//如果没有数据
			int time;
			if(Kit.getKit(kitname).getTimes() == null) time = -1;
			else time = Kit.getKit(kitname).getTimes();
			setKitToFile(playername, kitname, value, time);
		}else {
			new PlayerSQLData().update_Data_Data(playername, kitname, value);
		}
	}

	@Override
	public void setKitTime(String playername, String kitname, int value) {
		if(!contain_Kit(playername, kitname)) {//如果没有数据
			setKitToFile(playername, kitname, "2009-5-17-8-0-0", value);
		}else {
			new PlayerSQLData().update_Time_Data(playername, kitname, value);
		}

	}

	@Override
	public Boolean contain_Kit(String playername, String kitname) {
		List<String> list = WkKit.getPlayerData().getKits(playername);
		if(list.contains(kitname)) {return true;}
		return false;
	}

	@Override
	public Boolean contain_Kit(String playername) {
		return new PlayerSQLData().findPlayer(playername);
	}

	@Override
	public void setMailToFile(String playername, String kitname, int num) {
		new MailSQLData().insertData(playername, kitname, num);
		
	}

	@Override
	public void delMailToFile(String playername, String kitname) {
		new MailSQLData().deleteData(playername, kitname);
		
	}

	@Override
	public void setMailNum(String playername, String kitname, int num) {
		if(!contain_Mail(playername, kitname)) {//如果没有数据
			setMailToFile(playername, kitname, num);
		}else {
			new MailSQLData().update_Num(playername, kitname, num);
		}
	}

	@Override
	public List<String> getMailKits(String playername) {
		return new MailSQLData().findKitName(playername);
	}

	@Override
	public Boolean contain_Mail(String playername, String kitname) {
		List<String> list = WkKit.getPlayerData().getMailKits(playername);
		if(list.contains(kitname)) {return true;}
		return false;
	}

	@Override
	public Boolean contain_Mail(String playername) {
		return new MailSQLData().findPlayer(playername);
	}

	@Override
	public Integer getMailKitNum(String playername, String kitname) {
		return new MailSQLData().findKitNum(playername, kitname);
	}

}
