package cn.wekyjay.www.wkkit.edit;

import cn.wekyjay.www.wkkit.invholder.EditPlayerHolder;
import cn.wekyjay.www.wkkit.tool.WKTool;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EditPlayer {
    public static void selectPlayerGUI(Player player){
        Inventory inv = Bukkit.createInventory(new EditPlayerHolder(),6*9,"选择玩家");
        for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()){
            ItemStack head = WKTool.getPlayerHead(offlinePlayer);
            inv.addItem(head);
        }
        player.openInventory(inv);
    }

}
