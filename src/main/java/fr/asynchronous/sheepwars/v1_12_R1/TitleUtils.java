package fr.asynchronous.sheepwars.v1_12_R1;

import fr.asynchronous.sheepwars.a.aj.ajB;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TitleUtils extends ajB {

    @Override
    public void titlePacket(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packetPlayOutTimes);
        IChatBaseComponent titleMain;
        PacketPlayOutTitle packetPlayOutTitle;
        if (subtitle != null) {
            titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleMain);
            connection.sendPacket(packetPlayOutTitle);
        }

        if (title != null) {
            titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
            connection.sendPacket(packetPlayOutTitle);
        }

    }

    @Override
    public void tabPacket(Player player, String footer, String header) {
        if (header == null) {
            header = "";
        }

        if (footer == null) {
            footer = "";
        }

        IChatBaseComponent tabTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tabFoot = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field fieldA = headerPacket.getClass().getDeclaredField("a");
            fieldA.setAccessible(true);
            fieldA.set(headerPacket, tabTitle);
            Field fieldB = headerPacket.getClass().getDeclaredField("b");
            fieldB.setAccessible(true);
            fieldB.set(headerPacket, tabFoot);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.sendPacket(headerPacket);
        }

    }

    @Override
    public void actionBarPacket(Player player, String message) {
        IChatBaseComponent actionbar = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat actionbarPacket = new PacketPlayOutChat(actionbar, ChatMessageType.GAME_INFO);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionbarPacket);
    }

}
