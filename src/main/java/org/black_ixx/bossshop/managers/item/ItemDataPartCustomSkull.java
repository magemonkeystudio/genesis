package org.black_ixx.bossshop.managers.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ItemDataPartCustomSkull extends ItemDataPart {

    public static ItemStack transformSkull(ItemStack i, String input) {
        if (input == null || input.isEmpty()) {
            return i;
        }

        SkullMeta skullMeta = (SkullMeta) i.getItemMeta();
        if (input.contains("http://textures.minecraft.net/texture")) {
            try {
                PlayerProfile pprofile = Bukkit.createPlayerProfile(UUID.randomUUID());
                pprofile.getTextures().setSkin(new URL(input));
                skullMeta.setOwnerProfile(pprofile);
                i.setItemMeta(skullMeta);
                return i;
            } catch (MalformedURLException e) {
                throw new RuntimeException("Could not convert url to texture: " + input, e);
            }
        }

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        // TODO Use NBT to set skull texture b64
        Property property = getProperty(input);
        profile.getProperties().put("textures", property);

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            ClassManager.manager.getBugFinder().warn("Could not set profile texture.");
            e.printStackTrace();
        }
        i.setItemMeta(skullMeta);
        return i;
    }

    private static Property getProperty(String texture) {
        return new Property("textures", texture);
    }

    public static String readSkullTexture(ItemStack i) {
        if (i.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta         = (SkullMeta) i.getItemMeta();
            Field     profileField = null;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);

                GameProfile profile = (GameProfile) profileField.get(meta);
                if (profile != null) {
                    if (profile.getProperties() != null) {
                        Collection<Property> properties = profile.getProperties().get("textures");
                        if (properties != null) {

                            Iterator<Property> iterator = properties.iterator();
                            if (iterator.hasNext()) {
                                Property property = iterator.next();
                                return property.getValue();
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if (!(item.getItemMeta() instanceof SkullMeta)) {
            ClassManager.manager.getBugFinder().warn("Mistake in Config: Itemdata of type '" + used_name + "' with value '" + argument + "' can not be added to an item with material '" + item.getType().name() + "'. Don't worry I'll automatically transform the material into '" + Material.PLAYER_HEAD + ".");
            item.setType(Material.PLAYER_HEAD);
        }
        item = transformSkull(item, argument);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_EARLY;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"customskull", "skull"};
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        String skulltexture = readSkullTexture(i);
        if (skulltexture != null) {
            output.add("customskull:" + skulltexture);
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return true; //Custom skull textures do not matter
    }


}
