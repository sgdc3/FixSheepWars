package fr.asynchronous.sheepwars.v1_12_R1;

import fr.asynchronous.sheepwars.a.UltimateSheepWarsPlugin;
import fr.asynchronous.sheepwars.a.aj.ajA;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class AnvilGUI extends ajA {
    public AnvilGUI(Player player, UltimateSheepWarsPlugin plugin, AnvilClickEventHandler handler) {
        super(player, plugin, handler);
    }

    @Override
    public void open() {
        EntityPlayer player = ((CraftPlayer) this.player).getHandle();
        AnvilGUI.AnvilContainer container = new AnvilGUI.AnvilContainer(player);
        this.inv = container.getBukkitView().getTopInventory();

        for (AnvilSlot slot : this.items.keySet()) {
            this.inv.setItem(slot.getSlot(), this.items.get(slot));
        }

        int count = player.nextContainerCounter();
        player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(count, "minecraft:anvil", new ChatMessage("Repairing"), 0));
        player.activeContainer = container;
        player.activeContainer.windowId = count;
        player.activeContainer.addSlotListener(player);
    }

    private class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean canUse(EntityHuman entityhuman) {
            return true;
        }
    }
}
