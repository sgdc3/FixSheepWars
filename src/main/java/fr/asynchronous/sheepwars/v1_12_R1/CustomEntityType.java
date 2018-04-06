package fr.asynchronous.sheepwars.v1_12_R1;

import fr.asynchronous.sheepwars.a.ai.aiE;
import fr.asynchronous.sheepwars.a.ai.aiG;
import fr.asynchronous.sheepwars.a.aj.ajC;
import fr.asynchronous.sheepwars.v1_12_R1.entity.CustomSheep;
import fr.asynchronous.sheepwars.v1_12_R1.entity.EntityMeteor;
import fr.asynchronous.sheepwars.v1_12_R1.entity.firework.FireworkSpawner;
import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.BiomeBase.BiomeMeta;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    private static Object getStaticField(Class<EntityTypes> clazz, String fieldName) throws Exception {
        return aiG.getField(clazz, true, fieldName).get(null);
    }

    @SuppressWarnings("unchecked")
    private static void a(int paramInt, String paramString1, Class<?> paramClass) {
        MinecraftKey localMinecraftKey = new MinecraftKey(paramString1);
        try {
            RegistryMaterials<MinecraftKey, Class<?>> instance = (RegistryMaterials<MinecraftKey, Class<?>>) RegistryMaterials.class.newInstance();
            Method method = aiG.getMethod(RegistryMaterials.class, "a", Integer.class, MinecraftKey.class, Class.class);
            method.setAccessible(true);
            method.invoke(instance, paramInt, localMinecraftKey, paramClass);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public static class GlobalMethods implements ajC {

        @SuppressWarnings("unchecked")
        @Override
        public void registerEntities() {
            System.out.println("Registering entities!");
            CustomEntityType[] customEntities = CustomEntityType.values();
            for (CustomEntityType entity : customEntities) {
                CustomEntityType.a(entity.getID(), entity.getName().toLowerCase(), entity.getCustomClass());
            }

            BiomeBase[] biomes;
            try {
                biomes = new BiomeBase[]{Biomes.a, Biomes.c, Biomes.d, Biomes.e, Biomes.f, Biomes.g, Biomes.h, Biomes.i, Biomes.m, Biomes.n, Biomes.o, Biomes.p, Biomes.q, Biomes.r, Biomes.s, Biomes.t, Biomes.u, Biomes.w, Biomes.x, Biomes.y, Biomes.z, Biomes.A, Biomes.B, Biomes.C, Biomes.D, Biomes.E, Biomes.F, Biomes.G, Biomes.H, Biomes.I, Biomes.J, Biomes.K, Biomes.L, Biomes.M, Biomes.N, Biomes.O};
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            for (BiomeBase biome : biomes) {
                if (biome.equals(BiomeBase.getBiome(8))) {
                    continue;
                }
                String[] fields = new String[]{"u", "v", "w", "t"};
                for (String field : fields) {
                    try {
                        Field list = BiomeBase.class.getDeclaredField(field);
                        list.setAccessible(true);
                        List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biome);
                        for (BiomeMeta meta : mobList) {
                            for (CustomEntityType entity : customEntities) {
                                if (entity.getNMSClass().equals(meta.b)) {
                                    if (!(entity.getCustomClass().isAssignableFrom(EntityInsentient.class))) {
                                        continue;
                                    }
                                    meta.b = (Class<? extends EntityInsentient>) entity.getCustomClass();
                                }
                            }
                        }
                    } catch (Exception var19) {
                        var19.printStackTrace();
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void unregisterEntities() {
            CustomEntityType[] customEntities = CustomEntityType.values();
            for (CustomEntityType entity : customEntities) {
                try {
                    ((List) CustomEntityType.getStaticField(EntityTypes.class, "g")).remove(entity.getID());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (CustomEntityType entity : customEntities) {
                try {
                    CustomEntityType.a(entity.getID(), entity.getName().toLowerCase(), entity.getNMSClass());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            BiomeBase[] biomes;
            try {
                biomes = new BiomeBase[]{Biomes.a, Biomes.c, Biomes.d, Biomes.e, Biomes.f, Biomes.g, Biomes.h, Biomes.i, Biomes.m, Biomes.n, Biomes.o, Biomes.p, Biomes.q, Biomes.r, Biomes.s, Biomes.t, Biomes.u, Biomes.w, Biomes.x, Biomes.y, Biomes.z, Biomes.A, Biomes.B, Biomes.C, Biomes.D, Biomes.E, Biomes.F, Biomes.G, Biomes.H, Biomes.I, Biomes.J, Biomes.K, Biomes.L, Biomes.M, Biomes.N, Biomes.O};
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            for (BiomeBase biomeBase : biomes) {
                if (biomeBase == BiomeBase.getBiome(8)) {
                    continue;
                }
                String[] fields = new String[]{"u", "v", "w", "t"};
                for (String field : fields) {
                    try {
                        Field list = BiomeBase.class.getDeclaredField(field);
                        list.setAccessible(true);
                        List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
                        for (BiomeMeta meta : mobList) {
                            for (CustomEntityType entity : customEntities) {
                                if (entity.getCustomClass().equals(meta.b)) {
                                    if (!(entity.getNMSClass().isAssignableFrom(EntityInsentient.class))) {
                                        continue;
                                    }
                                    meta.b = (Class<? extends EntityInsentient>) entity.getNMSClass();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

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
