package fr.asynchronous.sheepwars.v1_12_R1;

import fr.asynchronous.sheepwars.a.UltimateSheepWarsPlugin;
import fr.asynchronous.sheepwars.a.ac.acI;
import fr.asynchronous.sheepwars.a.aj.ajG;
import fr.asynchronous.sheepwars.v1_12_R1.entity.CustomSheep;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

public class SheepSpawner implements ajG {

    @Override
    public Sheep spawnSheepStatic(Location location, Player player, UltimateSheepWarsPlugin plugin) {
        Location playerLocation = player.getLocation();
        CustomSheep customSheep = new CustomSheep(((CraftWorld) location.getWorld()).getHandle(), player, plugin);
        customSheep.setPositionRotation(location.getX(), location.getY(), location.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
        customSheep.setHeadRotation(playerLocation.getYaw());
        ((CraftWorld) location.getWorld()).getHandle().addEntity(customSheep);
        return (Sheep) customSheep.getBukkitEntity();
    }

    @Override
    public Sheep spawnSheep(Location location, Player player, acI sheep, UltimateSheepWarsPlugin plugin) {
        Location playerLocation = player.getLocation();
        CustomSheep customSheep = new CustomSheep(((CraftWorld) location.getWorld()).getHandle(), player, sheep, plugin);
        customSheep.setPositionRotation(location.getX(), location.getY(), location.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
        customSheep.setHeadRotation(playerLocation.getYaw());
        ((CraftWorld) location.getWorld()).getHandle().addEntity(customSheep);
        return (Sheep) customSheep.getBukkitEntity();
    }

}
