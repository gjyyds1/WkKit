package cn.wekyjay.www.wkkit.hook;

import cn.wekyjay.www.wkkit.WkKit;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MythicMobsHooker {
    public static MythicMobsHooker mythicMobsHooker;
    public MythicMobsHooker(){
        WkKit.getWkKit().getLogger().info("§7已成功与 §6MythicMobs §7挂钩服务.");
    }

    /**
     * 获取MM怪的Hooker
     * @return MythicMobsHooker
     */
    public static MythicMobsHooker getMythicMobs(){
        return mythicMobsHooker == null?mythicMobsHooker = new MythicMobsHooker():mythicMobsHooker;
    }

    /**
     * 指定MM怪名称在玩家Location生成Mob
     * @param player 玩家
     * @param mobName 怪物名称
     * @return 布尔值(是否生成成功)
     */
    public boolean spawnMob(Player player, String mobName){

        MythicMob mob = MythicMobs.inst().getMobManager().getMythicMob(mobName);
        Location spawnLocation = player.getLocation();
        if(mob != null){
            // 生成Mob
            ActiveMob activeMob = mob.spawn(BukkitAdapter.adapt(spawnLocation),1);
            // 将mob转化为bukkit生物
            Entity entity = activeMob.getEntity().getBukkitEntity();
            return true;
        }
        return false;
    }
}
