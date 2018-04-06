package fr.asynchronous.sheepwars.v1_12_R1;

import fr.asynchronous.sheepwars.a.ac.acK;
import fr.asynchronous.sheepwars.a.aj.ajD;
import fr.asynchronous.sheepwars.v1_12_R1.entity.CustomSheep;
import fr.asynchronous.sheepwars.v1_12_R1.util.SpecialMessage;
import net.minecraft.server.v1_12_R1.ChatHoverable;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EventHelper implements ajD {

    @Override
    public Boolean onEntityTargerEvent(Entity entity, Entity target) {
        net.minecraft.server.v1_12_R1.Entity entityHandler = ((CraftEntity) entity).getHandle();
        if (entityHandler instanceof CustomSheep) {
            CustomSheep sheep = (CustomSheep) entityHandler;
            acK team = acK.getPlayerTeam((Player) target);
            return sheep.getColor().ordinal() != DyeColor.LIME.ordinal() || team == acK.SPEC || team == acK.getPlayerTeam(sheep.getPlayer());
        }

        return false;
    }

    @Override
    public void onAsyncPlayerChat(String prefix, String suffix, Player online, AsyncPlayerChatEvent event, String hover, Boolean spec) {
        SpecialMessage msg = new SpecialMessage("");
        Player player = event.getPlayer();
        acK playerTeam = acK.getPlayerTeam(player);
        if (spec) {
            msg.setHover(prefix + ChatColor.WHITE.toString() + ChatColor.BOLD + player.getName() + suffix, ChatHoverable.EnumHoverAction.SHOW_TEXT, hover);
            msg.append(": " + event.getMessage());
            msg.sendToPlayer(online);
        } else {
            msg.setHover(prefix + (playerTeam != null ? playerTeam.getColor() : ChatColor.GRAY) + player.getName() + suffix, ChatHoverable.EnumHoverAction.SHOW_TEXT, hover);
            msg.append(": " + event.getMessage());
            msg.sendToPlayer(online);
        }
    }

}
