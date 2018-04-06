package fr.asynchronous.sheepwars.v1_12_R1;

import fr.asynchronous.sheepwars.a.ac.acG;
import fr.asynchronous.sheepwars.a.ac.acH;
import fr.asynchronous.sheepwars.a.aj.ajF;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ParticleSpawner implements ajF {

    @Override
    public void playParticles(acG particle, Location location, Float fx, Float fy, Float fz, int amount, Float particleData, int... list) {
        ArrayList<OfflinePlayer> players = new ArrayList<>(acH.getParticlePlayers());
        if (!players.isEmpty()) {
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.a(particle.getId()), true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), fx, fy, fz, particleData, amount, list);
            for (OfflinePlayer player : players) {
                if (player.isOnline() && player instanceof CraftPlayer) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }

    }

    @Override
    public void playParticles(Player player, acG particle, Location location, Float fx, Float fy, Float fz, int amount, Float particleData, int... list) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.a(particle.getId()), true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), fx, fy, fz, particleData, amount, list);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
