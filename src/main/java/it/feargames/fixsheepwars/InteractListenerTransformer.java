package it.feargames.fixsheepwars;

import fr.asynchronous.sheepwars.a.UltimateSheepWarsPlugin;
import fr.asynchronous.sheepwars.a.ab.bd.bdI;
import fr.asynchronous.sheepwars.a.ac.acK;
import me.yamakaja.runtimetransformer.annotation.Inject;
import me.yamakaja.runtimetransformer.annotation.InjectionType;
import me.yamakaja.runtimetransformer.annotation.Transform;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@Transform(bdI.class)
public class InteractListenerTransformer extends bdI {

    public InteractListenerTransformer(UltimateSheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    @Inject(InjectionType.INSERT)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        throw null;
    }

}
