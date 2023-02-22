package cn.wekyjay.www.wkkit.hook;

import cn.wekyjay.www.wkkit.WkKit;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHooker {
    private static Economy econ = null;

    public VaultHooker(){
        if(this.setupEconomy()) WkKit.getWkKit().getLogger().info("§7已成功与 §6Vault §7挂钩 §6Economy §7服务.");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = WkKit.getWkKit().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static Economy getEcon() {
        return econ;
    }
}
