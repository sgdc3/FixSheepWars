package fr.asynchronous.sheepwars.v1_12_R1.entity;

import com.google.common.collect.Sets;
import fr.asynchronous.sheepwars.a.UltimateSheepWarsPlugin;
import fr.asynchronous.sheepwars.a.ac.acG;
import fr.asynchronous.sheepwars.a.ac.acH;
import fr.asynchronous.sheepwars.a.ac.acI;
import fr.asynchronous.sheepwars.a.ai.aiA;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.Iterator;

public class CustomSheep extends EntitySheep {
    private acI sheep;
    private Player player;
    private World world;
    private boolean explosion;
    private boolean ground;
    private long defaultTicks;
    private long ticks;
    private boolean drop;
    private int noclipDistance;
    private UltimateSheepWarsPlugin plugin;

    public CustomSheep(World world, Player player, UltimateSheepWarsPlugin plugin) {
        super(world);
        this.explosion = true;
        this.player = player;
        this.plugin = plugin;
        this.world = ((CraftWorld) player.getWorld()).getHandle();
    }

    public CustomSheep(World world, Player player, acI sheep, UltimateSheepWarsPlugin plugin) {
        this(world, player, plugin);
        this.getNavigation();
        this.a(0.9F, 1.3F);
        this.sheep = sheep;
        this.ticks = sheep.getDuration() == -1 ? 9223372036854775807L : (long) (sheep.getDuration() * 20);
        this.defaultTicks = this.ticks;
        this.ground = !sheep.isOnGround();
        this.drop = sheep.isDrop();
        this.noclip = false;
        this.noclipDistance = aiA.getViewField(player, 6);
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

    @Override
    public void move(EnumMoveType enummovetype, double d0, double d1, double d2) {
        if (this.noclip && this.player.getLocation().distance(this.getBukkitEntity().getLocation()) >= (double) this.noclipDistance) {
            this.noclip = false;
        }

        super.move(enummovetype, d0, d1, d2);
    }

    @Override
    public void f(double d0, double d1, double d2) {
    }

    @Override
    public void e(float sideMot, float forMot) {
        if (this.sheep != null && this.onGround && this.sheep == acI.REMOTE && this.passengers.size() == 1) {
            for (Iterator var4 = this.passengers.iterator(); var4.hasNext(); this.aF += this.aD) {
                Entity passenger = (Entity) var4.next();
                if (passenger == null || !(passenger instanceof EntityHuman) || this.sheep != acI.REMOTE) {
                    super.g(sideMot, forMot);
                    this.P = 1.0F;
                    this.aO = 0.02F;
                    return;
                }

                this.lastYaw = this.yaw = passenger.yaw;
                this.pitch = passenger.pitch * 0.5F;
                this.setYawPitch(this.yaw, this.pitch);
                this.aN = this.aL = this.yaw;
                sideMot = ((EntityLiving) passenger).be * 0.15F;
                forMot = ((EntityLiving) passenger).bf * 0.15F;
                if (forMot <= 0.0F) {
                    forMot *= 0.15F;
                }

                Field jump;
                try {
                    jump = EntityLiving.class.getDeclaredField("bd");
                } catch (NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                    return;
                }

                jump.setAccessible(true);
                double jumpHeight;
                if (this.onGround) {
                    try {
                        if (jump.getBoolean(passenger)) {
                            jumpHeight = 0.5D;
                            this.motY = jumpHeight;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                this.P = 1.0F;
                this.aP = (float) this.bW() * 0.1F;
                if (!this.world.isClientSide) {
                    this.k(0.35F);
                    super.g(sideMot, forMot);
                }

                this.aC = this.aD;
                jumpHeight = this.locX - this.lastX;
                double d1 = this.locZ - this.lastZ;
                float f4 = MathHelper.sqrt(jumpHeight * jumpHeight + d1 * d1) * 4.0F;
                if (f4 > 1.0F) {
                    f4 = 1.0F;
                }

                this.aD += (f4 - this.aD) * 0.4F;
            }
        }

        super.g(sideMot, forMot);
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
