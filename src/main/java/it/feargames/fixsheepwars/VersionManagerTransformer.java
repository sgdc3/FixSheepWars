package it.feargames.fixsheepwars;

import fr.asynchronous.sheepwars.a.UltimateSheepWarsPlugin;
import fr.asynchronous.sheepwars.a.ac.acM;
import fr.asynchronous.sheepwars.a.ai.aiG;
import fr.asynchronous.sheepwars.a.aj.*;
import me.yamakaja.runtimetransformer.annotation.Inject;
import me.yamakaja.runtimetransformer.annotation.InjectionType;
import me.yamakaja.runtimetransformer.annotation.Transform;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

@Transform(acM.class)
public class VersionManagerTransformer extends acM {

    // Just placeholders
    private Constructor<?> anvilGUIConstructor;
    private ajB ATitleUtils;
    private ajC ICustomEntityType;
    private ajD IEventHelper;
    private ajE INMSUtils;
    private ajF IParticleSpawner;
    private ajG ISheepSpawner;
    private ajH IWorldUtils;
    public VersionManagerTransformer(ajI version) {
        super(version);
    }

    @Inject(InjectionType.OVERRIDE)
    public void load() throws ReflectiveOperationException {
        this.ATitleUtils = this.loadModule("TitleUtils");
        this.ICustomEntityType = this.loadModule("CustomEntityType$GlobalMethods");
        this.IEventHelper = this.loadModule("EventHelper");
        this.INMSUtils = this.loadModule("NMSUtils");
        this.IParticleSpawner = this.loadModule("ParticleSpawner");
        this.ISheepSpawner = this.loadModule("SheepSpawner");
        this.IWorldUtils = this.loadModule("util.WorldUtils");
        this.anvilGUIConstructor = aiG.getConstructor(Class.forName("fr.asynchronous.sheepwars.v1_12_R1.AnvilGUI"), Player.class, UltimateSheepWarsPlugin.class, ajA.AnvilClickEventHandler.class);
        this.anvilGUIConstructor.setAccessible(true);
        Field field = this.getClass().getDeclaredField("version");
        field.setAccessible(true);
        field.set(this, ajI.v1_11_R1);
    }


    @Inject(InjectionType.OVERRIDE)
    @SuppressWarnings("unchecked")
    private <T> T loadModule(String name) throws ReflectiveOperationException {
        System.out.println("INJECTED > Loading module " + name);
        return (T) aiG.instantiateObject(Class.forName("fr.asynchronous.sheepwars.v1_12_R1." + name), new Object[0]);
    }

}
