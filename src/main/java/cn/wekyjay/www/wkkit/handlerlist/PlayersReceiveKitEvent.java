package cn.wekyjay.www.wkkit.handlerlist;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import cn.wekyjay.www.wkkit.kit.Kit;

public class PlayersReceiveKitEvent extends PlayerEvent implements Cancellable {
	private Kit kit;
	private boolean isCancelled = false;
	private ReceiveType receivetype;
    private static final HandlerList handlers = new HandlerList();
	public PlayersReceiveKitEvent(Player who,Kit kit, ReceiveType type) {
		super(who);
		this.kit = kit;
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
	
	public ReceiveType getType() {
		return receivetype;
	}
	
    public static HandlerList getHandlerList() {// 事件类的「获取处理器」方法
        return handlers;
    }

}


