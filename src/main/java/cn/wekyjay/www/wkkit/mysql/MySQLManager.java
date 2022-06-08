package cn.wekyjay.www.wkkit.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLTimeoutException;

import org.bukkit.ChatColor;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.mysql.cdksqldata.CdkSQLData;
import cn.wekyjay.www.wkkit.mysql.mailsqldata.MailSQLData;
import cn.wekyjay.www.wkkit.mysql.playersqldata.PlayerSQLData;

public class MySQLManager {
	private String ip;
	private String databaseName;
	private String userName;
	private String userPassword;
	private Connection connection;
	private String port;
	public static MySQLManager instance = null;

	
	public static MySQLManager get() {
        return instance == null ? instance = new MySQLManager() : instance;
    }
	

	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * 启动数据库
	 */
	public void enableMySQL()
	{
		WkKit.getWkKit().getLogger().info(LangConfigLoader.getString("MYSQL_CONNECTING"));
		ip = WkKit.getWkKit().getConfig().getString("MySQL.ip");
		databaseName = WkKit.getWkKit().getConfig().getString("MySQL.databasename");
		userName = WkKit.getWkKit().getConfig().getString("MySQL.username");
		userPassword = WkKit.getWkKit().getConfig().getString("MySQL.password");
		port = WkKit.getWkKit().getConfig().getString("MySQL.port");
		// 连接数据库
		connectMySQL();
		// 创建表
		MailSQLData.createTable();
		PlayerSQLData.createTable();
		CdkSQLData.createTable();
	}
	
	/**
	 * 连接数据库
	 */
	private void connectMySQL(){
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?autoReconnect=true", userName, userPassword);
			WkKit.getWkKit().getLogger().info(LangConfigLoader.getString("MYSQL_CONNECT_SUCCESS"));
		}catch(SQLTimeoutException e1) {
			WkKit.getWkKit().getLogger().severe(LangConfigLoader.getString("MYSQL_CONECTTIMEOUT"));
			WkKit.getWkKit().getConfig().set("MySQL.Enable",false);
			WkKit.getWkKit().saveConfig();
		}catch (SQLException e) {
			WkKit.getWkKit().getLogger().severe(LangConfigLoader.getString("MYSQL_CONECTFAILED"));
			WkKit.getWkKit().getConfig().set("MySQL.Enable",false);
			WkKit.getWkKit().saveConfig();
		}
	}
	/**
	 * 执行MySQL命令
	 * @param ps
	 */
	public void doCommand(PreparedStatement ps){
		try {
			ps.executeUpdate();
		} catch(SQLIntegrityConstraintViolationException sql) {
			WkKit.getWkKit().getLogger().warning(ChatColor.YELLOW + LangConfigLoader.getString("MYSQL_DATEEXISTS"));
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭数据库
	 */
	public void shutdown() {
		try {
			connection.close();
			WkKit.getWkKit().getLogger().info(LangConfigLoader.getString("MYSQL_SHUTDOWN"));
		} catch (SQLException e) {
			WkKit.getWkKit().getLogger().severe(LangConfigLoader.getString("MYSQL_SHUTDOWN_FAILED"));
			e.printStackTrace();
		}
	}
	
	
}
