package cn.wekyjay.www.wkkit.handlerlist;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import cn.wekyjay.www.wkkit.kit.Kit;

public class PlayersReceiveKitEvent extends PlayerEvent implements Cancellable {
	private Kit kit;
	private String menuname = null;
	private boolean isCancelled = false;
	
	private ReceiveType receivetype;
    private static final HandlerList handlers = new HandlerList();
    
	public PlayersReceiveKitEvent(Player who,Kit kit, ReceiveType type) {
		super(who);
		this.kit = kit;
		this.receivetype = type;
	}
	public PlayersReceiveKitEvent(Player who,Kit kit,String menuname, ReceiveType type) {
		super(who);
		this.kit = kit;
		this.menuname = menuname;
		this.receivetype = type;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		isCancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Kit getKit() {
		return kit;
	}
	
	/**
	 * 返回领取礼包的菜单名
	 * @return
	 * @Nullable 从何处领取的菜单名称，有可能为NULL.
	 */
	public String getMenuname() {
		return menuname;
	}
	
	public ReceiveType getType() {
		return receivetype;
	}
	
    public static HandlerList getHandlerList() {// 事件类的「获取处理器」方法
        return handlers;
    }
    
	/**
	 * 回调方法
	 * @return
	 */
	public static PlayersReceiveKitEvent callEvent(Player player,Kit kit,ReceiveType type) {
	    PlayersReceiveKitEvent event = new PlayersReceiveKitEvent(player, kit, type);
	    Bukkit.getPluginManager().callEvent(event);
	    return event;
	}
	public static PlayersReceiveKitEvent callEvent(Player player,Kit kit,String value,ReceiveType type) {
	    PlayersReceiveKitEvent event = new PlayersReceiveKitEvent(player, kit, value, type);
	    Bukkit.getPluginManager().callEvent(event);
	    return event;
	}

}


