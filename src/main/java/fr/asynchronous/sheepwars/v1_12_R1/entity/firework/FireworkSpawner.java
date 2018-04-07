package fr.asynchronous.sheepwars.v1_12_R1.entity.firework;

import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerEntityStatus;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;
import java.util.UUID;

public class FireworkSpawner {

    public static void spawn(Location location, FireworkEffect effect, Player... players) {
        if (location == null || effect == null || players == null || players.length == 0) {
            return;
        }

        //Generate some basic info
        int entityId = FireworkSpawner.getNextEntityId();
        UUID entityUuid = UUID.randomUUID();

        //Spawn firework entity
        WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity();
        spawnPacket.setEntityID(entityId);
        spawnPacket.setUniqueId(entityUuid);
        spawnPacket.setType(76);
        spawnPacket.setObjectData(0);
        spawnPacket.setX(location.getX());
        spawnPacket.setY(location.getY());
        spawnPacket.setZ(location.getZ());
        spawnPacket.setYaw(location.getYaw());
        spawnPacket.setPitch(location.getPitch());
        spawnPacket.setOptionalSpeedX(0);
        spawnPacket.setOptionalSpeedY(0);
        spawnPacket.setOptionalSpeedZ(0);
        for (Player player : players) {
            spawnPacket.sendPacket(player);
        }

        //From here we put things inside a try-catch to make sure the player always receives a destroy packet (otherwise their client will crash)
        try {
            //Send firework meta
            ItemStack item = new ItemStack(Material.FIREWORK);
            FireworkMeta meta = (FireworkMeta) item.getItemMeta();
            meta.addEffect(effect);
            item.setItemMeta(meta);

            WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata();
            metadataPacket.setEntityID(entityId);

            WrappedDataWatcher metadata = new WrappedDataWatcher();
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0);
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(1, WrappedDataWatcher.Registry.get(Integer.class)), 300);
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.get(String.class)), "");
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), false);
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(4, WrappedDataWatcher.Registry.get(Boolean.class)), false);
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), false);
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(6, WrappedDataWatcher.Registry.getItemStackSerializer(false)), item);
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(7, WrappedDataWatcher.Registry.get(Integer.class)), 0);
            for (Player player : players) {
                metadataPacket.sendPacket(player);
            }

            //Play explosion effect
            WrapperPlayServerEntityStatus statusPacket = new WrapperPlayServerEntityStatus();
            statusPacket.setEntityID(entityId);
            statusPacket.setEntityStatus((byte) 17);
            for (Player player : players) {
                statusPacket.sendPacket(player);
            }
        } finally {
            //Send destroy packet (very important, otherwise the client will crash)
            WrapperPlayServerEntityDestroy destroyPacket = new WrapperPlayServerEntityDestroy();
            destroyPacket.setEntityIds(new int[]{entityId});
            for (Player player : players) {
                destroyPacket.sendPacket(player);
            }
        }
    }

    public static void spawn(Location location, FireworkEffect effect, List<Player> players) {
        FireworkSpawner.spawn(location, effect, players.toArray(new Player[players.size()]));
    }

    /**
     * Utils
     */
    private static int currentEntityId = Integer.MAX_VALUE;

    private static int getNextEntityId() {
        return FireworkSpawner.currentEntityId--;
    }
}
