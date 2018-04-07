package fr.asynchronous.sheepwars.v1_12_R1;

import fr.asynchronous.sheepwars.a.ai.aiE;
import fr.asynchronous.sheepwars.a.aj.ajC;
import fr.asynchronous.sheepwars.v1_12_R1.entity.CustomSheep;
import fr.asynchronous.sheepwars.v1_12_R1.entity.EntityMeteor;
import fr.asynchronous.sheepwars.v1_12_R1.entity.firework.FireworkSpawner;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public enum CustomEntityType {
    SHEEP("Sheep", 91, EntityType.SHEEP, EntitySheep.class, CustomSheep.class),
    METEOR("Fireball", 12, EntityType.FIREBALL, EntityFireball.class, EntityMeteor.class);

    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends Entity> nmsClass;
    private Class<? extends Entity> customClass;

    CustomEntityType(String name, int id, EntityType entityType, Class<? extends Entity> nmsClass, Class<? extends Entity> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Class<? extends Entity> getNMSClass() {
        return this.nmsClass;
    }

    public Class<? extends Entity> getCustomClass() {
        return this.customClass;
    }

    public static class GlobalMethods implements ajC {

        @SuppressWarnings("unchecked")
        @Override
        public void registerEntities() {
            for (CustomEntityType entity : CustomEntityType.values()) {
                MinecraftKey customKey = new MinecraftKey("Custom" + entity.getName());
                EntityTypes.b.a(entity.getID(), customKey, entity.getCustomClass());
                EntityTypes.d.add(customKey);
            }
        }

        @Override
        public void unregisterEntities() {
        }

        @Override
        public void spawnInstantExplodingFirework(Location location, FireworkEffect effect, ArrayList<Player> players) {
            FireworkSpawner.spawn(location, effect, players);
        }

        @Override
        public Fireball spawnFireball(Location location, Player sender) {
            EntityMeteor meteor = new EntityMeteor(((CraftWorld) location.getWorld()).getHandle(), sender);
            meteor.setPosition(location.getX() + (double) aiE.random(-20, 20), location.getY() + 40.0D, location.getZ() + (double) aiE.random(-20, 20));
            ((CraftWorld) location.getWorld()).getHandle().addEntity(meteor);
            return (Fireball) meteor.getBukkitEntity();
        }
    }

}
