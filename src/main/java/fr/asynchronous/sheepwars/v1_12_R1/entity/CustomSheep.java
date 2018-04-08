package fr.asynchronous.sheepwars.v1_12_R1.entity;

import com.google.common.collect.Sets;
import fr.asynchronous.sheepwars.a.UltimateSheepWarsPlugin;
import fr.asynchronous.sheepwars.a.ac.acG;
import fr.asynchronous.sheepwars.a.ac.acH;
import fr.asynchronous.sheepwars.a.ac.acI;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;

public class CustomSheep extends EntitySheep {

    private acI sheep;
    private Player player;
    //private World world;
    private boolean explosion;
    private boolean ground;
    private long defaultTicks;
    private long ticks;
    private boolean drop;
    //private int noclipDistance;
    private UltimateSheepWarsPlugin plugin;

    public CustomSheep(World world, Player player, UltimateSheepWarsPlugin plugin) {
        super(world);
        this.explosion = true;
        this.player = player;
        this.plugin = plugin;
        //this.world = ((CraftWorld) player.getWorld()).getHandle();
    }

    public CustomSheep(World world, Player player, acI sheep, UltimateSheepWarsPlugin plugin) {
        this(world, player, plugin);
        //this.getNavigation();
        this.setSize(0.9F, 1.3F);
        this.sheep = sheep;
        this.ticks = sheep.getDuration() == -1 ? Long.MAX_VALUE : (long) (sheep.getDuration() * 20);
        this.defaultTicks = this.ticks;
        this.ground = !sheep.isOnGround();
        this.drop = sheep.isDrop();
        //this.noclip = true;
        //this.noclipDistance = aiA.getViewField(player, 6);
        this.setColor(EnumColor.fromColorIndex(sheep.getColor().getWoolData()));
        sheep.getAction().onSpawn(player, this.getBukkitSheep(), plugin);

        if (sheep != acI.INTERGALACTIC && sheep != acI.LIGHTNING) {
            if (sheep == acI.SEEKER) {
                try {
                    Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
                    bField.setAccessible(true);
                    Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
                    cField.setAccessible(true);
                    bField.set(this.goalSelector, Sets.newLinkedHashSet());
                    bField.set(this.targetSelector, Sets.newLinkedHashSet());
                    cField.set(this.goalSelector, Sets.newLinkedHashSet());
                    cField.set(this.targetSelector, Sets.newLinkedHashSet());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.getNavigation();
                this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
                this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
                this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
                this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
                this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
            } else {
                PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 2147483647, 1, false, false);
                this.getBukkitSheep().addPotionEffect(effect);
            }
        } else {
            this.fireProof = true;
        }
    }

    /*
    @Override
    public void move(EnumMoveType enummovetype, double d0, double d1, double d2) {
        if (this.noclip && this.player.getLocation().distance(this.getBukkitEntity().getLocation()) >= (double) this.noclipDistance) {
            this.noclip = false;
        }

        super.move(enummovetype, d0, d1, d2);
    }
    */

    @Override
    public void f(double d0, double d1, double d2) {
    }

