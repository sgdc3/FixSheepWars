package it.feargames.fixsheepwars;

import fr.asynchronous.sheepwars.a.UltimateSheepWarsPlugin;
import fr.asynchronous.sheepwars.a.ai.aiC;
import fr.asynchronous.sheepwars.a.ai.aiG;
import fr.asynchronous.sheepwars.a.ai.aiI;
import me.yamakaja.runtimetransformer.annotation.Inject;
import me.yamakaja.runtimetransformer.annotation.InjectionType;
import me.yamakaja.runtimetransformer.annotation.Transform;
import org.bukkit.Bukkit;

import java.io.File;

@Transform(UltimateSheepWarsPlugin.class)
public class SheepWarsPluginTransformer extends UltimateSheepWarsPlugin {

    @Inject(InjectionType.OVERRIDE)
    public void onLoad() {
        System.out.println("INJECTED > Loading the plugin");
        try {
            Bukkit.unloadWorld("world", true);
            this.getLogger().info("Loading directories...");
            File worldContainer = this.getServer().getWorldContainer();
            File worldFolder = new File(worldContainer, "world");
            File copyFolder = new File(worldContainer, "sheepwars-backup");
            if (copyFolder.exists()) {
                this.getLogger().info("World is reseting...");
                aiG.getClass("RegionFileCache", aiG.PackageType.MINECRAFT_SERVER).getMethod("a").invoke(null);
                aiC.delete(worldFolder);
                aiC.copyFolder(copyFolder, worldFolder);
                this.getLogger().info("World reset!");
            } else {
                this.getLogger().info("Don't find the world backup save, creating it...");
                aiC.copyFolder(worldFolder, copyFolder);
                this.getLogger().info("Backup save created!");
            }
        } catch (Throwable var5) {
            this.getLogger().severe("*** An error occured when reseting the map. Please contact the developer. ***");
            aiI.registerException(var5, false);
        }
    }

}
