package fr.asynchronous.sheepwars.v1_12_R1;

import fr.asynchronous.sheepwars.a.UltimateSheepWarsPlugin;
import fr.asynchronous.sheepwars.a.ad.adA;
import fr.asynchronous.sheepwars.a.ai.aiA;
import fr.asynchronous.sheepwars.a.ai.aiI;
import fr.asynchronous.sheepwars.a.aj.ajE;
import fr.asynchronous.sheepwars.v1_12_R1.util.SpecialMessage;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class NMSUtils implements ajE {

    @Override
    public void setKiller(Entity entity, Entity killer) {
        ((CraftPlayer) entity).getHandle().killer = ((CraftPlayer) killer).getHandle();
    }

    @Override
    public Block getBoosterBlock(Arrow arrow, UltimateSheepWarsPlugin plugin) {
        try {
            EntityArrow entityArrow = ((CraftArrow) arrow).getHandle();
            Field fieldX = EntityArrow.class.getDeclaredField("h");
            Field fieldY = EntityArrow.class.getDeclaredField("at");
            Field fieldZ = EntityArrow.class.getDeclaredField("au");
            fieldX.setAccessible(true);
            fieldY.setAccessible(true);
            fieldZ.setAccessible(true);
            int x = fieldX.getInt(entityArrow);
            int y = fieldY.getInt(entityArrow);
            int z = fieldZ.getInt(entityArrow);
            Block sourceBlock = arrow.getWorld().getBlockAt(x, y, z);
            entityArrow.die();
            ArrayList<Block> arrayList = aiA.getSurrounding(sourceBlock, false);
            arrayList.add(sourceBlock);

            for (Block block : arrayList) {
                if (block.getType() == Material.WOOL && plugin.isBooster(block.getLocation())) {
                    return block;
                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var14) {
            aiI.registerException(var14, true);
        }

        return null;
    }

    @Override
    public ItemStack setIllegallyGlowing(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }

        if (tag == null) {
            tag = nmsStack.getTag();
        }

        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    @Override
    public void displayAvailableLanguages(Player player) {
        for (adA langs : adA.getLanguages()) {
            SpecialMessage msg = new SpecialMessage(ChatColor.YELLOW + "- " + langs.getName() + " " + ChatColor.DARK_GRAY + "[");
            msg.setClick(ChatColor.GREEN + "➔", ChatClickable.EnumClickAction.RUN_COMMAND, "/lang " + langs.getLocale().replace(".yml", "")).setChatModifier((new ChatModifier()).setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, new ChatMessage(ChatColor.YELLOW + "Click to select", new Object[0]))));
            msg.append(ChatColor.DARK_GRAY + "]");
            msg.sendToPlayer(player);
        }
    }

    @Override
    public void displayRedScreen(Player player, boolean activate) {
        WorldBorder worldBorder;
        if (activate) {
            worldBorder = new WorldBorder();
            worldBorder.world = ((CraftWorld)player.getWorld()).getHandle();
            worldBorder.setSize(1.0D);
            worldBorder.setCenter(player.getLocation().getX() + 10000.0D, player.getLocation().getZ() + 10000.0D);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
        } else {
            worldBorder = new WorldBorder();
            worldBorder.world = ((CraftWorld)player.getWorld()).getHandle();
            worldBorder.setSize(3.0E7D);
            worldBorder.setCenter(player.getLocation().getX(), player.getLocation().getZ());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
        }
    }

}
