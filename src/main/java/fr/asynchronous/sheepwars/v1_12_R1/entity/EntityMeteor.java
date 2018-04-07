package fr.asynchronous.sheepwars.v1_12_R1.entity;

import fr.asynchronous.sheepwars.a.ac.acG;
import fr.asynchronous.sheepwars.a.ac.acH;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class EntityMeteor extends EntityFireball {
    private final float SPEED_MODIFIER = 1.05F;
    private final float IMPACT_POWER = 3.0F;

    public EntityMeteor(World world, Player shooter) {
        super(world);
        if (shooter instanceof EntityLiving) {
            this.shooter = (EntityLiving) shooter;
        }
        this.motY = -50;
    }

    /*
    @Override
    public void B_() {
        if (this.inWater) {
            a((MovingObjectPosition) null);
        } else {
            super.as();
            this.motX *= SPEED_MODIFIER;
            this.motY *= SPEED_MODIFIER;
            this.motZ *= SPEED_MODIFIER;
            this.playParticles(acG.EXPLOSION_NORMAL, this.getBukkitEntity().getLocation(), 0.0F, 0.0F, 0.0F, 1, 0.1F);
            this.playParticles(acG.SMOKE_NORMAL, this.getBukkitEntity().getLocation(), 0.0F, 0.0F, 0.0F, 1, 0.2F);
        }
    }
    */

    @Override
    public void a(MovingObjectPosition movingobjectposition) {
        this.world.createExplosion(this.shooter, this.locX, this.locY, this.locZ, IMPACT_POWER, true, true);
        this.die();
    }

    public void playParticles(acG particle, Location location, Float fx, Float fy, Float fz, int amount, Float particleData, int... list) {
        ArrayList<OfflinePlayer> copy = new ArrayList<>(acH.getParticlePlayers());
        if (!copy.isEmpty()) {
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.a(particle.getId()), true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), fx, fy, fz, particleData, amount, list);

            for (OfflinePlayer player : copy) {
                if (player.isOnline() && player instanceof CraftPlayer) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }

    }
}
