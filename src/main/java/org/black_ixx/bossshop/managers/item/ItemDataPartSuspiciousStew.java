package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class ItemDataPartSuspiciousStew extends ItemDataPart{
    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if(!item.getType().equals(Material.SUSPICIOUS_STEW)){
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. The material must be SUSPICIOUS_STEW");
        }
        SuspiciousStewMeta meta = (SuspiciousStewMeta) item.getItemMeta();
        String[] parts = argument.split("#");
        PotionEffectType pet = PotionEffectType.getByName(parts[0].toUpperCase());
        PotionEffect pe;
        int duration = (InputReader.getInt(parts[2].trim(),0) * 20);
        if(pet != null) {
            pe = new PotionEffect(pet, duration, InputReader.getInt(parts[1].trim(),0));
        }else {
            pe = new PotionEffect(PotionEffectType.POISON, duration,InputReader.getInt(parts[1].trim(),0));
            ClassManager.manager.getBugFinder().warn("The potion effect type "+parts[0]+" is not found. Fallback to POISON.");
        }
        meta.addCustomEffect(pe,false);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return true;
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        SuspiciousStewMeta meta = (SuspiciousStewMeta) i.getItemMeta();
        if(meta.hasCustomEffects()){
            for(PotionEffect pe:meta.getCustomEffects()){
                String effectName = pe.getType().getName();
                int duration = pe.getDuration();
                int amplifier = pe.getAmplifier();
                output.add("suspiciousstew:"+effectName+"#"+amplifier+"#"+duration);
            }
        }
        return output;
    }

    @Override
    public int getPriority() {
        return PRIORITY_NORMAL;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"suspiciousstew"};
    }
}
