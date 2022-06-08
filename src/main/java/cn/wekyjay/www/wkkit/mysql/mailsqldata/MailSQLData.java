package cn.wekyjay.www.wkkit.mysql.mailsqldata;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.wekyjay.www.wkkit.mysql.MySQLManager;

public class MailSQLData {
	public static void createTable(){
		String cmd = SQLCommand.CREATE_TABLE.commandToString();
		try {
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(cmd);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {
			System.out.println("§c邮箱数据表创建失败");
			e.printStackTrace();
		}
	}
	
	public void insertData(String playername, String kitname, int num) {
		try {
			PreparedStatement ps;
			String s = SQLCommand.ADD_DATA.commandToString();
			ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setInt(1, 0);  // id
			ps.setString(2, playername);
			ps.setString(3, kitname);
			ps.setInt(4, num);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteData(String playername,String kitname) {
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
	public int findKitNum(String playername,String kitname) {
		try {
			String s = SQLCommand.SELECT_DATA.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, playername);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				if(rs.getString("kitname").equals(kitname)) {
					return rs.getInt("num");
				}
			}
		} catch (SQLException e) {}
		return 0;
	}
	
	public void update_Num(String playername,String kitname, int value) {
		try {
			String s = SQLCommand.UPDATE_NUM_DATA.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setInt(1, value);
			ps.setString(2, playername);
			ps.setString(3, kitname);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {}
	}
}
