package cn.wekyjay.www.wkkit.mysql.playersqldata;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.wekyjay.www.wkkit.mysql.MySQLManager;

public class PlayerSQLData {
	public static void createTable(){
		String cmd = SQLCommand.CREATE_TABLE.commandToString();
		try {
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(cmd);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {
			System.out.println("§c玩家数据表创建失败");
			e.printStackTrace();
		}
	}
	/**
	 * 向数据库添加数据
	 * @param kitname
	 * @param player
	 * @param data
	 * @param time
	 * @param sender
	 */
	public void insertData(String kitname, String playername,String data, int time) {
		try {
			PreparedStatement ps;
			String s = SQLCommand.ADD_DATA.commandToString();
			ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setInt(1, 0);
			ps.setString(2, playername);
			ps.setString(3, kitname);
			ps.setString(4, data);
			ps.setInt(5, time);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteData(String kitname, String playername) {
		try {
			PreparedStatement ps;
			String s = SQLCommand.DELETE_DATA.commandToString();
			ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, playername);
			ps.setString(2, kitname);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<String> findKitName(String playername) {
		List<String> list = new ArrayList<String>();
		try {
			String s = SQLCommand.SELECT_DATA.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, playername);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				list.add(rs.getString("kitname"));
			}
		} catch (SQLException e) {}
		return list;
	}
	public Boolean findPlayer(String playername) {
		try {
			String s = SQLCommand.SELECT_DATA.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, playername);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				if(rs.getString("player").equals(playername)) {
					return true;
				}
			}
		} catch (SQLException e) {}
		return false;
	}
	public String findKitData(String playername,String kitname) {
		try {
			String s = SQLCommand.SELECT_DATA.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, playername);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				if(rs.getString("kitname").equals(kitname)) {
					return rs.getString("data");
				}
			}
		} catch (SQLException e) {}
		return null;
	}
	public Integer findKitTime(String playername,String kitname) {
		try {
			String s = SQLCommand.SELECT_DATA.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, playername);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				if(rs.getString("kitname").equals(kitname)) {
					if(rs.getString("time") == null) return null;
					else return rs.getInt("time");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void update_Time_Data(String playername,String kitname, int value) {
		try {
			String s = SQLCommand.UPDATE_TIME_DATA.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setInt(1, value);
			ps.setString(2, playername);
			ps.setString(3, kitname);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {}
	}
	public void update_Data_Data(String playername,String kitname, String value) {
		try {
			String s = SQLCommand.UPDATE_DATA_DATA.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, value);
			ps.setString(2, playername);
			ps.setString(3, kitname);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {}
	}
}
