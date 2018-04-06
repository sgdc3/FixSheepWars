package fr.asynchronous.sheepwars.v1_12_R1.entity.firework;

import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;

public class jDataWatcherItem<T> implements iNmsObject {

    private static Class<?> clazz;
    private static Constructor<?> constructor;
    private static Method method_flag_set;

    static {
        try {
            jDataWatcherItem.clazz = ProtocolUtils.getMinecraftClass("DataWatcher$Item");

            jDataWatcherItem.constructor = jDataWatcherItem.clazz.getDeclaredConstructor(ProtocolUtils.getMinecraftClass("DataWatcherObject"), Object.class);
            jDataWatcherItem.constructor.setAccessible(true);

            jDataWatcherItem.method_flag_set = jDataWatcherItem.clazz.getDeclaredMethod("a", boolean.class);
            jDataWatcherItem.method_flag_set.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final jDataWatcherObject<T> watcherObject;
    private T value;
    private boolean flag;

    public jDataWatcherItem(jDataWatcherObject<T> watcherObject, T value) {
        this.watcherObject = watcherObject;
        this.value = value;
        this.flag = true;
    }

    /**
     * WatcherObject
     */
    //Get
    public jDataWatcherObject<T> getWatcherObject() {
        return this.watcherObject;
    }

    /**
     * Value
     */
    //Set
    public void setValue(T value) {
        this.value = value;
    }

    //Get
    public T getValue() {
        return this.value;
    }

    /**
     * Flag
     */
    //Get
    public boolean getFlag() {
        return this.flag;
    }

    //Set
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * Build
     */
    @Override
    public Object build() throws Exception {
        Object val;

        if (this.value instanceof ItemStack) {
            val = Optional.of(ProtocolUtils.refl_itemAsNMSCopy((ItemStack) this.value));
        } else {
            val = this.value;
        }

        Object item = jDataWatcherItem.constructor.newInstance(this.watcherObject.build(), val);
        jDataWatcherItem.method_flag_set.invoke(item, this.flag);

        return item;
    }
}