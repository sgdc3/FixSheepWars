package fr.asynchronous.sheepwars.v1_12_R1.util;

import fr.asynchronous.sheepwars.a.aj.ajH;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class WorldUtils implements ajH {

    @Override
    public void createExplosion(Player player, Location location, float power) {
        this.createExplosion(player, location.getWorld(), location.getX(), location.getY(), location.getZ(), power, true, false);
    }

    @Override
    public void createExplosion(Player player, Location location, float power, boolean fire) {
        this.createExplosion(player, location.getWorld(), location.getX(), location.getY(), location.getZ(), power, true, fire);
    }

    @Override
    public void createExplosion(Player player, Location location, float power, boolean breakBlocks, boolean fire) {
        this.createExplosion(player, location.getWorld(), location.getX(), location.getY(), location.getZ(), power, breakBlocks, fire);
    }

    @Override
    public void createExplosion(Player player, World world, double x, double y, double z, float power, boolean breakBlocks, boolean fire) {
        ((CraftWorld)world).getHandle().createExplosion(((CraftPlayer)player).getHandle(), x, y, z, power, fire, breakBlocks);
    }

}
