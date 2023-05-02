package cn.wekyjay.www.wkkit.mysql.cdksqldata;

import cn.wekyjay.www.wkkit.mysql.MySQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CdkSQLData {
	public static void createTable(){
		String cmd = SQLCommand.CREATE_TABLE.commandToString();
		try {
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(cmd);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {
			System.out.println("§cCDK数据表创建失败");
			e.printStackTrace();
		}
	}
	// 插入数据
	public void insertData(String CDK, String kits, String date, String mark) {
		try {
			PreparedStatement ps;
			String s = SQLCommand.ADD_DATA.commandToString();
			ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setInt(1, 0);  // id
			ps.setString(2, CDK);
			ps.setString(3, kits);
			ps.setString(4, date);
			ps.setString(5, "Available");
			ps.setString(6, mark);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// 删除数据
	public void deleteData(String CDK) {
		try {
			PreparedStatement ps;
			String s = SQLCommand.DELETE_DATA.commandToString();
			ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, CDK);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<String> findCDK(String mark) {
		List<String> list = new ArrayList<String>();
		try {
			String s = SQLCommand.SELECT_MARK.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, mark);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				list.add(rs.getString("cdk"));
			}
		} catch (SQLException e) {}
		return list;
	}
	public String findKits(String cdk) {
		try {
			String s = SQLCommand.SELECT_CDK.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, cdk);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				if(rs.getString("cdk").equals(cdk))return rs.getString("kits");
			}
		} catch (SQLException e) {}
		return null;
	}
	public String findDate(String cdk) {
		try {
			String s = SQLCommand.SELECT_CDK.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, cdk);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				if(rs.getString("cdk").equals(cdk))return rs.getString("date");
			}
		} catch (SQLException e) {}
		return null;
	}
	public String findStatus(String cdk) {
		try {
			String s = SQLCommand.SELECT_CDK.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, cdk);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				if(rs.getString("cdk").equals(cdk))return rs.getString("status");
			}
		} catch (SQLException e) {}
		return null;
	}
	public String findMark(String cdk) {
		try {
			String s = SQLCommand.SELECT_CDK.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, cdk);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				if(rs.getString("cdk").equals(cdk))return rs.getString("mark");
			}
		} catch (SQLException e) {}
		return null;
	}
	/**
	 * 判断指定mark是否存在
	 * @param mark
	 * @return
	 */
	public Boolean containMark(String mark) {
		try {
			String s = SQLCommand.SELECT_MARK.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, mark);
			ResultSet rs = ps.executeQuery();
			if(rs.next())return true;
		} catch (SQLException e) {}
		return false;
	}
	public Boolean containCDK(String CDK) {
		try {
			String s = SQLCommand.SELECT_CDK.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, CDK);
			ResultSet rs = ps.executeQuery();
			if(rs.next())return true;
		} catch (SQLException e) {}
		return false;
	}
	
	/**
	 * 更新status
	 * @param playername
	 * @param CDK
	 */
	public void update_Status(String playername,String CDK) {
		try {
			String s = SQLCommand.UPDATE_STATUS_DATA.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, playername);
			ps.setString(2, CDK);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {}
	}
	/**
	 * 更新mark
	 * @param newmark
	 * @param mark
	 */
	public void update_Mark(String newmark,String mark) {
		try {
			String s = SQLCommand.UPDATE_MARK_DATA.commandToString();
			PreparedStatement ps = MySQLManager.get().getConnection().prepareStatement(s);
			ps.setString(1, newmark);
			ps.setString(2, mark);
			MySQLManager.get().doCommand(ps);
		} catch (SQLException e) {}
	}
}