    @Override
    public void a(float sizeControl, float upDownControl, float forwardControl) {
        if (this.sheep == acI.REMOTE && this.onGround && !passengers.isEmpty()) {
            EntityLiving passenger = (EntityLiving) this.passengers.get(0);

            // Pitch/Yaw
            this.yaw = passenger.yaw;
            this.lastYaw = this.yaw;
            this.pitch = passenger.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);
            this.aN = this.yaw;
            this.aP = this.aN;

            // XZ movement
            sizeControl = passenger.be * 0.15F;
            forwardControl = passenger.bg * 0.15F;
            if (forwardControl < 0.0F) {
                forwardControl *= 0.45F; // Backward multiplier
            }

            // Jump logic
            if (this.onGround) {
                try {
                    Field jump = EntityLiving.class.getDeclaredField("bd");
                    jump.setAccessible(true);
                    if (jump.getBoolean(passenger)) {
                        this.motY = 0.6D; // Jump force
                        this.impulse = true;
                        if (forwardControl > 0.0F) {
                            float f3 = MathHelper.sin(this.yaw * 0.017453292F);
                            float f4 = MathHelper.cos(this.yaw * 0.017453292F);
                            this.motX += (double)(-0.25F * f3);
                            this.motZ += (double)(0.25F * f4);
                        }
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }
            }

            this.aR = this.cy() * 0.1F;
            this.k(0.7F); // Movement speed
            super.a(sizeControl, upDownControl, forwardControl); // Actually move

            this.aF = this.aG;
            double d0 = this.locX - this.lastX;
            double d1 = this.locZ - this.lastZ;
            float f5 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
            if (f5 > 1.0F) {
                f5 = 1.0F;
            }

            this.aG += (f5 - this.aG) * 0.4F;
            this.aH += this.aG;
        } else {
            this.aR = 0.02F;
            super.a(sizeControl, upDownControl, forwardControl);
        }
    }

    @Override
    public void n() {
        try {
            if (this.sheep != null) {
                Location location = this.getBukkitEntity().getLocation();
                if (!this.ground) {
                    this.ground = this.sheep.isFriendly() || this.onGround || this.inWater;
                } else {
                    if ((this.sheep.isFriendly() || this.ticks <= this.defaultTicks - 20L) && (this.ticks == 0L || this.sheep.getAction().onTicking(this.player, this.ticks, this.getBukkitSheep(), this.plugin) || !this.isAlive())) {
                        boolean death = true;
                        if (this.getBukkitSheep().getPassenger() != null) {
                            this.getBukkitSheep().getPassenger().eject();
                        }

                        if (this.isAlive()) {
                            this.die();
                            death = false;
                        }

                        this.sheep.getAction().onFinish(this.player, this.getBukkitSheep(), death, this.plugin);
                        this.dropDeathLoot();
                        return;
                    }

                    --this.ticks;
                }

                if (!this.onGround && this.ticksLived < 100 && !this.sheep.isFriendly()) {
                    this.plugin.versionManager.getParticleFactory().playParticles(acG.FIREWORKS_SPARK, location.add(0.0D, 0.5D, 0.0D), 0.0F, 0.0F, 0.0F, 1, 0.0F);
                }

                this.explosion = !this.explosion;
            }
        } catch (Exception var6) {
            return;
        } finally {
            super.n();
        }

        super.n();
    }

    public void dropDeathLoot() {
        if (this.drop) {
            this.drop = false;
            if (this.getBukkitEntity().getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {
                if (this.getBukkitSheep().getKiller() != null) {
                    acH.getPlayerData(this.plugin, this.getBukkitSheep().getKiller()).increaseSheepKilled(1);
                    acI.giveSheep(this.getBukkitSheep().getKiller(), this.sheep);
                }
            } else if (this.getBukkitEntity().getLastDamageCause().getCause() == DamageCause.PROJECTILE && this.getBukkitSheep().getKiller() != null) {
                acH.getPlayerData(this.plugin, this.getBukkitSheep().getKiller()).increaseSheepKilled(1);
                this.getBukkitSheep().getWorld().dropItem(this.getBukkitSheep().getLocation(), this.sheep.getIcon(this.getBukkitSheep().getKiller()));
            }
        }

    }

    public Player getPlayer() {
        return this.player;
    }

    public Sheep getBukkitSheep() {
        return (Sheep) this.getBukkitEntity();
    }

    public void explode(float power) {
        this.explode(power, true, false);
    }

    public void explode(float power, boolean fire) {
        this.explode(power, true, fire);
    }

    public void explode(float power, boolean breakBlocks, boolean fire) {
        this.drop = false;
        this.getBukkitEntity().remove();
        this.plugin.versionManager.getWorldUtils().createExplosion(this.player, this.getBukkitSheep().getWorld(), this.locX, this.locY, this.locZ, power, breakBlocks, fire);
    }
}
