package cn.wekyjay.www.wkkit.api;

import cn.wekyjay.www.wkkit.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayersKitRefreshEvent extends Event implements Cancellable {
    private Kit kit;
    private boolean isCancelled = false;
    private OfflinePlayer player;
    private static final HandlerList handlers = new HandlerList();

    public PlayersKitRefreshEvent(@NotNull OfflinePlayer who, Kit kit) {
        player = who;
        this.kit = kit;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {// 事件类的「获取处理器」方法
        return handlers;
    }

    public Kit getKit() {
        return kit;
    }

    public OfflinePlayer getOfflinePlayer() {
        return player;
    }

    /**
     * 回调方法
     * @return
     */
    public static PlayersKitRefreshEvent callEvent(OfflinePlayer player, Kit kit) {
        PlayersKitRefreshEvent event = new PlayersKitRefreshEvent(player, kit);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
