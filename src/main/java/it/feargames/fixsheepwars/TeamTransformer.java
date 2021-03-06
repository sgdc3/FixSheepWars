package it.feargames.fixsheepwars;

import fr.asynchronous.sheepwars.a.ac.acK;
import fr.asynchronous.sheepwars.a.ad.adB;
import fr.asynchronous.sheepwars.a.ai.aiD;
import me.yamakaja.runtimetransformer.annotation.Inject;
import me.yamakaja.runtimetransformer.annotation.InjectionType;
import me.yamakaja.runtimetransformer.annotation.Transform;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

@Transform(acK.class)
public enum TeamTransformer {
    ;

    private Material material;
    private DyeColor dyecolor;

    @Inject(InjectionType.OVERRIDE)
    public ItemStack getIcon(Player p) {
        ItemStack iconItem = (new aiD(Material.BARRIER)).setTitle("Null").build();
        if (this.material == null) {
            return iconItem;
        } else {
            if(material == Material.BANNER) {
                iconItem = new ItemStack(this.material, 1, dyecolor.getDyeData());
            } else {
                iconItem = new ItemStack(this.material, 1, dyecolor.getWoolData());
            }
            ItemMeta iconMeta = iconItem.getItemMeta();
            iconMeta.setDisplayName(dyecolor == DyeColor.BLUE ? adB.getMessage(p, "" + ChatColor.BLUE, adB.JOIN_BLUE_ITEM, "") : adB.getMessage(p, "" + ChatColor.RED, adB.JOIN_RED_ITEM, ""));
            iconItem.setItemMeta(iconMeta);
            return iconItem;
        }
    }

}
