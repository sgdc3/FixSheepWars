package it.feargames.fixsheepwars;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import fr.asynchronous.sheepwars.a.ac.acJ;
import me.yamakaja.runtimetransformer.RuntimeTransformer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class FixSheepWars extends JavaPlugin implements Listener {

    public FixSheepWars() {
        getLogger().info("Injecting...");
        new RuntimeTransformer(
                VersionManagerTransformer.class,
                SheepWarsPluginTransformer.class,
                ServiceTransformer.class,
                InteractListenerTransformer.class,
                TeamTransformer.class
        );
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementCriterionGrantEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if(!acJ.isStep(acJ.IN_GAME) && (!event.isRightClick() || event.isShiftClick())) {
            event.setCancelled(true);
            ((Player)event.getWhoClicked()).updateInventory();
        }
    }

}
