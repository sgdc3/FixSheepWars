package fr.asynchronous.sheepwars.v1_12_R1.util;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SpecialMessage {
    private IChatBaseComponent base;

    public SpecialMessage(String s) {
        this.base = new ChatMessage(s);
    }

    public IChatBaseComponent create() {
        return this.base;
    }

    public void sendToPlayer(Player player) {
        ((CraftPlayer) player).getHandle().sendMessage(this.base);
    }

    public IChatBaseComponent append(String string) {
        return this.base.a(string);
    }

    public IChatBaseComponent setHover(String string, ChatHoverable.EnumHoverAction hoverAction, String hoverString) {
        return this.base.addSibling((new ChatMessage(string)).setChatModifier((new ChatModifier()).setChatHoverable(new ChatHoverable(hoverAction, new ChatMessage(hoverString)))));
    }

    public IChatBaseComponent setClick(String string, ChatClickable.EnumClickAction clickAction, String clickString) {
        return this.base.addSibling((new ChatMessage(string)).setChatModifier((new ChatModifier()).setChatClickable(new ChatClickable(clickAction, clickString))));
    }
}
