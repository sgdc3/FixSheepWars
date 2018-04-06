package it.feargames.fixsheepwars;

import fr.asynchronous.sheepwars.a.ai.aiI;
import me.yamakaja.runtimetransformer.annotation.Inject;
import me.yamakaja.runtimetransformer.annotation.InjectionType;
import me.yamakaja.runtimetransformer.annotation.Transform;

@Transform(aiI.class)
public class ServiceTransformer extends aiI {

    @Inject(InjectionType.INSERT)
    public static void registerException(Exception ex, Boolean cast) {
        ex.printStackTrace();
    }

}
