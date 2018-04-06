package it.feargames.fixsheepwars;

import me.yamakaja.runtimetransformer.RuntimeTransformer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class FixSheepWars extends JavaPlugin implements Listener {

    public FixSheepWars() {
        getLogger().info("Injecting...");
        new RuntimeTransformer(
                VersionManagerTransformer.class,
                SheepWarsPluginTransformer.class,
                ServiceTransformer.class,
                InteractListenerTransformer.class
        );
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        getLogger().severe("ENTITY " + event.getEntity().toString());
    }

}
