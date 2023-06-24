package cn.wekyjay.www.wkkit.tool;

import cn.wekyjay.www.wkkit.WkKit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class MessageManager {
    /**
     * 后台打印消息，颜色代码 - &
     * @param text
     */
    public static void info(String text) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    }

    /**
     * 后台打印警告消息，颜色代码 - &
     * @param text
     */
    public  static void warning(String text) {
        Bukkit.getLogger().warning(ChatColor.translateAlternateColorCodes('&', text));
    }
    /**
     * 后台打印严重错误消息，颜色代码 - &
     * @param text
     */
    public  static void severe(String text) {
        Bukkit.getLogger().severe(ChatColor.translateAlternateColorCodes('&', text));
    }
    /**
     * 给指定玩家发送消息
     * @param msg
     * @param player
     */
    public static void sendMessage(String msg, Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
    /**
     * 给控制台发送消息
     * @param msg
     */
    public static void sendMessage(String msg) {
        WkKit.getWkKit().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
    /**
     * 给控制台发送带前缀的消息
     * @param msg
     */
    public static void sendMessageWithPrefix(String msg) {
        WkKit.getWkKit().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', WKTool.ofTextWithPrefix(msg)));
    }
    /**
     * 给控制台发送List<String>类型的消息
     * @param msgList
     */
    public static void sendMessage(List<String> msgList) {
        msgList.forEach(msg->{
            WkKit.getWkKit().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        });
    }
    /**
     * 给控制台发送List<String>类型的消息
     * @param msgList
     */
    public static void sendMessageWithPrefix(List<String> msgList, CommandSender sender) {
        msgList.forEach(msg->{
            sender.sendMessage(WKTool.ofTextWithPrefix(msg));
        });
    }
    public static void sendMessageWithPrefix(List<String> msgList, Collection<? extends Player> collection) {
        collection.forEach(player->{
            msgList.forEach(msg->{
                player.sendMessage(WKTool.ofTextWithPrefix(msg));
            });
        });

    }
    /**
     * 给列表内的玩家发送消息
     * @param msg
     */
    public static void sendMessage(String msg,Collection<? extends Player> collection) {
        collection.forEach(player->{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        });
    }

}
